package com.raizesdonordeste.application.usecase.pedido;

public class CriarItemPedidoCommand {

    private Long itemCardapioId;

    public Long getItemCardapioId() {
        return itemCardapioId;
    }

    public void setItemCardapioId(Long itemCardapioId) {
        this.itemCardapioId = itemCardapioId;
    }

    private Integer quantidade;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
