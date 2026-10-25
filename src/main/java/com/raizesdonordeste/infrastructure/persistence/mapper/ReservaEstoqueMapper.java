package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.ReservaEstoque;
import com.raizesdonordeste.infrastructure.persistence.ReservaEstoqueJpaEntity;

public class ReservaEstoqueMapper {
    public static ReservaEstoqueJpaEntity toJpa(ReservaEstoque reservaEstoque) {
        ReservaEstoqueJpaEntity entity = new ReservaEstoqueJpaEntity();
        entity.setId(reservaEstoque.getId());
        entity.setEstoque(
                EstoqueMapper.toJpa(reservaEstoque.getEstoque())
        );
        entity.setItemPedido(
                ItemPedidoMapper.toJpa(reservaEstoque.getItemPedido())
        );
        entity.setQuantidade(reservaEstoque.getQuantidade());
        entity.setStatus(reservaEstoque.getStatus());
        entity.setCriadoEm(reservaEstoque.getCriadoEm());
        entity.setResolvidoEm(reservaEstoque.getResolvidoEm());
        return entity;
    }

    public static ReservaEstoque toDomain(ReservaEstoqueJpaEntity entity) {
        ReservaEstoque reservaEstoque = new ReservaEstoque();
        reservaEstoque.setId(entity.getId());
        reservaEstoque.setEstoque(
                EstoqueMapper.toDomain(entity.getEstoque())
        );

        reservaEstoque.setItemPedido(
                ItemPedidoMapper.toDomain(entity.getItemPedido())
        );
        reservaEstoque.setQuantidade(entity.getQuantidade());
        reservaEstoque.setStatus(entity.getStatus());
        reservaEstoque.setCriadoEm(entity.getCriadoEm());
        reservaEstoque.setResolvidoEm(entity.getResolvidoEm());
        return reservaEstoque;
    }
}
