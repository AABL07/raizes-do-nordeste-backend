package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.ContaFidelidadeRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.ContaFidelidade;
import com.raizesdonordeste.infrastructure.persistence.mapper.ContaFidelidadeMapper;
import java.util.Optional;

@Repository
public class ContaFidelidadeRepositoryAdapter implements ContaFidelidadeRepository {

    private final ContaFidelidadeJpaRepository contaFidelidadeJpaRepository;

    public ContaFidelidadeRepositoryAdapter(
            ContaFidelidadeJpaRepository contaFidelidadeJpaRepository
    ) {
        this.contaFidelidadeJpaRepository = contaFidelidadeJpaRepository;
    }

    @Override
    public ContaFidelidade salvar(ContaFidelidade contaFidelidade) {

        if (contaFidelidade.getId() == null) {
            var novaEntity = ContaFidelidadeMapper.toJpa(contaFidelidade);
            var salva = contaFidelidadeJpaRepository.save(novaEntity);

            return ContaFidelidadeMapper.toDomain(salva);
        }

        var entityExistente = contaFidelidadeJpaRepository.findById(contaFidelidade.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conta de fidelidade não encontrada: " + contaFidelidade.getId()
                        )
                );

        entityExistente.setSaldoPontos(contaFidelidade.getSaldoPontos());
        entityExistente.setPontosReservados(contaFidelidade.getPontosReservados());
        entityExistente.setAtiva(contaFidelidade.getAtiva());
        entityExistente.setAtualizadoEm(contaFidelidade.getAtualizadoEm());

        var salva = contaFidelidadeJpaRepository.save(entityExistente);

        return ContaFidelidadeMapper.toDomain(salva);
    }

    @Override
    public Optional<ContaFidelidade> buscarPorId(Long id) {
        return contaFidelidadeJpaRepository.findById(id)
                .map(ContaFidelidadeMapper::toDomain);
    }

    @Override
    public Optional<ContaFidelidade> buscarPorCliente(Long clienteId) {
        return contaFidelidadeJpaRepository.findByClienteId(clienteId)
                .map(ContaFidelidadeMapper::toDomain);
    }
}