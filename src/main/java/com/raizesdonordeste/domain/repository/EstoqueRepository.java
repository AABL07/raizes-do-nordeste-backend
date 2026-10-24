package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Estoque;

import java.util.Optional;

public interface EstoqueRepository {

    Estoque salvar(Estoque estoque);

    Optional<Estoque> buscarPorId(Long id);

    Optional<Estoque> buscarPorUnidadeEProduto(Long unidadeId, Long produtoId);
}