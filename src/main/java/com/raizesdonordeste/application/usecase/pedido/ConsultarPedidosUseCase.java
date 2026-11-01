package com.raizesdonordeste.application.usecase.pedido;

import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.model.Pedido;
import com.raizesdonordeste.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConsultarPedidosUseCase {

    private final PedidoRepository pedidoRepository;

    public ConsultarPedidosUseCase(
            PedidoRepository pedidoRepository) {

        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long pedidoId) {

        return pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Pedido não encontrado."
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Pedido> listar(CanalPedido canalPedido) {

        if (canalPedido == null) {
            return pedidoRepository.buscarTodos();
        }

        return pedidoRepository.buscarPorCanal(canalPedido);
    }
}