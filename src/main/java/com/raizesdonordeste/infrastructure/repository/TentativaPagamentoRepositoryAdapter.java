package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.TentativaPagamentoRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.TentativaPagamento;
import com.raizesdonordeste.infrastructure.persistence.mapper.TentativaPagamentoMapper;
import java.util.Optional;
import java.util.List;

@Repository
public class TentativaPagamentoRepositoryAdapter implements TentativaPagamentoRepository {

    private final TentativaPagamentoJpaRepository tentativaPagamentoJpaRepository;

    public TentativaPagamentoRepositoryAdapter(
            TentativaPagamentoJpaRepository tentativaPagamentoJpaRepository
    ) {
        this.tentativaPagamentoJpaRepository = tentativaPagamentoJpaRepository;
    }

    @Override
    public TentativaPagamento salvar(TentativaPagamento tentativaPagamento) {
        var entity = TentativaPagamentoMapper.toJpa(tentativaPagamento);
        var salva = tentativaPagamentoJpaRepository.save(entity);

        return TentativaPagamentoMapper.toDomain(salva);
    }

    @Override
    public Optional<TentativaPagamento> buscarPorId(Long id) {
        return tentativaPagamentoJpaRepository.findById(id)
                .map(TentativaPagamentoMapper::toDomain);
    }

    @Override
    public Optional<TentativaPagamento> buscarPorPedidoEChaveIdempotencia(
            Long pedidoId,
            String chaveIdempotencia
    ) {
        return tentativaPagamentoJpaRepository
                .findByPedidoIdAndChaveIdempotencia(
                        pedidoId,
                        chaveIdempotencia
                )
                .map(TentativaPagamentoMapper::toDomain);
    }

    @Override
    public List<TentativaPagamento> buscarPorPedido(Long pedidoId) {
        return tentativaPagamentoJpaRepository.findByPedidoId(pedidoId)
                .stream()
                .map(TentativaPagamentoMapper::toDomain)
                .toList();
    }
}