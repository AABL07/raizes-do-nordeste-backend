package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.PedidoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoJpaRepository
        extends JpaRepository<PedidoJpaEntity, Long> {
}