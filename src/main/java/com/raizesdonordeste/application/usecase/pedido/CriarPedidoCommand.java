package com.raizesdonordeste.application.usecase.pedido;

import com.raizesdonordeste.domain.enums.CanalPedido;
import java.util.List;

public class CriarPedidoCommand {

    private Long unidadeId;

    public Long getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(Long unidadeId) {
        this.unidadeId = unidadeId;
    }

    private Long clienteId;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    private CanalPedido canalPedido;

    public CanalPedido getCanalPedido() {
        return canalPedido;
    }

    public void setCanalPedido(CanalPedido canalPedido) {
        this.canalPedido = canalPedido;
    }

    private List<CriarItemPedidoCommand> itens;

    public List<CriarItemPedidoCommand> getItens() {
        return itens;
    }

    public void setItens(List<CriarItemPedidoCommand> itens) {
        this.itens = itens;
    }
}
