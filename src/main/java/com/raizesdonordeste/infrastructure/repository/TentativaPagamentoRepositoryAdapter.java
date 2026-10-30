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

    private final PedidoJpaRepository pedidoJpaRepository;

    public TentativaPagamentoRepositoryAdapter(
            TentativaPagamentoJpaRepository tentativaPagamentoJpaRepository,
            PedidoJpaRepository pedidoJpaRepository
    ) {
        this.tentativaPagamentoJpaRepository = tentativaPagamentoJpaRepository;
        this.pedidoJpaRepository = pedidoJpaRepository;
    }

    @Override
    public TentativaPagamento salvar(TentativaPagamento tentativaPagamento) {

        var entity = TentativaPagamentoMapper.toJpa(tentativaPagamento);

        var pedidoJpa = pedidoJpaRepository
                .findById(tentativaPagamento.getPedido().getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Pedido não encontrado ao persistir tentativa de pagamento."
                        )
                );

        entity.setPedido(pedidoJpa);

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