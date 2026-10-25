package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.PromocaoRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Promocao;
import com.raizesdonordeste.infrastructure.persistence.mapper.PromocaoMapper;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PromocaoRepositoryAdapter implements PromocaoRepository {

    private final PromocaoJpaRepository promocaoJpaRepository;

    public PromocaoRepositoryAdapter(PromocaoJpaRepository promocaoJpaRepository) {
        this.promocaoJpaRepository = promocaoJpaRepository;
    }

    @Override
    public Promocao salvar(Promocao promocao) {
        var entity = PromocaoMapper.toJpa(promocao);
        var salva = promocaoJpaRepository.save(entity);

        return PromocaoMapper.toDomain(salva);
    }

    @Override
    public Optional<Promocao> buscarPorId(Long id) {
        return promocaoJpaRepository.findById(id)
                .map(PromocaoMapper::toDomain);
    }

    @Override
    public List<Promocao> buscarAtivasNoPeriodo(LocalDateTime momento) {
        return promocaoJpaRepository
                .findByAtivaTrueAndInicioEmLessThanEqualAndFimEmGreaterThanEqual(
                        momento,
                        momento
                )
                .stream()
                .map(PromocaoMapper::toDomain)
                .toList();
    }

}