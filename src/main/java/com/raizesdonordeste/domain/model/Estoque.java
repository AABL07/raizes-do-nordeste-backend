package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Estoque {

    private Long id;
    private Unidade unidade;
    private Produto produto;
    private List<MovimentacaoEstoque> movimentacoes;
    private List<ReservaEstoque> reservas;
    private Integer quantidadeFisica;
    private Integer quantidadeReservada;
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

    public List<MovimentacaoEstoque> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<MovimentacaoEstoque> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public List<ReservaEstoque> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaEstoque> reservas) {
        this.reservas = reservas;
    }

    public Integer getQuantidadeFisica() {
        return quantidadeFisica;
    }

    public void setQuantidadeFisica(Integer quantidadeFisica) {
        this.quantidadeFisica = quantidadeFisica;
    }

    public Integer getQuantidadeReservada() {
        return quantidadeReservada;
    }

    public void setQuantidadeReservada(Integer quantidadeReservada) {
        this.quantidadeReservada = quantidadeReservada;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}