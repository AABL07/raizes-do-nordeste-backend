package com.raizesdonordeste.application.usecase.pedido;

import com.raizesdonordeste.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.domain.model.Pedido;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import com.raizesdonordeste.application.usecase.estoque.LiberarReservaEstoqueUseCase;
import com.raizesdonordeste.application.usecase.fidelidade.CreditarPontosEntregaUseCase;
import com.raizesdonordeste.application.usecase.fidelidade.LiberarReservaPontosUseCase;
import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;
import com.raizesdonordeste.domain.repository.ContaFidelidadeRepository;
import com.raizesdonordeste.domain.repository.ConsentimentoRepository;
import com.raizesdonordeste.domain.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.domain.repository.ReservaEstoqueRepository;

@Service
public class AlterarStatusPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    private final ReservaEstoqueRepository reservaEstoqueRepository;

    private final LiberarReservaEstoqueUseCase liberarReservaEstoqueUseCase;

    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    private final LiberarReservaPontosUseCase liberarReservaPontosUseCase;

    private final ConsentimentoRepository consentimentoRepository;

    private final ContaFidelidadeRepository contaFidelidadeRepository;

    private final CreditarPontosEntregaUseCase creditarPontosEntregaUseCase;

    public AlterarStatusPedidoUseCase(
            PedidoRepository pedidoRepository,
            ReservaEstoqueRepository reservaEstoqueRepository,
            LiberarReservaEstoqueUseCase liberarReservaEstoqueUseCase,
            MovimentacaoPontosRepository movimentacaoPontosRepository,
            LiberarReservaPontosUseCase liberarReservaPontosUseCase,
            ConsentimentoRepository consentimentoRepository,
            ContaFidelidadeRepository contaFidelidadeRepository,
            CreditarPontosEntregaUseCase creditarPontosEntregaUseCase) {

        this.pedidoRepository = pedidoRepository;
        this.reservaEstoqueRepository = reservaEstoqueRepository;
        this.liberarReservaEstoqueUseCase = liberarReservaEstoqueUseCase;

        this.movimentacaoPontosRepository = movimentacaoPontosRepository;
        this.liberarReservaPontosUseCase = liberarReservaPontosUseCase;

        this.consentimentoRepository = consentimentoRepository;
        this.contaFidelidadeRepository = contaFidelidadeRepository;
        this.creditarPontosEntregaUseCase = creditarPontosEntregaUseCase;
    }

    @Transactional
    public Pedido executar(
            Long pedidoId,
            StatusPedido novoStatus) {

        if (pedidoId == null) {
            throw new RegraNegocioException(
                    "O pedido é obrigatório para alterar o status."
            );
        }

        if (novoStatus == null) {
            throw new RegraNegocioException(
                    "O novo status é obrigatório."
            );
        }

        var pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Pedido não encontrado."
                        )
                );

        StatusPedido statusAtual = pedido.getStatus();

        if (statusAtual == novoStatus) {
            throw new ConflitoException(
                    "O pedido já está no status informado."
            );
        }

        boolean transicaoPermitida =
                switch (statusAtual) {

                    case EM_PREPARO ->
                            novoStatus == StatusPedido.PRONTO;

                    case PRONTO ->
                            novoStatus == StatusPedido.ENTREGUE;

                    case AGUARDANDO_PAGAMENTO,
                         PAGAMENTO_RECUSADO,
                         FALHA_PAGAMENTO ->
                            novoStatus == StatusPedido.CANCELADO;

                    case ENTREGUE,
                         CANCELADO ->
                            false;
                };

        if (!transicaoPermitida) {
            throw new ConflitoException(
                    "Transição de status não permitida: "
                            + statusAtual
                            + " -> "
                            + novoStatus
                            + "."
            );
        }

        pedido.setStatus(novoStatus);
        pedido.setAtualizadoEm(LocalDateTime.now());

        Pedido pedidoSalvo =
                pedidoRepository.salvar(pedido);

        if (novoStatus == StatusPedido.CANCELADO) {

            var reservasEstoqueAtivas =
                    reservaEstoqueRepository.buscarAtivasPorPedido(pedidoId);

            for (var reserva : reservasEstoqueAtivas) {
                liberarReservaEstoqueUseCase.executar(
                        reserva.getId()
                );
            }

            var movimentacoesPontos =
                    movimentacaoPontosRepository.buscarPorPedido(pedidoId);

            int totalReservado =
                    movimentacoesPontos.stream()
                            .filter(movimentacao ->
                                    movimentacao.getTipo()
                                            == TipoMovimentacaoPontos.RESERVA)
                            .mapToInt(movimentacao ->
                                    movimentacao.getQuantidade())
                            .sum();

            int totalFinalizado =
                    movimentacoesPontos.stream()
                            .filter(movimentacao ->
                                    movimentacao.getTipo()
                                            == TipoMovimentacaoPontos.DEBITO
                                            || movimentacao.getTipo()
                                            == TipoMovimentacaoPontos.LIBERACAO)
                            .mapToInt(movimentacao ->
                                    movimentacao.getQuantidade())
                            .sum();

            boolean possuiReservaPontosAtiva =
                    totalReservado - totalFinalizado > 0;

            if (possuiReservaPontosAtiva) {
                liberarReservaPontosUseCase.executar(pedidoId);
            }
        }

        if (novoStatus == StatusPedido.ENTREGUE
                && pedidoSalvo.getCliente() != null
                && pedidoSalvo.getCliente().getId() != null) {

            Long clienteId =
                    pedidoSalvo.getCliente().getId();

            boolean possuiConsentimento =
                    consentimentoRepository
                            .buscarAtivoPorClienteEFinalidade(
                                    clienteId,
                                    FinalidadeConsentimento.FIDELIDADE
                            )
                            .isPresent();

            var contaFidelidade =
                    contaFidelidadeRepository
                            .buscarPorCliente(clienteId);

            boolean participaFidelidade =
                    possuiConsentimento
                            && contaFidelidade.isPresent()
                            && Boolean.TRUE.equals(
                            contaFidelidade.get().getAtiva()
                    );

            if (participaFidelidade) {
                creditarPontosEntregaUseCase.executar(
                        pedidoId
                );
            }
        }

        return pedidoSalvo;

    }

}