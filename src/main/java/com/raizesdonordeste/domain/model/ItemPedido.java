package com.raizesdonordeste.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class ItemPedido {

    private Long id;
    private Pedido pedido;
    private ItemCardapio itemCardapio;
    private List<ReservaEstoque> reservasEstoque;
    private List<MovimentacaoEstoque> movimentacoesEstoque;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}
