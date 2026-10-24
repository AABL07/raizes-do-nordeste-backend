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
}
