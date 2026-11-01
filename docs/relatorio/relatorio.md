# Projeto Multidisciplinar — Back-End

## 1. Introdução

Este relatório apresenta o desenvolvimento do back-end do sistema **Raízes do Nordeste**, realizado no contexto do Projeto Multidisciplinar da trilha Back-End.

A solução foi desenvolvida como uma API REST destinada a apoiar a operação de uma rede de lanchonetes com múltiplas unidades e diferentes canais de atendimento. O sistema centraliza operações relacionadas a pedidos, cardápio, estoque, pagamento simulado, fidelização de clientes, controle de status e segurança de acesso.

O desenvolvimento buscou aplicar conceitos de arquitetura de software, orientação a objetos, persistência de dados, APIs REST, autenticação e autorização, validação de dados, tratamento de erros, documentação de APIs e testes.

A implementação foi realizada utilizando **Java 21**, **Spring Boot 4.0.9** e **PostgreSQL**, com gerenciamento do esquema do banco por meio do Flyway. A API também utiliza Spring Security e JWT para autenticação, Spring Data JPA para persistência e Springdoc OpenAPI para geração da documentação interativa.

O código-fonte e os artefatos técnicos do projeto são mantidos em repositório Git público, permitindo rastreabilidade das alterações realizadas durante o desenvolvimento.

## 2. Contexto e requisitos do sistema

O sistema Raízes do Nordeste representa o back-end de uma rede de lanchonetes que necessita operar com diferentes unidades, produtos, cardápios regionais, canais de atendimento e perfis de usuários.

Entre os principais processos contemplados pelo domínio estão a criação de pedidos, validação dos itens solicitados, controle de estoque por unidade, processamento simulado de pagamentos, evolução do estado do pedido e participação do cliente em programa de fidelidade.

### 2.1 Pedidos e unidades

Todo pedido deve estar associado a uma unidade válida da rede. Para novas operações, a unidade precisa estar ativa, preservando-se entretanto o histórico de operações já realizadas.

Os itens adicionados ao pedido devem possuir quantidade maior que zero e devem corresponder a produtos e itens de cardápio existentes e disponíveis. Caso qualquer item seja inválido, o pedido não deve ser criado parcialmente.

O valor dos itens e do pedido é calculado pelo sistema. O preço utilizado na operação é preservado no histórico do pedido, evitando que alterações posteriores no cardápio modifiquem transações anteriores.

O modelo também considera disponibilidade regional e sazonal dos produtos.

### 2.2 Estoque

A criação do pedido realiza a validação da disponibilidade de estoque da unidade.

O estoque disponível é determinado considerando o estoque físico e as quantidades já reservadas. Uma reserva não pode provocar disponibilidade negativa.

Quando um pedido é criado, os itens necessários são reservados. Após a confirmação do pagamento, a reserva pode ser convertida em saída efetiva. Em situações como pagamento recusado, falha de pagamento ou cancelamento permitido, as reservas correspondentes são liberadas.

### 2.3 Pagamento

O pagamento é representado por um gateway simulado, permitindo reproduzir resultados de aprovação, recusa e falha técnica sem integração com um provedor financeiro real.

O processamento utiliza uma chave de idempotência para evitar que a repetição da mesma requisição gere múltiplas operações de pagamento.

Um pedido somente pode entrar em preparação após a aprovação do pagamento.

Quando o pagamento é aprovado, o pedido avança para `EM_PREPARO`. Em caso de recusa, assume `PAGAMENTO_RECUSADO`, e em caso de falha técnica, `FALHA_PAGAMENTO`.

O sistema também permite nova tentativa de pagamento nos estados previstos pelas regras de negócio, realizando novamente as validações necessárias antes do processamento.

### 2.4 Fluxo de estados

O fluxo de pedido utiliza estados controlados pela aplicação, evitando transições incompatíveis.

Entre os principais estados estão:

```text
AGUARDANDO_PAGAMENTO
PAGAMENTO_RECUSADO
FALHA_PAGAMENTO
EM_PREPARO
PRONTO
ENTREGUE
CANCELADO
```

Após a aprovação do pagamento, o pedido segue para preparação. Posteriormente pode avançar de `EM_PREPARO` para `PRONTO` e de `PRONTO` para `ENTREGUE`.

O estado `ENTREGUE` representa a conclusão bem-sucedida da operação, enquanto `CANCELADO` representa encerramento por cancelamento. As regras impedem alterações incompatíveis após estados terminais.

