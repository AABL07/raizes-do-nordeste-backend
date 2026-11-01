package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.domain.enums.ResultadoPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProcessarPagamentoRequest {

    @NotBlank
    private String chaveIdempotencia;

    @NotNull
    private ResultadoPagamento resultadoSimulado;

    public String getChaveIdempotencia() {
        return chaveIdempotencia;
    }

    public void setChaveIdempotencia(String chaveIdempotencia) {
        this.chaveIdempotencia = chaveIdempotencia;
    }

    public ResultadoPagamento getResultadoSimulado() {
        return resultadoSimulado;
    }

    public void setResultadoSimulado(
            ResultadoPagamento resultadoSimulado) {

        this.resultadoSimulado = resultadoSimulado;
    }
}