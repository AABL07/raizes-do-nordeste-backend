package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.CanalPedido;
import com.raizesdonordeste.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {

    private Long id;
    private CanalPedido canalPedido;
    private StatusPedido status;
    private BigDecimal subtotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorFinal;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}