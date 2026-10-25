package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Unidade;
import com.raizesdonordeste.infrastructure.persistence.UnidadeJpaEntity;

public class UnidadeMapper {

    public static UnidadeJpaEntity toJpa(Unidade unidade) {
        UnidadeJpaEntity entity = new UnidadeJpaEntity();

        entity.setId(unidade.getId());
        entity.setNome(unidade.getNome());
        entity.setRegiao(unidade.getRegiao());
        entity.setLogradouro(unidade.getLogradouro());
        entity.setNumero(unidade.getNumero());
        entity.setComplemento(unidade.getComplemento());
        entity.setBairro(unidade.getBairro());
        entity.setCidade(unidade.getCidade());
        entity.setEstado(unidade.getEstado());
        entity.setCep(unidade.getCep());
        entity.setAtiva(unidade.getAtiva());

        return entity;
    }

    public static Unidade toDomain(UnidadeJpaEntity entity) {
        Unidade unidade = new Unidade();

        unidade.setId(entity.getId());
        unidade.setNome(entity.getNome());
        unidade.setRegiao(entity.getRegiao());
        unidade.setLogradouro(entity.getLogradouro());
        unidade.setNumero(entity.getNumero());
        unidade.setComplemento(entity.getComplemento());
        unidade.setBairro(entity.getBairro());
        unidade.setCidade(entity.getCidade());
        unidade.setEstado(entity.getEstado());
        unidade.setCep(entity.getCep());
        unidade.setAtiva(entity.getAtiva());

        return unidade;
    }
}