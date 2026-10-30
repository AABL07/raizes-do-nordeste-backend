package com.raizesdonordeste.application.usecase.fidelidade;

import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;
import com.raizesdonordeste.domain.model.MovimentacaoPontos;
import com.raizesdonordeste.domain.repository.ConsentimentoRepository;
import com.raizesdonordeste.domain.repository.ContaFidelidadeRepository;
import com.raizesdonordeste.domain.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReservarPontosUseCase {

    private final PedidoRepository pedidoRepository;
    private final ContaFidelidadeRepository contaFidelidadeRepository;
    private final ConsentimentoRepository consentimentoRepository;
    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    public ReservarPontosUseCase(
            PedidoRepository pedidoRepository,
            ContaFidelidadeRepository contaFidelidadeRepository,
            ConsentimentoRepository consentimentoRepository,
            MovimentacaoPontosRepository movimentacaoPontosRepository) {

        this.pedidoRepository = pedidoRepository;
        this.contaFidelidadeRepository = contaFidelidadeRepository;
        this.consentimentoRepository = consentimentoRepository;
        this.movimentacaoPontosRepository = movimentacaoPontosRepository;
    }

    @Transactional
    public MovimentacaoPontos executar(
            Long pedidoId,
            Integer quantidadePontos) {

        if (pedidoId == null) {
            throw new RegraNegocioException(
                    "O pedido é obrigatório para reservar pontos."
            );
        }

        if (quantidadePontos == null || quantidadePontos <= 0) {
            throw new RegraNegocioException(
                    "A quantidade de pontos deve ser maior que zero."
            );
        }

        var pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Pedido não encontrado."
                        )
                );

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new ConflitoException(
                    "Os pontos só podem ser reservados enquanto o pedido aguarda pagamento."
            );
        }

        if (pedido.getCliente() == null
                || pedido.getCliente().getId() == null) {

            throw new RegraNegocioException(
                    "O pedido não possui cliente associado para utilizar fidelidade."
            );
        }

        Long clienteId = pedido.getCliente().getId();

        consentimentoRepository
                .buscarAtivoPorClienteEFinalidade(
                        clienteId,
                        FinalidadeConsentimento.FIDELIDADE
                )
                .orElseThrow(() ->
                        new ConflitoException(
                                "O cliente não possui consentimento ativo para fidelidade."
                        )
                );

        var conta = contaFidelidadeRepository
                .buscarPorCliente(clienteId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Conta de fidelidade não encontrada."
                        )
                );

        if (!Boolean.TRUE.equals(conta.getAtiva())) {
            throw new ConflitoException(
                    "A conta de fidelidade está inativa."
            );
        }

        int saldo = conta.getSaldoPontos() == null
                ? 0
                : conta.getSaldoPontos();

        int reservados = conta.getPontosReservados() == null
                ? 0
                : conta.getPontosReservados();

        int disponiveis = saldo - reservados;

        if (quantidadePontos > disponiveis) {
            throw new ConflitoException(
                    "Saldo de pontos insuficiente."
            );
        }

        var movimentacoesAnteriores =
                movimentacaoPontosRepository.buscarPorPedido(pedidoId);

        int totalReservadoAnterior =
                movimentacoesAnteriores.stream()
                        .filter(movimentacao ->
                                movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.RESERVA)
                        .mapToInt(MovimentacaoPontos::getQuantidade)
                        .sum();

        int totalFinalizadoAnterior =
                movimentacoesAnteriores.stream()
                        .filter(movimentacao ->
                                movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.DEBITO
                                        || movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.LIBERACAO)
                        .mapToInt(MovimentacaoPontos::getQuantidade)
                        .sum();

        int reservaAtivaAnterior =
                totalReservadoAnterior - totalFinalizadoAnterior;

        if (reservaAtivaAnterior > 0) {
            throw new ConflitoException(
                    "O pedido já possui uma reserva de pontos ativa."
            );
        }

        boolean primeiraReserva =
                totalReservadoAnterior == 0;

        LocalDateTime agora = LocalDateTime.now();

        if (primeiraReserva) {

            if (pedido.getValorFinal() == null
                    || pedido.getValorFinal().signum() <= 0) {

                throw new RegraNegocioException(
                        "O pedido não possui valor final válido para aplicar o resgate."
                );
            }

            BigDecimal descontoPontos =
                    BigDecimal.valueOf(quantidadePontos)
                            .movePointLeft(2);

            BigDecimal novoValorFinal =
                    pedido.getValorFinal()
                            .subtract(descontoPontos);

            if (novoValorFinal.signum() <= 0) {
                throw new ConflitoException(
                        "O resgate de pontos deve manter o valor final do pedido maior que zero."
                );
            }

            BigDecimal descontoAtual =
                    pedido.getValorDesconto() == null
                            ? BigDecimal.ZERO
                            : pedido.getValorDesconto();

            pedido.setValorDesconto(
                    descontoAtual.add(descontoPontos)
            );

            pedido.setValorFinal(novoValorFinal);
            pedido.setAtualizadoEm(agora);

            pedido = pedidoRepository.salvar(pedido);
        }

        conta.setPontosReservados(
                reservados + quantidadePontos
        );

        conta.setAtualizadoEm(agora);

        var contaSalva =
                contaFidelidadeRepository.salvar(conta);

        MovimentacaoPontos movimentacao =
                new MovimentacaoPontos();

        movimentacao.setContaFidelidade(contaSalva);
        movimentacao.setPedido(pedido);
        movimentacao.setTipo(
                TipoMovimentacaoPontos.RESERVA
        );
        movimentacao.setQuantidade(quantidadePontos);
        movimentacao.setDescricao(
                "Reserva de pontos para pagamento do pedido."
        );
        movimentacao.setCriadoEm(agora);

        return movimentacaoPontosRepository.salvar(
                movimentacao
        );
    }
}