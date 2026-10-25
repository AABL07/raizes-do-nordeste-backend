package com.raizesdonordeste.infrastructure.persistence.mapper;

import com.raizesdonordeste.domain.model.Promocao;
import com.raizesdonordeste.infrastructure.persistence.PromocaoJpaEntity;

public class PromocaoMapper {
    public static PromocaoJpaEntity toJpa(Promocao promocao) {
        PromocaoJpaEntity entity = new PromocaoJpaEntity();
        entity.setId(promocao.getId());
        entity.setNome(promocao.getNome());
        entity.setDescricao(promocao.getDescricao());
        entity.setTipoBeneficio(promocao.getTipoBeneficio());
        entity.setValorBeneficio(promocao.getValorBeneficio());
        entity.setPermiteCombinacao(promocao.getPermiteCombinacao());
        entity.setInicioEm(promocao.getInicioEm());
        entity.setFimEm(promocao.getFimEm());
        entity.setAtiva(promocao.getAtiva());
        return entity;
        }

    public static Promocao toDomain(PromocaoJpaEntity entity) {
        Promocao promocao = new Promocao();
        promocao.setId(entity.getId());
        promocao.setNome(entity.getNome());
        promocao.setDescricao(entity.getDescricao());
        promocao.setTipoBeneficio(entity.getTipoBeneficio());
        promocao.setValorBeneficio(entity.getValorBeneficio());
        promocao.setPermiteCombinacao(entity.getPermiteCombinacao());
        promocao.setInicioEm(entity.getInicioEm());
        promocao.setFimEm(entity.getFimEm());
        promocao.setAtiva(entity.getAtiva());
        return promocao;
        }

}
