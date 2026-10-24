package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.StatusReservaEstoque;

import java.time.LocalDateTime;

public class ReservaEstoque {

    private Long id;
    private Estoque estoque;
    private ItemPedido itemPedido;
    private MovimentacaoEstoque movimentacaoEstoque;
    private Integer quantidade;
    private StatusReservaEstoque status;
    private LocalDateTime criadoEm;
    private LocalDateTime resolvidoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public ItemPedido getItemPedido() {
        return itemPedido;
    }

    public void setItemPedido(ItemPedido itemPedido) {
        this.itemPedido = itemPedido;
    }

    public MovimentacaoEstoque getMovimentacaoEstoque() {
        return movimentacaoEstoque;
    }

    public void setMovimentacaoEstoque(MovimentacaoEstoque movimentacaoEstoque) {
        this.movimentacaoEstoque = movimentacaoEstoque;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public StatusReservaEstoque getStatus() {
        return status;
    }

    public void setStatus(StatusReservaEstoque status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getResolvidoEm() {
        return resolvidoEm;
    }

    public void setResolvidoEm(LocalDateTime resolvidoEm) {
        this.resolvidoEm = resolvidoEm;
    }
}