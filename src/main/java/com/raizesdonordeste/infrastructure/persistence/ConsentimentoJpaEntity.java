package com.raizesdonordeste.infrastructure.persistence;

import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.StatusConsentimento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consentimento")
public class ConsentimentoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJpaEntity cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FinalidadeConsentimento finalidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusConsentimento status;

    @Column(name = "versao_termo", nullable = false, length = 50)
    private String versaoTermo;

    @Column(name = "concedido_em", nullable = false)
    private LocalDateTime concedidoEm;

    @Column(name = "revogado_em")
    private LocalDateTime revogadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteJpaEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteJpaEntity cliente) {
        this.cliente = cliente;
    }

    public FinalidadeConsentimento getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(FinalidadeConsentimento finalidade) {
        this.finalidade = finalidade;
    }

    public StatusConsentimento getStatus() {
        return status;
    }

    public void setStatus(StatusConsentimento status) {
        this.status = status;
    }

    public String getVersaoTermo() {
        return versaoTermo;
    }

    public void setVersaoTermo(String versaoTermo) {
        this.versaoTermo = versaoTermo;
    }

    public LocalDateTime getConcedidoEm() {
        return concedidoEm;
    }

    public void setConcedidoEm(LocalDateTime concedidoEm) {
        this.concedidoEm = concedidoEm;
    }

    public LocalDateTime getRevogadoEm() {
        return revogadoEm;
    }

    public void setRevogadoEm(LocalDateTime revogadoEm) {
        this.revogadoEm = revogadoEm;
    }
}
