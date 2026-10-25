package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.MovimentacaoEstoque;
import com.raizesdonordeste.infrastructure.persistence.MovimentacaoEstoqueJpaEntity;

public class MovimentacaoEstoqueMapper {
    public static MovimentacaoEstoqueJpaEntity toJpa(MovimentacaoEstoque movimentacaoEstoque) {
        MovimentacaoEstoqueJpaEntity entity = new MovimentacaoEstoqueJpaEntity();
        entity.setId(movimentacaoEstoque.getId());
        entity.setEstoque(
                EstoqueMapper.toJpa(movimentacaoEstoque.getEstoque())
        );
        entity.setItemPedido(
                movimentacaoEstoque.getItemPedido() != null
                        ? ItemPedidoMapper.toJpa(movimentacaoEstoque.getItemPedido())
                        : null
        );
        entity.setReservaEstoque(
                movimentacaoEstoque.getReservaEstoque() != null
                        ? ReservaEstoqueMapper.toJpa(movimentacaoEstoque.getReservaEstoque())
                        : null
        );
        entity.setTipo(movimentacaoEstoque.getTipo());
        entity.setQuantidade(movimentacaoEstoque.getQuantidade());
        entity.setMotivo(movimentacaoEstoque.getMotivo());
        entity.setCriadoEm(movimentacaoEstoque.getCriadoEm());
        return entity;
    }

    public static MovimentacaoEstoque toDomain(MovimentacaoEstoqueJpaEntity entity) {
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();
        movimentacaoEstoque.setId(entity.getId());
        movimentacaoEstoque.setEstoque(
                EstoqueMapper.toDomain(entity.getEstoque())
        );
        movimentacaoEstoque.setItemPedido(
                entity.getItemPedido() != null
                        ? ItemPedidoMapper.toDomain(entity.getItemPedido())
                        : null
        );
        movimentacaoEstoque.setReservaEstoque(
                entity.getReservaEstoque() != null
                ? ReservaEstoqueMapper.toDomain(entity.getReservaEstoque())
                : null
        );
        movimentacaoEstoque.setTipo(entity.getTipo());
        movimentacaoEstoque.setQuantidade(entity.getQuantidade());
        movimentacaoEstoque.setMotivo(entity.getMotivo());
        movimentacaoEstoque.setCriadoEm(entity.getCriadoEm());
        return movimentacaoEstoque;
    }
}
