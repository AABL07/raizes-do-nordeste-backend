package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.ItemPedidoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemPedidoJpaRepository
        extends JpaRepository<ItemPedidoJpaEntity, Long> {

    List<ItemPedidoJpaEntity> findByPedidoId(Long pedidoId);
}