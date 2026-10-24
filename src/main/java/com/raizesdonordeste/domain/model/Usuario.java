package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;

public class Usuario {

    private Long id;
    private String nome;
    private String login;
    private String senhaHash;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}