### 2.5 Fidelização

O sistema contempla participação voluntária no programa de fidelidade.

A utilização do recurso depende da existência de cliente válido, conta de fidelidade ativa e consentimento correspondente.

O cliente pode reservar pontos antes do pagamento quando possuir saldo disponível. Os pontos reservados são confirmados quando a operação é efetivamente concluída conforme as regras de negócio ou liberados quando ocorre recusa, falha ou cancelamento aplicável.

O crédito de pontos referente à compra ocorre apenas após o pedido atingir o estado `ENTREGUE` e deve ocorrer uma única vez por pedido.

### 2.6 Segurança e perfis

As operações protegidas exigem autenticação.

O sistema utiliza perfis de acesso para limitar as funcionalidades disponíveis a cada tipo de usuário. Os principais perfis são:

```text
ADMIN
GERENTE
ATENDENTE
COZINHA
CLIENTE
```

Além da autenticação, os endpoints aplicam autorização conforme o perfil necessário para cada operação.

## 3. Arquitetura da solução

A aplicação foi organizada buscando separar responsabilidades entre interface HTTP, regras de aplicação, domínio e infraestrutura.

A estrutura geral pode ser representada pelo seguinte fluxo:

```text
Cliente / App / Web / Totem
             |
             v
          API REST
             |
             v
        Application
             |
             v
           Domain
             ^
             |
      Infrastructure
             |
             v
        PostgreSQL
```

### 3.1 Camada de API

A camada de API representa o ponto de entrada das requisições HTTP.

Ela contém:

- controllers REST;
- DTOs de entrada e saída;
- validação das requisições;
- integração com Spring Security;
- tratamento padronizado de erros;
- documentação OpenAPI/Swagger.

Os controllers recebem as requisições, validam os contratos HTTP e delegam a execução das regras aos casos de uso da camada de aplicação.

### 3.2 Camada de aplicação

A camada de aplicação coordena os casos de uso do sistema.

Nela estão concentrados os fluxos relacionados principalmente a:

- pedidos;
- estoque;
- pagamento;
- fidelização.

Essa camada utiliza os objetos do domínio e depende de contratos definidos pela aplicação e pelo domínio, evitando acoplamento direto das regras de negócio com tecnologias externas.

O processamento de pagamento utiliza uma abstração de gateway. No ambiente desenvolvido para o projeto, esse contrato é implementado por um gateway de pagamento simulado.

### 3.3 Camada de domínio

A camada de domínio representa os conceitos centrais do negócio.

Ela contém:

- entidades;
- enums;
- regras de negócio;
- contratos de repositório.

A definição dos contratos de persistência no domínio permite que as regras de negócio não dependam diretamente do Spring Data JPA ou do PostgreSQL.

### 3.4 Camada de infraestrutura

A infraestrutura fornece as implementações técnicas necessárias para os contratos definidos pelas camadas superiores.

Entre seus componentes estão:

- adaptadores de repositório;
- Spring Data JPA;
- mapeadores entre persistência e domínio;
- implementação de autenticação JWT;
- filtro de autenticação;
- gateway de pagamento simulado.

A persistência utiliza PostgreSQL, enquanto o gerenciamento e versionamento do esquema são realizados pelo Flyway.

### 3.5 Diagramas

Os diagramas produzidos durante o projeto estão armazenados no diretório `docs/diagramas`.

Foram elaborados os seguintes artefatos:

```text
arquitetura.md
casos-de-uso.md
der.md
classes.md
sequencia.md
```

Esses diagramas representam, respectivamente, a visão arquitetural da solução, atores e casos de uso, estrutura do banco de dados, principais classes do domínio e fluxo de uma operação crítica de pedido e pagamento.

## 4. Modelagem do domínio e banco de dados

A persistência da aplicação utiliza PostgreSQL, com versionamento do esquema realizado por migrations do Flyway.

A migration inicial cria as estruturas necessárias para representar unidades, usuários, clientes, produtos, pedidos, estoque, fidelização, promoções e pagamentos.

### 4.1 Estrutura do banco

O esquema principal contém as seguintes tabelas:

```text
unidade
produto
perfil_acesso
usuario
cliente
pedido
variacao_produto
item_cardapio
item_pedido
estoque
reserva_estoque
movimentacao_estoque
conta_fidelidade
consentimento
movimentacao_pontos
registro_auditoria
promocao
promocao_aplicada
tentativa_pagamento
sazonalidade_produto
```

