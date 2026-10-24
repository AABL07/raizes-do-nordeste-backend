package com.raizesdonordeste.infrastructure.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conta_fidelidade")
public class ContaFidelidadeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private ClienteJpaEntity cliente;

    @Column(name = "saldo_pontos", nullable = false)
    private Integer saldoPontos;

    @Column(name = "pontos_reservados", nullable = false)
    private Integer pontosReservados;

    @Column(nullable = false)
    private Boolean ativa;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Version
    @Column(nullable = false)
    private Long version;

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

    public Integer getSaldoPontos() {
        return saldoPontos;
    }

    public void setSaldoPontos(Integer saldoPontos) {
        this.saldoPontos = saldoPontos;
    }

    public Integer getPontosReservados() {
        return pontosReservados;
    }

    public void setPontosReservados(Integer pontosReservados) {
        this.pontosReservados = pontosReservados;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
