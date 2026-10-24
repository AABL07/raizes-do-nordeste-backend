package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.ItemCardapioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCardapioJpaRepository
        extends JpaRepository<ItemCardapioJpaEntity, Long> {

    List<ItemCardapioJpaEntity> findByUnidadeId(Long unidadeId);
}