As tabelas utilizam chaves primárias e estrangeiras para manter a integridade referencial entre os principais elementos do domínio.

Por exemplo, pedidos estão relacionados à unidade de origem e, quando aplicável, ao cliente. Os itens de pedido mantêm relação com os itens de cardápio, enquanto movimentações e reservas permitem controlar o estoque associado às operações.

### 4.2 Controle de pagamento

A tabela `tentativa_pagamento` registra as tentativas realizadas para cada pedido.

Entre seus dados estão:

- pedido relacionado;
- chave de idempotência;
- resultado;
- valor solicitado;
- mensagem de retorno;
- data e hora da tentativa.

Existe uma restrição de unicidade entre pedido e chave de idempotência, auxiliando a impedir duplicação lógica de uma mesma solicitação de pagamento.

### 4.3 Estoque

O modelo de estoque diferencia quantidade física de quantidade reservada.

As reservas são registradas separadamente, permitindo que o sistema determine a disponibilidade real antes da confirmação de uma operação.

As movimentações de estoque permitem representar entradas e saídas associadas ao ciclo dos pedidos.

### 4.4 Fidelização e consentimento

O domínio possui estruturas específicas para conta de fidelidade, movimentação de pontos e consentimento.

A tabela `consentimento` permite registrar a finalidade, situação e informações relacionadas à autorização do cliente.

O banco também possui restrições para impedir mais de um consentimento ativo para a mesma finalidade e cliente.

As movimentações de pontos permitem representar operações de crédito, reserva, confirmação e liberação conforme o fluxo de fidelidade.

### 4.5 Concorrência

As estruturas de estoque e conta de fidelidade possuem campo de versão utilizado para controle de concorrência otimista.

Esse mecanismo reduz o risco de alterações simultâneas produzirem inconsistências em recursos sensíveis, principalmente estoque e saldo de pontos.

### 4.6 Migrations

O banco é versionado pelo Flyway.

As migrations implementadas contemplam:

```text
V1 — criação do esquema inicial
V2 — inclusão de versionamento para controle otimista
V3 — carga dos perfis de acesso
```

Os perfis cadastrados pela migration correspondente são:

```text
ADMIN
GERENTE
ATENDENTE
COZINHA
CLIENTE
```

Além das migrations, o diretório `docs/database` contém scripts específicos para geração e restauração de dados de demonstração. Esses scripts não fazem parte do versionamento estrutural do Flyway e são destinados ao ambiente local de testes.

O Diagrama Entidade-Relacionamento está documentado em:

```text
docs/diagramas/der.md
```

## 5. Implementação do Back-End

O back-end foi implementado em Java 21 utilizando o ecossistema Spring.

Os principais componentes utilizados são:

```text
Spring Boot
Spring Web MVC
Spring Data JPA
Spring Security
Jakarta Validation
Springdoc OpenAPI
Flyway
JJWT
PostgreSQL
```

### 5.1 Controllers e DTOs

Os controllers representam a interface REST da aplicação.

As requisições recebidas são representadas por DTOs específicos e validadas antes da execução dos casos de uso.

Entre as validações existentes estão campos obrigatórios, quantidade mínima dos itens e validação dos valores pertencentes aos enums do domínio.

Essa abordagem evita que objetos HTTP sejam utilizados diretamente como entidades de domínio.

### 5.2 Casos de uso

As regras de aplicação são organizadas em casos de uso.

Entre os principais fluxos implementados estão:

- autenticação;
- criação de pedido;
- consulta de pedidos;
- alteração de status;
- controle e reserva de estoque;
- processamento de pagamento;
- reserva e utilização de pontos de fidelidade.

Os casos de uso coordenam os objetos do domínio e os contratos de persistência.

### 5.3 Persistência

O acesso ao banco é realizado por meio de adaptadores que utilizam Spring Data JPA.

Essa separação permite que os contratos utilizados pelas regras de negócio permaneçam independentes da tecnologia específica de persistência.

### 5.4 Pagamento simulado

O projeto não realiza integração financeira real.

Foi implementado um `PagamentoGateway` e uma implementação simulada utilizada para reproduzir os resultados:

```text
APROVADO
RECUSADO
FALHA_TECNICA
```

