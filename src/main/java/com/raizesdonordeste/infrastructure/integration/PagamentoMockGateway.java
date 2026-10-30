package com.raizesdonordeste.infrastructure.integration;

import com.raizesdonordeste.application.service.PagamentoGateway;
import com.raizesdonordeste.domain.enums.ResultadoPagamento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PagamentoMockGateway implements PagamentoGateway {

    @Override
    public ResultadoPagamento processar(
            Long pedidoId,
            BigDecimal valor,
            ResultadoPagamento resultadoSimulado) {

        return resultadoSimulado;
    }
}