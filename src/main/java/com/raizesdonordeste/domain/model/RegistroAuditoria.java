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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getRecursoTipo() {
        return recursoTipo;
    }

    public void setRecursoTipo(String recursoTipo) {
        this.recursoTipo = recursoTipo;
    }

    public Long getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(Long recursoId) {
        this.recursoId = recursoId;
    }

    public OrigemAuditoria getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemAuditoria origem) {
        this.origem = origem;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
