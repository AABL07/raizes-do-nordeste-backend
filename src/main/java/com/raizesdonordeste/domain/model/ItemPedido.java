package com.raizesdonordeste.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class ItemPedido {

    private Long id;
    private Pedido pedido;
    private ItemCardapio itemCardapio;
    private List<ReservaEstoque> reservasEstoque;
    private List<MovimentacaoEstoque> movimentacoesEstoque;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ItemCardapio getItemCardapio() {
        return itemCardapio;
    }

    public void setItemCardapio(ItemCardapio itemCardapio) {
        this.itemCardapio = itemCardapio;
    }

    public List<ReservaEstoque> getReservasEstoque() {
        return reservasEstoque;
    }

    public void setReservasEstoque(List<ReservaEstoque> reservasEstoque) {
        this.reservasEstoque = reservasEstoque;
    }

    public List<MovimentacaoEstoque> getMovimentacoesEstoque() {
        return movimentacoesEstoque;
    }

    public void setMovimentacoesEstoque(List<MovimentacaoEstoque> movimentacoesEstoque) {
        this.movimentacoesEstoque = movimentacoesEstoque;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
