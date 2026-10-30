package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.ReservaEstoqueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.raizesdonordeste.domain.enums.StatusReservaEstoque;
import java.util.List;

public interface ReservaEstoqueJpaRepository
        extends JpaRepository<ReservaEstoqueJpaEntity, Long> {

    List<ReservaEstoqueJpaEntity> findByItemPedidoPedidoIdAndStatus(
            Long pedidoId,
            StatusReservaEstoque status
    );
}