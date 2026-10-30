package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.MovimentacaoEstoque;

import java.util.Optional;

public interface MovimentacaoEstoqueRepository {

    MovimentacaoEstoque salvar(MovimentacaoEstoque movimentacaoEstoque);

    Optional<MovimentacaoEstoque> buscarPorId(Long id);
}