package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {

    Produto salvar(Produto produto);

    Optional<Produto> buscarPorId(Long id);

    List<Produto> buscarTodos();
}