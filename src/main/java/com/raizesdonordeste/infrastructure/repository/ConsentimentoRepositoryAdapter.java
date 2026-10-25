package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.ConsentimentoRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Consentimento;
import com.raizesdonordeste.infrastructure.persistence.mapper.ConsentimentoMapper;
import java.util.Optional;
import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.StatusConsentimento;
import java.util.List;

@Repository
public class ConsentimentoRepositoryAdapter implements ConsentimentoRepository {

    private final ConsentimentoJpaRepository consentimentoJpaRepository;

    public ConsentimentoRepositoryAdapter(
            ConsentimentoJpaRepository consentimentoJpaRepository
    ) {
        this.consentimentoJpaRepository = consentimentoJpaRepository;
    }

    @Override
    public Consentimento salvar(Consentimento consentimento) {
        var entity = ConsentimentoMapper.toJpa(consentimento);
        var salvo = consentimentoJpaRepository.save(entity);

        return ConsentimentoMapper.toDomain(salvo);
    }

    @Override
    public Optional<Consentimento> buscarPorId(Long id) {
        return consentimentoJpaRepository.findById(id)
                .map(ConsentimentoMapper::toDomain);
    }

    @Override
    public Optional<Consentimento> buscarAtivoPorClienteEFinalidade(
            Long clienteId,
            FinalidadeConsentimento finalidade
    ) {
        return consentimentoJpaRepository
                .findByClienteIdAndFinalidadeAndStatus(
                        clienteId,
                        finalidade,
                        StatusConsentimento.ATIVO
                )
                .map(ConsentimentoMapper::toDomain);
    }

    @Override
    public List<Consentimento> buscarPorCliente(Long clienteId) {
        return consentimentoJpaRepository.findByClienteId(clienteId)
                .stream()
                .map(ConsentimentoMapper::toDomain)
                .toList();
    }
}