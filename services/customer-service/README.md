# Customer Service

## Overview

The **Customer Service** is a Spring Boot 3.2 microservice responsible for managing core customer data (creation, update, soft‑deletion, lookup).  It owns the **source‑of‑truth** for customer attributes while keeping only the *minimal* replicated view of related account information.

---

## Key Design Decisions

| Area                           | Choice                                                 | Rationale                                                                                                                                                      |
| ------------------------------ | ------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Inter‑service data sharing** | **Replication via events** (RabbitMQ direct exchange)  | Each service remains autonomously deployable; only a minimal projection (`AccountInfo` → `customer-service`, `CustomerInfo` → `account-service`) is exchanged. |
| **Event transport**            | RabbitMQ (`spring‑boot‑starter‑amqp`)                  | Low‑latency, durable, and easy local setup; supports fan‑out for additional consumers.                                                                         |
| **Schema evolution**           | Versioned JSON events                                  | Loose coupling—services deserialize only required fields.                                                                                                      |
| **Persistence**                | MySQL 8 + Spring Data JPA                              | Relational consistency with minimal boilerplate.                                                                                                               |
| **Mapping**                    | MapStruct 1.6.3                                        | Compile‑time mapper with near‑zero runtime overhead.                                                                                                           |
| **API contract**               | SpringDoc OpenAPI 3 (`v3/api-docs`, `swagger-ui.html`) | Self‑documenting REST endpoints.                                                                                                                               |
| **Resilience**                 | Feign client with fallback (for balance check)         | Customer deletion verifies account balances synchronously.                                                                                                     |
| **Testing**                    | JUnit 5 + Mockito + Jacoco (≥ 70 % line)               | CI‑friendly coverage reports.                                                                                                                                  |

---

## Maven Coordinates

```xml
<groupId>com.blackstone</groupId>
<artifactId>customer-service</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

Full `pom.xml` is included in the repo (see `/pom.xml`).  Key plugins:

* **Jacoco** – automatic unit‑test coverage report.
* **maven‑compiler‑plugin** – Java 17 source/target, annotation‑processor path for MapStruct/Lombok.

---

## Core Domain

### `Customer` ➜ RDBMS table `customer`

```text
id (PK) | customer_id* | name | legal_id | type | address | created_at | updated_at | is_deleted
```

*7‑digit business identifier enforced by DTO validation.*

### `AccountInfo` (replicated projection)

Stores only **accountId** and **type** for each account belonging to the customer.  Populated exclusively by **RabbitMQ** events from Account Service.

---

## Event‑Driven Replication 📡

```
           ┌──────────────────┐            ┌──────────────────┐
           │  AccountService │            │ CustomerService  │
           └─────────▲────────┘            └─────────▲────────┘
                     │   ACCOUNT_* events             │
                     │  (created/updated/deleted)     │
   CUSTOMER_* events │                                │ minimal projection
 (created/updated/   │                                ▼  (AccountInfo)
       deleted)      │                        RabbitMQ direct exchange
```

* **Exchange**: `customer.events`
* **Routing Keys**: `customer.created`, `customer.updated`, `customer.deleted`
* **Queue (consumer)**: `account.customer.queue`
* Mirror setup in Account Service for account → customer events.

---

## REST Endpoints

| Method   | Path                  | Description                       |
| -------- | --------------------- | --------------------------------- |
| `POST`   | `/api/customers`      | Create customer                   |
| `GET`    | `/api/customers/{id}` | Fetch by customerId               |
| `PUT`    | `/api/customers/{id}` | Update non‑ID fields              |
| `DELETE` | `/api/customers/{id}` | Soft‑delete (after balance check) |
| `GET`    | `/api/customers`      | List active customers             |

> See Swagger UI at `/swagger-ui.html` for full request/response examples.
: `http://localhost:8282/swagger-ui.html`

---

## Build & Run Locally

```bash
mvn clean install
# requires local MySQL + RabbitMQ (default creds)
java -jar target/customer-service-0.0.1-SNAPSHOT.jar
```

*Environment variables* (override `application.yml`): `SPRING_DATASOURCE_URL`, `RABBIT_HOST`, `RABBIT_USERNAME`, `RABBIT_PASSWORD`.

---

## Tests & Coverage

```bash
mvn test
open target/site/jacoco/index.html   # view HTML report
```

Unit tests cover controllers, services, event processors, and DTO validation (≥ 90 % lines).

---

## Future Enhancements

* Dead‑letter queues for poison messages
* Outbox pattern (transactional publish) via Debezium
* Kubernetes Helm charts & observability (Prometheus/Grafana)
