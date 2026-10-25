package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.ItemCardapio;
import com.raizesdonordeste.infrastructure.persistence.ItemCardapioJpaEntity;

public class ItemCardapioMapper {
    public static ItemCardapioJpaEntity toJpa(ItemCardapio itemCardapio)
        {
        ItemCardapioJpaEntity entity = new ItemCardapioJpaEntity();
        entity.setId(itemCardapio.getId());
        entity.setUnidade(
                UnidadeMapper.toJpa(itemCardapio.getUnidade())
            );

        entity.setProduto(
                ProdutoMapper.toJpa(itemCardapio.getProduto())
            );
        entity.setVariacaoProduto(
                    itemCardapio.getVariacaoProduto() != null
                            ? VariacaoProdutoMapper.toJpa(itemCardapio.getVariacaoProduto())
                            : null
            );
        entity.setPreco(itemCardapio.getPreco());
        entity.setDisponivel(itemCardapio.getDisponivel());
        entity.setCriadoEm(itemCardapio.getCriadoEm());
        entity.setAtualizadoEm(itemCardapio.getAtualizadoEm());
        return entity;
        }
    public static ItemCardapio toDomain(ItemCardapioJpaEntity entity)
        {
        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setId(entity.getId());
        itemCardapio.setUnidade(
                UnidadeMapper.toDomain(entity.getUnidade())
            );

        itemCardapio.setProduto(
                ProdutoMapper.toDomain(entity.getProduto())
            );
        itemCardapio.setVariacaoProduto(
                    entity.getVariacaoProduto() != null
                            ? VariacaoProdutoMapper.toDomain(entity.getVariacaoProduto())
                            : null
            );
        itemCardapio.setPreco(entity.getPreco());
        itemCardapio.setDisponivel(entity.getDisponivel());
        itemCardapio.setCriadoEm(entity.getCriadoEm());
        itemCardapio.setAtualizadoEm(entity.getAtualizadoEm());
        return itemCardapio;
        }
}
