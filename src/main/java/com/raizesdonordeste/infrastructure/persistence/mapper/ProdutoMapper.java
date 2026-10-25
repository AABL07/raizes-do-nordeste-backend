package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Produto;
import com.raizesdonordeste.infrastructure.persistence.ProdutoJpaEntity;

public class ProdutoMapper {
    public static ProdutoJpaEntity toJpa(Produto produto)  {
        ProdutoJpaEntity entity = new ProdutoJpaEntity();
        entity.setId(produto.getId());
        entity.setNome(produto.getNome());
        entity.setDescricao(produto.getDescricao());
        entity.setAtivo(produto.getAtivo());
        return entity;
    }
    public static Produto toDomain(ProdutoJpaEntity entity) {
        Produto produto = new Produto();
        produto.setId(entity.getId());
        produto.setNome(entity.getNome());
        produto.setDescricao(entity.getDescricao());
        produto.setAtivo(entity.getAtivo());
        return produto;

    }

}
