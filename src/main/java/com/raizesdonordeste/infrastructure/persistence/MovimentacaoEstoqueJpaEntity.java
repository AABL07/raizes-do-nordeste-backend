package com.raizesdonordeste.infrastructure.persistence;

import com.raizesdonordeste.domain.enums.TipoMovimentacaoEstoque;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoqueJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estoque_id", nullable = false)
    private EstoqueJpaEntity estoque;

    @ManyToOne
    @JoinColumn(name = "item_pedido_id")
    private ItemPedidoJpaEntity itemPedido;

    @OneToOne
    @JoinColumn(name = "reserva_estoque_id", unique = true)
    private ReservaEstoqueJpaEntity reservaEstoque;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimentacaoEstoque tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, length = 255)
    private String motivo;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstoqueJpaEntity getEstoque() {
        return estoque;
    }

    public void setEstoque(EstoqueJpaEntity estoque) {
        this.estoque = estoque;
    }

    public ItemPedidoJpaEntity getItemPedido() {
        return itemPedido;
    }

    public void setItemPedido(ItemPedidoJpaEntity itemPedido) {
        this.itemPedido = itemPedido;
    }

    public ReservaEstoqueJpaEntity getReservaEstoque() {
        return reservaEstoque;
    }

    public void setReservaEstoque(ReservaEstoqueJpaEntity reservaEstoque) {
        this.reservaEstoque = reservaEstoque;
    }

    public TipoMovimentacaoEstoque getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacaoEstoque tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}