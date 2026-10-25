package com.raizesdonordeste.infrastructure.persistence;

import com.raizesdonordeste.domain.enums.OrigemAuditoria;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_auditoria")
public class RegistroAuditoriaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioJpaEntity usuario;

    @Column(nullable = false, length = 100)
    private String acao;

    @Column(name = "recurso_tipo", nullable = false, length = 100)
    private String recursoTipo;

    @Column(name = "recurso_id")
    private Long recursoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrigemAuditoria origem;

    @Column(columnDefinition = "TEXT")
    private String detalhes;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioJpaEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioJpaEntity usuario) {
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
