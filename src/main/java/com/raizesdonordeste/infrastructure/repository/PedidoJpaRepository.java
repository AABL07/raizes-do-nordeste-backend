package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.PedidoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.raizesdonordeste.domain.enums.CanalPedido;
import java.util.List;

public interface PedidoJpaRepository
        extends JpaRepository<PedidoJpaEntity, Long> {

    List<PedidoJpaEntity> findByCanalPedido(
            CanalPedido canalPedido
    );
}