package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.TipoBeneficio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Promocao {

    private Long id;
    private List<PromocaoAplicada> promocoesAplicadas;
    private String nome;
    private String descricao;
    private TipoBeneficio tipoBeneficio;
    private BigDecimal valorBeneficio;
    private Boolean permiteCombinacao;
    private LocalDateTime inicioEm;
    private LocalDateTime fimEm;
    private Boolean ativa;
}