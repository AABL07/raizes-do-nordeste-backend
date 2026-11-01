package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.domain.enums.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CriarPedidoRequest {

    @NotNull
    private Long unidadeId;

    private Long clienteId;

    @NotNull
    private CanalPedido canalPedido;

    @Valid
    @NotEmpty
    private List<CriarItemPedidoRequest> itens;

    public Long getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(Long unidadeId) {
        this.unidadeId = unidadeId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public CanalPedido getCanalPedido() {
        return canalPedido;
    }

    public void setCanalPedido(CanalPedido canalPedido) {
        this.canalPedido = canalPedido;
    }

    public List<CriarItemPedidoRequest> getItens() {
        return itens;
    }

    public void setItens(List<CriarItemPedidoRequest> itens) {
        this.itens = itens;
    }
}