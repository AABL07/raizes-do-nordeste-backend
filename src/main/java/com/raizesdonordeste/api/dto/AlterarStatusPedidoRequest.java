package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public class AlterarStatusPedidoRequest {

    @NotNull
    private StatusPedido status;

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}