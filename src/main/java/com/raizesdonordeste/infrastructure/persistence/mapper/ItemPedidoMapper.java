package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.ItemPedido;
import com.raizesdonordeste.infrastructure.persistence.ItemPedidoJpaEntity;

public class ItemPedidoMapper {
    public static ItemPedidoJpaEntity toJpa(ItemPedido itemPedido)
        {
        ItemPedidoJpaEntity entity = new ItemPedidoJpaEntity();
        entity.setId(itemPedido.getId());
        entity.setPedido(
                    PedidoMapper.toJpa(itemPedido.getPedido())
            );
        entity.setItemCardapio(
                    ItemCardapioMapper.toJpa(itemPedido.getItemCardapio())
            );
        entity.setQuantidade(itemPedido.getQuantidade());
        entity.setPrecoUnitario(itemPedido.getPrecoUnitario());
        entity.setSubtotal(itemPedido.getSubtotal());
        return entity;
        }
    public static ItemPedido toDomain(ItemPedidoJpaEntity entity)
        {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(entity.getId());
        itemPedido.setPedido(
                    PedidoMapper.toDomain(entity.getPedido())
            );

        itemPedido.setItemCardapio(
                    ItemCardapioMapper.toDomain(entity.getItemCardapio())
            );
        itemPedido.setQuantidade(entity.getQuantidade());
        itemPedido.setPrecoUnitario(entity.getPrecoUnitario());
        itemPedido.setSubtotal(entity.getSubtotal());
        return itemPedido;
        }
}
