# Diagrama Entidade-Relacionamento — DER



O banco de dados da aplicação utiliza PostgreSQL e é versionado por migrations Flyway.



O modelo atual possui 20 tabelas. O diagrama abaixo representa as entidades persistidas, suas chaves primárias e estrangeiras e os principais relacionamentos.



```mermaid

erDiagram



    UNIDADE {

        bigint id PK

        varchar nome

        varchar regiao

        varchar logradouro

        varchar numero

        varchar complemento

        varchar bairro

        varchar cidade

        varchar estado

        varchar cep

        boolean ativa

    }



    PRODUTO {

        bigint id PK

        varchar nome

        text descricao

        boolean ativo

    }



    PERFIL_ACESSO {

        bigint id PK

        varchar nome UK

        text descricao

        boolean ativo

    }



    USUARIO {

        bigint id PK

        bigint unidade_id FK

        bigint perfil_acesso_id FK

        varchar nome

        varchar login UK

        varchar senha_hash

        boolean ativo

        timestamp criado_em

        timestamp atualizado_em

    }



    CLIENTE {

        bigint id PK

        bigint usuario_id FK

        varchar nome

        varchar email

        varchar telefone

        boolean ativo

        timestamp criado_em

        timestamp atualizado_em

    }



    PEDIDO {

        bigint id PK

        bigint unidade_id FK

        bigint cliente_id FK

        varchar canal_pedido

        varchar status

        decimal subtotal

        decimal valor_desconto

        decimal valor_final

        timestamp criado_em

        timestamp atualizado_em

    }



    VARIACAO_PRODUTO {

        bigint id PK

        bigint produto_id FK

        varchar nome

        text descricao

        boolean ativa

    }



    ITEM_CARDAPIO {

        bigint id PK

        bigint unidade_id FK

        bigint produto_id FK

        bigint variacao_produto_id FK

        decimal preco

        boolean disponivel

        timestamp criado_em

        timestamp atualizado_em

    }



    ITEM_PEDIDO {

        bigint id PK

        bigint pedido_id FK

        bigint item_cardapio_id FK

        integer quantidade

        decimal preco_unitario

        decimal subtotal

    }



    ESTOQUE {

        bigint id PK

        bigint unidade_id FK

        bigint produto_id FK

        integer quantidade_fisica

        integer quantidade_reservada

        timestamp atualizado_em

        bigint version

    }



    RESERVA_ESTOQUE {

        bigint id PK

        bigint estoque_id FK

        bigint item_pedido_id FK

        integer quantidade

        varchar status

        timestamp criado_em

        timestamp resolvido_em

    }



    MOVIMENTACAO_ESTOQUE {

        bigint id PK

        bigint estoque_id FK

        bigint item_pedido_id FK

        bigint reserva_estoque_id FK

        varchar tipo

        integer quantidade

        varchar motivo

        timestamp criado_em

    }



    CONTA_FIDELIDADE {

        bigint id PK

        bigint cliente_id FK

        integer saldo_pontos

        integer pontos_reservados

        boolean ativa

        timestamp criado_em

        timestamp atualizado_em

        bigint version

    }



    CONSENTIMENTO {

        bigint id PK

        bigint cliente_id FK

        varchar finalidade

        varchar status

        varchar versao_termo

        timestamp concedido_em

        timestamp revogado_em

    }



    MOVIMENTACAO_PONTOS {

        bigint id PK

        bigint conta_fidelidade_id FK

        bigint pedido_id FK

        varchar tipo

        integer quantidade

        varchar descricao

        timestamp criado_em

    }



    REGISTRO_AUDITORIA {

        bigint id PK

        bigint usuario_id FK

        varchar acao

        varchar recurso_tipo

        bigint recurso_id

        varchar origem

        text detalhes

        timestamp criado_em

    }



    PROMOCAO {

        bigint id PK

        varchar nome

        text descricao

        varchar tipo_beneficio

        decimal valor_beneficio

        boolean permite_combinacao

        timestamp inicio_em

        timestamp fim_em

        boolean ativa

    }



    PROMOCAO_APLICADA {

        bigint id PK

        bigint pedido_id FK

        bigint promocao_id FK

        varchar tipo_beneficio

        decimal valor_beneficio

        decimal valor_desconto_aplicado

        timestamp aplicado_em

    }



    TENTATIVA_PAGAMENTO {

        bigint id PK

        bigint pedido_id FK

        varchar chave_idempotencia

        varchar resultado

        decimal valor_solicitado

        varchar mensagem_retorno

        timestamp criado_em

    }



    SAZONALIDADE_PRODUTO {

        bigint id PK

        bigint item_cardapio_id FK

        date inicio_em

        date fim_em

        boolean ativa

    }



    UNIDADE o|--o{ USUARIO : possui

    PERFIL_ACESSO ||--o{ USUARIO : define



    USUARIO o|--o| CLIENTE : vincula



    UNIDADE ||--o{ PEDIDO : recebe

    CLIENTE o|--o{ PEDIDO : realiza



    PRODUTO ||--o{ VARIACAO_PRODUTO : possui



    UNIDADE ||--o{ ITEM_CARDAPIO : disponibiliza

    PRODUTO ||--o{ ITEM_CARDAPIO : compoe

    VARIACAO_PRODUTO o|--o{ ITEM_CARDAPIO : especifica



    PEDIDO ||--o{ ITEM_PEDIDO : contem

    ITEM_CARDAPIO ||--o{ ITEM_PEDIDO : referencia



    UNIDADE ||--o{ ESTOQUE : mantem

    PRODUTO ||--o{ ESTOQUE : possui



    ESTOQUE ||--o{ RESERVA_ESTOQUE : reserva

    ITEM_PEDIDO ||--o{ RESERVA_ESTOQUE : origina



    ESTOQUE ||--o{ MOVIMENTACAO_ESTOQUE : registra

    ITEM_PEDIDO o|--o{ MOVIMENTACAO_ESTOQUE : referencia

    RESERVA_ESTOQUE o|--o| MOVIMENTACAO_ESTOQUE : converte



    CLIENTE ||--o| CONTA_FIDELIDADE : possui

    CLIENTE ||--o{ CONSENTIMENTO : concede



    CONTA_FIDELIDADE ||--o{ MOVIMENTACAO_PONTOS : registra

    PEDIDO ||--o{ MOVIMENTACAO_PONTOS : origina



    USUARIO o|--o{ REGISTRO_AUDITORIA : gera



    PEDIDO ||--o{ PROMOCAO_APLICADA : recebe

    PROMOCAO ||--o{ PROMOCAO_APLICADA : aplica



    PEDIDO ||--o{ TENTATIVA_PAGAMENTO : possui



    ITEM_CARDAPIO ||--o{ SAZONALIDADE_PRODUTO : possui

```



