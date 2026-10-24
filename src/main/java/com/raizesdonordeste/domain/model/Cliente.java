package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;

public class Cliente {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}