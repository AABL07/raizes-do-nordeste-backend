package com.raizesdonordeste.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ItemCardapio {

    private Long id;
    private Unidade unidade;
    private Produto produto;
    private VariacaoProduto variacaoProduto;
    private List<ItemPedido> itensPedido;
    private List<SazonalidadeProduto> sazonalidades;
    private BigDecimal preco;
    private Boolean disponivel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public VariacaoProduto getVariacaoProduto() {
        return variacaoProduto;
    }

    public void setVariacaoProduto(VariacaoProduto variacaoProduto) {
        this.variacaoProduto = variacaoProduto;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public List<SazonalidadeProduto> getSazonalidades() {
        return sazonalidades;
    }

    public void setSazonalidades(List<SazonalidadeProduto> sazonalidades) {
        this.sazonalidades = sazonalidades;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}