package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.TentativaPagamento;

import java.util.List;
import java.util.Optional;

public interface TentativaPagamentoRepository {

    TentativaPagamento salvar(TentativaPagamento tentativaPagamento);

    Optional<TentativaPagamento> buscarPorId(Long id);

    Optional<TentativaPagamento> buscarPorPedidoEChaveIdempotencia(
            Long pedidoId,
            String chaveIdempotencia
    );

    List<TentativaPagamento> buscarPorPedido(Long pedidoId);
}