# Raízes do Nordeste — Back-End

API REST desenvolvida para o Projeto Multidisciplinar — Trilha Back-End, com foco na operação de uma rede de lanchonetes com múltiplas unidades e canais de atendimento.

O projeto contempla autenticação e autorização, pedidos, estoque por unidade, pagamento simulado, controle de status e fidelização.

## Tecnologias

- Java 21
- Spring Boot 4.0.9
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Jakarta Validation
- PostgreSQL
- Flyway
- JJWT 0.13.0
- Maven / Maven Wrapper

## Requisitos

Para executar o projeto localmente:

- Java JDK 21
- PostgreSQL
- Git
- Porta 8080 disponível
- Maven não é obrigatório, pois o projeto inclui Maven Wrapper

## Configuração do banco de dados

Crie um banco PostgreSQL para a aplicação.

Exemplo:

```sql
CREATE USER raizes_app WITH PASSWORD 'SUA_SENHA';

CREATE DATABASE raizes_do_nordeste
    OWNER raizes_app;
```

Por padrão, a aplicação utiliza:

```text
Banco: raizes_do_nordeste
Host: localhost
Porta: 5432
Usuário: raizes_app
```

Esses valores podem ser alterados por variáveis de ambiente.

## Variáveis de ambiente

O arquivo `.env.example` documenta as variáveis utilizadas pelo projeto.

> O Spring Boot não carrega automaticamente o arquivo `.env`. Ele serve como referência. As variáveis devem ser configuradas no terminal, IDE ou ambiente de execução.

Variáveis disponíveis:

| Variável | Obrigatória | Descrição |
|---|---|---|
| `DB_URL` | Não | URL JDBC do PostgreSQL |
| `DB_USERNAME` | Não | Usuário do PostgreSQL |
| `DB_PASSWORD` | Sim | Senha do usuário do banco |
| `JWT_SECRET` | Sim | Chave utilizada para assinatura dos tokens JWT |
| `JWT_EXPIRATION_MS` | Não | Tempo de validade do JWT em milissegundos |

Valores padrão existentes na aplicação:

```text
DB_URL=jdbc:postgresql://localhost:5432/raizes_do_nordeste
DB_USERNAME=raizes_app
JWT_EXPIRATION_MS=3600000
```

### Exemplo no PowerShell

```powershell
$env:DB_PASSWORD = "SUA_SENHA"

$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
$rng.Dispose()
```

Opcionalmente:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/raizes_do_nordeste"
$env:DB_USERNAME = "raizes_app"
$env:JWT_EXPIRATION_MS = "3600000"
```

## Migrations

O banco é versionado pelo Flyway.

As migrations atuais são:

```text
V1__create_schema.sql
V2__add_optimistic_locking.sql
V3__seed_perfis_acesso.sql
```

### V1

Criação inicial das tabelas, relacionamentos e restrições do domínio.

### V2

Adição de controle de versão para concorrência otimista.

### V3

Carga inicial dos perfis de acesso utilizados pelo sistema.

As migrations são executadas automaticamente durante a inicialização da aplicação.

A configuração JPA utiliza:

```text
spring.jpa.hibernate.ddl-auto=validate
```

Portanto, o Hibernate valida a compatibilidade entre as entidades e o schema, enquanto as alterações estruturais são responsabilidade do Flyway.

## Executando a aplicação

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Quando a inicialização for concluída, a API estará disponível em:

```text
http://localhost:8080
```

## Fluxo principal implementado

O fluxo crítico implementado contempla:

1. autenticação do usuário;
2. criação do pedido vinculado a uma unidade;
3. validação dos itens;
4. cálculo dos valores pelo servidor;
5. verificação e reserva do estoque;
6. processamento de pagamento simulado;
7. conversão ou liberação das reservas de estoque;
8. atualização controlada do status do pedido;
9. conclusão do pedido;
10. crédito de pontos de fidelidade quando aplicável.

O pagamento é uma integração simulada, permitindo exercitar cenários de aprovação, recusa e falha sem utilizar um gateway externo real.

## Principais endpoints

Entre os endpoints implementados estão:

```text
POST   /auth/login
GET    /auth/me

POST   /pedidos
GET    /pedidos
GET    /pedidos/{pedidoId}
PATCH  /pedidos/{pedidoId}/status

POST   /pedidos/{pedidoId}/pagamentos
POST   /pedidos/{pedidoId}/fidelidade/resgate
```

Os contratos completos serão disponibilizados pela documentação OpenAPI/Swagger.

## Segurança

O projeto utiliza:

- autenticação por JWT;
- Spring Security;
- autorização baseada em perfis;
- senhas armazenadas por hash;
- validação dos dados recebidos pela API;
- tratamento padronizado de erros;
- variáveis de ambiente para credenciais e segredos;
- controle das transições de estado dos pedidos.

Os principais perfis previstos no domínio incluem:

```text
ADMIN
GERENTE
ATENDENTE
COZINHA
CLIENTE
```

## Testes

Execute a suíte automatizada com:

Windows:

```powershell
.\mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

Na validação atual:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Atualmente existe um teste automatizado de carregamento do contexto Spring (`contextLoads`).

Os fluxos funcionais principais também foram validados de forma integrada durante o desenvolvimento. Uma coleção Postman reproduzível com cenários positivos e negativos será adicionada ao repositório.

## Swagger / OpenAPI

A documentação OpenAPI/Swagger será configurada antes da entrega final do projeto.

Após sua implementação, esta seção será atualizada com o endereço de acesso à interface Swagger.

## Estrutura principal

```text
src/
├── main/
│   ├── java/
│   └── resources/
│       └── db/
│           └── migration/
└── test/
    └── java/
```

## Repositório

Repositório público:

```text
https://github.com/AABL07/raizes-do-nordeste-backend
```

## Status do projeto

Backend funcional com:

- persistência PostgreSQL;
- migrations Flyway;
- autenticação JWT;
- autorização por perfis;
- pedidos;
- estoque;
- pagamento mock;
- controle de status;
- fidelização;
- tratamento de erros.

Documentação OpenAPI/Swagger, coleção Postman e documentação acadêmica final estão em preparação para a entrega.