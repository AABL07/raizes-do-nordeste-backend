package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Estoque {

    private Long id;
    private Unidade unidade;
    private Produto produto;
    private List<MovimentacaoEstoque> movimentacoes;
    private List<ReservaEstoque> reservas;
    private Integer quantidadeFisica;
    private Integer quantidadeReservada;
    private LocalDateTime atualizadoEm;
}