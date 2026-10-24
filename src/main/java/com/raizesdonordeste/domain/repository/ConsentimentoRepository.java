package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.model.Consentimento;

import java.util.List;
import java.util.Optional;

public interface ConsentimentoRepository {

    Consentimento salvar(Consentimento consentimento);

    Optional<Consentimento> buscarPorId(Long id);

    Optional<Consentimento> buscarAtivoPorClienteEFinalidade(
            Long clienteId,
            FinalidadeConsentimento finalidade
    );

    List<Consentimento> buscarPorCliente(Long clienteId);
}