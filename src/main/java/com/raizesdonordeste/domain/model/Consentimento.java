package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.StatusConsentimento;

import java.time.LocalDateTime;

public class Consentimento {

    private Long id;
    private Cliente cliente;
    private FinalidadeConsentimento finalidade;
    private StatusConsentimento status;
    private String versaoTermo;
    private LocalDateTime concedidoEm;
    private LocalDateTime revogadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
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