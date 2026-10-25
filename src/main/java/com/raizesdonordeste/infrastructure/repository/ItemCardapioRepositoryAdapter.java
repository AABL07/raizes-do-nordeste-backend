package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.ItemCardapioRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.ItemCardapio;
import com.raizesdonordeste.infrastructure.persistence.mapper.ItemCardapioMapper;
import java.util.Optional;
import java.util.List;

@Repository
public class ItemCardapioRepositoryAdapter implements ItemCardapioRepository {

    private final ItemCardapioJpaRepository itemCardapioJpaRepository;

    public ItemCardapioRepositoryAdapter(ItemCardapioJpaRepository itemCardapioJpaRepository) {
        this.itemCardapioJpaRepository = itemCardapioJpaRepository;
    }

    @Override
    public ItemCardapio salvar(ItemCardapio itemCardapio) {
        var entity = ItemCardapioMapper.toJpa(itemCardapio);
        var salvo = itemCardapioJpaRepository.save(entity);

        return ItemCardapioMapper.toDomain(salvo);
    }

    @Override
    public Optional<ItemCardapio> buscarPorId(Long id) {
        return itemCardapioJpaRepository.findById(id)
                .map(ItemCardapioMapper::toDomain);
    }

    @Override
    public List<ItemCardapio> buscarPorUnidade(Long unidadeId) {
        return itemCardapioJpaRepository.findByUnidadeId(unidadeId)
                .stream()
                .map(ItemCardapioMapper::toDomain)
                .toList();
    }
}