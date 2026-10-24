package com.raizesdonordeste.domain.model;

import java.util.List;

public class Produto {

    private Long id;
    private List<VariacaoProduto> variacoes;
    private List<ItemCardapio> itensCardapio;
    private List<Estoque> estoques;
    private String nome;
    private String descricao;
    private Boolean ativo;
}