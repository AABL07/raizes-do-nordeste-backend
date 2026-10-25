package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.PedidoRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Pedido;
import com.raizesdonordeste.infrastructure.persistence.mapper.PedidoMapper;
import java.util.Optional;
import java.util.List;

@Repository
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final PedidoJpaRepository pedidoJpaRepository;

    public PedidoRepositoryAdapter(PedidoJpaRepository pedidoJpaRepository) {
        this.pedidoJpaRepository = pedidoJpaRepository;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        var entity = PedidoMapper.toJpa(pedido);
        var salvo = pedidoJpaRepository.save(entity);

        return PedidoMapper.toDomain(salvo);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoJpaRepository.findById(id)
                .map(PedidoMapper::toDomain);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return pedidoJpaRepository.findAll()
                .stream()
                .map(PedidoMapper::toDomain)
                .toList();
    }
}
