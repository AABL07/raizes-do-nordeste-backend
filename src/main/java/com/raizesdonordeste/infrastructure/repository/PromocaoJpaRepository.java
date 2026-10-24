package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.PromocaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PromocaoJpaRepository
        extends JpaRepository<PromocaoJpaEntity, Long> {

    List<PromocaoJpaEntity> findByAtivaTrueAndInicioEmLessThanEqualAndFimEmGreaterThanEqual(
            LocalDateTime inicio,
            LocalDateTime fim
    );
}