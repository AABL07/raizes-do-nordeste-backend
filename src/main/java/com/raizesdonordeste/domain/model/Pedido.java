package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private Long id;
    private Unidade unidade;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private List<MovimentacaoPontos> movimentacoesPontos;
    private List<TentativaPagamento> tentativasPagamento;
    private List<PromocaoAplicada> promocoesAplicadas;
    private CanalPedido canalPedido;
    private StatusPedido status;
    private BigDecimal subtotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorFinal;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public List<MovimentacaoPontos> getMovimentacoesPontos() {
        return movimentacoesPontos;
    }

    public void setMovimentacoesPontos(List<MovimentacaoPontos> movimentacoesPontos) {
        this.movimentacoesPontos = movimentacoesPontos;
    }

    public List<TentativaPagamento> getTentativasPagamento() {
        return tentativasPagamento;
    }

    public void setTentativasPagamento(List<TentativaPagamento> tentativasPagamento) {
        this.tentativasPagamento = tentativasPagamento;
    }

    public List<PromocaoAplicada> getPromocoesAplicadas() {
        return promocoesAplicadas;
    }

    public void setPromocoesAplicadas(List<PromocaoAplicada> promocoesAplicadas) {
        this.promocoesAplicadas = promocoesAplicadas;
    }

    public CanalPedido getCanalPedido() {
        return canalPedido;
    }

    public void setCanalPedido(CanalPedido canalPedido) {
        this.canalPedido = canalPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
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