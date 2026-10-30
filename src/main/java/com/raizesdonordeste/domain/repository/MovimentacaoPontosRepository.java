package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.MovimentacaoPontos;

import java.util.List;
import java.util.Optional;

public interface MovimentacaoPontosRepository {

    MovimentacaoPontos salvar(MovimentacaoPontos movimentacaoPontos);

    Optional<MovimentacaoPontos> buscarPorId(Long id);

    List<MovimentacaoPontos> buscarPorContaFidelidade(Long contaFidelidadeId);

    List<MovimentacaoPontos> buscarPorPedido(Long pedidoId);
}