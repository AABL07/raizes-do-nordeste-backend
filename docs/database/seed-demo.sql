-- ============================================================
-- Raízes do Nordeste
-- Seed de demonstração e testes da API
--
-- Uso exclusivo em ambiente local/de testes.
-- Não executar em produção.
--
-- Senha de todos os usuários de demonstração:
-- Teste@123
-- ============================================================

BEGIN;

-- ------------------------------------------------------------
-- Unidade
-- ------------------------------------------------------------

INSERT INTO unidade (
    id,
    nome,
    regiao,
    logradouro,
    numero,
    complemento,
    bairro,
    cidade,
    estado,
    cep,
    ativa
)
VALUES (
    900001,
    'Unidade Demo',
    'NORDESTE',
    'Rua de Demonstração',
    '100',
    NULL,
    'Centro',
    'Recife',
    'PE',
    '50000-000',
    TRUE
)
ON CONFLICT DO NOTHING;


-- ------------------------------------------------------------
-- Usuários
--
-- Senha em texto para os testes:
-- Teste@123
--
-- O valor abaixo é o hash BCrypt correspondente.
-- ------------------------------------------------------------

INSERT INTO usuario (
    id,
    unidade_id,
    perfil_acesso_id,
    nome,
    login,
    senha_hash,
    ativo,
    criado_em,
    atualizado_em
)
SELECT
    900101,
    900001,
    id,
    'Administrador Demo',
    'admin.demo',
    '$2a$10$BczJbi2kg1fAEUaQImvlLe47gEAHyTQ0CxKhuZ9ichUZTMtem8/Fe',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM perfil_acesso
WHERE nome = 'ADMIN'
ON CONFLICT DO NOTHING;


INSERT INTO usuario (
    id,
    unidade_id,
    perfil_acesso_id,
    nome,
    login,
    senha_hash,
    ativo,
    criado_em,
    atualizado_em
)
SELECT
    900102,
    900001,
    id,
    'Gerente Demo',
    'gerente.demo',
    '$2a$10$BczJbi2kg1fAEUaQImvlLe47gEAHyTQ0CxKhuZ9ichUZTMtem8/Fe',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM perfil_acesso
WHERE nome = 'GERENTE'
ON CONFLICT DO NOTHING;


INSERT INTO usuario (
    id,
    unidade_id,
    perfil_acesso_id,
    nome,
    login,
    senha_hash,
    ativo,
    criado_em,
    atualizado_em
)
SELECT
    900103,
    900001,
    id,
    'Atendente Demo',
    'atendente.demo',
    '$2a$10$BczJbi2kg1fAEUaQImvlLe47gEAHyTQ0CxKhuZ9ichUZTMtem8/Fe',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM perfil_acesso
WHERE nome = 'ATENDENTE'
ON CONFLICT DO NOTHING;


INSERT INTO usuario (
    id,
    unidade_id,
    perfil_acesso_id,
    nome,
    login,
    senha_hash,
    ativo,
    criado_em,
    atualizado_em
)
SELECT
    900104,
    900001,
    id,
    'Cozinha Demo',
    'cozinha.demo',
    '$2a$10$BczJbi2kg1fAEUaQImvlLe47gEAHyTQ0CxKhuZ9ichUZTMtem8/Fe',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM perfil_acesso
WHERE nome = 'COZINHA'
ON CONFLICT DO NOTHING;


INSERT INTO usuario (
    id,
    unidade_id,
    perfil_acesso_id,
    nome,
    login,
    senha_hash,
    ativo,
    criado_em,
    atualizado_em
)
SELECT
    900105,
    900001,
    id,
    'Cliente Demo',
    'cliente.demo',
    '$2a$10$BczJbi2kg1fAEUaQImvlLe47gEAHyTQ0CxKhuZ9ichUZTMtem8/Fe',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM perfil_acesso
WHERE nome = 'CLIENTE'
ON CONFLICT DO NOTHING;


-- ------------------------------------------------------------
-- Cliente
-- ------------------------------------------------------------

INSERT INTO cliente (
    id,
    usuario_id,
    nome,
    email,
    telefone,
    ativo,
    criado_em,
    atualizado_em
)
VALUES (
    900201,
    900105,
    'Cliente Demo',
    'cliente.demo@example.com',
    '(81) 99999-0000',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT DO NOTHING;


-- ------------------------------------------------------------
-- Produto
-- ------------------------------------------------------------

INSERT INTO produto (
    id,
    nome,
    descricao,
    ativo
)
VALUES (
    900301,
    'Baião de Dois Demo',
    'Produto utilizado nos testes reproduzíveis da API.',
    TRUE
)
ON CONFLICT DO NOTHING;


-- ------------------------------------------------------------
-- Cardápio
-- ------------------------------------------------------------

INSERT INTO item_cardapio (
    id,
    unidade_id,
    produto_id,
    variacao_produto_id,
    preco,
    disponivel,
    criado_em,
    atualizado_em
)
VALUES (
    900401,
    900001,
    900301,
    NULL,
    29.90,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT DO NOTHING;


-- ------------------------------------------------------------
-- Estoque
-- ------------------------------------------------------------

INSERT INTO estoque (
    id,
    unidade_id,
    produto_id,
    quantidade_fisica,
    quantidade_reservada,
    atualizado_em,
    version
)
VALUES (
    900501,
    900001,
    900301,
    100,
    0,
    CURRENT_TIMESTAMP,
    0
)
ON CONFLICT DO NOTHING;


-- ------------------------------------------------------------
-- Fidelidade
-- ------------------------------------------------------------

INSERT INTO conta_fidelidade (
    id,
    cliente_id,
    saldo_pontos,
    pontos_reservados,
    ativa,
    criado_em,
    atualizado_em,
    version
)
VALUES (
    900601,
    900201,
    1000,
    0,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    0
)
ON CONFLICT DO NOTHING;


-- ------------------------------------------------------------
-- Consentimento LGPD para fidelidade
-- ------------------------------------------------------------

INSERT INTO consentimento (
    id,
    cliente_id,
    finalidade,
    status,
    versao_termo,
    concedido_em,
    revogado_em
)
VALUES (
    900701,
    900201,
    'FIDELIDADE',
    'ATIVO',
    '1.0',
    CURRENT_TIMESTAMP,
    NULL
)
ON CONFLICT DO NOTHING;


COMMIT;


-- ============================================================
-- Dados de referência para Postman
-- ============================================================

SELECT
    900001 AS unidade_id,
    900201 AS cliente_id,
    900401 AS item_cardapio_id,
    900501 AS estoque_id,
    900601 AS conta_fidelidade_id;