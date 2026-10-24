package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cliente {

    private Long id;
    private Usuario usuario;
    private List<Pedido> pedidos;
    private ContaFidelidade contaFidelidade;
    private List<Consentimento> consentimentos;
    private String nome;
    private String email;
    private String telefone;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}