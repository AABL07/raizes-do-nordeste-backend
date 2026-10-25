package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.TentativaPagamento;
import com.raizesdonordeste.infrastructure.persistence.TentativaPagamentoJpaEntity;

public class TentativaPagamentoMapper {
    public static TentativaPagamentoJpaEntity toJpa(TentativaPagamento tentativaPagamento) {
        TentativaPagamentoJpaEntity entity = new TentativaPagamentoJpaEntity();
        entity.setId(tentativaPagamento.getId());
        entity.setPedido(
                PedidoMapper.toJpa(tentativaPagamento.getPedido())
        );
        entity.setChaveIdempotencia(tentativaPagamento.getChaveIdempotencia());
        entity.setResultado(tentativaPagamento.getResultado());
        entity.setValorSolicitado(tentativaPagamento.getValorSolicitado());
        entity.setMensagemRetorno(tentativaPagamento.getMensagemRetorno());
        entity.setCriadoEm(tentativaPagamento.getCriadoEm());
        return entity;
    }

    public static TentativaPagamento toDomain(TentativaPagamentoJpaEntity entity) {
        TentativaPagamento tentativaPagamento = new TentativaPagamento();
        tentativaPagamento.setId(entity.getId());
        tentativaPagamento.setPedido(
                PedidoMapper.toDomain(entity.getPedido())
        );
        tentativaPagamento.setChaveIdempotencia(entity.getChaveIdempotencia());
        tentativaPagamento.setResultado(entity.getResultado());
        tentativaPagamento.setValorSolicitado(entity.getValorSolicitado());
        tentativaPagamento.setMensagemRetorno(entity.getMensagemRetorno());
        tentativaPagamento.setCriadoEm(entity.getCriadoEm());

        return tentativaPagamento;
    }
}
