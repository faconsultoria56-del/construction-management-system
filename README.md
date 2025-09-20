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
