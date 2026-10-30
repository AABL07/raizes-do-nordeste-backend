package com.raizesdonordeste.application.usecase.fidelidade;

import com.raizesdonordeste.domain.repository.PedidoRepository;
import com.raizesdonordeste.domain.repository.ContaFidelidadeRepository;
import com.raizesdonordeste.domain.repository.ConsentimentoRepository;
import com.raizesdonordeste.domain.repository.MovimentacaoPontosRepository;
import org.springframework.stereotype.Service;
import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.StatusPedido;
import com.raizesdonordeste.domain.model.MovimentacaoPontos;
import org.springframework.transaction.annotation.Transactional;
import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class CreditarPontosEntregaUseCase {

    private final PedidoRepository pedidoRepository;
    private final ContaFidelidadeRepository contaFidelidadeRepository;
    private final ConsentimentoRepository consentimentoRepository;
    private final MovimentacaoPontosRepository movimentacaoPontosRepository;

    public CreditarPontosEntregaUseCase(
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
    public MovimentacaoPontos executar(Long pedidoId) {

        if (pedidoId == null) {
            throw new RegraNegocioException(
                    "O pedido é obrigatório para creditar pontos."
            );
        }

        var pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Pedido não encontrado."
                        )
                );

        if (pedido.getStatus() != StatusPedido.ENTREGUE) {
            throw new ConflitoException(
                    "Os pontos só podem ser creditados após a entrega do pedido."
            );
        }

        if (pedido.getCliente() == null
                || pedido.getCliente().getId() == null) {

            throw new RegraNegocioException(
                    "O pedido não possui cliente associado para fidelidade."
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

        boolean creditoJaRealizado =
                movimentacaoPontosRepository.buscarPorPedido(pedidoId)
                        .stream()
                        .anyMatch(movimentacao ->
                                movimentacao.getTipo()
                                        == TipoMovimentacaoPontos.CREDITO
                        );

        if (creditoJaRealizado) {
            throw new ConflitoException(
                    "Os pontos deste pedido já foram creditados."
            );
        }

        if (pedido.getValorFinal() == null
                || pedido.getValorFinal().signum() <= 0) {

            throw new RegraNegocioException(
                    "O pedido não possui valor final válido para gerar pontos."
            );
        }

        int pontosGerados =
                pedido.getValorFinal()
                        .setScale(0, RoundingMode.DOWN)
                        .intValue();

        if (pontosGerados <= 0) {
            throw new RegraNegocioException(
                    "O valor do pedido não gera pontos de fidelidade."
            );
        }

        int saldoAtual = conta.getSaldoPontos() == null
                ? 0
                : conta.getSaldoPontos();

        LocalDateTime agora = LocalDateTime.now();

        conta.setSaldoPontos(
                saldoAtual + pontosGerados
        );

        conta.setAtualizadoEm(agora);

        var contaSalva =
                contaFidelidadeRepository.salvar(conta);

        MovimentacaoPontos credito =
                new MovimentacaoPontos();

        credito.setContaFidelidade(contaSalva);
        credito.setPedido(pedido);
        credito.setTipo(
                TipoMovimentacaoPontos.CREDITO
        );
        credito.setQuantidade(pontosGerados);
        credito.setDescricao(
                "Crédito de pontos após entrega do pedido."
        );
        credito.setCriadoEm(agora);

        return movimentacaoPontosRepository.salvar(
                credito
        );
    }
}