## Restrições relevantes



Além das chaves primárias e estrangeiras representadas no DER, o banco possui restrições adicionais importantes para preservar a integridade das regras de negócio.



- `perfil_acesso.nome` é único.

- `usuario.login` é único.

- `cliente.usuario_id` é único, permitindo no máximo um cliente associado a cada usuário.

- A combinação `estoque(unidade_id, produto_id)` é única.

- `movimentacao_estoque.reserva_estoque_id` é único quando informado.

- `conta_fidelidade.cliente_id` é único, permitindo no máximo uma conta de fidelidade por cliente.

- A combinação `item_cardapio(unidade_id, produto_id, variacao_produto_id)` é única, incluindo valores nulos de variação.

- Pode existir apenas um consentimento com status `ATIVO` para a mesma combinação de cliente e finalidade.

- Pode existir apenas uma movimentação de pontos do tipo `CREDITO` para cada pedido.

- A combinação `tentativa_pagamento(pedido_id, chave_idempotencia)` é única, garantindo suporte à idempotência do processamento de pagamentos.



## Concorrência otimista



As tabelas `estoque` e `conta_fidelidade` possuem o campo:



```text

version BIGINT NOT NULL DEFAULT 0

```



Esse campo é utilizado para controle de concorrência otimista, evitando sobrescritas silenciosas em operações concorrentes envolvendo estoque e saldo de fidelidade.