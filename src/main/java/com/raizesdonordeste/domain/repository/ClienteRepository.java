package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorEmail(String email);

    List<Cliente> buscarTodos();
}