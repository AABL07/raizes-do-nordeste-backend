Add-Type -AssemblyName System.Net.Http

$ErrorActionPreference = "Stop"

$environmentPath = ".\docs\postman\Raizes-do-Nordeste.postman_environment.json"
$resultPath = ".\docs\postman\test-results.txt"

$environment = Get-Content $environmentPath -Raw -Encoding UTF8 | ConvertFrom-Json

$vars = @{}

foreach ($item in $environment.values) {
    $vars[$item.key] = $item.value
}

$baseUrl = $vars["baseUrl"]

$client = [System.Net.Http.HttpClient]::new()

$results = New-Object System.Collections.Generic.List[object]

$adminToken = $null
$clienteToken = $null
$cozinhaToken = $null
$pedidoId = $null
$tentativaPagamentoId = $null
$idempotencyKey = $null

function Send-ApiRequest {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Method,

        [Parameter(Mandatory = $true)]
        [string]$Path,

        [object]$Body = $null,

        [string]$Token = $null
    )

    $httpMethod = [System.Net.Http.HttpMethod]::new($Method)

    $request = [System.Net.Http.HttpRequestMessage]::new(
        $httpMethod,
        "$baseUrl$Path"
    )

    if ($Token) {
        $request.Headers.Authorization =
            [System.Net.Http.Headers.AuthenticationHeaderValue]::new(
                "Bearer",
                $Token
            )
    }

    if ($null -ne $Body) {

        $jsonBody = $Body |
            ConvertTo-Json -Depth 20 -Compress

        $request.Content =
            [System.Net.Http.StringContent]::new(
                $jsonBody,
                [System.Text.Encoding]::UTF8,
                "application/json"
            )
    }

    $response = $client.SendAsync($request).GetAwaiter().GetResult()

    $text = $response.Content.ReadAsStringAsync().GetAwaiter().GetResult()

    $json = $null

    if ($text) {
        try {
            $json = $text | ConvertFrom-Json
        }
        catch {
            $json = $null
        }
    }

    return [pscustomobject]@{
        Status = [int]$response.StatusCode
        Text   = $text
        Json   = $json
    }
}

function Assert-Equal {
    param(
        $Actual,
        $Expected,
        [string]$Message
    )

    if ($Actual -ne $Expected) {
        throw "$Message | Esperado: [$Expected] | Obtido: [$Actual]"
    }
}

function Assert-True {
    param(
        [bool]$Condition,
        [string]$Message
    )

    if (-not $Condition) {
        throw $Message
    }
}

function Run-Test {
    param(
        [int]$Number,
        [string]$Name,
        [scriptblock]$Action
    )

    try {

        & $Action

        $results.Add(
            [pscustomobject]@{
                Numero    = $Number
                Cenario   = $Name
                Resultado = "PASS"
                Detalhe   = ""
            }
        )

        Write-Host "[PASS] $Number - $Name"
    }
    catch {

        $results.Add(
            [pscustomobject]@{
                Numero    = $Number
                Cenario   = $Name
                Resultado = "FAIL"
                Detalhe   = $_.Exception.Message
            }
        )

        Write-Host "[FAIL] $Number - $Name"
        Write-Host "       $($_.Exception.Message)"
    }
}

Write-Host ""
Write-Host "=========================================="
Write-Host " RAIZES DO NORDESTE - TESTES DA API"
Write-Host "=========================================="
Write-Host ""

# ============================================================
# AUTH
# ============================================================

Run-Test 1 "Login ADMIN" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/auth/login" `
        -Body @{
            login = $vars["adminLogin"]
            senha = $vars["demoPassword"]
        }

    Assert-Equal $response.Status 200 "Login ADMIN deveria retornar HTTP 200"

    Assert-True `
        ($null -ne $response.Json.token -and $response.Json.token.Length -gt 0) `
        "Token ADMIN não foi retornado"

    $script:adminToken = $response.Json.token
}

Run-Test 2 "Consultar usuário ADMIN autenticado" {

    $response = Send-ApiRequest `
        -Method "GET" `
        -Path "/auth/me" `
        -Token $adminToken

    Assert-Equal $response.Status 200 "/auth/me deveria retornar HTTP 200"

    Assert-Equal `
        $response.Text.Trim() `
        "admin.demo" `
        "Usuário autenticado incorreto"
}

Run-Test 3 "Login CLIENTE" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/auth/login" `
        -Body @{
            login = $vars["clienteLogin"]
            senha = $vars["demoPassword"]
        }

    Assert-Equal $response.Status 200 "Login CLIENTE deveria retornar HTTP 200"

    Assert-True `
        ($null -ne $response.Json.token -and $response.Json.token.Length -gt 0) `
        "Token CLIENTE não foi retornado"

    $script:clienteToken = $response.Json.token
}

