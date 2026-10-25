package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.MovimentacaoPontos;
import com.raizesdonordeste.infrastructure.persistence.MovimentacaoPontosJpaEntity;

public class MovimentacaoPontosMapper {
    public static MovimentacaoPontosJpaEntity toJpa(MovimentacaoPontos movimentacaoPontos) {
        MovimentacaoPontosJpaEntity entity = new MovimentacaoPontosJpaEntity();
        entity.setId(movimentacaoPontos.getId());
        entity.setContaFidelidade(
                ContaFidelidadeMapper.toJpa(movimentacaoPontos.getContaFidelidade())
        );
        entity.setPedido(
                PedidoMapper.toJpa(movimentacaoPontos.getPedido())
        );
        entity.setTipo(movimentacaoPontos.getTipo());
        entity.setQuantidade(movimentacaoPontos.getQuantidade());
        entity.setDescricao(movimentacaoPontos.getDescricao());
        entity.setCriadoEm(movimentacaoPontos.getCriadoEm());
        return entity;
    }

    public static MovimentacaoPontos toDomain(MovimentacaoPontosJpaEntity entity) {
        MovimentacaoPontos movimentacaoPontos = new MovimentacaoPontos();
        movimentacaoPontos.setId(entity.getId());
        movimentacaoPontos.setContaFidelidade(
                ContaFidelidadeMapper.toDomain(entity.getContaFidelidade())
        );

        movimentacaoPontos.setPedido(
                PedidoMapper.toDomain(entity.getPedido())
        );
        movimentacaoPontos.setTipo(entity.getTipo());
        movimentacaoPontos.setQuantidade(entity.getQuantidade());
        movimentacaoPontos.setDescricao(entity.getDescricao());
        movimentacaoPontos.setCriadoEm(entity.getCriadoEm());
        return movimentacaoPontos;
    }


}
