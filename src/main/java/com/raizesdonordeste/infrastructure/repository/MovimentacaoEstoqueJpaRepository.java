package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.MovimentacaoEstoqueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoEstoqueJpaRepository
        extends JpaRepository<MovimentacaoEstoqueJpaEntity, Long> {
}