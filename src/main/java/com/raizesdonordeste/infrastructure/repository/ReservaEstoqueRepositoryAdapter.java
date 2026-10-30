package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.model.ReservaEstoque;
import com.raizesdonordeste.domain.repository.ReservaEstoqueRepository;
import com.raizesdonordeste.infrastructure.persistence.mapper.ReservaEstoqueMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.raizesdonordeste.domain.enums.StatusReservaEstoque;
import java.util.List;

@Repository
public class ReservaEstoqueRepositoryAdapter
        implements ReservaEstoqueRepository {

    private final ReservaEstoqueJpaRepository reservaEstoqueJpaRepository;

    private final EstoqueJpaRepository estoqueJpaRepository;

    private final ItemPedidoJpaRepository itemPedidoJpaRepository;

    public ReservaEstoqueRepositoryAdapter(
            ReservaEstoqueJpaRepository reservaEstoqueJpaRepository,
            EstoqueJpaRepository estoqueJpaRepository,
            ItemPedidoJpaRepository itemPedidoJpaRepository) {

        this.reservaEstoqueJpaRepository = reservaEstoqueJpaRepository;
        this.estoqueJpaRepository = estoqueJpaRepository;
        this.itemPedidoJpaRepository = itemPedidoJpaRepository;
    }

    @Override
    public ReservaEstoque salvar(ReservaEstoque reservaEstoque) {

        var entity = ReservaEstoqueMapper.toJpa(reservaEstoque);

        var estoque = estoqueJpaRepository.findById(
                reservaEstoque.getEstoque().getId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Estoque não encontrado.")
        );

        var itemPedido = itemPedidoJpaRepository.findById(
                reservaEstoque.getItemPedido().getId()
        ).orElseThrow(() ->
                new IllegalArgumentException("Item do pedido não encontrado.")
        );

        entity.setEstoque(estoque);
        entity.setItemPedido(itemPedido);

        var salva = reservaEstoqueJpaRepository.save(entity);

        return ReservaEstoqueMapper.toDomain(salva);
    }

    @Override
    public Optional<ReservaEstoque> buscarPorId(Long id) {
        return reservaEstoqueJpaRepository.findById(id)
                .map(ReservaEstoqueMapper::toDomain);
    }

    @Override
    public List<ReservaEstoque> buscarAtivasPorPedido(Long pedidoId) {
        return reservaEstoqueJpaRepository
                .findByItemPedidoPedidoIdAndStatus(
                        pedidoId,
                        StatusReservaEstoque.ATIVA
                )
                .stream()
                .map(ReservaEstoqueMapper::toDomain)
                .toList();
    }
}