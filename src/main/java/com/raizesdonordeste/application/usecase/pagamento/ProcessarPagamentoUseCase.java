package com.raizesdonordeste.application.usecase.pagamento;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.raizesdonordeste.domain.model.TentativaPagamento;
import com.raizesdonordeste.domain.repository.PedidoRepository;
import com.raizesdonordeste.domain.repository.TentativaPagamentoRepository;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.application.service.PagamentoGateway;
import com.raizesdonordeste.domain.enums.ResultadoPagamento;
import com.raizesdonordeste.application.usecase.estoque.ReservarEstoqueUseCase;
import com.raizesdonordeste.domain.repository.ReservaEstoqueRepository;
import com.raizesdonordeste.application.usecase.estoque.LiberarReservaEstoqueUseCase;
import com.raizesdonordeste.application.usecase.estoque.ConverterReservaEstoqueUseCase;
import java.time.LocalDateTime;
import com.raizesdonordeste.application.usecase.fidelidade.ConfirmarReservaPontosUseCase;
import com.raizesdonordeste.application.usecase.fidelidade.LiberarReservaPontosUseCase;
import com.raizesdonordeste.application.usecase.fidelidade.ReservarPontosUseCase;
import com.raizesdonordeste.domain.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;
import com.raizesdonordeste.domain.model.MovimentacaoPontos;

@Service
public class ProcessarPagamentoUseCase {

    private final PedidoRepository pedidoRepository;

    private final TentativaPagamentoRepository tentativaPagamentoRepository;

    private final PagamentoGateway pagamentoGateway;

    private final ReservarEstoqueUseCase reservarEstoqueUseCase;

    private final ReservaEstoqueRepository reservaEstoqueRepository;

    private final LiberarReservaEstoqueUseCase liberarReservaEstoqueUseCase;

    private final ConverterReservaEstoqueUseCase converterReservaEstoqueUseCase;

    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    private final ReservarPontosUseCase reservarPontosUseCase;

    private final ConfirmarReservaPontosUseCase confirmarReservaPontosUseCase;

    private final LiberarReservaPontosUseCase liberarReservaPontosUseCase;

    public ProcessarPagamentoUseCase(
            PedidoRepository pedidoRepository,
            TentativaPagamentoRepository tentativaPagamentoRepository,
            PagamentoGateway pagamentoGateway,
            ReservarEstoqueUseCase reservarEstoqueUseCase,
            ReservaEstoqueRepository reservaEstoqueRepository,
            LiberarReservaEstoqueUseCase liberarReservaEstoqueUseCase,
            ConverterReservaEstoqueUseCase converterReservaEstoqueUseCase,
            MovimentacaoPontosRepository movimentacaoPontosRepository,
            ReservarPontosUseCase reservarPontosUseCase,
            ConfirmarReservaPontosUseCase confirmarReservaPontosUseCase,
            LiberarReservaPontosUseCase liberarReservaPontosUseCase) {

        this.pedidoRepository = pedidoRepository;
        this.tentativaPagamentoRepository = tentativaPagamentoRepository;
        this.pagamentoGateway = pagamentoGateway;
        this.reservarEstoqueUseCase = reservarEstoqueUseCase;
        this.reservaEstoqueRepository = reservaEstoqueRepository;
        this.liberarReservaEstoqueUseCase = liberarReservaEstoqueUseCase;
        this.converterReservaEstoqueUseCase = converterReservaEstoqueUseCase;

        this.movimentacaoPontosRepository = movimentacaoPontosRepository;
        this.reservarPontosUseCase = reservarPontosUseCase;
        this.confirmarReservaPontosUseCase = confirmarReservaPontosUseCase;
        this.liberarReservaPontosUseCase = liberarReservaPontosUseCase;
    }