A utilização de um gateway simulado permite exercitar o ciclo completo do pedido sem necessidade de utilizar uma instituição financeira ou serviço externo.

### 5.5 Tratamento de erros

A API utiliza tratamento padronizado de exceções.

Erros de autenticação, autorização, validação, conflitos e recursos inexistentes são convertidos em respostas HTTP coerentes com o tipo de falha ocorrido.

Isso também permitiu verificar programaticamente cenários negativos durante os testes da API.

## 6. API REST e documentação OpenAPI

A aplicação disponibiliza sua funcionalidade por meio de uma API REST executada, por padrão, na porta `8080`.

Os principais endpoints implementados são:

| Método | Endpoint | Finalidade |
|---|---|---|
| POST | `/auth/login` | Autenticar usuário e gerar JWT |
| GET | `/auth/me` | Consultar usuário autenticado |
| POST | `/pedidos` | Criar pedido |
| GET | `/pedidos` | Listar pedidos |
| GET | `/pedidos/{pedidoId}` | Consultar pedido |
| PATCH | `/pedidos/{pedidoId}/status` | Alterar status |
| POST | `/pedidos/{pedidoId}/pagamentos` | Processar pagamento |
| POST | `/pedidos/{pedidoId}/fidelidade/resgate` | Reservar/resgatar pontos |

A aplicação não possui um endpoint REST específico de consulta de produtos. A existência, disponibilidade, cardápio, sazonalidade e estoque dos produtos são verificadas dentro do fluxo de criação e revalidação dos pedidos.

### 6.1 OpenAPI e Swagger

A documentação da API é gerada com Springdoc OpenAPI.

Com a aplicação em execução, estão disponíveis:

```text
Swagger UI:
http://localhost:8080/swagger-ui/index.html

Documento OpenAPI:
http://localhost:8080/v3/api-docs
```

Os endpoints protegidos apresentam o esquema Bearer JWT na documentação.

O login é público e permite obter o token utilizado para acessar as demais operações autorizadas.

## 7. Segurança e controle de acesso

A segurança da aplicação utiliza Spring Security em conjunto com JWT.

### 7.1 Autenticação

O endpoint:

```text
POST /auth/login
```

recebe as credenciais do usuário.

Após autenticação válida, a aplicação gera um token JWT que deve ser enviado nas requisições protegidas.

A aplicação utiliza política stateless, portanto a autenticação não depende de sessão HTTP armazenada no servidor.

As senhas dos usuários são armazenadas utilizando hash BCrypt.

Segredos de infraestrutura, como senha do banco e chave de assinatura JWT, são fornecidos por variáveis de ambiente e não são mantidos diretamente no código-fonte.

### 7.2 Autorização por perfil

Além da autenticação, a aplicação restringe operações conforme o perfil do usuário.

Exemplos:

| Operação | Perfis autorizados |
|---|---|
| Criar pedido | ADMIN, GERENTE, ATENDENTE, CLIENTE |
| Consultar pedidos | ADMIN, GERENTE, ATENDENTE, COZINHA |
| Alterar status | ADMIN, GERENTE, COZINHA |
| Processar pagamento | ADMIN, GERENTE, ATENDENTE, CLIENTE |
| Utilizar fidelidade | ADMIN, GERENTE, ATENDENTE, CLIENTE |

O endpoint `/auth/me` foi utilizado como operação administrativa de validação e exige perfil `ADMIN`.

As verificações de autorização são realizadas por meio do Spring Security e de anotações `@PreAuthorize`.

### 7.3 Respostas de segurança

A API diferencia falhas de autenticação e autorização.

Uma requisição sem autenticação válida resulta em resposta HTTP `401`.

Quando o usuário está autenticado, mas não possui o perfil exigido para a operação, a API retorna HTTP `403`.

Esses comportamentos também foram incluídos nos testes funcionais.

## 8. Privacidade e LGPD

O sistema manipula informações associadas a usuários e clientes e, portanto, o projeto considera princípios de proteção de dados pessoais previstos pela Lei Geral de Proteção de Dados Pessoais — LGPD.

A implementação acadêmica não pretende representar, isoladamente, uma certificação de conformidade legal completa. Entretanto, foram adotadas decisões de projeto alinhadas a princípios relevantes de privacidade e segurança.

### 8.1 Finalidade e necessidade

Os dados modelados são relacionados às funcionalidades necessárias ao funcionamento do sistema, tais como:

