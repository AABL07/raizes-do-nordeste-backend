package com.raizesdonordeste.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservarPontosRequest {

    @NotNull
    @Min(1)
    private Integer quantidadePontos;

    public Integer getQuantidadePontos() {
        return quantidadePontos;
    }

    public void setQuantidadePontos(Integer quantidadePontos) {
        this.quantidadePontos = quantidadePontos;
    }
}