package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class ContaFidelidade {

    private Long id;
    private Cliente cliente;
    private List<MovimentacaoPontos> movimentacoes;
    private Integer saldoPontos;
    private Integer pontosReservados;
    private Boolean ativa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<MovimentacaoPontos> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<MovimentacaoPontos> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public Integer getSaldoPontos() {
        return saldoPontos;
    }

    public void setSaldoPontos(Integer saldoPontos) {
        this.saldoPontos = saldoPontos;
    }

    public Integer getPontosReservados() {
        return pontosReservados;
    }

    public void setPontosReservados(Integer pontosReservados) {
        this.pontosReservados = pontosReservados;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
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