package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.ContaFidelidade;

import java.util.Optional;

public interface ContaFidelidadeRepository {

    ContaFidelidade salvar(ContaFidelidade contaFidelidade);

    Optional<ContaFidelidade> buscarPorId(Long id);

    Optional<ContaFidelidade> buscarPorCliente(Long clienteId);
}