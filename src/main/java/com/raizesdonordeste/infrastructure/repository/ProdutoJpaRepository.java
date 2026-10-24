package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.ProdutoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoJpaRepository
        extends JpaRepository<ProdutoJpaEntity, Long> {
}