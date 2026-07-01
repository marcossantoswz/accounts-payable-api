# Accounts Payable API

API REST desenvolvida em **Java** utilizando **Spring Boot** para gerenciamento de clientes e contas. O projeto simula uma integração com o SERPRO para validação e processamento de pagamentos, seguindo uma arquitetura em camadas.

## Funcionalidades

* Cadastro de clientes
* Consulta de clientes cadastrados
* Cadastro de contas vinculadas a um cliente
* Geração de comprovantes de arrecadação
* Emissão de relatórios fiscais
* Validação dos dados de pagamento
* Mapeamento entre DTOs e entidades utilizando Mappers
* Validação de dados de entrada com Bean Validation

---

## Tecnologias utilizadas

* Java 25
* Spring Boot 4
* Spring Web
* Spring Validation
* Spring Actuator
* Maven

---

## Arquitetura

O projeto segue uma arquitetura em camadas para separar responsabilidades:

```text
Controller
     ↓
Service
     ↓
Repository
```

Além disso, utiliza DTOs e Mappers para desacoplar a camada de apresentação das entidades de domínio.

```text
src
└── main
    ├── controller
    │   ├── mappers
    │   └── ...
    ├── model
    ├── service
    └── Application.java
```

---

## Endpoints

### Clientes

| Método | Endpoint    | Descrição                |
| ------ | ----------- | ------------------------ |
| GET    | `/clientes` | Lista todos os clientes  |
| POST   | `/clientes` | Cadastra um novo cliente |
| ... | ... | ... |

### Contas

| Método | Endpoint           | Descrição                          |
| ------ | ------------------ | ---------------------------------- |
| POST   | `/conta/{cpfCnpj}` | Cadastra uma conta para um cliente |
| ... | ... | ... |
### Outros recursos

O projeto também disponibiliza controladores para:

* Comprovantes de arrecadação serpro
* Relatório fiscal serpro
* Dados de pagamento serpro

---

## Como executar

### Pré-requisitos

* Java 25
* Maven 3.9+

### Clone o repositório

```bash
git clone https://github.com/marcossantoswz/accounts-payable-api.git
```

Entre na pasta do projeto:

```bash
cd accounts-payable-api
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```cmd
mvnw.cmd spring-boot:run
```

---

## Exemplo de requisição

Cadastro de cliente:

```http
POST /clientes
Content-Type: application/json
```

```json
{
  "nome": "João Silva",
  "cpfCnpj": "12345678900",
  "idade": 18,
  "endereco": "Rua Um"
}
```

Cadastro de conta:

```http
POST /conta/{cpfCnpj}
```

```json
{
  ...
}
```

> Os campos podem variar conforme os DTOs definidos na aplicação.

---

## Organização do projeto

* **controller** → Endpoints REST
* **repository** → banco de dados
* **service** → Regras de negócio
* **model** → Entidades do domínio
* **controller/mappers** → Conversão entre DTOs e entidades
