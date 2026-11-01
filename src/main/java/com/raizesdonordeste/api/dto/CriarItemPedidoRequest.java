package com.raizesdonordeste.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CriarItemPedidoRequest {

    @NotNull
    private Long itemCardapioId;

    @NotNull
    @Min(1)
    private Integer quantidade;

    public Long getItemCardapioId() {
        return itemCardapioId;
    }

    public void setItemCardapioId(Long itemCardapioId) {
        this.itemCardapioId = itemCardapioId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}