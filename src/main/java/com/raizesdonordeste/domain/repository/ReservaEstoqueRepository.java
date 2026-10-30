package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.ReservaEstoque;
import java.util.Optional;
import java.util.List;



public interface ReservaEstoqueRepository {

    ReservaEstoque salvar(ReservaEstoque reservaEstoque);

    Optional<ReservaEstoque> buscarPorId(Long id);

    List<ReservaEstoque> buscarAtivasPorPedido(Long pedidoId);
}