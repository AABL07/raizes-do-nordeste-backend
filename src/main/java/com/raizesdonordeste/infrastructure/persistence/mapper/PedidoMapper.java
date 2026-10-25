package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Pedido;
import com.raizesdonordeste.infrastructure.persistence.PedidoJpaEntity;

public class PedidoMapper {
    public static PedidoJpaEntity toJpa(Pedido pedido)
        {
        PedidoJpaEntity entity = new PedidoJpaEntity();
        entity.setId(pedido.getId());
        entity.setUnidade(
                    UnidadeMapper.toJpa(pedido.getUnidade())
            );
        entity.setCliente(
                    pedido.getCliente() != null
                            ? ClienteMapper.toJpa(pedido.getCliente())
                            : null
            );
        entity.setCanalPedido(pedido.getCanalPedido());
        entity.setStatus(pedido.getStatus());
        entity.setSubtotal(pedido.getSubtotal());
        entity.setValorDesconto(pedido.getValorDesconto());
        entity.setValorFinal(pedido.getValorFinal());
        entity.setCriadoEm(pedido.getCriadoEm());
        entity.setAtualizadoEm(pedido.getAtualizadoEm());
        return entity;

        }

    public static Pedido toDomain(PedidoJpaEntity entity)
        {
        Pedido pedido = new Pedido();
        pedido.setId(entity.getId());
        pedido.setUnidade(
                    UnidadeMapper.toDomain(entity.getUnidade())
            );
        pedido.setCliente(
                    entity.getCliente() != null
                            ? ClienteMapper.toDomain(entity.getCliente())
                            : null
            );
        pedido.setCanalPedido(entity.getCanalPedido());
        pedido.setStatus(entity.getStatus());
        pedido.setSubtotal(entity.getSubtotal());
        pedido.setValorDesconto(entity.getValorDesconto());
        pedido.setValorFinal(entity.getValorFinal());
        pedido.setCriadoEm(entity.getCriadoEm());
        pedido.setAtualizadoEm(entity.getAtualizadoEm());
        return pedido;
        }
}
