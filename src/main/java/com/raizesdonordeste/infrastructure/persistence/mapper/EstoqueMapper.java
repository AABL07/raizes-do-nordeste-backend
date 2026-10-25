package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Estoque;
import com.raizesdonordeste.infrastructure.persistence.EstoqueJpaEntity;

public class EstoqueMapper {

    public static EstoqueJpaEntity toJpa(Estoque estoque)
        {
        EstoqueJpaEntity entity = new EstoqueJpaEntity();
        entity.setId(estoque.getId());
        entity.setUnidade(
                    UnidadeMapper.toJpa(estoque.getUnidade())
            );
        entity.setProduto(
                    ProdutoMapper.toJpa(estoque.getProduto())
            );
        entity.setQuantidadeFisica(estoque.getQuantidadeFisica());
        entity.setQuantidadeReservada(estoque.getQuantidadeReservada());
        entity.setAtualizadoEm(estoque.getAtualizadoEm());
        return entity;
        }
    public static Estoque toDomain(EstoqueJpaEntity entity)
        {
        Estoque estoque = new Estoque();
        estoque.setId(entity.getId());
        estoque.setUnidade(
                    UnidadeMapper.toDomain(entity.getUnidade())
            );

        estoque.setProduto(
                    ProdutoMapper.toDomain(entity.getProduto())
            );
        estoque.setQuantidadeFisica(entity.getQuantidadeFisica());
        estoque.setQuantidadeReservada(entity.getQuantidadeReservada());
        estoque.setAtualizadoEm(entity.getAtualizadoEm());
        return estoque;
        }
}