Run-Test 4 "Login COZINHA" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/auth/login" `
        -Body @{
            login = $vars["cozinhaLogin"]
            senha = $vars["demoPassword"]
        }

    Assert-Equal $response.Status 200 "Login COZINHA deveria retornar HTTP 200"

    Assert-True `
        ($null -ne $response.Json.token -and $response.Json.token.Length -gt 0) `
        "Token COZINHA não foi retornado"

    $script:cozinhaToken = $response.Json.token
}

# ============================================================
# PRODUCTS / CARDAPIO
# ============================================================

Run-Test 5 "Item de cardápio inexistente" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos" `
        -Token $clienteToken `
        -Body @{
            unidadeId = [long]$vars["unidadeId"]
            clienteId = [long]$vars["clienteId"]
            canalPedido = "APP"
            itens = @(
                @{
                    itemCardapioId = 999999999
                    quantidade = 1
                }
            )
        }

    Assert-Equal $response.Status 404 "Item inexistente deveria retornar HTTP 404"

    Assert-Equal `
        $response.Json.erro `
        "RECURSO_NAO_ENCONTRADO" `
        "Código de erro incorreto"
}

# ============================================================
# ORDERS
# ============================================================

Run-Test 6 "Criar pedido" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos" `
        -Token $clienteToken `
        -Body @{
            unidadeId = [long]$vars["unidadeId"]
            clienteId = [long]$vars["clienteId"]
            canalPedido = "APP"
            itens = @(
                @{
                    itemCardapioId = [long]$vars["itemCardapioId"]
                    quantidade = 2
                }
            )
        }

    Assert-Equal $response.Status 201 "Criação deveria retornar HTTP 201"

    Assert-Equal `
        $response.Json.status `
        "AGUARDANDO_PAGAMENTO" `
        "Status inicial do pedido incorreto"

    Assert-True `
        ([decimal]$response.Json.valorFinal -gt 0) `
        "Valor final deveria ser maior que zero"

    $script:pedidoId = [long]$response.Json.id
}

Run-Test 7 "Consultar pedido por ID" {

    $response = Send-ApiRequest `
        -Method "GET" `
        -Path "/pedidos/$pedidoId" `
        -Token $adminToken

    Assert-Equal $response.Status 200 "Consulta deveria retornar HTTP 200"

    Assert-Equal `
        ([long]$response.Json.id) `
        ([long]$pedidoId) `
        "Pedido retornado possui ID incorreto"
}

Run-Test 8 "Listar pedidos" {

    $response = Send-ApiRequest `
        -Method "GET" `
        -Path "/pedidos" `
        -Token $adminToken

    Assert-Equal $response.Status 200 "Listagem deveria retornar HTTP 200"

    Assert-True `
        ($null -ne $response.Json) `
        "A lista de pedidos não foi retornada"
}

# ============================================================
# FIDELITY
# ============================================================

Run-Test 9 "Reservar pontos no pedido" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos/$pedidoId/fidelidade/resgate" `
        -Token $clienteToken `
        -Body @{
            quantidadePontos = 100
        }

    Assert-Equal $response.Status 201 "Reserva deveria retornar HTTP 201"

    Assert-Equal `
        $response.Json.tipo `
        "RESERVA" `
        "Tipo da movimentação deveria ser RESERVA"

    Assert-Equal `
        ([int]$response.Json.quantidade) `
        100 `
        "Quantidade reservada incorreta"
}

# ============================================================
# PAYMENT
# ============================================================

Run-Test 10 "Aprovar pagamento" {

    $script:idempotencyKey =
        "powershell-" + [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds()

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos/$pedidoId/pagamentos" `
        -Token $clienteToken `
        -Body @{
            chaveIdempotencia = $idempotencyKey
            resultadoSimulado = "APROVADO"
        }

    Assert-Equal $response.Status 200 "Pagamento deveria retornar HTTP 200"

    Assert-Equal `
        $response.Json.resultado `
        "APROVADO" `
        "Pagamento deveria estar APROVADO"

    $script:tentativaPagamentoId = [long]$response.Json.id
}

Run-Test 11 "Repetir pagamento com mesma chave - idempotência" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos/$pedidoId/pagamentos" `
        -Token $clienteToken `
        -Body @{
            chaveIdempotencia = $idempotencyKey
            resultadoSimulado = "APROVADO"
        }

    Assert-Equal $response.Status 200 "Repetição deveria retornar HTTP 200"

    Assert-Equal `
        ([long]$response.Json.id) `
        ([long]$tentativaPagamentoId) `
        "A mesma tentativa deveria ser retornada"

    Assert-Equal `
        $response.Json.resultado `
        "APROVADO" `
        "Resultado idempotente incorreto"
}

Run-Test 12 "Alterar pedido para PRONTO" {

    $response = Send-ApiRequest `
        -Method "PATCH" `
        -Path "/pedidos/$pedidoId/status" `
        -Token $cozinhaToken `
        -Body @{
            status = "PRONTO"
        }

    Assert-Equal $response.Status 200 "Alteração deveria retornar HTTP 200"

    Assert-Equal `
        $response.Json.status `
        "PRONTO" `
        "Pedido deveria estar PRONTO"
}

