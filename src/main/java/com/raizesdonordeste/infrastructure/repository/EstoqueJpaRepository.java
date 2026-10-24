package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.EstoqueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueJpaRepository
        extends JpaRepository<EstoqueJpaEntity, Long> {

    Optional<EstoqueJpaEntity> findByUnidadeIdAndProdutoId(
            Long unidadeId,
            Long produtoId
    );
}