package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Usuario;
import com.raizesdonordeste.infrastructure.persistence.mapper.UsuarioMapper;
import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        var entity = UsuarioMapper.toJpa(usuario);
        var salvo = usuarioJpaRepository.save(entity);

        return UsuarioMapper.toDomain(salvo);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioJpaRepository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        return usuarioJpaRepository.findByLogin(login)
                .map(UsuarioMapper::toDomain);
    }
}