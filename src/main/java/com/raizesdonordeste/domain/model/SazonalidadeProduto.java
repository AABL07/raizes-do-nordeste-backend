package com.raizesdonordeste.domain.model;

import java.time.LocalDate;

public class SazonalidadeProduto {

    private Long id;
    private ItemCardapio itemCardapio;
    private LocalDate inicioEm;
    private LocalDate fimEm;
    private Boolean ativa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemCardapio getItemCardapio() {
        return itemCardapio;
    }

    public void setItemCardapio(ItemCardapio itemCardapio) {
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
