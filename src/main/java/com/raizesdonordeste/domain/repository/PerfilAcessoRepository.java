package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.PerfilAcesso;

import java.util.List;
import java.util.Optional;

public interface PerfilAcessoRepository {

    PerfilAcesso salvar(PerfilAcesso perfilAcesso);

    Optional<PerfilAcesso> buscarPorId(Long id);

    Optional<PerfilAcesso> buscarPorNome(String nome);

    List<PerfilAcesso> buscarTodos();
}