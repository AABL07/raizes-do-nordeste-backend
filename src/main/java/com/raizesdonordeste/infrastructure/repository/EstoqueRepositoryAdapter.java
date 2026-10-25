package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.EstoqueRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Estoque;
import com.raizesdonordeste.infrastructure.persistence.mapper.EstoqueMapper;
import java.util.Optional;

@Repository
public class EstoqueRepositoryAdapter implements EstoqueRepository {

    private final EstoqueJpaRepository estoqueJpaRepository;

    public EstoqueRepositoryAdapter(EstoqueJpaRepository estoqueJpaRepository) {
        this.estoqueJpaRepository = estoqueJpaRepository;
    }

    @Override
    public Estoque salvar(Estoque estoque) {

        if (estoque.getId() == null) {
            var novaEntity = EstoqueMapper.toJpa(estoque);
            var salva = estoqueJpaRepository.save(novaEntity);

            return EstoqueMapper.toDomain(salva);
        }

        var entityExistente = estoqueJpaRepository.findById(estoque.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Estoque não encontrado: " + estoque.getId()
                        )
                );

        entityExistente.setQuantidadeFisica(estoque.getQuantidadeFisica());
        entityExistente.setQuantidadeReservada(estoque.getQuantidadeReservada());
        entityExistente.setAtualizadoEm(estoque.getAtualizadoEm());

        var salva = estoqueJpaRepository.save(entityExistente);

        return EstoqueMapper.toDomain(salva);
    }

    @Override
    public Optional<Estoque> buscarPorId(Long id) {
        return estoqueJpaRepository.findById(id)
                .map(EstoqueMapper::toDomain);
    }

    @Override
    public Optional<Estoque> buscarPorUnidadeEProduto(Long unidadeId, Long produtoId) {
        return estoqueJpaRepository.findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .map(EstoqueMapper::toDomain);
    }
}