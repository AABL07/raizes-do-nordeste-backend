package com.raizesdonordeste.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemCardapio {

    private Long id;
    private BigDecimal preco;
    private Boolean disponivel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}