package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.OrigemAuditoria;

import java.time.LocalDateTime;

public class RegistroAuditoria {

    private Long id;
    private Usuario usuario;
    private String acao;
    private String recursoTipo;
    private Long recursoId;
    private OrigemAuditoria origem;
    private String detalhes;
    private LocalDateTime criadoEm;
}
