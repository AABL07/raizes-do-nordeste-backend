package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteJpaRepository
        extends JpaRepository<ClienteJpaEntity, Long> {

    Optional<ClienteJpaEntity> findByEmail(String email);
}