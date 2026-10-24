package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.ResultadoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TentativaPagamento {

    private Long id;
    private Pedido pedido;
    private String chaveIdempotencia;
    private ResultadoPagamento resultado;
    private BigDecimal valorSolicitado;
    private String mensagemRetorno;
    private LocalDateTime criadoEm;

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

    public String getChaveIdempotencia() {
        return chaveIdempotencia;
    }

    public void setChaveIdempotencia(String chaveIdempotencia) {
        this.chaveIdempotencia = chaveIdempotencia;
    }

    public ResultadoPagamento getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoPagamento resultado) {
        this.resultado = resultado;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public String getMensagemRetorno() {
        return mensagemRetorno;
    }

    public void setMensagemRetorno(String mensagemRetorno) {
        this.mensagemRetorno = mensagemRetorno;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
