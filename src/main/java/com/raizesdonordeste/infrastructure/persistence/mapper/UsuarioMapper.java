package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Usuario;
import com.raizesdonordeste.infrastructure.persistence.UsuarioJpaEntity;

public class UsuarioMapper {

    public static UsuarioJpaEntity toJpa(Usuario usuario) {
        UsuarioJpaEntity entity = new UsuarioJpaEntity();

        entity.setId(usuario.getId());
        entity.setUnidade(
                usuario.getUnidade() != null
                        ? UnidadeMapper.toJpa(usuario.getUnidade())
                        : null
        );
        entity.setPerfilAcesso(
                PerfilAcessoMapper.toJpa(usuario.getPerfilAcesso())
        );
        entity.setNome(usuario.getNome());
        entity.setLogin(usuario.getLogin());
        entity.setSenhaHash(usuario.getSenhaHash());
        entity.setAtivo(usuario.getAtivo());
        entity.setCriadoEm(usuario.getCriadoEm());
        entity.setAtualizadoEm(usuario.getAtualizadoEm());

        return entity;
    }

    public static Usuario toDomain(UsuarioJpaEntity entity) {
        Usuario usuario = new Usuario();

        usuario.setId(entity.getId());
        usuario.setUnidade(
                entity.getUnidade() != null
                        ? UnidadeMapper.toDomain(entity.getUnidade())
                        : null
        );
        usuario.setPerfilAcesso(
                PerfilAcessoMapper.toDomain(entity.getPerfilAcesso())
        );
        usuario.setNome(entity.getNome());
        usuario.setLogin(entity.getLogin());
        usuario.setSenhaHash(entity.getSenhaHash());
        usuario.setAtivo(entity.getAtivo());
        usuario.setCriadoEm(entity.getCriadoEm());
        usuario.setAtualizadoEm(entity.getAtualizadoEm());
        return usuario;
    }
}