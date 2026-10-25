package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.PerfilAcessoRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.PerfilAcesso;
import com.raizesdonordeste.infrastructure.persistence.mapper.PerfilAcessoMapper;
import java.util.Optional;
import java.util.List;

@Repository
public class PerfilAcessoRepositoryAdapter implements PerfilAcessoRepository {

    private final PerfilAcessoJpaRepository perfilAcessoJpaRepository;

    public PerfilAcessoRepositoryAdapter(
            PerfilAcessoJpaRepository perfilAcessoJpaRepository
    ) {
        this.perfilAcessoJpaRepository = perfilAcessoJpaRepository;
    }

    @Override
    public PerfilAcesso salvar(PerfilAcesso perfilAcesso) {
        var entity = PerfilAcessoMapper.toJpa(perfilAcesso);
        var salvo = perfilAcessoJpaRepository.save(entity);

        return PerfilAcessoMapper.toDomain(salvo);
    }

    @Override
    public Optional<PerfilAcesso> buscarPorId(Long id) {
        return perfilAcessoJpaRepository.findById(id)
                .map(PerfilAcessoMapper::toDomain);
    }

    @Override
    public Optional<PerfilAcesso> buscarPorNome(String nome) {
        return perfilAcessoJpaRepository.findByNome(nome)
                .map(PerfilAcessoMapper::toDomain);
    }

    @Override
    public List<PerfilAcesso> buscarTodos() {
        return perfilAcessoJpaRepository.findAll()
                .stream()
                .map(PerfilAcessoMapper::toDomain)
                .toList();
    }


}