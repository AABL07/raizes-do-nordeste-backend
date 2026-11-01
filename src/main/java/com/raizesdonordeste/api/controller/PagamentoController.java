package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.dto.PagamentoResponse;
import com.raizesdonordeste.api.dto.ProcessarPagamentoRequest;
import com.raizesdonordeste.application.usecase.pagamento.ProcessarPagamentoUseCase;
import com.raizesdonordeste.domain.model.TentativaPagamento;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/pedidos/{pedidoId}/pagamentos")
@SecurityRequirement(name = "bearerAuth")
public class PagamentoController {

    private final ProcessarPagamentoUseCase processarPagamentoUseCase;

    public PagamentoController(
            ProcessarPagamentoUseCase processarPagamentoUseCase) {

        this.processarPagamentoUseCase = processarPagamentoUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE', 'CLIENTE')")
    public PagamentoResponse processar(
            @PathVariable Long pedidoId,
            @Valid @RequestBody ProcessarPagamentoRequest request) {

        TentativaPagamento tentativa =
                processarPagamentoUseCase.executar(
                        pedidoId,
                        request.getChaveIdempotencia(),
                        request.getResultadoSimulado()
                );

        return toResponse(tentativa);
    }

    private PagamentoResponse toResponse(
            TentativaPagamento tentativa) {

        PagamentoResponse response =
                new PagamentoResponse();

        response.setId(tentativa.getId());

        if (tentativa.getPedido() != null) {
            response.setPedidoId(
                    tentativa.getPedido().getId()
            );
        }

        response.setChaveIdempotencia(
                tentativa.getChaveIdempotencia()
        );

        response.setResultado(
                tentativa.getResultado()
        );

        response.setValorSolicitado(
                tentativa.getValorSolicitado()
        );

        response.setMensagemRetorno(
                tentativa.getMensagemRetorno()
        );

        response.setCriadoEm(
                tentativa.getCriadoEm()
        );

        return response;
    }
}