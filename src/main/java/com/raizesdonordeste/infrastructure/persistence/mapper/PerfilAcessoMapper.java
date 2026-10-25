package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.PerfilAcesso;
import com.raizesdonordeste.infrastructure.persistence.PerfilAcessoJpaEntity;

public class PerfilAcessoMapper {
    public static PerfilAcessoJpaEntity toJpa(PerfilAcesso perfilAcesso) {
        PerfilAcessoJpaEntity entity = new PerfilAcessoJpaEntity();

        entity.setId(perfilAcesso.getId());
        entity.setNome(perfilAcesso.getNome());
        entity.setDescricao(perfilAcesso.getDescricao());
        entity.setAtivo(perfilAcesso.getAtivo());
        return entity;
    }

    public static PerfilAcesso toDomain(PerfilAcessoJpaEntity entity) {
        PerfilAcesso perfilAcesso = new PerfilAcesso();

        perfilAcesso.setId(entity.getId());
        perfilAcesso.setNome(entity.getNome());
        perfilAcesso.setDescricao(entity.getDescricao());
        perfilAcesso.setAtivo(entity.getAtivo());

        return perfilAcesso;
    }
}
