# Construction Management System

## 🎯 Visão Geral
O sistema é responsável por gerenciar empresas de construção civil, oferecendo:

- Cadastro de funcionários (CLT, PJ, Diária)
- Cadastro de obras e relatórios diários
- Solicitação e aprovação de materiais
- Gestão financeira com aprovação de sócios
- Múltiplos papéis: Worker, Manager, Partner, Owner
- Fluxos críticos orquestrados via Saga Pattern, garantindo consistência

---

## 🚀 Getting Started

This guide will walk you through setting up and running the application locally.

### Prerequisites
- **Java 17**: Make sure you have JDK 17 installed.
- **PostgreSQL**: The application requires a running PostgreSQL database instance.
- **Maven**: The project uses Maven for dependency management. The included Maven Wrapper (`mvnw`) is recommended, so you don't need a local Maven installation.

### Configuration
1.  **Create a Database**:
    -   In your PostgreSQL instance, create a new database. The application is configured to use a database named `constructiondb`.
    ```sql
    CREATE DATABASE constructiondb;
    ```

2.  **Environment Variables**:
    -   The application connects to the database using environment variables. You must set the following variables in your environment before running the application:
    -   `SPRING_DATASOURCE_URL`: The full JDBC URL for your PostgreSQL database. Example: `jdbc:postgresql://localhost:5432/constructiondb`
    -   `SPRING_DATASOURCE_USERNAME`: The username for your database.
    -   `SPRING_DATASOURCE_PASSWORD`: The password for your database.

### Running the Application
1.  **Build the Project**:
    -   Open a terminal in the project's root directory and run the following command to build the project using the Maven Wrapper:
    ```bash
    ./mvnw clean install
    ```

2.  **Run the Application**:
    -   Once the build is complete, you can run the application with:
    ```bash
    ./mvnw spring-boot:run
    ```
    -   The application will start on `http://localhost:8080`.
    -   On the first startup, **Flyway** will automatically run the database migrations to create the necessary tables.

### Authentication
The application is secured using JWT. To access protected endpoints, you need to authenticate and get a token.

1.  **Default Admin User**:
    -   On the first startup, a default user with the `MANAGER` role is created automatically. The credentials are:
        -   **username** (CPF): `admin`
        -   **password**: `password`

2.  **Get a JWT**:
    -   Send a `POST` request to the `/api/v1/authenticate` endpoint with the admin credentials in the request body.
    -   **Example using `curl`**:
    ```bash
    curl -X POST http://localhost:8080/api/v1/authenticate \
    -H "Content-Type: application/json" \
    -d '{"username": "admin", "password": "password"}'
    ```
    -   The response will be a JSON object containing the JWT:
    ```json
    {
        "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6..."
    }
    ```

3.  **Use the JWT**:
    -   To access protected endpoints, include the JWT in the `Authorization` header of your requests, prefixed with `Bearer `.
    -   **Example using `curl` to get all employees**:
    ```bash
    curl -X GET http://localhost:8080/api/v1/employees \
    -H "Authorization: Bearer <your_jwt_here>"
    ```

---

## 🧩 Agentes

### 1. API
**Responsabilidade:** Expor endpoints REST  
**Componentes:**  
- `controller/`: Recebe requisições HTTP  
- `dto/request`: Estruturas de entrada  
- `dto/response`: Estruturas de saída  

**Fluxo:** API → Facade → Service  

### 2. Service
**Responsabilidade:** Contém lógica de negócio  
**Funções:**  
- Orquestrar chamadas de `repository` e `facade`  
- Implementar regras de negócio  
- Executar etapas de sagas (ações locais e compensações)  

**Pacotes:**  
- `sagas/`: Serviços de orquestração de fluxos distribuídos  
  - `OwnerTransferSaga` → troca de sócio principal  
  - `MaterialRequestSaga` → fluxo de solicitação/aprovação de materiais  

### 3. Repository
**Responsabilidade:** Persistência de dados via Hibernate/JPA  
**Funções:**  
- Gerenciar entidades (`model/`)  
- Expor consultas e salvar registros  

### 4. Model
**Responsabilidade:** Entidades de domínio  
**Exemplos:**  
- `Employee` → tipo de contrato (CLT, PJ, Diária)  
- `ConstructionSite` → obra  
- `MaterialRequest` → pedido de material  
- `FinanceTransaction` → movimentação financeira  
- `Partner` → sócio (com CPF vinculado)  

### 5. Exception
**Responsabilidade:** Tratamento de erros  
**Componentes:**  
- `handler/`: traduz exceções em respostas HTTP  
- Exceções específicas (`EmployeeNotFoundException`, `MaterialApprovalException`)  

### 6. Filter
**Responsabilidade:** Interceptação de requisições  
**Exemplos:**  
- Autenticação JWT com roles (Worker, Manager, Partner, Owner)  
- Logging e auditoria de operações críticas  

### 7. Facade
**Responsabilidade:** Camada de orquestração entre API e Service  
**Funções:**  
- Reduzir acoplamento  
- Coordenar chamadas de múltiplos serviços  

### 8. Configuration
**Responsabilidade:** Configurações da aplicação  
**Exemplos:**  
- Beans Spring  
- Datasource (PostgreSQL)  
- Flyway migrations  
- Configurações de segurança (JWT, CORS)  

---

## 🔑 Roles & Permissions
- **Worker:** acesso às obras e relatórios  
- **Manager:** gerencia obras, aprova materiais  
- **Partner:** sócio, aprova solicitações, visão parcial do financeiro  
- **Owner:** dono principal, controle total (inclui financeiro)  

---

## 🔄 Fluxos Saga (Exemplos)

### 1. MaterialRequestSaga
1. API recebe pedido de material (`POST /materials/request`)  
2. Facade chama `MaterialService`  
3. `MaterialService` cria a solicitação e publica evento `MaterialRequested`  
4. `ManagerService` aprova ou rejeita  
5. `FinanceService` reserva verba  
6. `Partner/OwnerService` aprova gasto final  
7. Se algum passo falhar → compensações (ex.: liberar verba, cancelar pedido)  

### 2. OwnerTransferSaga
1. Sócio solicita transferência de propriedade (novo CPF)  
2. `UserService` valida o novo CPF  
3. `FinanceService` atualiza responsabilidade financeira  
4. `CompanyService` altera registro societário  
5. Em caso de falha → rollback (restaura CPF antigo, desfaz mudanças)  

---

## 📡 Eventos
- `MaterialRequested`  
- `MaterialApproved`  
- `MaterialRejected`  
- `FinanceReserved`  
- `FinanceReleased`  
- `OwnershipTransferred`  
- `OwnershipTransferFailed`  

---

## 🗄 Banco de Dados
- **PostgreSQL**  
- **Convenções:**  
  - Tabelas → `snake_case` (`construction_site`, `employee_contract`)  
  - Colunas → `snake_case`  
  - Enums → strings (`'CLT'`, `'PJ'`, `'DIARIA'`)  
- **Migrations:** Flyway  

---

## 🧑‍💻 Stack Tecnológico
- Java 17  
- Spring Boot 3  
- Spring Security (JWT)  
- Hibernate / JPA  
- PostgreSQL  
- Flyway  
- Docker
