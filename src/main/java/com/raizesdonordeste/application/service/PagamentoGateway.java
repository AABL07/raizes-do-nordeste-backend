package com.raizesdonordeste.application.service;

import com.raizesdonordeste.domain.enums.ResultadoPagamento;

import java.math.BigDecimal;

public interface PagamentoGateway {

    ResultadoPagamento processar(
            Long pedidoId,
            BigDecimal valor,
            ResultadoPagamento resultadoSimulado
    );
}