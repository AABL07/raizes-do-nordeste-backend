package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.SazonalidadeProduto;
import com.raizesdonordeste.infrastructure.persistence.SazonalidadeProdutoJpaEntity;

public class SazonalidadeProdutoMapper {
    public static SazonalidadeProdutoJpaEntity toJpa(SazonalidadeProduto sazonalidadeProduto)  {
        SazonalidadeProdutoJpaEntity entity = new SazonalidadeProdutoJpaEntity();
        entity.setId(sazonalidadeProduto.getId());
        entity.setItemCardapio(
                ItemCardapioMapper.toJpa(sazonalidadeProduto.getItemCardapio())
        );
        entity.setInicioEm(sazonalidadeProduto.getInicioEm());
        entity.setFimEm(sazonalidadeProduto.getFimEm());
        entity.setAtiva(sazonalidadeProduto.getAtiva());
        return entity;
    }

    public static SazonalidadeProduto toDomain(SazonalidadeProdutoJpaEntity entity) {
        SazonalidadeProduto sazonalidadeProduto  = new SazonalidadeProduto();
        sazonalidadeProduto.setId(entity.getId());
        sazonalidadeProduto.setItemCardapio(
                ItemCardapioMapper.toDomain(entity.getItemCardapio())
        );
        sazonalidadeProduto.setInicioEm(entity.getInicioEm());
        sazonalidadeProduto.setFimEm(entity.getFimEm());
        sazonalidadeProduto.setAtiva(entity.getAtiva());
        return sazonalidadeProduto;
    }
}
