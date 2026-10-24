package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Usuario {

    private Long id;
    private Unidade unidade;
    private PerfilAcesso perfilAcesso;
    private Cliente cliente;
    private List<RegistroAuditoria> registrosAuditoria;
    private String nome;
    private String login;
    private String senhaHash;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}