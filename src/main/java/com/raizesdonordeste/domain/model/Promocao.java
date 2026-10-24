package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.TipoBeneficio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Promocao {

    private Long id;
    private List<PromocaoAplicada> promocoesAplicadas;
    private String nome;
    private String descricao;
    private TipoBeneficio tipoBeneficio;
    private BigDecimal valorBeneficio;
    private Boolean permiteCombinacao;
    private LocalDateTime inicioEm;
    private LocalDateTime fimEm;
    private Boolean ativa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PromocaoAplicada> getPromocoesAplicadas() {
        return promocoesAplicadas;
    }

    public void setPromocoesAplicadas(List<PromocaoAplicada> promocoesAplicadas) {
        this.promocoesAplicadas = promocoesAplicadas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoBeneficio getTipoBeneficio() {
        return tipoBeneficio;
    }

    public void setTipoBeneficio(TipoBeneficio tipoBeneficio) {
        this.tipoBeneficio = tipoBeneficio;
    }

    public BigDecimal getValorBeneficio() {
        return valorBeneficio;
    }

    public void setValorBeneficio(BigDecimal valorBeneficio) {
        this.valorBeneficio = valorBeneficio;
    }

    public Boolean getPermiteCombinacao() {
        return permiteCombinacao;
    }

    public void setPermiteCombinacao(Boolean permiteCombinacao) {
        this.permiteCombinacao = permiteCombinacao;
    }

    public LocalDateTime getInicioEm() {
        return inicioEm;
    }

    public void setInicioEm(LocalDateTime inicioEm) {
        this.inicioEm = inicioEm;
    }

    public LocalDateTime getFimEm() {
        return fimEm;
    }

    public void setFimEm(LocalDateTime fimEm) {
        this.fimEm = fimEm;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}