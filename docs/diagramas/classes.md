# Diagrama de Classes



O diagrama representa as principais classes do domínio da aplicação Raízes do Nordeste, seus atributos e relacionamentos.



Getters, setters e demais métodos triviais foram omitidos para manter a legibilidade.



```mermaid

classDiagram



    class Unidade {

        -Long id

        -String nome

        -String regiao

        -String logradouro

        -String numero

        -String complemento

        -String bairro

        -String cidade

        -String estado

        -String cep

        -Boolean ativa

    }



    class Produto {

        -Long id

        -String nome

        -String descricao

        -Boolean ativo

    }



    class VariacaoProduto {

        -Long id

        -String nome

        -String descricao

        -Boolean ativa

    }



    class ItemCardapio {

        -Long id

        -BigDecimal preco

        -Boolean disponivel

        -LocalDateTime criadoEm

        -LocalDateTime atualizadoEm

    }



    class PerfilAcesso {

        -Long id

        -String nome

        -String descricao

        -Boolean ativo

    }



    class Usuario {

        -Long id

        -String nome

        -String login

        -String senhaHash

        -Boolean ativo

        -LocalDateTime criadoEm

        -LocalDateTime atualizadoEm

    }



    class Cliente {

        -Long id

        -String nome

        -String email

        -String telefone

        -Boolean ativo

        -LocalDateTime criadoEm

        -LocalDateTime atualizadoEm

    }



    class Pedido {

        -Long id

        -CanalPedido canalPedido

        -StatusPedido status

        -BigDecimal subtotal

        -BigDecimal valorDesconto

        -BigDecimal valorFinal

        -LocalDateTime criadoEm

        -LocalDateTime atualizadoEm

    }



    class ItemPedido {

        -Long id

        -Integer quantidade

        -BigDecimal precoUnitario

        -BigDecimal subtotal

    }



    class Estoque {

        -Long id

        -Integer quantidadeFisica

        -Integer quantidadeReservada

        -LocalDateTime atualizadoEm

    }



    class ReservaEstoque {

        -Long id

        -Integer quantidade

        -StatusReservaEstoque status

        -LocalDateTime criadoEm

        -LocalDateTime resolvidoEm

    }



    class MovimentacaoEstoque {

        -Long id

        -TipoMovimentacaoEstoque tipo

        -Integer quantidade

        -String motivo

        -LocalDateTime criadoEm

    }



    class ContaFidelidade {

        -Long id

        -Integer saldoPontos

        -Integer pontosReservados

        -Boolean ativa

        -LocalDateTime criadoEm

        -LocalDateTime atualizadoEm

    }



    class Consentimento {

        -Long id

        -FinalidadeConsentimento finalidade

        -StatusConsentimento status

        -String versaoTermo

        -LocalDateTime concedidoEm

        -LocalDateTime revogadoEm

    }



    class MovimentacaoPontos {

        -Long id

        -TipoMovimentacaoPontos tipo

        -Integer quantidade

        -String descricao

        -LocalDateTime criadoEm

    }



    class Promocao {

        -Long id

        -String nome

        -String descricao

        -TipoBeneficio tipoBeneficio

        -BigDecimal valorBeneficio

        -Boolean permiteCombinacao

        -LocalDateTime inicioEm

        -LocalDateTime fimEm

        -Boolean ativa

    }



    class PromocaoAplicada {

        -Long id

        -TipoBeneficio tipoBeneficio

        -BigDecimal valorBeneficio

        -BigDecimal valorDescontoAplicado

        -LocalDateTime aplicadoEm

    }



    class TentativaPagamento {

        -Long id

        -String chaveIdempotencia

        -ResultadoPagamento resultado

        -BigDecimal valorSolicitado

        -String mensagemRetorno

        -LocalDateTime criadoEm

    }



    class SazonalidadeProduto {

        -Long id

        -LocalDate inicioEm

        -LocalDate fimEm

        -Boolean ativa

    }



    class RegistroAuditoria {

        -Long id

        -String acao

        -String recursoTipo

        -Long recursoId

        -OrigemAuditoria origem

        -String detalhes

        -LocalDateTime criadoEm

    }



    Unidade "1" --> "0..*" Usuario : possui

    PerfilAcesso "1" --> "0..*" Usuario : define



    Usuario "0..1" --> "0..1" Cliente : vincula



    Unidade "1" --> "0..*" Pedido : recebe

    Cliente "0..1" --> "0..*" Pedido : realiza



    Produto "1" --> "0..*" VariacaoProduto : possui



    Unidade "1" --> "0..*" ItemCardapio : disponibiliza

    Produto "1" --> "0..*" ItemCardapio : compoe

    VariacaoProduto "0..1" --> "0..*" ItemCardapio : especifica



    Pedido "1" --> "0..*" ItemPedido : contem

    ItemCardapio "1" --> "0..*" ItemPedido : referencia



    Unidade "1" --> "0..*" Estoque : mantem

    Produto "1" --> "0..*" Estoque : armazenado



    Estoque "1" --> "0..*" ReservaEstoque : possui

    ItemPedido "1" --> "0..*" ReservaEstoque : origina



    Estoque "1" --> "0..*" MovimentacaoEstoque : registra

    ItemPedido "0..1" --> "0..*" MovimentacaoEstoque : referencia

    ReservaEstoque "0..1" --> "0..1" MovimentacaoEstoque : converte



    Cliente "1" --> "0..1" ContaFidelidade : possui

    Cliente "1" --> "0..*" Consentimento : concede



    ContaFidelidade "1" --> "0..*" MovimentacaoPontos : registra

    Pedido "1" --> "0..*" MovimentacaoPontos : origina



    Pedido "1" --> "0..*" TentativaPagamento : possui



    Pedido "1" --> "0..*" PromocaoAplicada : recebe

    Promocao "1" --> "0..*" PromocaoAplicada : origina



    ItemCardapio "1" --> "0..*" SazonalidadeProduto : possui



    Usuario "0..1" --> "0..*" RegistroAuditoria : gera

```



## Observações



O diagrama representa as classes da camada `domain.model`. As classes de persistência JPA, DTOs, controllers, casos de uso, adapters e configurações pertencem a outras camadas da arquitetura e não são duplicadas neste diagrama.



Os tipos `CanalPedido`, `StatusPedido`, `StatusReservaEstoque`, `TipoMovimentacaoEstoque`, `TipoMovimentacaoPontos`, `ResultadoPagamento`, `TipoBeneficio`, `FinalidadeConsentimento`, `StatusConsentimento` e `OrigemAuditoria` são enums do domínio.



Os campos de controle de concorrência otimista `version`, presentes nas tabelas `estoque` e `conta_fidelidade`, pertencem ao mapeamento de persistência e por isso aparecem no DER, mas não nas classes de domínio acima.