package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.ContaFidelidade;
import com.raizesdonordeste.infrastructure.persistence.ContaFidelidadeJpaEntity;

public class ContaFidelidadeMapper {
    public static ContaFidelidadeJpaEntity toJpa(ContaFidelidade contaFidelidade) {
        ContaFidelidadeJpaEntity entity = new ContaFidelidadeJpaEntity();
        entity.setId(contaFidelidade.getId());
        entity.setCliente(
                ClienteMapper.toJpa(contaFidelidade.getCliente())
        );
        entity.setSaldoPontos(contaFidelidade.getSaldoPontos());
        entity.setPontosReservados(contaFidelidade.getPontosReservados());
        entity.setAtiva(contaFidelidade.getAtiva());
        entity.setCriadoEm(contaFidelidade.getCriadoEm());
        entity.setAtualizadoEm(contaFidelidade.getAtualizadoEm());
        return entity;
    }
    public static ContaFidelidade toDomain(ContaFidelidadeJpaEntity entity) {
        ContaFidelidade contaFidelidade = new ContaFidelidade();
        contaFidelidade.setId(entity.getId());
        contaFidelidade.setCliente(
                ClienteMapper.toDomain(entity.getCliente())
        );
        contaFidelidade.setSaldoPontos(entity.getSaldoPontos());
        contaFidelidade.setPontosReservados(entity.getPontosReservados());
        contaFidelidade.setAtiva(entity.getAtiva());
        contaFidelidade.setCriadoEm(entity.getCriadoEm());
        contaFidelidade.setAtualizadoEm(entity.getAtualizadoEm());
        return contaFidelidade;
    }
}
