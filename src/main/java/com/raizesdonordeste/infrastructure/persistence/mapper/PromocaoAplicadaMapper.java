package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.PromocaoAplicada;
import com.raizesdonordeste.infrastructure.persistence.PromocaoAplicadaJpaEntity;

public class PromocaoAplicadaMapper {
    public static PromocaoAplicadaJpaEntity toJpa(PromocaoAplicada promocaoAplicada) {
        PromocaoAplicadaJpaEntity entity = new PromocaoAplicadaJpaEntity();
        entity.setId(promocaoAplicada.getId());
        entity.setPedido(
                PedidoMapper.toJpa(promocaoAplicada.getPedido())
        );
        entity.setPromocao(
                PromocaoMapper.toJpa(promocaoAplicada.getPromocao())
        );
        entity.setTipoBeneficio(promocaoAplicada.getTipoBeneficio());
        entity.setValorBeneficio(promocaoAplicada.getValorBeneficio());
        entity.setValorDescontoAplicado(promocaoAplicada.getValorDescontoAplicado());
        entity.setAplicadoEm(promocaoAplicada.getAplicadoEm());
        return entity;
    }

    public static PromocaoAplicada toDomain(PromocaoAplicadaJpaEntity entity) {
        PromocaoAplicada promocaoAplicada = new PromocaoAplicada();

        promocaoAplicada.setId(entity.getId());

        promocaoAplicada.setPedido(
                PedidoMapper.toDomain(entity.getPedido())
        );

        promocaoAplicada.setPromocao(
                PromocaoMapper.toDomain(entity.getPromocao())
        );

        promocaoAplicada.setTipoBeneficio(entity.getTipoBeneficio());
        promocaoAplicada.setValorBeneficio(entity.getValorBeneficio());
        promocaoAplicada.setValorDescontoAplicado(entity.getValorDescontoAplicado());
        promocaoAplicada.setAplicadoEm(entity.getAplicadoEm());

        return promocaoAplicada;
    }
}
