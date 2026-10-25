package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Cliente;
import com.raizesdonordeste.infrastructure.persistence.ClienteJpaEntity;

public class ClienteMapper {

    public static ClienteJpaEntity toJpa(Cliente cliente) {
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.setId(cliente.getId());
        entity.setUsuario(
                cliente.getUsuario() != null
                        ? UsuarioMapper.toJpa(cliente.getUsuario())
                        : null
        );
        entity.setNome(cliente.getNome());
        entity.setEmail(cliente.getEmail());
        entity.setTelefone(cliente.getTelefone());
        entity.setAtivo(cliente.getAtivo());
        entity.setCriadoEm(cliente.getCriadoEm());
        entity.setAtualizadoEm(cliente.getAtualizadoEm());

        return entity;
        }

    public static Cliente toDomain(ClienteJpaEntity entity) {
        Cliente cliente = new Cliente();
        cliente.setId(entity.getId());
        cliente.setUsuario(
                entity.getUsuario() != null
                        ? UsuarioMapper.toDomain(entity.getUsuario())
                        : null
        );
        cliente.setNome(entity.getNome());
        cliente.setEmail(entity.getEmail());
        cliente.setTelefone(entity.getTelefone());
        cliente.setAtivo(entity.getAtivo());
        cliente.setCriadoEm(entity.getCriadoEm());
        cliente.setAtualizadoEm(entity.getAtualizadoEm());
        return cliente;
        }
}