- identificação de usuários;
- associação de clientes a pedidos;
- autenticação;
- operação de fidelidade;
- registro das operações realizadas.

A aplicação evita utilizar dados pessoais para finalidades externas ao domínio proposto.

### 8.2 Consentimento

A participação no programa de fidelidade depende de consentimento válido.

O domínio possui uma estrutura específica para registrar consentimentos e suas finalidades.

A ausência ou revogação do consentimento impede novos usos relacionados à fidelização conforme as regras implementadas.

Esse mecanismo permite separar a existência de um cliente da sua participação voluntária no programa de benefícios.

### 8.3 Segurança

Entre as medidas técnicas aplicadas estão:

- armazenamento de senhas por hash BCrypt;
- autenticação com JWT;
- autorização baseada em perfis;
- segredos configurados por variáveis de ambiente;
- validação de dados de entrada;
- controle das transições de estado;
- integridade referencial no banco de dados;
- controle otimista de concorrência em recursos sensíveis.

### 8.4 Histórico e rastreabilidade

O modelo preserva informações históricas necessárias às operações, como preço utilizado no pedido e registros das tentativas de pagamento.

Existe ainda estrutura de banco destinada ao registro de auditoria de operações sensíveis.

Durante a validação funcional apresentada neste trabalho, porém, não foi realizada uma suíte específica de testes de auditoria; dessa forma, este relatório não apresenta evidência de cobertura automatizada dessa funcionalidade.

### 8.5 Ambiente de demonstração

As credenciais utilizadas nos scripts de demonstração destinam-se exclusivamente ao ambiente acadêmico e local.

Elas não devem ser reutilizadas em produção.

O arquivo de exemplo de variáveis de ambiente utiliza valores fictícios, enquanto as credenciais reais de banco e a chave JWT são configuradas fora do repositório.

## 9. Testes e validação

A validação do projeto foi realizada em duas frentes complementares:

1. teste automatizado de carregamento do contexto Spring Boot;
2. testes funcionais da API utilizando os mesmos fluxos documentados na coleção Postman.

### 9.1 Teste automatizado Java

O projeto possui um teste de carregamento do contexto da aplicação, executado por meio do Maven:

```powershell
.\mvnw.cmd test
```

Na execução final realizada em 01/11/2026 foi obtido:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

O teste existente verifica se o contexto Spring Boot consegue ser inicializado corretamente com os componentes e dependências configurados.

Esse resultado não deve ser interpretado como cobertura unitária completa das regras de negócio, pois a suíte Java atual contém especificamente o teste de inicialização do contexto.

### 9.2 Coleção Postman

Foi criada uma coleção Postman no formato Collection v2.1:

```text
docs/postman/Raizes-do-Nordeste.postman_collection.json
```

Também foi criado um ambiente Postman:

```text
docs/postman/Raizes-do-Nordeste.postman_environment.json
```

A coleção está organizada nos grupos:

```text
Auth
Products
Orders
Fidelity
Payment
Errors
```

Ao todo, foram definidos 18 cenários envolvendo operações positivas e negativas da API.

A coleção pode ser importada no Postman para execução e inspeção manual dos cenários.

### 9.3 Execução automatizada dos cenários da API

No ambiente utilizado durante a validação final, Newman e Postman CLI não estavam instalados.

Para evitar a inclusão de uma dependência adicional próxima ao prazo de entrega, foi utilizado o script:

```text
docs/postman/run-tests.ps1
```

Esse script reproduz os mesmos dados e fluxos definidos na coleção Postman e executa as requisições diretamente contra a API local.

O comando utilizado foi:

```powershell
powershell -ExecutionPolicy Bypass -File .\docs\postman\run-tests.ps1
```

A execução final apresentou:

```text
Total: 18
PASS:  18
FAIL:  0
```

Portanto, todos os 18 cenários funcionais definidos para essa validação foram concluídos com sucesso.

### 9.4 Cenários validados

Entre os cenários executados estão:

1. login com usuário administrador;
2. consulta do usuário administrador autenticado;
3. login com usuário cliente;
4. login com usuário da cozinha;
5. tentativa de utilizar item de cardápio inexistente;
6. criação de pedido;
7. consulta de pedido por identificador;
8. listagem de pedidos;
9. reserva de pontos no pedido;
10. aprovação de pagamento;
11. repetição do pagamento com a mesma chave de idempotência;
12. alteração do pedido para `PRONTO`;
13. alteração do pedido para `ENTREGUE`;
14. tentativa de acesso sem JWT;
15. tentativa de listagem por usuário sem permissão;
16. criação de pedido com quantidade zero;
17. criação de pedido com valor de enum inválido;
18. tentativa de resgate de pontos após pedido entregue.

