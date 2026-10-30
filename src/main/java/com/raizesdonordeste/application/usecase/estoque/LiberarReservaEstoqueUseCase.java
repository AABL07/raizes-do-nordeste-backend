package com.raizesdonordeste.application.usecase.estoque;

import com.raizesdonordeste.application.exception.ConflitoException;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.domain.enums.StatusReservaEstoque;
import com.raizesdonordeste.domain.model.ReservaEstoque;
import com.raizesdonordeste.domain.repository.EstoqueRepository;
import com.raizesdonordeste.domain.repository.ReservaEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LiberarReservaEstoqueUseCase {

    private final ReservaEstoqueRepository reservaEstoqueRepository;
    private final EstoqueRepository estoqueRepository;

    public LiberarReservaEstoqueUseCase(
            ReservaEstoqueRepository reservaEstoqueRepository,
            EstoqueRepository estoqueRepository) {

        this.reservaEstoqueRepository = reservaEstoqueRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public ReservaEstoque executar(Long reservaId) {

        if (reservaId == null) {
            throw new RegraNegocioException(
                    "A reserva de estoque é obrigatória."
            );
        }

        var reserva = reservaEstoqueRepository.buscarPorId(reservaId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Reserva de estoque não encontrada."
                        )
                );

        if (reserva.getStatus() != StatusReservaEstoque.ATIVA) {
            throw new ConflitoException(
                    "Somente uma reserva ativa pode ser liberada."
            );
        }

        var estoque = reserva.getEstoque();

        if (estoque == null) {
            throw new RegraNegocioException(
                    "A reserva não possui estoque associado."
            );
        }

        int quantidadeReservada = estoque.getQuantidadeReservada() != null
                ? estoque.getQuantidadeReservada()
                : 0;

        if (reserva.getQuantidade() == null
                || reserva.getQuantidade() <= 0) {

            throw new RegraNegocioException(
                    "A reserva possui quantidade inválida."
            );
        }

        if (quantidadeReservada < reserva.getQuantidade()) {
            throw new ConflitoException(
                    "A quantidade reservada do estoque está inconsistente."
            );
        }

        LocalDateTime agora = LocalDateTime.now();

        estoque.setQuantidadeReservada(
                quantidadeReservada - reserva.getQuantidade()
        );
        estoque.setAtualizadoEm(agora);

        var estoqueAtualizado = estoqueRepository.salvar(estoque);

        reserva.setEstoque(estoqueAtualizado);
        reserva.setStatus(StatusReservaEstoque.LIBERADA);
        reserva.setResolvidoEm(agora);

        return reservaEstoqueRepository.salvar(reserva);
    }
}