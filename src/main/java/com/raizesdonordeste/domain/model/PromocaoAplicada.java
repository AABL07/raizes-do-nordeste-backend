package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.TipoBeneficio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PromocaoAplicada {

    private Long id;
    private TipoBeneficio tipoBeneficio;
    private BigDecimal valorBeneficio;
    private BigDecimal valorDescontoAplicado;
    private LocalDateTime aplicadoEm;
}