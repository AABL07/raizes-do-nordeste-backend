package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.VariacaoProduto;
import com.raizesdonordeste.infrastructure.persistence.VariacaoProdutoJpaEntity;

public class VariacaoProdutoMapper {
    public static VariacaoProdutoJpaEntity toJpa(VariacaoProduto variacaoProduto)
        {
        VariacaoProdutoJpaEntity entity = new VariacaoProdutoJpaEntity();
        entity.setId(variacaoProduto.getId());
        entity.setProduto(
                    ProdutoMapper.toJpa(variacaoProduto.getProduto())
            );
        entity.setNome(variacaoProduto.getNome());
        entity.setDescricao(variacaoProduto.getDescricao());
        entity.setAtiva(variacaoProduto.getAtiva());
        return entity;
        }

    public static VariacaoProduto toDomain(VariacaoProdutoJpaEntity entity)
        {
        VariacaoProduto variacaoProduto = new VariacaoProduto();
        variacaoProduto.setId(entity.getId());
        variacaoProduto.setProduto(
                    ProdutoMapper.toDomain(entity.getProduto())
            );
        variacaoProduto.setNome(entity.getNome());
        variacaoProduto.setDescricao(entity.getDescricao());
        variacaoProduto.setAtiva(entity.getAtiva());
        return variacaoProduto;
        }
}
