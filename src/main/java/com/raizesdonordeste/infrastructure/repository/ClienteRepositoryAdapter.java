package com.raizesdonordeste.infrastructure.repository;

import com.raizesdonordeste.domain.repository.ClienteRepository;
import org.springframework.stereotype.Repository;
import com.raizesdonordeste.domain.model.Cliente;
import com.raizesdonordeste.infrastructure.persistence.mapper.ClienteMapper;
import java.util.Optional;
import java.util.List;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final ClienteJpaRepository clienteJpaRepository;

    public ClienteRepositoryAdapter(ClienteJpaRepository clienteJpaRepository) {
        this.clienteJpaRepository = clienteJpaRepository;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        var entity = ClienteMapper.toJpa(cliente);
        var salvo = clienteJpaRepository.save(entity);

        return ClienteMapper.toDomain(salvo);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteJpaRepository.findById(id)
                .map(ClienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteJpaRepository.findByEmail(email)
                .map(ClienteMapper::toDomain);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return clienteJpaRepository.findAll()
                .stream()
                .map(ClienteMapper::toDomain)
                .toList();
    }

}