Run-Test 13 "Alterar pedido para ENTREGUE" {

    $response = Send-ApiRequest `
        -Method "PATCH" `
        -Path "/pedidos/$pedidoId/status" `
        -Token $cozinhaToken `
        -Body @{
            status = "ENTREGUE"
        }

    Assert-Equal $response.Status 200 "Alteração deveria retornar HTTP 200"

    Assert-Equal `
        $response.Json.status `
        "ENTREGUE" `
        "Pedido deveria estar ENTREGUE"
}

# ============================================================
# ERRORS
# ============================================================

Run-Test 14 "Acesso sem JWT" {

    $response = Send-ApiRequest `
        -Method "GET" `
        -Path "/pedidos"

    Assert-Equal $response.Status 401 "Acesso sem JWT deveria retornar HTTP 401"

    Assert-Equal `
        $response.Json.erro `
        "NAO_AUTENTICADO" `
        "Código de autenticação incorreto"
}

Run-Test 15 "CLIENTE sem permissão para listar pedidos" {

    $response = Send-ApiRequest `
        -Method "GET" `
        -Path "/pedidos" `
        -Token $clienteToken

    Assert-Equal $response.Status 403 "CLIENTE deveria receber HTTP 403"

    Assert-Equal `
        $response.Json.erro `
        "ACESSO_NEGADO" `
        "Código de autorização incorreto"
}

Run-Test 16 "Criar pedido com quantidade zero" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos" `
        -Token $clienteToken `
        -Body @{
            unidadeId = [long]$vars["unidadeId"]
            clienteId = [long]$vars["clienteId"]
            canalPedido = "APP"
            itens = @(
                @{
                    itemCardapioId = [long]$vars["itemCardapioId"]
                    quantidade = 0
                }
            )
        }

    Assert-Equal $response.Status 400 "Quantidade zero deveria retornar HTTP 400"

    Assert-Equal `
        $response.Json.erro `
        "DADOS_INVALIDOS" `
        "Código de validação incorreto"
}

Run-Test 17 "Criar pedido com enum inválido" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos" `
        -Token $clienteToken `
        -Body @{
            unidadeId = [long]$vars["unidadeId"]
            clienteId = [long]$vars["clienteId"]
            canalPedido = "CANAL_INVALIDO"
            itens = @(
                @{
                    itemCardapioId = [long]$vars["itemCardapioId"]
                    quantidade = 1
                }
            )
        }

    Assert-Equal $response.Status 400 "Enum inválido deveria retornar HTTP 400"

    Assert-Equal `
        $response.Json.erro `
        "JSON_INVALIDO" `
        "Código de JSON inválido incorreto"
}

Run-Test 18 "Resgatar pontos após pedido entregue" {

    $response = Send-ApiRequest `
        -Method "POST" `
        -Path "/pedidos/$pedidoId/fidelidade/resgate" `
        -Token $clienteToken `
        -Body @{
            quantidadePontos = 100
        }

    Assert-Equal $response.Status 409 "Operação deveria retornar HTTP 409"

    Assert-Equal `
        $response.Json.erro `
        "CONFLITO" `
        "Código de conflito incorreto"
}

$client.Dispose()

$pass = @($results | Where-Object Resultado -eq "PASS").Count
$fail = @($results | Where-Object Resultado -eq "FAIL").Count
$total = $results.Count

Write-Host ""
Write-Host "=========================================="
Write-Host " RESULTADO"
Write-Host "=========================================="
Write-Host "Total : $total"
Write-Host "PASS  : $pass"
Write-Host "FAIL  : $fail"
Write-Host ""

$results | Format-Table Numero, Resultado, Cenario -AutoSize

$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

$evidence = @()
$evidence += "Raízes do Nordeste - Evidência de Testes da API"
$evidence += "Executado em: $timestamp"
$evidence += ""
$evidence += "Total: $total"
$evidence += "PASS:  $pass"
$evidence += "FAIL:  $fail"
$evidence += ""
$evidence += (
    $results |
        Format-Table Numero, Resultado, Cenario, Detalhe -AutoSize |
        Out-String
)

$evidence |
    Set-Content $resultPath -Encoding UTF8

Write-Host "Evidência salva em:"
Write-Host $resultPath

if ($fail -gt 0) {
    exit 1
}

exit 0
