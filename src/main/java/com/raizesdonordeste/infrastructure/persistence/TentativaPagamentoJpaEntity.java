package com.raizesdonordeste.infrastructure.persistence;

import com.raizesdonordeste.domain.enums.ResultadoPagamento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "tentativa_pagamento",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_tentativa_pagamento_pedido_chave",
                        columnNames = {"pedido_id", "chave_idempotencia"}
                )
        }
)
public class TentativaPagamentoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoJpaEntity pedido;

    @Column(name = "chave_idempotencia", nullable = false, length = 100)
    private String chaveIdempotencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ResultadoPagamento resultado;

    @Column(name = "valor_solicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorSolicitado;

    @Column(name = "mensagem_retorno", length = 255)
    private String mensagemRetorno;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PedidoJpaEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoJpaEntity pedido) {
        this.pedido = pedido;
    }

    public String getChaveIdempotencia() {
        return chaveIdempotencia;
    }

    public void setChaveIdempotencia(String chaveIdempotencia) {
        this.chaveIdempotencia = chaveIdempotencia;
    }

    public ResultadoPagamento getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoPagamento resultado) {
        this.resultado = resultado;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public String getMensagemRetorno() {
        return mensagemRetorno;
    }

    public void setMensagemRetorno(String mensagemRetorno) {
        this.mensagemRetorno = mensagemRetorno;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}