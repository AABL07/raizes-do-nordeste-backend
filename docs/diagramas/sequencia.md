# Diagrama de Sequência — Pedido e Pagamento

O diagrama abaixo representa o fluxo crítico da aplicação, desde a criação do pedido até o processamento do pagamento, incluindo idempotência, novas tentativas de pagamento, estoque e fidelidade.

```mermaid
sequenceDiagram
    actor Usuario as Cliente / Atendente
    participant PedidoController
    participant CriarPedido as CriarPedidoUseCase
    participant UnidadeRepo as UnidadeRepository
    participant ClienteRepo as ClienteRepository
    participant CardapioRepo as ItemCardapioRepository
    participant PedidoRepo as PedidoRepository
    participant ReservarEstoque as ReservarEstoqueUseCase
    participant PagamentoController
    participant ProcessarPagamento as ProcessarPagamentoUseCase
    participant TentativaRepo as TentativaPagamentoRepository
    participant ReservaRepo as ReservaEstoqueRepository
    participant PontosRepo as MovimentacaoPontosRepository
    participant Gateway as PagamentoGateway
    participant ConverterEstoque as ConverterReservaEstoqueUseCase
    participant LiberarEstoque as LiberarReservaEstoqueUseCase
    participant ReservarPontos as ReservarPontosUseCase
    participant ConfirmarPontos as ConfirmarReservaPontosUseCase
    participant LiberarPontos as LiberarReservaPontosUseCase

    Usuario->>PedidoController: POST /pedidos
    PedidoController->>CriarPedido: executar(command)

    CriarPedido->>CriarPedido: validar dados obrigatórios
    CriarPedido->>UnidadeRepo: buscarPorId(unidadeId)
    UnidadeRepo-->>CriarPedido: unidade

    opt cliente informado
        CriarPedido->>ClienteRepo: buscarPorId(clienteId)
        ClienteRepo-->>CriarPedido: cliente
    end

    loop para cada item solicitado
        CriarPedido->>CardapioRepo: buscarPorId(itemCardapioId)
        CardapioRepo-->>CriarPedido: itemCardapio
        CriarPedido->>CriarPedido: validar unidade, disponibilidade e quantidade
        CriarPedido->>CriarPedido: calcular preço e subtotal
    end

    CriarPedido->>CriarPedido: criar pedido com status AGUARDANDO_PAGAMENTO
    CriarPedido->>PedidoRepo: salvar(pedido)
    PedidoRepo-->>CriarPedido: pedidoSalvo

    loop para cada item do pedido
        CriarPedido->>ReservarEstoque: executar(itemPedido)
        ReservarEstoque-->>CriarPedido: estoque reservado
    end

    CriarPedido-->>PedidoController: pedidoSalvo
    PedidoController-->>Usuario: 201 Created

    Usuario->>PagamentoController: POST /pedidos/{id}/pagamentos
    PagamentoController->>ProcessarPagamento: executar(pedidoId, chave, resultadoSimulado)

    ProcessarPagamento->>ProcessarPagamento: validar pedidoId, chave e resultado
    ProcessarPagamento->>TentativaRepo: buscarPorPedidoEChaveIdempotencia

    alt tentativa já existe
        TentativaRepo-->>ProcessarPagamento: tentativa existente
        ProcessarPagamento-->>PagamentoController: mesma tentativa
        PagamentoController-->>Usuario: resposta idempotente

    else nova tentativa
        TentativaRepo-->>ProcessarPagamento: vazio

        ProcessarPagamento->>PedidoRepo: buscarPorId(pedidoId)
        PedidoRepo-->>ProcessarPagamento: pedido

        ProcessarPagamento->>ProcessarPagamento: validar status permitido

        ProcessarPagamento->>PontosRepo: buscarPorPedido(pedidoId)
        PontosRepo-->>ProcessarPagamento: movimentações anteriores
        ProcessarPagamento->>ProcessarPagamento: localizar última reserva de pontos

        alt retry após recusa ou falha
            loop para cada item do pedido
                ProcessarPagamento->>ReservarEstoque: executar(itemPedido)
                ReservarEstoque-->>ProcessarPagamento: nova reserva
            end

            ProcessarPagamento->>ProcessarPagamento: status = AGUARDANDO_PAGAMENTO
            ProcessarPagamento->>PedidoRepo: salvar(pedido)

            opt havia reserva anterior de pontos
                ProcessarPagamento->>ReservarPontos: executar(pedidoId, quantidade)
                ReservarPontos-->>ProcessarPagamento: pontos reservados novamente
            end
        end

        ProcessarPagamento->>ReservaRepo: buscarAtivasPorPedido(pedidoId)
        ReservaRepo-->>ProcessarPagamento: reservas ativas

        ProcessarPagamento->>PontosRepo: buscarPorPedido(pedidoId)
        PontosRepo-->>ProcessarPagamento: movimentações de pontos
        ProcessarPagamento->>ProcessarPagamento: verificar reserva ativa de pontos

        ProcessarPagamento->>Gateway: processar(pedidoId, valorFinal, resultadoSimulado)
        Gateway-->>ProcessarPagamento: resultado

        alt pagamento APROVADO
            loop para cada reserva ativa
                ProcessarPagamento->>ConverterEstoque: executar(reservaId)
                ConverterEstoque-->>ProcessarPagamento: saída confirmada
            end

            opt existe reserva ativa de pontos
                ProcessarPagamento->>ConfirmarPontos: executar(pedidoId)
                ConfirmarPontos-->>ProcessarPagamento: pontos debitados
            end

            ProcessarPagamento->>ProcessarPagamento: status = EM_PREPARO

        else pagamento RECUSADO
            loop para cada reserva ativa
                ProcessarPagamento->>LiberarEstoque: executar(reservaId)
                LiberarEstoque-->>ProcessarPagamento: reserva liberada
            end

            opt existe reserva ativa de pontos
                ProcessarPagamento->>LiberarPontos: executar(pedidoId)
                LiberarPontos-->>ProcessarPagamento: pontos liberados
            end

            ProcessarPagamento->>ProcessarPagamento: status = PAGAMENTO_RECUSADO

        else FALHA_TECNICA
            loop para cada reserva ativa
                ProcessarPagamento->>LiberarEstoque: executar(reservaId)
                LiberarEstoque-->>ProcessarPagamento: reserva liberada
            end

            opt existe reserva ativa de pontos
                ProcessarPagamento->>LiberarPontos: executar(pedidoId)
                LiberarPontos-->>ProcessarPagamento: pontos liberados
            end

            ProcessarPagamento->>ProcessarPagamento: status = FALHA_PAGAMENTO
        end

        ProcessarPagamento->>TentativaRepo: salvar(tentativaPagamento)
        TentativaRepo-->>ProcessarPagamento: tentativaSalva

        ProcessarPagamento->>PedidoRepo: salvar(pedido)
        PedidoRepo-->>ProcessarPagamento: pedido atualizado

        ProcessarPagamento-->>PagamentoController: tentativaSalva
        PagamentoController-->>Usuario: resultado do pagamento
    end
```

## Regras representadas

O fluxo demonstra que o pedido é salvo inicialmente com status `AGUARDANDO_PAGAMENTO` e que o estoque é reservado antes do processamento financeiro.

A chave de idempotência é consultada antes de qualquer novo processamento. Se uma tentativa com a mesma combinação de pedido e chave já existir, a tentativa anterior é retornada sem repetir os efeitos de estoque, pontos ou pagamento.

Novas tentativas são permitidas apenas quando o pedido está em `AGUARDANDO_PAGAMENTO`, `PAGAMENTO_RECUSADO` ou `FALHA_PAGAMENTO`.

Em um retry após recusa ou falha, o sistema reserva novamente o estoque dos itens e, quando aplicável, recria a reserva de pontos de fidelidade antes de chamar o gateway.

Quando o pagamento é aprovado, as reservas de estoque são convertidas em saídas confirmadas, a reserva de pontos é confirmada quando existente e o pedido avança para `EM_PREPARO`.

Quando o pagamento é recusado ou ocorre falha técnica, as reservas de estoque e de pontos são liberadas e o pedido assume respectivamente `PAGAMENTO_RECUSADO` ou `FALHA_PAGAMENTO`.
