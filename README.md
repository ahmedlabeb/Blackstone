# Blackstone
# Account Service

## Overview

The **Account Service** is a Spring Boot 3.4 microservice that owns all bank‑account data. It creates, updates, and soft‑closes accounts while enforcing the domain rules below.  The service also stores a **replicated projection** of minimal customer data (`CustomerInfo`) received asynchronously from the Customer Service via RabbitMQ events.

See the **Account‑Service docs** ✨ ➜ [README](services/account-service/README.md)

## Overview

The **Customer Service** is a Spring Boot 3.2 microservice responsible for managing core customer data (creation, update, soft‑deletion, lookup).  It owns the **source‑of‑truth** for customer attributes while keeping only the *minimal* replicated view of related account information.

See the **Customer‑Service docs** ✨ ➜ [README](services/customer-service/README.md)

