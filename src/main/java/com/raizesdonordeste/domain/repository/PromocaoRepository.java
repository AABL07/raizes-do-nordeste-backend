package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Promocao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PromocaoRepository {

    Promocao salvar(Promocao promocao);

    Optional<Promocao> buscarPorId(Long id);

    List<Promocao> buscarAtivasNoPeriodo(LocalDateTime momento);
}