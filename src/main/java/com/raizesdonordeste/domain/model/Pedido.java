package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private Long id;
    private Unidade unidade;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private List<MovimentacaoPontos> movimentacoesPontos;
    private List<TentativaPagamento> tentativasPagamento;
    private List<PromocaoAplicada> promocoesAplicadas;
    private CanalPedido canalPedido;
    private StatusPedido status;
    private BigDecimal subtotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorFinal;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}