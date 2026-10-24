package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;

import java.time.LocalDateTime;

public class MovimentacaoPontos {

    private Long id;
    private ContaFidelidade contaFidelidade;
    private Pedido pedido;
    private TipoMovimentacaoPontos tipo;
    private Integer quantidade;
    private String descricao;
    private LocalDateTime criadoEm;
}