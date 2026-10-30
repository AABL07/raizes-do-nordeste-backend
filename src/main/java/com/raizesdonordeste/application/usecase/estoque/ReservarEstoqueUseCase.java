package com.raizesdonordeste.application.usecase.estoque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.raizesdonordeste.domain.repository.EstoqueRepository;
import com.raizesdonordeste.domain.model.ItemPedido;
import com.raizesdonordeste.domain.model.ReservaEstoque;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.StatusReservaEstoque;
import com.raizesdonordeste.domain.repository.ReservaEstoqueRepository;
import com.raizesdonordeste.domain.model.Estoque;
import java.time.LocalDateTime;

@Service
public class ReservarEstoqueUseCase {

    private final EstoqueRepository estoqueRepository;

    private final ReservaEstoqueRepository reservaEstoqueRepository;

    public ReservarEstoqueUseCase(
            EstoqueRepository estoqueRepository,
            ReservaEstoqueRepository reservaEstoqueRepository) {

        this.estoqueRepository = estoqueRepository;
        this.reservaEstoqueRepository = reservaEstoqueRepository;
    }

    @Transactional
    public ReservaEstoque executar(ItemPedido itemPedido) {
        if (itemPedido == null) {
            throw new RegraNegocioException("O item do pedido é obrigatório para reservar estoque.");
        }

        if (itemPedido.getItemCardapio() == null) {
            throw new RegraNegocioException("O item do pedido deve possuir um item de cardápio.");
        }

        if (itemPedido.getQuantidade() == null || itemPedido.getQuantidade() <= 0) {
            throw new RegraNegocioException("A quantidade a reservar deve ser maior que zero.");
        }

        var itemCardapio = itemPedido.getItemCardapio();

        if (itemCardapio.getUnidade() == null
                || itemCardapio.getProduto() == null) {

            throw new RegraNegocioException(
                    "O item do cardápio deve possuir unidade e produto."
            );
        }

        var estoque = estoqueRepository.buscarPorUnidadeEProduto(
                itemCardapio.getUnidade().getId(),
                itemCardapio.getProduto().getId()
        ).orElseThrow(() ->
                new RecursoNaoEncontradoException(
                        "Estoque não encontrado para o produto na unidade."
                )
        );

        int quantidadeFisica = estoque.getQuantidadeFisica() != null
                ? estoque.getQuantidadeFisica()
                : 0;

        int quantidadeReservada = estoque.getQuantidadeReservada() != null
                ? estoque.getQuantidadeReservada()
                : 0;

        int quantidadeDisponivel =
                quantidadeFisica - quantidadeReservada;

        if (quantidadeDisponivel < itemPedido.getQuantidade()) {
            throw new ConflitoException(
                    "Estoque insuficiente para o item solicitado."
            );
        }

        estoque.setQuantidadeReservada(
                quantidadeReservada + itemPedido.getQuantidade()
        );

        LocalDateTime agora = LocalDateTime.now();
        estoque.setAtualizadoEm(agora);

        Estoque estoqueAtualizado =
                estoqueRepository.salvar(estoque);

        ReservaEstoque reserva = new ReservaEstoque();

        reserva.setEstoque(estoqueAtualizado);
        reserva.setItemPedido(itemPedido);
        reserva.setQuantidade(itemPedido.getQuantidade());
        reserva.setStatus(StatusReservaEstoque.ATIVA);
        reserva.setCriadoEm(agora);
        reserva.setResolvidoEm(null);
        reserva.setMovimentacaoEstoque(null);

        return reservaEstoqueRepository.salvar(reserva);

    }



}
