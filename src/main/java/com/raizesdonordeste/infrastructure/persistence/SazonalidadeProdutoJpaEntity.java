package com.raizesdonordeste.infrastructure.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "sazonalidade_produto")
public class SazonalidadeProdutoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_cardapio_id", nullable = false)
    private ItemCardapioJpaEntity itemCardapio;

    @Column(name = "inicio_em", nullable = false)
    private LocalDate inicioEm;

    @Column(name = "fim_em", nullable = false)
    private LocalDate fimEm;

    @Column(nullable = false)
    private Boolean ativa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemCardapioJpaEntity getItemCardapio() {
        return itemCardapio;
    }

    public void setItemCardapio(ItemCardapioJpaEntity itemCardapio) {
        this.itemCardapio = itemCardapio;
    }

    public LocalDate getInicioEm() {
        return inicioEm;
    }

    public void setInicioEm(LocalDate inicioEm) {
        this.inicioEm = inicioEm;
    }

    public LocalDate getFimEm() {
        return fimEm;
    }

    public void setFimEm(LocalDate fimEm) {
        this.fimEm = fimEm;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}