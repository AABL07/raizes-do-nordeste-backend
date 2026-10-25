package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.RegistroAuditoria;
import com.raizesdonordeste.infrastructure.persistence.RegistroAuditoriaJpaEntity;

public class RegistroAuditoriaMapper {
    public static RegistroAuditoriaJpaEntity toJpa(RegistroAuditoria registroAuditoria) {
        RegistroAuditoriaJpaEntity entity = new RegistroAuditoriaJpaEntity();
        entity.setId(registroAuditoria.getId());
        entity.setUsuario(
                registroAuditoria.getUsuario() != null
                        ? UsuarioMapper.toJpa(registroAuditoria.getUsuario())
                        : null
        );
        entity.setAcao(registroAuditoria.getAcao());
        entity.setRecursoTipo(registroAuditoria.getRecursoTipo());
        entity.setRecursoId(registroAuditoria.getRecursoId());
        entity.setOrigem(registroAuditoria.getOrigem());
        entity.setDetalhes(registroAuditoria.getDetalhes());
        entity.setCriadoEm(registroAuditoria.getCriadoEm());
        return entity;
    }

    public static RegistroAuditoria toDomain(RegistroAuditoriaJpaEntity entity) {
        RegistroAuditoria registroAuditoria = new RegistroAuditoria();
        registroAuditoria.setId(entity.getId());
        registroAuditoria.setUsuario(
                entity.getUsuario() != null
                        ? UsuarioMapper.toDomain(entity.getUsuario())
                        : null
        );
        registroAuditoria.setAcao(entity.getAcao());
        registroAuditoria.setRecursoTipo(entity.getRecursoTipo());
        registroAuditoria.setRecursoId(entity.getRecursoId());
        registroAuditoria.setOrigem(entity.getOrigem());
        registroAuditoria.setDetalhes(entity.getDetalhes());
        registroAuditoria.setCriadoEm(entity.getCriadoEm());
        return registroAuditoria;
    }
}