Os testes verificaram, entre outros aspectos:

- autenticação JWT;
- autorização baseada em perfil;
- validação das requisições;
- criação e consulta de pedidos;
- estoque;
- reserva de pontos;
- processamento de pagamento;
- idempotência;
- transições de estado;
- respostas HTTP para cenários inválidos.

### 9.5 Preparação do ambiente de teste

Para tornar a execução reproduzível, foram criados dois scripts SQL auxiliares:

```text
docs/database/seed-demo.sql
docs/database/reset-demo.sql
```

O primeiro fornece dados destinados exclusivamente ao ambiente local de demonstração.

O segundo restaura o estado necessário para repetir os testes, incluindo estoque e conta de fidelidade.

Antes da validação final, o estado esperado era:

```text
Pedidos demo:       0
Estoque físico:     100
Estoque reservado:  0
Saldo de pontos:    1000
Pontos reservados:  0
```

### 9.6 Limitações da validação

A validação realizada não representa uma cobertura exaustiva de todas as combinações possíveis do domínio.

O projeto possui:

- um teste automatizado Java de carregamento do contexto;
- uma suíte funcional de 18 cenários executada contra a API;
- uma coleção Postman correspondente aos fluxos documentados.

Não foi executada uma suíte específica de testes automatizados para auditoria.

Também não foi utilizado Newman ou Postman CLI na execução final em linha de comando.

## 10. Evidências de execução

Os artefatos utilizados para comprovar a execução dos testes permanecem versionados no repositório.

### 10.1 Evidência dos testes funcionais

O resultado da execução dos 18 cenários está armazenado em:

```text
docs/postman/test-results.txt
```

O resumo registrado nesse arquivo é:

```text
Total: 18
PASS:  18
FAIL:  0
```

Esse arquivo registra individualmente o resultado de cada cenário executado pelo runner PowerShell.

### 10.2 Evidência do Maven

Na validação final do projeto, o comando:

```powershell
.\mvnw.cmd test
```

apresentou:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Durante essa execução, a aplicação conseguiu inicializar o contexto Spring, estabelecer conexão com o PostgreSQL e validar as migrations existentes.

### 10.3 Banco de dados e migrations

Durante a inicialização validada, o Flyway reconheceu o banco PostgreSQL e verificou as migrations do projeto.

O schema estava na versão esperada e não havia novas migrations pendentes.

Essa execução confirma a compatibilidade entre a aplicação, o esquema versionado e o banco utilizado no ambiente local de validação.

### 10.4 Swagger/OpenAPI

A documentação interativa foi validada com a aplicação em execução.

Os principais pontos de acesso são:

```text
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```

O Swagger apresenta o mecanismo Bearer JWT nos endpoints protegidos, mantendo o endpoint de login acessível sem autenticação.

### 10.5 Repositório e histórico

O projeto utiliza Git para controle de versão.

O repositório público utilizado é:

```text
https://github.com/AABL07/raizes-do-nordeste-backend
```

Os artefatos de testes, scripts de demonstração, documentação e código-fonte permanecem versionados juntamente com o histórico de desenvolvimento.

O commit responsável pela inclusão dos dados reproduzíveis e da suíte funcional da API foi:

```text
d7fdb7a test: adiciona dados de demonstracao e testes da API
```

## 11. Uso de Inteligência Artificial no desenvolvimento

Durante o desenvolvimento deste projeto foram utilizadas ferramentas de Inteligência Artificial generativa como apoio ao processo de análise, implementação, revisão e documentação.

As ferramentas utilizadas foram:

```text
ChatGPT Plus — OpenAI
Claude Sonnet — Anthropic
```

A utilização dessas ferramentas ocorreu como suporte ao desenvolvimento e não como substituição da análise, execução ou validação do projeto.

As sugestões produzidas por IA foram revisadas antes de serem incorporadas ao código-fonte ou à documentação.

### 11.1 Objetivos do uso de IA

As ferramentas foram utilizadas principalmente para:

