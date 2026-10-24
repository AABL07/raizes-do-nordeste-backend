package com.raizesdonordeste.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_cardapio")
public class ItemCardapioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id", nullable = false)
    private UnidadeJpaEntity unidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutoJpaEntity produto;

    @ManyToOne
    @JoinColumn(name = "variacao_produto_id")
    private VariacaoProdutoJpaEntity variacaoProduto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean disponivel;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnidadeJpaEntity getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeJpaEntity unidade) {
        this.unidade = unidade;
    }

    public ProdutoJpaEntity getProduto() {
        return produto;
    }

    public void setProduto(ProdutoJpaEntity produto) {
        this.produto = produto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
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

    public VariacaoProdutoJpaEntity getVariacaoProduto() {
        return variacaoProduto;
    }

    public void setVariacaoProduto(VariacaoProdutoJpaEntity variacaoProduto) {
        this.variacaoProduto = variacaoProduto;
    }
}
