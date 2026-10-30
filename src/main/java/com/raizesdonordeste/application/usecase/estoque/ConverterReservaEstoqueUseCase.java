package com.raizesdonordeste.application.usecase.estoque;

import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.StatusReservaEstoque;
import com.raizesdonordeste.domain.enums.TipoMovimentacaoEstoque;
import com.raizesdonordeste.domain.model.MovimentacaoEstoque;
import com.raizesdonordeste.domain.model.ReservaEstoque;
import com.raizesdonordeste.domain.repository.EstoqueRepository;
import com.raizesdonordeste.domain.repository.MovimentacaoEstoqueRepository;
import com.raizesdonordeste.domain.repository.ReservaEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ConverterReservaEstoqueUseCase {

    private final ReservaEstoqueRepository reservaEstoqueRepository;
    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public ConverterReservaEstoqueUseCase(
            ReservaEstoqueRepository reservaEstoqueRepository,
            EstoqueRepository estoqueRepository,
            MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {

        this.reservaEstoqueRepository = reservaEstoqueRepository;
        this.estoqueRepository = estoqueRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    @Transactional
    public ReservaEstoque executar(Long reservaId) {

        if (reservaId == null) {
            throw new RegraNegocioException(
                    "A reserva de estoque é obrigatória."
            );
        }

        var reserva = reservaEstoqueRepository.buscarPorId(reservaId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Reserva de estoque não encontrada."
                        )
                );

        if (reserva.getStatus() != StatusReservaEstoque.ATIVA) {
            throw new ConflitoException(
                    "Somente uma reserva ativa pode ser convertida em saída."
            );
        }

        var estoque = reserva.getEstoque();

        if (estoque == null) {
            throw new RegraNegocioException(
                    "A reserva não possui estoque associado."
            );
        }

        if (reserva.getItemPedido() == null) {
            throw new RegraNegocioException(
                    "A reserva não possui item de pedido associado."
            );
        }

        if (reserva.getQuantidade() == null
                || reserva.getQuantidade() <= 0) {

            throw new RegraNegocioException(
                    "A reserva possui quantidade inválida."
            );
        }

        int quantidadeFisica = estoque.getQuantidadeFisica() != null
                ? estoque.getQuantidadeFisica()
                : 0;

        int quantidadeReservada = estoque.getQuantidadeReservada() != null
                ? estoque.getQuantidadeReservada()
                : 0;

        if (quantidadeReservada < reserva.getQuantidade()) {
            throw new ConflitoException(
                    "A quantidade reservada do estoque está inconsistente."
            );
        }

        if (quantidadeFisica < reserva.getQuantidade()) {
            throw new ConflitoException(
                    "A quantidade física do estoque está inconsistente."
            );
        }

        LocalDateTime agora = LocalDateTime.now();

        estoque.setQuantidadeFisica(
                quantidadeFisica - reserva.getQuantidade()
        );

        estoque.setQuantidadeReservada(
                quantidadeReservada - reserva.getQuantidade()
        );

        estoque.setAtualizadoEm(agora);

        var estoqueAtualizado = estoqueRepository.salvar(estoque);

        reserva.setEstoque(estoqueAtualizado);
        reserva.setStatus(StatusReservaEstoque.CONVERTIDA);
        reserva.setResolvidoEm(agora);

        var reservaConvertida =
                reservaEstoqueRepository.salvar(reserva);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();

        movimentacao.setEstoque(estoqueAtualizado);
        movimentacao.setItemPedido(reservaConvertida.getItemPedido());
        movimentacao.setReservaEstoque(reservaConvertida);
        movimentacao.setTipo(TipoMovimentacaoEstoque.SAIDA);
        movimentacao.setQuantidade(reservaConvertida.getQuantidade());
        movimentacao.setMotivo(
                "Saída de estoque por confirmação do pagamento do pedido."
        );
        movimentacao.setCriadoEm(agora);

        var movimentacaoSalva =
                movimentacaoEstoqueRepository.salvar(movimentacao);

        reservaConvertida.setMovimentacaoEstoque(movimentacaoSalva);

        return reservaConvertida;
    }
}