package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;
import java.time.LocalDateTime;

public class MovimentacaoPontosResponse {

    private Long id;
    private Long contaFidelidadeId;
    private Long pedidoId;
    private TipoMovimentacaoPontos tipo;
    private Integer quantidade;
    private LocalDateTime criadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContaFidelidadeId() {
        return contaFidelidadeId;
    }

    public void setContaFidelidadeId(Long contaFidelidadeId) {
        this.contaFidelidadeId = contaFidelidadeId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public TipoMovimentacaoPontos getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacaoPontos tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}