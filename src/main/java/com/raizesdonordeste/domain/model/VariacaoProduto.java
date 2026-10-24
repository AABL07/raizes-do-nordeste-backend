package com.raizesdonordeste.domain.model;

import java.util.List;

public class VariacaoProduto {

    private Long id;
    private Produto produto;
    private List<ItemCardapio> itensCardapio;
    private String nome;
    private String descricao;
    private Boolean ativa;
}
