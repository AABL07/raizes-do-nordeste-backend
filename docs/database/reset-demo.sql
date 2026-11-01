BEGIN;

-- Remove movimentações de estoque ligadas aos pedidos demo.
DELETE FROM movimentacao_estoque
WHERE item_pedido_id IN (
    SELECT ip.id
    FROM item_pedido ip
    JOIN pedido p ON p.id = ip.pedido_id
    WHERE p.unidade_id = 900001
       OR p.cliente_id = 900201
)
OR reserva_estoque_id IN (
    SELECT re.id
    FROM reserva_estoque re
    JOIN item_pedido ip ON ip.id = re.item_pedido_id
    JOIN pedido p ON p.id = ip.pedido_id
    WHERE p.unidade_id = 900001
       OR p.cliente_id = 900201
);

-- Remove reservas de estoque.
DELETE FROM reserva_estoque
WHERE item_pedido_id IN (
    SELECT ip.id
    FROM item_pedido ip
    JOIN pedido p ON p.id = ip.pedido_id
    WHERE p.unidade_id = 900001
       OR p.cliente_id = 900201
);

-- Remove movimentações de fidelidade.
DELETE FROM movimentacao_pontos
WHERE pedido_id IN (
    SELECT id
    FROM pedido
    WHERE unidade_id = 900001
       OR cliente_id = 900201
);

-- Remove tentativas de pagamento.
DELETE FROM tentativa_pagamento
WHERE pedido_id IN (
    SELECT id
    FROM pedido
    WHERE unidade_id = 900001
       OR cliente_id = 900201
);

-- Remove promoções aplicadas.
DELETE FROM promocao_aplicada
WHERE pedido_id IN (
    SELECT id
    FROM pedido
    WHERE unidade_id = 900001
       OR cliente_id = 900201
);

-- Remove itens e pedidos criados pelos testes.
DELETE FROM item_pedido
WHERE pedido_id IN (
    SELECT id
    FROM pedido
    WHERE unidade_id = 900001
       OR cliente_id = 900201
);

DELETE FROM pedido
WHERE unidade_id = 900001
   OR cliente_id = 900201;

-- Restaura estoque demo.
UPDATE estoque
SET quantidade_fisica = 100,
    quantidade_reservada = 0,
    atualizado_em = CURRENT_TIMESTAMP,
    version = 0
WHERE id = 900501;

-- Restaura fidelidade demo.
UPDATE conta_fidelidade
SET saldo_pontos = 1000,
    pontos_reservados = 0,
    ativa = TRUE,
    atualizado_em = CURRENT_TIMESTAMP,
    version = 0
WHERE id = 900601;

-- Garante que os principais registros demo continuam disponíveis.
UPDATE unidade
SET ativa = TRUE
WHERE id = 900001;

UPDATE produto
SET ativo = TRUE
WHERE id = 900301;

UPDATE item_cardapio
SET disponivel = TRUE,
    preco = 29.90,
    atualizado_em = CURRENT_TIMESTAMP
WHERE id = 900401;

UPDATE cliente
SET ativo = TRUE,
    atualizado_em = CURRENT_TIMESTAMP
WHERE id = 900201;

COMMIT;

SELECT
    (SELECT COUNT(*) FROM pedido
     WHERE unidade_id = 900001 OR cliente_id = 900201) AS pedidos_demo,
    (SELECT quantidade_fisica FROM estoque WHERE id = 900501) AS estoque_fisico,
    (SELECT quantidade_reservada FROM estoque WHERE id = 900501) AS estoque_reservado,
    (SELECT saldo_pontos FROM conta_fidelidade WHERE id = 900601) AS saldo_pontos,
    (SELECT pontos_reservados FROM conta_fidelidade WHERE id = 900601) AS pontos_reservados;