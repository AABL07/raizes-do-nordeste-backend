package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.TipoBeneficio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PromocaoAplicada {

    private Long id;
    private Pedido pedido;
    private Promocao promocao;
    private TipoBeneficio tipoBeneficio;
    private BigDecimal valorBeneficio;
    private BigDecimal valorDescontoAplicado;
    private LocalDateTime aplicadoEm;

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

    public Promocao getPromocao() {
        return promocao;
    }

    public void setPromocao(Promocao promocao) {
        this.promocao = promocao;
    }

    public TipoBeneficio getTipoBeneficio() {
        return tipoBeneficio;
    }

    public void setTipoBeneficio(TipoBeneficio tipoBeneficio) {
        this.tipoBeneficio = tipoBeneficio;
    }

    public BigDecimal getValorBeneficio() {
        return valorBeneficio;
    }

    public void setValorBeneficio(BigDecimal valorBeneficio) {
        this.valorBeneficio = valorBeneficio;
    }

    public BigDecimal getValorDescontoAplicado() {
        return valorDescontoAplicado;
    }

    public void setValorDescontoAplicado(BigDecimal valorDescontoAplicado) {
        this.valorDescontoAplicado = valorDescontoAplicado;
    }

    public LocalDateTime getAplicadoEm() {
        return aplicadoEm;
    }

    public void setAplicadoEm(LocalDateTime aplicadoEm) {
        this.aplicadoEm = aplicadoEm;
    }
}