package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Unidade;

import java.util.List;
import java.util.Optional;

public interface UnidadeRepository {

    Unidade salvar(Unidade unidade);

    Optional<Unidade> buscarPorId(Long id);

    List<Unidade> buscarTodas();
}