- auxiliar na interpretação do estudo de caso;
- organizar requisitos e regras de negócio;
- discutir decisões arquiteturais;
- revisar estruturas de domínio;
- apoiar a implementação de partes do código;
- identificar inconsistências entre regras;
- auxiliar na criação de comandos e scripts;
- revisar documentação técnica;
- elaborar cenários de teste;
- analisar erros encontrados durante a execução;
- auxiliar na estruturação deste relatório.

O desenvolvimento foi realizado de forma iterativa, com execução local das alterações sugeridas e verificação dos resultados antes da continuidade do projeto.

### 11.2 ChatGPT Plus

O ChatGPT Plus foi utilizado como ferramenta principal de apoio interativo durante várias etapas do projeto.

Entre os usos realizados estão:

- decomposição do projeto em etapas menores;
- organização e consolidação das regras de negócio;
- revisão de classes e contratos;
- apoio à modelagem do banco;
- criação e revisão de migrations;
- revisão de endpoints;
- configuração de Swagger/OpenAPI;
- planejamento dos testes funcionais;
- preparação dos scripts de demonstração;
- diagnóstico de erros de PowerShell, banco e execução;
- revisão do README;
- organização da documentação acadêmica.

Exemplos representativos de solicitações realizadas durante o desenvolvimento:

```text
"Analise os requisitos e as regras de negócio do projeto e verifique
se existem conflitos ou redundâncias."

"Me auxilie a implementar o projeto passo a passo e valide cada etapa
antes de continuar."

"Revise os endpoints implementados e verifique se estão coerentes com
os casos de uso e perfis de acesso."

"Crie cenários positivos e negativos para validar a API."

"Analise a saída do terminal e identifique a causa do erro antes de
prosseguirmos."
```

Esses exemplos representam o tipo de interação realizado ao longo do projeto e não constituem uma transcrição integral de todas as conversas.

### 11.3 Claude Sonnet

O Claude Sonnet também foi utilizado como ferramenta complementar durante o desenvolvimento.

Seu uso ocorreu em conjunto com o ChatGPT para apoio à análise e revisão de propostas relacionadas ao projeto.

Exemplos representativos do tipo de solicitação utilizado:

```text
"Analise o estudo de caso e proponha uma estrutura para o projeto
Back-End."

"Revise as regras de negócio e identifique possíveis inconsistências."

"Analise a estrutura proposta e sugira melhorias de arquitetura e
organização."

"Revise a solução considerando os requisitos acadêmicos fornecidos."
```

As respostas obtidas foram utilizadas como apoio e comparação de alternativas, sendo avaliadas antes de qualquer incorporação ao projeto.

### 11.4 Forma de aproveitamento das respostas

As respostas das ferramentas de IA não foram utilizadas de maneira automática ou sem verificação.

O fluxo adotado foi, de forma geral:

```text
Requisito ou problema
        ↓
Consulta à ferramenta de IA
        ↓
Análise da sugestão
        ↓
Adaptação ao projeto
        ↓
Implementação local
        ↓
Compilação / execução
        ↓
Teste e validação
        ↓
Correção quando necessária
```

Esse processo foi particularmente importante em situações nas quais uma sugestão inicial não era compatível com o ambiente utilizado.

Durante o projeto ocorreram, por exemplo, ajustes relacionados a:

- diferenças do Windows PowerShell 5.1;
- configuração de variáveis de ambiente;
- autenticação do PostgreSQL;
- formato da chave JWT;
- execução dos testes funcionais;
- formatação dos arquivos de documentação.

Nesses casos, os erros observados no ambiente local foram analisados e as soluções foram ajustadas antes de serem consideradas concluídas.

### 11.5 Exemplos de conteúdo apoiado por IA

Entre os artefatos que receberam apoio de ferramentas de IA estão:

- organização das regras de negócio;
- estrutura de pacotes e arquitetura;
- modelos de domínio;
- migrations SQL;
- configurações de segurança;
- documentação OpenAPI;
- diagramas em Mermaid;
- scripts SQL de demonstração;
- coleção Postman;
- runner de testes em PowerShell;
- README;
- estrutura e redação técnica deste relatório.

O aproveitamento ocorreu mediante revisão, adaptação e validação prática.

### 11.6 Validação humana e responsabilidade

Todas as decisões finais sobre o projeto permaneceram sob responsabilidade do autor.

O código foi compilado e executado localmente, as migrations foram aplicadas em banco PostgreSQL e os principais fluxos da API foram testados.

As respostas de IA foram consideradas sugestões técnicas e não evidência de funcionamento por si mesmas.

