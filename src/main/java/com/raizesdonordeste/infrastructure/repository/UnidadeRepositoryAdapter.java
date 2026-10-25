package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.UnidadeRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Unidade;
import com.raizesdonordeste.infrastructure.persistence.mapper.UnidadeMapper;
import java.util.Optional;
import java.util.List;

@Repository
public class UnidadeRepositoryAdapter implements UnidadeRepository {

    private final UnidadeJpaRepository unidadeJpaRepository;

    public UnidadeRepositoryAdapter(UnidadeJpaRepository unidadeJpaRepository) {
        this.unidadeJpaRepository = unidadeJpaRepository;
    }
    @Override
    public Unidade salvar(Unidade unidade) {
        var entity = UnidadeMapper.toJpa(unidade);
        var salva = unidadeJpaRepository.save(entity);

        return UnidadeMapper.toDomain(salva);
    }
    @Override
    public Optional<Unidade> buscarPorId(Long id) {
        return unidadeJpaRepository.findById(id)
                .map(UnidadeMapper::toDomain);
    }

    @Override
    public List<Unidade> buscarTodas() {
        return unidadeJpaRepository.findAll()
                .stream()
                .map(UnidadeMapper::toDomain)
                .toList();
    }
}

