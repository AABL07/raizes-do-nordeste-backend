package com.raizesdonordeste.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cliente {

    private Long id;
    private Usuario usuario;
    private List<Pedido> pedidos;
    private ContaFidelidade contaFidelidade;
    private List<Consentimento> consentimentos;
    private String nome;
    private String email;
    private String telefone;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

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

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public ContaFidelidade getContaFidelidade() {
        return contaFidelidade;
    }

    public void setContaFidelidade(ContaFidelidade contaFidelidade) {
        this.contaFidelidade = contaFidelidade;
    }

    public List<Consentimento> getConsentimentos() {
        return consentimentos;
    }

    public void setConsentimentos(List<Consentimento> consentimentos) {
        this.consentimentos = consentimentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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
}