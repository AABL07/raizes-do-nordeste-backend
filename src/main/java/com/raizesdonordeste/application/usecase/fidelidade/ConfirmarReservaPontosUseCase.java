package com.raizesdonordeste.application.usecase.fidelidade;

import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;
import com.raizesdonordeste.domain.model.MovimentacaoPontos;
import com.raizesdonordeste.domain.repository.ContaFidelidadeRepository;
import com.raizesdonordeste.domain.repository.MovimentacaoPontosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ConfirmarReservaPontosUseCase {

    private final ContaFidelidadeRepository contaFidelidadeRepository;
    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    public ConfirmarReservaPontosUseCase(
            ContaFidelidadeRepository contaFidelidadeRepository,
            MovimentacaoPontosRepository movimentacaoPontosRepository) {

        this.contaFidelidadeRepository = contaFidelidadeRepository;
        this.movimentacaoPontosRepository = movimentacaoPontosRepository;
    }

    @Transactional
    public MovimentacaoPontos executar(Long pedidoId) {

        if (pedidoId == null) {
            throw new RegraNegocioException(
                    "O pedido é obrigatório para confirmar a reserva de pontos."
            );
        }

        var movimentacoes =
                movimentacaoPontosRepository.buscarPorPedido(pedidoId);

        int totalReservado = movimentacoes.stream()
                .filter(movimentacao ->
                        movimentacao.getTipo()
                                == TipoMovimentacaoPontos.RESERVA)
                .mapToInt(MovimentacaoPontos::getQuantidade)
                .sum();

        int totalFinalizado = movimentacoes.stream()
                .filter(movimentacao ->
                        movimentacao.getTipo()
                                == TipoMovimentacaoPontos.DEBITO
                                || movimentacao.getTipo()
                                == TipoMovimentacaoPontos.LIBERACAO)
                .mapToInt(MovimentacaoPontos::getQuantidade)
                .sum();

        int quantidadeAtiva =
                totalReservado - totalFinalizado;

        if (quantidadeAtiva <= 0) {
            throw new RecursoNaoEncontradoException(
                    "Não existe reserva de pontos ativa para o pedido."
            );
        }

        var reserva = movimentacoes.stream()
                .filter(movimentacao ->
                        movimentacao.getTipo()
                                == TipoMovimentacaoPontos.RESERVA)
                .max((movimentacao1, movimentacao2) ->
                        movimentacao1.getCriadoEm()
                                .compareTo(movimentacao2.getCriadoEm()))
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Reserva de pontos não encontrada para o pedido."
                        )
                );

        var conta = contaFidelidadeRepository
                .buscarPorId(
                        reserva.getContaFidelidade().getId()
                )
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Conta de fidelidade não encontrada."
                        )
                );

        int saldo = conta.getSaldoPontos() == null
                ? 0
                : conta.getSaldoPontos();

        int reservados = conta.getPontosReservados() == null
                ? 0
                : conta.getPontosReservados();

        int quantidade = quantidadeAtiva;

        if (quantidade <= 0
                || reservados < quantidade
                || saldo < quantidade) {

            throw new ConflitoException(
                    "Inconsistência na reserva de pontos."
            );
        }

        LocalDateTime agora = LocalDateTime.now();

        conta.setSaldoPontos(
                saldo - quantidade
        );

        conta.setPontosReservados(
                reservados - quantidade
        );

        conta.setAtualizadoEm(agora);

        var contaSalva =
                contaFidelidadeRepository.salvar(conta);

        MovimentacaoPontos debito =
                new MovimentacaoPontos();

        debito.setContaFidelidade(contaSalva);
        debito.setPedido(reserva.getPedido());
        debito.setTipo(
                TipoMovimentacaoPontos.DEBITO
        );
        debito.setQuantidade(quantidade);
        debito.setDescricao(
                "Débito de pontos após aprovação do pagamento."
        );
        debito.setCriadoEm(agora);

        return movimentacaoPontosRepository.salvar(
                debito
        );
    }
}