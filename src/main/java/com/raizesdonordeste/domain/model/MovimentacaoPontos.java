package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;

import java.time.LocalDateTime;

public class MovimentacaoPontos {

    private Long id;
    private ContaFidelidade contaFidelidade;
    private Pedido pedido;
    private TipoMovimentacaoPontos tipo;
    private Integer quantidade;
    private String descricao;
    private LocalDateTime criadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContaFidelidade getContaFidelidade() {
        return contaFidelidade;
    }

    public void setContaFidelidade(ContaFidelidade contaFidelidade) {
        this.contaFidelidade = contaFidelidade;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public TipoMovimentacaoPontos getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacaoPontos tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}