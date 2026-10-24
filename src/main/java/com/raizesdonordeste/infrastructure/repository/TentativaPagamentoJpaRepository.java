package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.TentativaPagamentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TentativaPagamentoJpaRepository
        extends JpaRepository<TentativaPagamentoJpaEntity, Long> {

    Optional<TentativaPagamentoJpaEntity> findByPedidoIdAndChaveIdempotencia(
            Long pedidoId,
            String chaveIdempotencia
    );

    List<TentativaPagamentoJpaEntity> findByPedidoId(Long pedidoId);
}