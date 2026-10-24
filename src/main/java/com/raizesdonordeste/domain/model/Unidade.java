package com.raizesdonordeste.domain.model;

import java.util.List;

public class Unidade {

    private Long id;
    private List<Pedido> pedidos;
    private List<ItemCardapio> itensCardapio;
    private List<Estoque> estoques;
    private List<Usuario> usuarios;
    private String nome;
    private String regiao;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private Boolean ativa;
}