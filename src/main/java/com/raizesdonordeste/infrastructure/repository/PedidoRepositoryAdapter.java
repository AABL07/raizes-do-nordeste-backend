package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.PedidoRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Pedido;
import com.raizesdonordeste.infrastructure.persistence.mapper.PedidoMapper;
import java.util.Optional;
import java.util.List;
import com.raizesdonordeste.domain.model.ItemPedido;
import com.raizesdonordeste.infrastructure.persistence.mapper.ItemPedidoMapper;
import com.raizesdonordeste.domain.enums.CanalPedido;

@Repository
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final PedidoJpaRepository pedidoJpaRepository;

    private final ItemPedidoJpaRepository itemPedidoJpaRepository;

    public PedidoRepositoryAdapter(
            PedidoJpaRepository pedidoJpaRepository,
            ItemPedidoJpaRepository itemPedidoJpaRepository) {

        this.pedidoJpaRepository = pedidoJpaRepository;
        this.itemPedidoJpaRepository = itemPedidoJpaRepository;
    }

    @Override
    public Pedido salvar(Pedido pedido) {

        var entity = PedidoMapper.toJpa(pedido);
        var salvo = pedidoJpaRepository.save(entity);

        List<ItemPedido> itensSalvos;

        if (pedido.getItens() == null) {
            itensSalvos = List.of();
        } else {
            itensSalvos = pedido.getItens()
                    .stream()
                    .map(itemPedido -> {
                        var itemEntity = ItemPedidoMapper.toJpa(itemPedido);

                        itemEntity.setPedido(salvo);

                        var itemSalvo = itemPedidoJpaRepository.save(itemEntity);

                        return ItemPedidoMapper.toDomain(itemSalvo);
                    })
                    .toList();
        }

        var pedidoSalvo = PedidoMapper.toDomain(salvo);

        for (ItemPedido itemPedido : itensSalvos) {
            itemPedido.setPedido(pedidoSalvo);
        }

        pedidoSalvo.setItens(itensSalvos);

        return pedidoSalvo;
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {

        return pedidoJpaRepository.findById(id)
                .map(entity -> {

                    Pedido pedido = PedidoMapper.toDomain(entity);

                    List<ItemPedido> itens = itemPedidoJpaRepository
                            .findByPedidoId(id)
                            .stream()
                            .map(ItemPedidoMapper::toDomain)
                            .toList();

                    for (ItemPedido itemPedido : itens) {
                        itemPedido.setPedido(pedido);
                    }

                    pedido.setItens(itens);

                    return pedido;
                });
    }

    @Override
    public List<Pedido> buscarTodos() {
        return pedidoJpaRepository.findAll()
                .stream()
                .map(PedidoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Pedido> buscarPorCanal(CanalPedido canalPedido) {

        return pedidoJpaRepository
                .findByCanalPedido(canalPedido)
                .stream()
                .map(PedidoMapper::toDomain)
                .toList();
    }

}
