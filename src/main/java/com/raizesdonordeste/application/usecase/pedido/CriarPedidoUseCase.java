package com.raizesdonordeste.application.usecase.pedido;

import org.springframework.stereotype.Service;
import com.raizesdonordeste.domain.repository.UnidadeRepository;
import com.raizesdonordeste.domain.repository.ClienteRepository;
import com.raizesdonordeste.domain.repository.ItemCardapioRepository;
import com.raizesdonordeste.domain.repository.PedidoRepository;
import com.raizesdonordeste.domain.model.Pedido;
import com.raizesdonordeste.application.exception.RegraNegocioException;
import com.raizesdonordeste.application.exception.RecursoNaoEncontradoException;
import java.math.BigDecimal;
import com.raizesdonordeste.domain.model.ItemPedido;
import java.util.ArrayList;
import com.raizesdonordeste.domain.enums.StatusPedido;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import com.raizesdonordeste.application.usecase.estoque.ReservarEstoqueUseCase;

@Service
public class CriarPedidoUseCase {

    private final UnidadeRepository unidadeRepository;

    private final ClienteRepository clienteRepository;

    private final ItemCardapioRepository itemCardapioRepository;

    private final PedidoRepository pedidoRepository;

    private final ReservarEstoqueUseCase reservarEstoqueUseCase;

    public CriarPedidoUseCase(
            UnidadeRepository unidadeRepository,
            ClienteRepository clienteRepository,
            ItemCardapioRepository itemCardapioRepository,
            PedidoRepository pedidoRepository,
            ReservarEstoqueUseCase reservarEstoqueUseCase) {

        this.unidadeRepository = unidadeRepository;
        this.clienteRepository = clienteRepository;
        this.itemCardapioRepository = itemCardapioRepository;
        this.pedidoRepository = pedidoRepository;
        this.reservarEstoqueUseCase = reservarEstoqueUseCase;
    }

    @Transactional
    public Pedido executar(CriarPedidoCommand command) {

        if (command == null) {
            throw new RegraNegocioException("Os dados do pedido são obrigatórios.");
        }

        if (command.getUnidadeId() == null) {
            throw new RegraNegocioException("A unidade do pedido é obrigatória.");
        }

        var unidade = unidadeRepository.buscarPorId(command.getUnidadeId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Unidade não encontrada.")
                );

        var cliente = command.getClienteId() != null
                ? clienteRepository.buscarPorId(command.getClienteId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado.")
                )
                : null;

        if (command.getCanalPedido() == null) {
            throw new RegraNegocioException("O canal do pedido é obrigatório.");
        }

        if (command.getItens() == null || command.getItens().isEmpty()) {
            throw new RegraNegocioException("O pedido deve possuir pelo menos um item.");
        }

        BigDecimal subtotalPedido = BigDecimal.ZERO;
        var itensPedido = new ArrayList<ItemPedido>();

        for (CriarItemPedidoCommand itemCommand : command.getItens()) {

            if (itemCommand == null) {
                throw new RegraNegocioException("Os itens do pedido não podem ser nulos.");
            }

            if (itemCommand.getItemCardapioId() == null) {
                throw new RegraNegocioException("O item do cardápio é obrigatório.");
            }

            if (itemCommand.getQuantidade() == null || itemCommand.getQuantidade() <= 0) {
                throw new RegraNegocioException("A quantidade do item deve ser maior que zero.");
            }

            var itemCardapio = itemCardapioRepository.buscarPorId(itemCommand.getItemCardapioId())
                    .orElseThrow(() ->
                            new RecursoNaoEncontradoException("Item do cardápio não encontrado.")
                    );

            if (!itemCardapio.getUnidade().getId().equals(unidade.getId())) {
                throw new RegraNegocioException(
                        "O item do cardápio não pertence à unidade do pedido."
                );
            }

            if (!Boolean.TRUE.equals(itemCardapio.getDisponivel())) {
                throw new RegraNegocioException(
                        "O item do cardápio não está disponível."
                );
            }

            BigDecimal precoUnitario = itemCardapio.getPreco();

            if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
                throw new RegraNegocioException(
                        "O item do cardápio possui um preço inválido."
                );
            }

            BigDecimal subtotalItem = precoUnitario.multiply(
                    BigDecimal.valueOf(itemCommand.getQuantidade())
            );

            subtotalPedido = subtotalPedido.add(subtotalItem);

            ItemPedido itemPedido = new ItemPedido();

            itemPedido.setItemCardapio(itemCardapio);
            itemPedido.setQuantidade(itemCommand.getQuantidade());
            itemPedido.setPrecoUnitario(precoUnitario);
            itemPedido.setSubtotal(subtotalItem);

            itensPedido.add(itemPedido);
        }

        Pedido pedido = new Pedido();

        pedido.setUnidade(unidade);
        pedido.setCliente(cliente);
        pedido.setCanalPedido(command.getCanalPedido());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setSubtotal(subtotalPedido);
        pedido.setValorDesconto(BigDecimal.ZERO);
        pedido.setValorFinal(subtotalPedido);

        LocalDateTime agora = LocalDateTime.now();

        pedido.setCriadoEm(agora);
        pedido.setAtualizadoEm(agora);

        for (ItemPedido itemPedido : itensPedido) {
            itemPedido.setPedido(pedido);
        }

        pedido.setItens(itensPedido);

        Pedido pedidoSalvo = pedidoRepository.salvar(pedido);

        for (ItemPedido itemPedido : pedidoSalvo.getItens()) {
            reservarEstoqueUseCase.executar(itemPedido);
        }

        return pedidoSalvo;    }

}
