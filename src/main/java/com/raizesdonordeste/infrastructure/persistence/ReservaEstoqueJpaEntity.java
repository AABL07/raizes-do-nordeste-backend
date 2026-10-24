package com.raizesdonordeste.infrastructure.persistence;

import com.raizesdonordeste.domain.enums.StatusReservaEstoque;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reserva_estoque")
public class ReservaEstoqueJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estoque_id", nullable = false)
    private EstoqueJpaEntity estoque;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_pedido_id", nullable = false)
    private ItemPedidoJpaEntity itemPedido;

    @Column(nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusReservaEstoque status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "resolvido_em")
    private LocalDateTime resolvidoEm;

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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public StatusReservaEstoque getStatus() {
        return status;
    }

    public void setStatus(StatusReservaEstoque status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getResolvidoEm() {
        return resolvidoEm;
    }

    public void setResolvidoEm(LocalDateTime resolvidoEm) {
        this.resolvidoEm = resolvidoEm;
    }
}