package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.MovimentacaoPontosJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoPontosJpaRepository
        extends JpaRepository<MovimentacaoPontosJpaEntity, Long> {

    List<MovimentacaoPontosJpaEntity> findByContaFidelidadeId(
            Long contaFidelidadeId
    );

    List<MovimentacaoPontosJpaEntity> findByPedidoId(
            Long pedidoId
    );
}