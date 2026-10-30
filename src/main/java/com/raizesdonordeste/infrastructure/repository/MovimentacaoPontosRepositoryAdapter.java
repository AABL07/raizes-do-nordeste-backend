package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.model.MovimentacaoPontos;
import com.raizesdonordeste.domain.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.infrastructure.persistence.mapper.MovimentacaoPontosMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MovimentacaoPontosRepositoryAdapter
        implements MovimentacaoPontosRepository {

    private final MovimentacaoPontosJpaRepository movimentacaoPontosJpaRepository;
    private final ContaFidelidadeJpaRepository contaFidelidadeJpaRepository;
    private final PedidoJpaRepository pedidoJpaRepository;

    public MovimentacaoPontosRepositoryAdapter(
            MovimentacaoPontosJpaRepository movimentacaoPontosJpaRepository,
            ContaFidelidadeJpaRepository contaFidelidadeJpaRepository,
            PedidoJpaRepository pedidoJpaRepository) {

        this.movimentacaoPontosJpaRepository = movimentacaoPontosJpaRepository;
        this.contaFidelidadeJpaRepository = contaFidelidadeJpaRepository;
        this.pedidoJpaRepository = pedidoJpaRepository;
    }

    @Override
    public MovimentacaoPontos salvar(
            MovimentacaoPontos movimentacaoPontos) {

        var entity =
                MovimentacaoPontosMapper.toJpa(movimentacaoPontos);

        var contaFidelidadeJpa =
                contaFidelidadeJpaRepository
                        .findById(
                                movimentacaoPontos
                                        .getContaFidelidade()
                                        .getId()
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Conta de fidelidade não encontrada ao persistir movimentação de pontos."
                                )
                        );

        var pedidoJpa =
                pedidoJpaRepository
                        .findById(
                                movimentacaoPontos
                                        .getPedido()
                                        .getId()
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Pedido não encontrado ao persistir movimentação de pontos."
                                )
                        );

        entity.setContaFidelidade(contaFidelidadeJpa);
        entity.setPedido(pedidoJpa);

        var salva =
                movimentacaoPontosJpaRepository.save(entity);

        return MovimentacaoPontosMapper.toDomain(salva);
    }

    @Override
    public Optional<MovimentacaoPontos> buscarPorId(Long id) {

        return movimentacaoPontosJpaRepository
                .findById(id)
                .map(MovimentacaoPontosMapper::toDomain);
    }

    @Override
    public List<MovimentacaoPontos> buscarPorContaFidelidade(
            Long contaFidelidadeId) {

        return movimentacaoPontosJpaRepository
                .findByContaFidelidadeId(contaFidelidadeId)
                .stream()
                .map(MovimentacaoPontosMapper::toDomain)
                .toList();
    }

    @Override
    public List<MovimentacaoPontos> buscarPorPedido(
            Long pedidoId) {

        return movimentacaoPontosJpaRepository
                .findByPedidoId(pedidoId)
                .stream()
                .map(MovimentacaoPontosMapper::toDomain)
                .toList();
    }
}