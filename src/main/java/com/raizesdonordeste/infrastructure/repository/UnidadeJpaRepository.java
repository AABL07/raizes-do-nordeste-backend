package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.UnidadeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadeJpaRepository
        extends JpaRepository<UnidadeJpaEntity, Long> {
}