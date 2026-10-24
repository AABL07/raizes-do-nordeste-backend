package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class ContaFidelidade {

    private Long id;
    private Cliente cliente;
    private List<MovimentacaoPontos> movimentacoes;
    private Integer saldoPontos;
    private Integer pontosReservados;
    private Boolean ativa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}