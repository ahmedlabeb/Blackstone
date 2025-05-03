# Account Service

## Overview

The **Account Service** is a Spring Boot 3.4 microservice that owns all bank‑account data. It creates, updates, and soft‑closes accounts while enforcing the domain rules below.  The service also stores a **replicated projection** of minimal customer data (`CustomerInfo`) received asynchronously from the Customer Service via RabbitMQ events.

---

## Key Design Decisions

| Area                         | Choice                                      | Rationale                                                                            |
| ---------------------------- | ------------------------------------------- | ------------------------------------------------------------------------------------ |
| **Event‑Driven Replication** | RabbitMQ direct‑exchange events             | Services remain autonomous; only the minimal `CustomerInfo` projection is exchanged. |
| **Messaging**                | RabbitMQ direct exchange (`account.events`) | Simple local setup, routing‑key fan‑out, durable stores.                             |
| **Persistence**              | MySQL 8 + Spring Data JPA                   | ACID + native SQL when needed.                                                       |
| **Validation**               | DTO annotations + service rules             | Enforces 10‑digit accountId, balance, salary rules, max 10 accounts.                 |
| **Mapping**                  | MapStruct 1.6.3                             | Compile‑time, zero‑reflection mapping.                                               |
| **API contract**             | SpringDoc OpenAPI UI (`/swagger-ui.html`)   | Live, self‑updating documentation.                                                   |
| **Testing**                  | JUnit 5, Mockito, AssertJ, Jacoco           | ≥ 90 % line coverage enforced in CI.                                                 |

---

## Maven Coordinates

```xml
<groupId>com.blackstone</groupId>
<artifactId>account-service</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

See full [`pom.xml`](./pom.xml) for dependencies and Jacoco configuration.

---

## Core Domain

### `Account` table

```
id | account_id* | customer_id (FK) | type | status | balance | created_at | updated_at
```

*`account_id` = 10 digits prefixed by the customer’s 7‑digit identifier.*

### `CustomerInfo` (replicated view)

Stores `customerId` & `type` only—populated exclusively by **customer events**.

---

## Event‑Driven Replication 📡

```text
┌──────────────────┐            ┌──────────────────┐
│  AccountService  │            │ CustomerService  │
└─────────▲────────┘            └─────────▲────────┘
          │  ACCOUNT_* events              │
          │ (created/updated/deleted)      │
CUSTOMER_* events                          │   minimal projection
(created/updated/deleted)                  ▼   (CustomerInfo)
                       RabbitMQ Direct Exchange

Bindings are declared at startup inside `RabbitMQConfig.bindQueueAndExchange()`; routing keys are configured via `RabbitProperties`.

```

Bindings are declared at startup inside `RabbitMQConfig.bindQueueAndExchange()`; routing keys are configured via `RabbitProperties`.
CustomerService ──► customer.events (created/updated/deleted) ──► AccountService
AccountService  ──► account.events  (created/updated/deleted) ──► CustomerService

## REST Endpoints

| Method   | Path                  | Description                       |
| -------- | --------------------- | --------------------------------- |
| `POST`   | `/api/accounts`      | Create account (validates all domain rules)                   |
| `GET`    | `/api/accounts/{accountId}` | Fetch single account                |
| `PUT`    | `/api/accounts/{accountId}` | Update mutable fields (accountId immutable)   |
| `DELETE` | `/api/accounts/{accountId}` | Soft‑close account (balance must be zero) |
| `GET`    | `/api/accounts`      |List active accounts             |
> Swagger UI available at `/swagger-ui.html`.
available at `/swagger-ui.html`.
: `http://localhost:8282/swagger-ui.html`

---
## Build & Run Locally
```bash
# compile + unit tests + coverage
mvn clean verify

# run (requires MySQL + RabbitMQ running with creds in application.yml)
java -jar target/account-service-0.0.1-SNAPSHOT.jar
````
## 🚢 Running the services with Docker Compose
 
### Prerequisites
| Tool | Version (minimum) | Notes |
|------|-------------------|-------|
| Docker Engine / Desktop | 24.x | Make sure the **“File sharing”** list in Docker Desktop → *Settings → Resources* contains the path to this repository so the SQL files can be mounted into MySQL. |
| Docker Compose | v2 (bundled with Docker Desktop) | We use the *Compose V2* CLI (`docker compose …`). |

---

### 1 — Quick start 🟢

```bash
# from the repository root
docker compose up -d

*Environment overrides*: `SPRING_DATASOURCE_URL`, `RABBIT_HOST`, `RABBIT_USERNAME`, `RABBIT_PASSWORD`.

---

## Tests & Coverage

```bash
mvn test
open target/site/jacoco/index.html  # view HTML coverage report
```

Unit tests cover:

* REST controllers (`@WebMvcTest` + MockMvc)
* Service layer business logic & validations
* All customer‑event processors (create/update/delete)

---

## Future Enhancements

* Dead‑letter queues & exponential back‑off retries
* Transactional outbox or Debezium for guaranteed event delivery
* Observability: Prometheus metrics, Grafana dashboards
* Kubernetes Helm charts and GitOps deployment
