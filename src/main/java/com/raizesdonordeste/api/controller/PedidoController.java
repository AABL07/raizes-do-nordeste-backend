package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.application.usecase.pedido.AlterarStatusPedidoUseCase;
import com.raizesdonordeste.application.usecase.pedido.CriarPedidoUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.raizesdonordeste.api.dto.AlterarStatusPedidoRequest;
import com.raizesdonordeste.api.dto.CriarPedidoRequest;
import com.raizesdonordeste.api.dto.PedidoResponse;
import com.raizesdonordeste.application.usecase.pedido.CriarItemPedidoCommand;
import com.raizesdonordeste.application.usecase.pedido.CriarPedidoCommand;
import com.raizesdonordeste.domain.model.Pedido;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import com.raizesdonordeste.application.usecase.pedido.ConsultarPedidosUseCase;
import com.raizesdonordeste.domain.enums.CanalPedido;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final CriarPedidoUseCase criarPedidoUseCase;

    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;

    private final ConsultarPedidosUseCase consultarPedidosUseCase;

    public PedidoController(
            CriarPedidoUseCase criarPedidoUseCase,
            AlterarStatusPedidoUseCase alterarStatusPedidoUseCase,
            ConsultarPedidosUseCase consultarPedidosUseCase) {

        this.criarPedidoUseCase = criarPedidoUseCase;
        this.alterarStatusPedidoUseCase = alterarStatusPedidoUseCase;
        this.consultarPedidosUseCase = consultarPedidosUseCase;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE', 'CLIENTE')")

    public PedidoResponse criar(
            @Valid @RequestBody CriarPedidoRequest request) {

        CriarPedidoCommand command =
                new CriarPedidoCommand();

        command.setUnidadeId(request.getUnidadeId());
        command.setClienteId(request.getClienteId());
        command.setCanalPedido(request.getCanalPedido());

        List<CriarItemPedidoCommand> itens =
                request.getItens()
                        .stream()
                        .map(itemRequest -> {

                            CriarItemPedidoCommand itemCommand =
                                    new CriarItemPedidoCommand();

                            itemCommand.setItemCardapioId(
                                    itemRequest.getItemCardapioId()
                            );

                            itemCommand.setQuantidade(
                                    itemRequest.getQuantidade()
                            );

                            return itemCommand;
                        })
                        .toList();

        command.setItens(itens);

        Pedido pedido =
                criarPedidoUseCase.executar(command);

        return toResponse(pedido);
    }

    @PatchMapping("/{pedidoId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COZINHA')")
    public PedidoResponse alterarStatus(
            @PathVariable Long pedidoId,
            @Valid @RequestBody AlterarStatusPedidoRequest request) {

        Pedido pedido =
                alterarStatusPedidoUseCase.executar(
                        pedidoId,
                        request.getStatus()
                );

        return toResponse(pedido);
    }

    @GetMapping("/{pedidoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE', 'COZINHA')")
    public PedidoResponse buscarPorId(
            @PathVariable Long pedidoId) {

        Pedido pedido =
                consultarPedidosUseCase.buscarPorId(pedidoId);

        return toResponse(pedido);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'ATENDENTE', 'COZINHA')")
    public List<PedidoResponse> listar(
            @RequestParam(required = false)
            CanalPedido canalPedido) {

        return consultarPedidosUseCase
                .listar(canalPedido)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PedidoResponse toResponse(Pedido pedido) {

        PedidoResponse response = new PedidoResponse();

        response.setId(pedido.getId());

        if (pedido.getUnidade() != null) {
            response.setUnidadeId(
                    pedido.getUnidade().getId()
            );
        }

        if (pedido.getCliente() != null) {
            response.setClienteId(
                    pedido.getCliente().getId()
            );
        }

        response.setCanalPedido(
                pedido.getCanalPedido()
        );

        response.setStatus(
                pedido.getStatus()
        );

        response.setValorSubtotal(
                pedido.getSubtotal()
        );

        response.setValorDesconto(
                pedido.getValorDesconto()
        );

        response.setValorFinal(
                pedido.getValorFinal()
        );

        response.setCriadoEm(
                pedido.getCriadoEm()
        );

        response.setAtualizadoEm(
                pedido.getAtualizadoEm()
        );

        return response;
    }
}