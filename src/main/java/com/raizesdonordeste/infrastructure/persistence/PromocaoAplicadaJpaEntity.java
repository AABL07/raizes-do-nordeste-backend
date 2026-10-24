package com.raizesdonordeste.infrastructure.persistence;

import com.raizesdonordeste.domain.enums.TipoBeneficio;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocao_aplicada")
public class PromocaoAplicadaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoJpaEntity pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "promocao_id", nullable = false)
    private PromocaoJpaEntity promocao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_beneficio", nullable = false, length = 30)
    private TipoBeneficio tipoBeneficio;

    @Column(name = "valor_beneficio", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorBeneficio;

    @Column(name = "valor_desconto_aplicado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorDescontoAplicado;

    @Column(name = "aplicado_em", nullable = false)
    private LocalDateTime aplicadoEm;

    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(PedidoJpaEntity pedido) {
        this.pedido = pedido;
    }

    public void setPromocao(PromocaoJpaEntity promocao) {
        this.promocao = promocao;
    }

    public void setTipoBeneficio(TipoBeneficio tipoBeneficio) {
        this.tipoBeneficio = tipoBeneficio;
    }

    public void setValorBeneficio(BigDecimal valorBeneficio) {
        this.valorBeneficio = valorBeneficio;
    }

    public void setValorDescontoAplicado(BigDecimal valorDescontoAplicado) {
        this.valorDescontoAplicado = valorDescontoAplicado;
    }

    public void setAplicadoEm(LocalDateTime aplicadoEm) {
        this.aplicadoEm = aplicadoEm;
    }
}