Somente resultados observados no ambiente de execução foram utilizados como evidências neste relatório.

### 11.7 Transparência

A inclusão desta seção tem como objetivo registrar explicitamente o uso de Inteligência Artificial no desenvolvimento do trabalho.

O uso das ferramentas ocorreu como apoio técnico, educacional e de revisão.

Não se pretende atribuir às ferramentas de IA autoria ou responsabilidade pelas decisões finais tomadas no projeto.

O resultado entregue representa a integração entre análise do autor, implementação prática, testes executados e apoio das ferramentas digitais utilizadas.

## 12. Considerações finais

O desenvolvimento do projeto Raízes do Nordeste permitiu aplicar de forma integrada conceitos de desenvolvimento Back-End, modelagem de domínio, persistência de dados, segurança, documentação e testes de APIs.

A solução resultante implementa uma API REST capaz de representar os principais fluxos previstos para a operação da rede de lanchonetes, incluindo autenticação, autorização por perfis, pedidos, controle de estoque, pagamento simulado, evolução de status e fidelização.

A arquitetura adotada buscou separar as responsabilidades entre API, aplicação, domínio e infraestrutura. Essa organização reduz o acoplamento das regras de negócio com tecnologias específicas e facilita a manutenção e evolução da solução.

O uso do PostgreSQL associado ao Flyway permitiu versionar a estrutura do banco de dados e manter a integridade das informações. Também foram adotados mecanismos como chave de idempotência para pagamentos, controle otimista de concorrência em recursos sensíveis e validação das transições do ciclo de vida dos pedidos.

Na área de segurança, foram utilizados Spring Security, autenticação JWT, armazenamento de senhas com BCrypt e autorização baseada em perfis.

A documentação OpenAPI/Swagger permite inspecionar e testar os contratos disponibilizados pela API, enquanto os diagramas produzidos registram diferentes perspectivas da solução.

A validação final incluiu um teste automatizado de inicialização do contexto Spring Boot e uma suíte funcional com 18 cenários de API, todos aprovados na execução registrada. Os artefatos necessários para repetição desses testes foram mantidos no repositório.

O projeto também registrou de forma explícita o uso de ferramentas de Inteligência Artificial como apoio ao desenvolvimento. As sugestões obtidas foram analisadas, adaptadas e verificadas por meio da execução prática da aplicação.

Como possibilidade de evolução futura, a solução pode receber maior cobertura de testes unitários e de integração automatizados, ampliação da auditoria, integração com um provedor real de pagamentos, novos endpoints administrativos e mecanismos adicionais de observabilidade.

Dessa forma, o projeto atende ao objetivo de construir e documentar uma solução Back-End funcional e organizada, relacionando os requisitos propostos às decisões de arquitetura, implementação e validação realizadas durante o desenvolvimento.

## Referências

BRASIL. **Lei nº 13.709, de 14 de agosto de 2018**. Lei Geral de Proteção de Dados Pessoais (LGPD). Brasília, DF: Presidência da República, 2018.

ANTHROPIC. **Claude**. Ferramenta de Inteligência Artificial generativa utilizada como apoio complementar no desenvolvimento do projeto.

FLYWAY. **Flyway Documentation**. Documentação técnica do sistema de versionamento e migração de bancos de dados.

OPENAI. **ChatGPT**. Ferramenta de Inteligência Artificial generativa utilizada como apoio à análise, revisão, implementação e documentação do projeto.

POSTGRESQL GLOBAL DEVELOPMENT GROUP. **PostgreSQL Documentation**. Documentação técnica do sistema gerenciador de banco de dados PostgreSQL.

SPRING. **Spring Boot Reference Documentation**. Documentação técnica do framework Spring Boot.

SPRING. **Spring Data JPA Reference Documentation**. Documentação técnica do Spring Data JPA.

SPRING. **Spring Security Reference Documentation**. Documentação técnica do Spring Security.

SPRINGDOC. **Springdoc OpenAPI Documentation**. Documentação da integração OpenAPI utilizada para geração da documentação da API.

UNINTER. **Projeto Multidisciplinar — Estudo de Caso: Raízes do Nordeste**. Material acadêmico fornecido para desenvolvimento da atividade.

UNINTER. **Roteiro de Atividade Prática — Projeto Multidisciplinar Back-End — 2026**. Material de orientação da atividade acadêmica.
