package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.ProdutoRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Produto;
import com.raizesdonordeste.infrastructure.persistence.mapper.ProdutoMapper;
import java.util.Optional;
import java.util.List;

@Repository
public class ProdutoRepositoryAdapter implements ProdutoRepository {

    private final ProdutoJpaRepository produtoJpaRepository;

    public ProdutoRepositoryAdapter(ProdutoJpaRepository produtoJpaRepository) {
        this.produtoJpaRepository = produtoJpaRepository;
    }

    @Override
    public Produto salvar(Produto produto) {
        var entity = ProdutoMapper.toJpa(produto);
        var salvo = produtoJpaRepository.save(entity);

        return ProdutoMapper.toDomain(salvo);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoJpaRepository.findById(id)
                .map(ProdutoMapper::toDomain);
    }

    @Override
    public List<Produto> buscarTodos() {
        return produtoJpaRepository.findAll()
                .stream()
                .map(ProdutoMapper::toDomain)
                .toList();
    }
}