# Desafio Técnico — Viasoft

Sistema de gestão de insumos agrícolas com CRUD completo de produtos e produtores, fluxo de venda com cálculo de desconto por safra e relatório de vendas. Desenvolvido em **Java 17 + Spring Boot** (back-end) e **Angular 13** (front-end).

---

## Sumário

- [Visão Geral](#visão-geral)
- [Domínio](#domínio)
- [Arquitetura](#arquitetura)
- [Back-end](#back-end)
- [Front-end](#front-end)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Endpoints da API](#endpoints-da-api)

---

## Visão Geral

A aplicação simula um sistema de vendas de insumos agrícolas. Um **Produtor** (identificado por CNPJ e limite de crédito) pode comprar **Produtos** (sementes, fertilizantes ou defensivos). O sistema aplica automaticamente um desconto de **5% para produtos cuja safra corresponde à estação atual**, calculado no back-end e refletido no front-end antes da finalização da compra.

---

## Domínio

```
Produtor (Producer)
  ├── id, name, cnpj, creditLimit
  └── possui um limite de crédito que é decrementado a cada venda

Produto (Product)
  ├── id, name, price, category (SEMENTE | FERTILIZANTE | DEFENSIVO)
  ├── stockQuantity, safra (VERAO | OUTONO | INVERNO | PRIMAVERA)
  ├── recipeProduct (obrigatorio apenas para categoria DEFENSIVO)
  └── version (controle de concorrencia otimista)

Venda (Sale)
  ├── id, producer, totalValue
  └── items → SaleItem (product, quantity, priceAtTimeOfSale)

Regras de negócio
  ├── Produto DEFENSIVO exige campo recipeProduct
  ├── Estoque é decrementado na criação e restaurado na exclusão da venda
  ├── Limite de crédito do produtor é validado antes de confirmar a venda
  ├── Desconto de 5% é aplicado quando a safra do produto = estação atual
  └── O back-end é a fonte de verdade para todos os calculos de preço
```

---

## Arquitetura

```
testeViasoft/
├── backend/api/          # Spring Boot — API REST
│   └── src/main/java/com/ronald/gustmann/api/
│       ├── controller/   # Camada HTTP — recebe e responde requisições
│       ├── service/      # Regras de negócio
│       ├── repository/   # Acesso a dados (Spring Data JPA)
│       ├── model/        # Entidades JPA
│       ├── dto/          # Objetos de transferência (records Java)
│       ├── mapper/       # Conversão entre DTO ↔ entidade (MapStruct)
│       ├── exceptions/   # Exceções de domínio customizadas
│       ├── infra/        # Exception handler global (@ControllerAdvice)
│       ├── config/       # CORS e configurações Spring
│       └── utils/        # SeasonUtils — cálculo da estação do frontend/             # Angular 13 — SPA
    └── src/app/
        ├── features/
        │   ├── product/  # Listagem, cadastro e edição de produtos
        │   └── sale/     # Checkout, relatório de vendas, form de produtores
        └── shared/
            ├── services/ # CartService (estado do carrinho via BehaviorSubject)
            ├── components/# Header, CartModal
            ├── directives/# Máscara de CNPJ
            ├── validators/# Validator customizado de CNPJ
            └── utils/     # Funções utilitárias de CNPJ
```

---

## Back-end

### Stack

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.3 | Framework web |
| Spring Data JPA | — | Acesso a dados |
| H2 Database | — | Banco em memória (sem configuração local) |
| Lombok | 1.18.30 | Redução de boilerplate |
| MapStruct | 1.6.0-Beta1 | Mapeamento DTO ↔ entidade |
| Bean Validation | — | Validação de entrada |
| Maven | — | Build e gerenciamento de dependências |

### Fluxo de venda

```
Frontend                          Backend
   │                                 │
   ├─ POST /sales/calculate-item ──► │ calcula desconto por safra
   │ ◄── priceAtTimeOfSale, discount ┤
   │                                 │
   ├─ POST /sales/create ──────────► │ valida estoque + crédito
   │                                 │ decrementa estoque
   │                                 │ decrementa creditLimit
   │ ◄── SaleResponseDTO ────────────┤
```

### Tratamento de erros

Todas as exceções são interceptadas por `RestExceptionHandler` (`@ControllerAdvice`) e retornam um JSON estruturado:

```json
{
  "status": 422,
  "message": "Estoque insuficiente para o produto 3"
}
```

| Exceção | Status HTTP |
|---|---|
| `EntityNotFoundException` | 404 |
| `InsufficientCreditLimitException` | 422 |
| `InsufficientProductStockException` | 422 |
| `DefensiveNotContainRecipeException` | 422 |
| `ObjectOptimisticLockingFailureException` | 409 |
| `Exception` (genérica) | 500 |

---

## Front-end

### Stack

| Tecnologia | Versão | Uso |
|---|---|---|
| Angular | 13 | Framework SPA |
| RxJS | 7.5 | Programação reativa |
| Reactive Forms | — | Formulários com validação |
| ng-icons / Heroicons | 13.3 | Ícones |
| Tailwind CSS | 3 | Estilos utilitários |

### Telas

| Rota | Componente | Descrição |
|---|---|---|
| `/products` | `ProductListComponent` | Lista de produtos com filtro por nome e categoria |
| `/products/new` | `ProductFormComponent` | Cadastro de produto com validações |
| `/products/edit/:id` | `ProductFormComponent` | Edição de produto (mesma tela) |
| `/producers/new` | `ProducerFormComponent` | Cadastro de produtor com máscara de CNPJ |
| `/producers/edit/:id` | `ProducerFormComponent` | Edição de produtor |
| `/checkout` | `CheckoutComponent` | Revisão do carrinho + finalização da compra |
| `/sales` | `SalesReportComponent` | Relatório de vendas com filtro por produtor |

### Estado do carrinho

O `CartService` gerencia o carrinho via `BehaviorSubject`, expondo observables para contagem e total. O `CheckoutComponent` consulta o back-end com `forkJoin` para obter os preços com desconto calculados pelo servidor antes de exibir o resumo ao usuário.

### Validações de formulário

- **ProductForm:** nome (min 3 chars), preço (> 0), estoque (≥ 0), safra obrigatória, `recipeProduct` obrigatório apenas quando categoria = `DEFENSIVO` (validação dinâmica via `valueChanges`)
- **ProducerForm:** nome, CNPJ (validator customizado por contagem de dígitos), limite de crédito (> 0)

---

## Como Executar

### Pré-requisitos

- Java 17+
- Maven (ou use o wrapper `./mvnw` incluso)
- Node.js 16+ e npm

### 1. Back-end

```bash
cd backend/api
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. O banco H2 é criado em memória automaticamente — **nenhuma configuração de banco é necessária**.

> Console H2 disponível em `http://localhost:8080/h2-console`
> - JDBC URL: `jdbc:h2:mem:database`
> - User: `sa` | Password: *(vazio)*

### 2. Front-end

```bash
cd frontend
npm install
npm start
```

A aplicação sobe em `http://localhost:4200`.

### Fluxo recomendado para demonstração

1. Cadastrar um **Produtor** (`/producers/new`) com limite de crédito
2. Cadastrar alguns **Produtos** (`/products/new`), variando categoria e safra
3. Na listagem de produtos, adicionar itens ao **carrinho**
4. Acessar **Checkout** (`/checkout`), selecionar o produtor e finalizar
5. Ver a venda registrada em **Relatório** (`/sales`)

---

## Testes

### Executar

```bash
cd backend/api
./mvnw test
```

### Cobertura atual

| Classe de teste | Escopo | Casos |
|---|---|---|
| `ApiApplicationTests` | Contexto Spring | Verifica que a aplicação sobe sem erros |
| `ProductServiceTest` | `ProductService` | Criação de produto, regra DEFENSIVO sem/com receita, busca por ID, delete inexistente |
| `SaleServiceDiscountTest` | `SaleService` | Desconto 5% na safra correta, sem desconto em safra distinta, desconto é por unidade |

Os testes usam `@SpringBootTest` com banco H2 em memória e são isolados com `@Transactional` (rollback automático).

> Os testes de `SaleServiceDiscountTest` constroem os cenários de forma dinâmica, determinando a estação atual via `SeasonUtils.getSeason(LocalDate.now())`, para garantir que o comportamento seja correto independente de quando os testes forem executados.

---

## Endpoints da API

> Uma collection Postman está disponível em `backend/api/postman/`.

### Produtores — `/producers`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/producers/create` | Cadastrar produtor |
| `GET` | `/producers/{id}` | Buscar por ID |
| `GET` | `/producers/all` | Listar todos |
| `PUT` | `/producers/{id}` | Editar produtor |
| `DELETE` | `/producers/{id}` | Remover produtor |

### Produtos — `/products`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/products/create` | Cadastrar produto |
| `GET` | `/products` | Listar todos |
| `GET` | `/products/{id}` | Buscar por ID |
| `PUT` | `/products/{id}` | Editar produto |
| `DELETE` | `/products/{id}` | Remover produto |

### Vendas — `/sales`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/sales/create` | Registrar venda |
| `GET` | `/sales/{id}` | Buscar por ID |
| `GET` | `/sales/all` | Listar todas |
| `PUT` | `/sales/{id}` | Editar venda |
| `DELETE` | `/sales/{id}` | Remover venda (restaura estoque) |
| `POST` | `/sales/calculate-item` | Calcular preço unitário com desconto (preview) |

### Exemplos de payload

**Cadastrar produto:**
```json
POST /products/create
{
  "name": "Glifosato 480",
  "price": 280.00,
  "category": "DEFENSIVO",
  "recipeProduct": "Receita Agronômica #042",
  "stockQuantity": 50,
  "safra": "VERAO"
}
```

**Cadastrar produtor:**
```json
POST /producers/create
{
  "name": "Fazenda São João",
  "cnpj": "12345678000195",
  "creditLimit": 10000.00
}
```

**Registrar venda:**
```json
POST /sales/create
{
  "producerId": 1,
  "items": [
    { "productId": 1, "quantity": 2, "total": 560.00 }
  ],
  "totalValue": 532.00
}
```

**Calcular item com desconto:**
```json
POST /sales/calculate-item
{
  "productId": 1,
  "quantity": 2,
  "total": 560.00
}
```
Resposta:
```json
{
  "productId": 1,
  "quantity": 2,
  "priceAtTimeOfSale": 266.00,
  "originalUnitPrice": 280.00,
  "discountValue": 14.00
}
```
