package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Pedido;
import java.util.List;
import java.util.Optional;
import com.raizesdonordeste.domain.enums.CanalPedido;

public interface PedidoRepository {

    Pedido salvar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    List<Pedido> buscarTodos();

    List<Pedido> buscarPorCanal(CanalPedido canalPedido);

}