    @Transactional
    public TentativaPagamento executar(
            Long pedidoId,
            String chaveIdempotencia,
            ResultadoPagamento resultadoSimulado) {

        if (pedidoId == null) {
            throw new RegraNegocioException(
                    "O pedido é obrigatório para processar o pagamento."
            );
        }

        if (chaveIdempotencia == null || chaveIdempotencia.isBlank()) {
            throw new RegraNegocioException(
                    "A chave de idempotência é obrigatória."
            );
        }

        if (resultadoSimulado == null) {
            throw new RegraNegocioException(
                    "O resultado simulado do pagamento é obrigatório."
            );
        }

        String chaveNormalizada = chaveIdempotencia.trim();

        var tentativaExistente =
                tentativaPagamentoRepository.buscarPorPedidoEChaveIdempotencia(
                        pedidoId,
                        chaveNormalizada
                );

        if (tentativaExistente.isPresent()) {
            return tentativaExistente.get();
        }

        var pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Pedido não encontrado."
                        )
                );

        boolean statusPermitido =
                pedido.getStatus() == StatusPedido.AGUARDANDO_PAGAMENTO
                        || pedido.getStatus() == StatusPedido.PAGAMENTO_RECUSADO
                        || pedido.getStatus() == StatusPedido.FALHA_PAGAMENTO;

        if (!statusPermitido) {
            throw new ConflitoException(
                    "O pedido não permite uma nova tentativa de pagamento neste status."
            );
        }

        boolean retry =
                pedido.getStatus() == StatusPedido.PAGAMENTO_RECUSADO
                        || pedido.getStatus() == StatusPedido.FALHA_PAGAMENTO;

        var movimentacoesPontosAnteriores =
                movimentacaoPontosRepository.buscarPorPedido(pedidoId);

        var ultimaReservaPontos =
                movimentacoesPontosAnteriores.stream()
                        .filter(movimentacao ->
                                movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.RESERVA)
                        .max((movimentacao1, movimentacao2) ->
                                movimentacao1.getCriadoEm()
                                        .compareTo(movimentacao2.getCriadoEm()));

        if (retry) {

            if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
                throw new RegraNegocioException(
                        "O pedido não possui itens para uma nova tentativa de pagamento."
                );
            }

            for (var itemPedido : pedido.getItens()) {
                reservarEstoqueUseCase.executar(itemPedido);
            }

            pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

            pedidoRepository.salvar(pedido);

            if (ultimaReservaPontos.isPresent()) {
                reservarPontosUseCase.executar(
                        pedidoId,
                        ultimaReservaPontos.get().getQuantidade()
                );
            }
        }

        var reservasAtivas =
                reservaEstoqueRepository.buscarAtivasPorPedido(pedidoId);

        if (reservasAtivas.isEmpty()) {
            throw new ConflitoException(
                    "O pedido não possui reservas de estoque ativas."
            );
        }

        var movimentacoesPontosAtuais =
                movimentacaoPontosRepository.buscarPorPedido(pedidoId);

        int totalPontosReservados =
                movimentacoesPontosAtuais.stream()
                        .filter(movimentacao ->
                                movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.RESERVA)
                        .mapToInt(MovimentacaoPontos::getQuantidade)
                        .sum();

        int totalPontosFinalizados =
                movimentacoesPontosAtuais.stream()
                        .filter(movimentacao ->
                                movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.DEBITO
                                        || movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.LIBERACAO)
                        .mapToInt(MovimentacaoPontos::getQuantidade)
                        .sum();

        boolean possuiReservaPontosAtiva =
                totalPontosReservados - totalPontosFinalizados > 0;

        ResultadoPagamento resultado =
                pagamentoGateway.processar(
                        pedido.getId(),
                        pedido.getValorFinal(),
                        resultadoSimulado
                );

        TentativaPagamento tentativaPagamento =
                new TentativaPagamento();

        tentativaPagamento.setPedido(pedido);
        tentativaPagamento.setChaveIdempotencia(chaveNormalizada);
        tentativaPagamento.setResultado(resultado);
        tentativaPagamento.setValorSolicitado(pedido.getValorFinal());
        tentativaPagamento.setCriadoEm(LocalDateTime.now());

        switch (resultado) {

            case APROVADO -> {

                tentativaPagamento.setMensagemRetorno(
                        "Pagamento aprovado."
                );

                for (var reserva : reservasAtivas) {
                    converterReservaEstoqueUseCase.executar(
                            reserva.getId()
                    );
                }

                if (possuiReservaPontosAtiva) {
                    confirmarReservaPontosUseCase.executar(pedidoId);
                }

                pedido.setStatus(StatusPedido.EM_PREPARO);
            }

            case RECUSADO -> {

                tentativaPagamento.setMensagemRetorno(
                        "Pagamento recusado."
                );

                for (var reserva : reservasAtivas) {
                    liberarReservaEstoqueUseCase.executar(
                            reserva.getId()
                    );
                }

                if (possuiReservaPontosAtiva) {
                    liberarReservaPontosUseCase.executar(pedidoId);
                }

                pedido.setStatus(StatusPedido.PAGAMENTO_RECUSADO);
            }

            case FALHA_TECNICA -> {

                tentativaPagamento.setMensagemRetorno(
                        "Falha técnica durante o processamento do pagamento."
                );

                for (var reserva : reservasAtivas) {
                    liberarReservaEstoqueUseCase.executar(
                            reserva.getId()
                    );
                }

                if (possuiReservaPontosAtiva) {
                    liberarReservaPontosUseCase.executar(pedidoId);
                }

                pedido.setStatus(StatusPedido.FALHA_PAGAMENTO);
            }
        }

        pedido.setAtualizadoEm(LocalDateTime.now());

        TentativaPagamento tentativaSalva =
                tentativaPagamentoRepository.salvar(
                        tentativaPagamento
                );

        pedidoRepository.salvar(pedido);

        return tentativaSalva;
    }
}
