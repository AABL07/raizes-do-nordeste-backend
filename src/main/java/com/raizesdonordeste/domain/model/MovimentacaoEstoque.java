package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.TipoMovimentacaoEstoque;

import java.time.LocalDateTime;

public class MovimentacaoEstoque {

    private Long id;
    private TipoMovimentacaoEstoque tipo;
    private Integer quantidade;
    private String motivo;
    private LocalDateTime criadoEm;
}