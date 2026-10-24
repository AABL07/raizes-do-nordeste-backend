package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.ContaFidelidadeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaFidelidadeJpaRepository
        extends JpaRepository<ContaFidelidadeJpaEntity, Long> {

    Optional<ContaFidelidadeJpaEntity> findByClienteId(Long clienteId);
}