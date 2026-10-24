package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.infrastructure.persistence.PerfilAcessoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilAcessoJpaRepository
        extends JpaRepository<PerfilAcessoJpaEntity, Long> {

    Optional<PerfilAcessoJpaEntity> findByNome(String nome);
}