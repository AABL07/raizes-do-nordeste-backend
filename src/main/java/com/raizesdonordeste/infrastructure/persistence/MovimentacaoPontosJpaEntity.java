package com.raizesdonordeste.infrastructure.persistence;

import com.raizesdonordeste.domain.enums.TipoMovimentacaoPontos;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_pontos")
public class MovimentacaoPontosJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_fidelidade_id", nullable = false)
    private ContaFidelidadeJpaEntity contaFidelidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoJpaEntity pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimentacaoPontos tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 255)
    private String descricao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContaFidelidadeJpaEntity getContaFidelidade() {
        return contaFidelidade;
    }

    public void setContaFidelidade(ContaFidelidadeJpaEntity contaFidelidade) {
        this.contaFidelidade = contaFidelidade;
    }

    public PedidoJpaEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoJpaEntity pedido) {
        this.pedido = pedido;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}