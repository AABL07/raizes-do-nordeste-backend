package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.domain.enums.ResultadoPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoResponse {

    private Long id;
    private Long pedidoId;
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

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
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