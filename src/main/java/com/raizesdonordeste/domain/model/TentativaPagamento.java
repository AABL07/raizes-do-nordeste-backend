package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.ResultadoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TentativaPagamento {

    private Long id;
    private String chaveIdempotencia;
    private ResultadoPagamento resultado;
    private BigDecimal valorSolicitado;
    private String mensagemRetorno;
    private LocalDateTime criadoEm;
}
