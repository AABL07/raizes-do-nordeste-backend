package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.MovimentacaoPontosResponse;
import com.raizesdonordeste.api.dto.ReservarPontosRequest;
import com.raizesdonordeste.application.usecase.fidelidade.ReservarPontosUseCase;
import com.raizesdonordeste.domain.model.MovimentacaoPontos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/pedidos/{pedidoId}/fidelidade")
public class FidelidadeController {

    private final ReservarPontosUseCase reservarPontosUseCase;

    public FidelidadeController(
            ReservarPontosUseCase reservarPontosUseCase) {

        this.reservarPontosUseCase = reservarPontosUseCase;
    }

    @PostMapping("/resgate")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE', 'CLIENTE')")
    public MovimentacaoPontosResponse reservar(
            @PathVariable Long pedidoId,
            @Valid @RequestBody ReservarPontosRequest request) {

        MovimentacaoPontos movimentacao =
                reservarPontosUseCase.executar(
                        pedidoId,
                        request.getQuantidadePontos()
                );

        return toResponse(movimentacao);
    }

    private MovimentacaoPontosResponse toResponse(
            MovimentacaoPontos movimentacao) {

        MovimentacaoPontosResponse response =
                new MovimentacaoPontosResponse();

        response.setId(movimentacao.getId());

        if (movimentacao.getContaFidelidade() != null) {
            response.setContaFidelidadeId(
                    movimentacao.getContaFidelidade().getId()
            );
        }

        if (movimentacao.getPedido() != null) {
            response.setPedidoId(
                    movimentacao.getPedido().getId()
            );
        }

        response.setTipo(
                movimentacao.getTipo()
        );

        response.setQuantidade(
                movimentacao.getQuantidade()
        );

        response.setCriadoEm(
                movimentacao.getCriadoEm()
        );

        return response;
    }
}