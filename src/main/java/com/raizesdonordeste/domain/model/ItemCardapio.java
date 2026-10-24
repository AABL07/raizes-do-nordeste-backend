package com.raizesdonordeste.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ItemCardapio {

    private Long id;
    private Unidade unidade;
    private Produto produto;
    private VariacaoProduto variacaoProduto;
    private List<ItemPedido> itensPedido;
    private List<SazonalidadeProduto> sazonalidades;
    private BigDecimal preco;
    private Boolean disponivel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}