package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Consentimento;
import com.raizesdonordeste.infrastructure.persistence.ConsentimentoJpaEntity;

public class ConsentimentoMapper {
    public static ConsentimentoJpaEntity toJpa(Consentimento consentimento) {
        ConsentimentoJpaEntity entity = new ConsentimentoJpaEntity();
        entity.setId(consentimento.getId());
        entity.setCliente(
                ClienteMapper.toJpa(consentimento.getCliente())
        );
        entity.setFinalidade(consentimento.getFinalidade());
        entity.setStatus(consentimento.getStatus());
        entity.setVersaoTermo(consentimento.getVersaoTermo());
        entity.setConcedidoEm(consentimento.getConcedidoEm());
        entity.setRevogadoEm(consentimento.getRevogadoEm());
        return entity;
    }
    public static Consentimento toDomain(ConsentimentoJpaEntity entity) {
        Consentimento consentimento = new Consentimento();
        consentimento.setId(entity.getId());
        consentimento.setCliente(
                ClienteMapper.toDomain(entity.getCliente())
        );
        consentimento.setFinalidade(entity.getFinalidade());
        consentimento.setStatus(entity.getStatus());
        consentimento.setVersaoTermo(entity.getVersaoTermo());
        consentimento.setConcedidoEm(entity.getConcedidoEm());
        consentimento.setRevogadoEm(entity.getRevogadoEm());
        return consentimento;
    }
}
