# AGENTS.md - Construction Management System

## Roles
- **Worker**: acesso às obras e relatórios, não mexe em financeiro
- **Manager**: gerencia obras, aprova materiais
- **Partner**: sócio, aprova solicitações, visão parcial do financeiro
- **Owner**: dono principal, controle total (inclui financeiro)

---

## Objetivo do Projeto
Construir uma **API Java com Spring Boot**, usando **Hibernate/JPA** para persistência, seguindo arquitetura modular:

- API, Service, Repository, Model, Facade, Exception, Filter, Configuration
- Implementar **Saga Pattern** para fluxos críticos (materiais, transferência de sócios)
- Suporte a múltiplos papéis e permissões (roles)

---

## Estrutura de Pacotes e Agentes

### 1. API
**Responsabilidade:** Expor endpoints REST  
**Pacotes:**  
- `controller/`: Recebe requisições HTTP  
- `dto/request/`: Estruturas de entrada  
- `dto/response/`: Estruturas de saída  

**Fluxo:** API → Facade → Service  

### 2. Service
**Responsabilidade:** Lógica de negócio central  
**Pacotes:**  
- `impl/`: Implementações dos serviços  
- `sagas/`: Orquestração de fluxos distribuídos  

**Exemplos de Sagas:**  
- `OwnerTransferSaga`: troca de sócio principal  
- `MaterialRequestSaga`: fluxo de solicitação/aprovação de materiais  

**Funções:**  
- Executar etapas das sagas (ações locais e compensações)  
- Orquestrar chamadas de repository e facade  
- Implementar regras de negócio  

### 3. Repository
**Responsabilidade:** Persistência de dados via Hibernate/JPA  
**Funções:**  
- Gerenciar entidades (`model/`)  
- Consultas e salvamentos  
- Suporte a transações  

### 4. Model
**Responsabilidade:** Representar entidades de domínio  
**Exemplos:**  
- `Employee`: tipo de contrato (CLT, PJ, Diária)  
- `ConstructionSite`: obra  
- `MaterialRequest`: pedido de material  
- `FinanceTransaction`: movimentação financeira  
- `Partner`: sócio (CPF vinculado)  

### 5. Exception
**Responsabilidade:** Tratamento de erros  
**Pacotes:**  
- `handler/`: traduz exceções para respostas HTTP  
- Exceções de negócio (`EmployeeNotFoundException`, `MaterialApprovalException`)  

### 6. Filter
**Responsabilidade:** Interceptar requisições  
**Exemplos:**  
- Autenticação JWT com roles (Worker, Manager, Partner, Owner)  
- Logging e auditoria  

### 7. Facade
**Responsabilidade:** Orquestração entre API e Service  
**Funções:**  
- Reduzir acoplamento  
- Coordenar chamadas múltiplas dentro de uma transação ou saga  

### 8. Configuration
**Responsabilidade:** Configurações da aplicação  
**Exemplos:**  
- Beans Spring  
- Datasource PostgreSQL  
- Flyway migrations  
- Segurança (JWT, CORS)  

---

## Fluxos de Saga

### MaterialRequestSaga
1. API recebe pedido de material (`POST /materials/request`)  
2. Facade chama `MaterialService`  
3. `MaterialService` cria solicitação e publica evento `MaterialRequested`  
4. `ManagerService` aprova ou rejeita  
5. `FinanceService` reserva verba  
6. `Partner/OwnerService` aprova gasto final  
7. Falha em algum passo → compensações (liberar verba, cancelar pedido)  

### OwnerTransferSaga
1. Sócio solicita transferência de propriedade (novo CPF)  
2. `UserService` valida CPF  
3. `FinanceService` atualiza responsabilidade financeira  
4. `CompanyService` altera registro societário  
5. Falha → rollback (restaura CPF antigo, desfaz mudanças)  

---

## Eventos
- `MaterialRequested`  
- `MaterialApproved`  
- `MaterialRejected`  
- `FinanceReserved`  
- `FinanceReleased`  
- `OwnershipTransferred`  
- `OwnershipTransferFailed`  

---

## Banco de Dados
- PostgreSQL  
- Convenções: snake_case em tabelas e colunas  
- Enums como strings (`'CLT'`, `'PJ'`, `'DIARIA'`)  
- Migrations via Flyway  

---

## Stack Tecnológico
- Java 17  
- Spring Boot 3  
- Spring Security (JWT)  
- Hibernate / JPA  
- PostgreSQL  
- Flyway  
- Docker
