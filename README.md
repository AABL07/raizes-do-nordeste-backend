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
- Springdoc OpenAPI 3.0.3 / Swagger UI

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

Os contratos completos estão disponíveis pela documentação OpenAPI/Swagger.

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

Os fluxos funcionais principais também foram validados de forma integrada. O repositório inclui uma coleção Postman reproduzível e um runner PowerShell com cenários positivos e negativos.

## Swagger / OpenAPI

A API possui documentação interativa utilizando Springdoc OpenAPI.

Com a aplicação em execução, acesse:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Especificação OpenAPI: http://localhost:8080/v3/api-docs

A documentação utiliza autenticação Bearer JWT.

Para testar endpoints protegidos pelo Swagger:

1. Execute `POST /auth/login`.
2. Copie o token JWT retornado.
3. Clique em **Authorize**.
4. Informe o token no campo disponível.
5. Execute os endpoints protegidos.

O endpoint `POST /auth/login` é público. Os demais endpoints documentados exigem autenticação conforme as regras de acesso da aplicação.

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

A documentação OpenAPI/Swagger, os scripts de demonstração e a coleção de testes da API estão concluídos. A documentação acadêmica final será preparada separadamente para a entrega.

## Dados de demonstração

O diretório `docs/database` contém scripts auxiliares para execução e testes locais.

### Carregar dados de demonstração

Com o PostgreSQL em execução:

~~~powershell
psql -h localhost -p 5432 -U raizes_app -d raizes_do_nordeste -f .\docs\database\seed-demo.sql
~~~

O script cria registros destinados exclusivamente ao ambiente local de demonstração, incluindo:

- unidade;
- usuários com diferentes perfis de acesso;
- cliente;
- produto e item de cardápio;
- estoque;
- conta de fidelidade;
- consentimento para fidelidade.

As credenciais presentes nos dados de demonstração não devem ser utilizadas em produção.

### Restaurar o estado de testes

Antes de repetir os testes integrados, execute:

~~~powershell
psql -h localhost -p 5432 -U raizes_app -d raizes_do_nordeste -f .\docs\database\reset-demo.sql
~~~

O reset remove transações geradas pelos testes e restaura o estado esperado do estoque e da fidelidade.

Estado inicial esperado:

~~~text
Pedidos demo:       0
Estoque físico:     100
Estoque reservado:  0
Saldo de pontos:    1000
Pontos reservados:  0
~~~

## Testes da API

Os artefatos de testes funcionais estão localizados em `docs/postman`.

Arquivos:

~~~text
Raizes-do-Nordeste.postman_collection.json
Raizes-do-Nordeste.postman_environment.json
run-tests.ps1
test-results.txt
~~~

A coleção Postman utiliza o formato Collection v2.1 e contém cenários organizados nos grupos:

~~~text
Auth
Products
Orders
Fidelity
Payment
Errors
~~~

Como a API não expõe um endpoint REST específico para produtos, a disponibilidade de produto, cardápio e estoque é validada indiretamente durante a criação do pedido.

A suíte contempla autenticação, autorização por perfil, criação e consulta de pedidos, reserva de pontos, processamento de pagamento, idempotência, evolução de status e cenários de erro.

### Executar os testes integrados

Pré-requisitos:

~~~text
Aplicação executando em http://localhost:8080
PostgreSQL disponível
Dados de demonstração carregados
Estado de testes restaurado
~~~

No PowerShell:

~~~powershell
powershell -ExecutionPolicy Bypass -File .\docs\postman\run-tests.ps1
~~~

O runner utiliza os mesmos dados e fluxos definidos na coleção Postman e salva a evidência em:

~~~text
docs/postman/test-results.txt
~~~

Na execução de validação realizada em 01/11/2026, foram obtidos:

~~~text
Total: 18
PASS:  18
FAIL:  0
~~~

Os cenários incluem casos positivos e negativos, entre eles:

- login e autenticação JWT;
- autorização por perfil;
- item de cardápio inexistente;
- criação e consulta de pedido;
- reserva de pontos;
- pagamento aprovado;
- idempotência de pagamento;
- transições para `PRONTO` e `ENTREGUE`;
- acesso sem autenticação;
- acesso com perfil sem permissão;
- validação de quantidade inválida;
- enum inválido;
- tentativa de resgate de pontos em estado incompatível.

## Testes automatizados Java

O projeto também possui teste de carregamento do contexto Spring Boot executado pelo Maven:

~~~powershell
.\mvnw.cmd test
~~~

Na validação realizada em 01/11/2026:

~~~text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
~~~

Esse teste verifica a inicialização do contexto da aplicação.

Os testes funcionais/integrados da API são mantidos separadamente nos artefatos de `docs/postman`.
