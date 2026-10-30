package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.model.MovimentacaoEstoque;
import com.raizesdonordeste.domain.repository.MovimentacaoEstoqueRepository;
import com.raizesdonordeste.infrastructure.persistence.mapper.MovimentacaoEstoqueMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MovimentacaoEstoqueRepositoryAdapter
        implements MovimentacaoEstoqueRepository {

    private final MovimentacaoEstoqueJpaRepository movimentacaoEstoqueJpaRepository;
    private final EstoqueJpaRepository estoqueJpaRepository;
    private final ItemPedidoJpaRepository itemPedidoJpaRepository;
    private final ReservaEstoqueJpaRepository reservaEstoqueJpaRepository;

    public MovimentacaoEstoqueRepositoryAdapter(
            MovimentacaoEstoqueJpaRepository movimentacaoEstoqueJpaRepository,
            EstoqueJpaRepository estoqueJpaRepository,
            ItemPedidoJpaRepository itemPedidoJpaRepository,
            ReservaEstoqueJpaRepository reservaEstoqueJpaRepository) {

        this.movimentacaoEstoqueJpaRepository = movimentacaoEstoqueJpaRepository;
        this.estoqueJpaRepository = estoqueJpaRepository;
        this.itemPedidoJpaRepository = itemPedidoJpaRepository;
        this.reservaEstoqueJpaRepository = reservaEstoqueJpaRepository;
    }

    @Override
    public MovimentacaoEstoque salvar(MovimentacaoEstoque movimentacaoEstoque) {

        var entity = MovimentacaoEstoqueMapper.toJpa(movimentacaoEstoque);

        var estoque = estoqueJpaRepository.findById(
                movimentacaoEstoque.getEstoque().getId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Estoque não encontrado.")
        );

        entity.setEstoque(estoque);

        if (movimentacaoEstoque.getItemPedido() != null) {
            var itemPedido = itemPedidoJpaRepository.findById(
                    movimentacaoEstoque.getItemPedido().getId()
            ).orElseThrow(() ->
                    new IllegalArgumentException("Item do pedido não encontrado.")
            );

            entity.setItemPedido(itemPedido);
        }

        if (movimentacaoEstoque.getReservaEstoque() != null) {
            var reserva = reservaEstoqueJpaRepository.findById(
                    movimentacaoEstoque.getReservaEstoque().getId()
            ).orElseThrow(() ->
                    new IllegalArgumentException("Reserva de estoque não encontrada.")
            );

            entity.setReservaEstoque(reserva);
        }

        var salva = movimentacaoEstoqueJpaRepository.save(entity);

        return MovimentacaoEstoqueMapper.toDomain(salva);
    }

    @Override
    public Optional<MovimentacaoEstoque> buscarPorId(Long id) {
        return movimentacaoEstoqueJpaRepository.findById(id)
                .map(MovimentacaoEstoqueMapper::toDomain);
    }
}