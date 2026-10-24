package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.StatusConsentimento;
import com.raizesdonordeste.infrastructure.persistence.ConsentimentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsentimentoJpaRepository
        extends JpaRepository<ConsentimentoJpaEntity, Long> {

    Optional<ConsentimentoJpaEntity> findByClienteIdAndFinalidadeAndStatus(
            Long clienteId,
            FinalidadeConsentimento finalidade,
            StatusConsentimento status
    );

    List<ConsentimentoJpaEntity> findByClienteId(Long clienteId);
}