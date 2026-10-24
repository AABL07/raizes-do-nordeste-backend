package com.raizesdonordeste.infrastructure.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "estoque",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_estoque_unidade_produto",
                        columnNames = {"unidade_id", "produto_id"}
                )
        }
)
public class EstoqueJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id", nullable = false)
    private UnidadeJpaEntity unidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutoJpaEntity produto;

    @Column(name = "quantidade_fisica", nullable = false)
    private Integer quantidadeFisica;

    @Column(name = "quantidade_reservada", nullable = false)
    private Integer quantidadeReservada;

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

    public UnidadeJpaEntity getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeJpaEntity unidade) {
        this.unidade = unidade;
    }

    public ProdutoJpaEntity getProduto() {
        return produto;
    }

    public void setProduto(ProdutoJpaEntity produto) {
        this.produto = produto;
    }

    public Integer getQuantidadeFisica() {
        return quantidadeFisica;
    }

    public void setQuantidadeFisica(Integer quantidadeFisica) {
        this.quantidadeFisica = quantidadeFisica;
    }

    public Integer getQuantidadeReservada() {
        return quantidadeReservada;
    }

    public void setQuantidadeReservada(Integer quantidadeReservada) {
        this.quantidadeReservada = quantidadeReservada;
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