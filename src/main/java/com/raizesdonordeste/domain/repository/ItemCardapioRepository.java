package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.ItemCardapio;

import java.util.List;
import java.util.Optional;

public interface ItemCardapioRepository {

    ItemCardapio salvar(ItemCardapio itemCardapio);

    Optional<ItemCardapio> buscarPorId(Long id);

    List<ItemCardapio> buscarPorUnidade(Long unidadeId);
}