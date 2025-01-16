Teller Application

Overview

The Teller Application is a banking solution for managing customers, accounts, and transactions with a maker-checker-authorizer workflow. It uses microservices architecture and integrates Keycloak for Identity and Access Management (IAM).

Features

Personas: Maker (create/initiate), Checker (review/approve), Authorizer (approve > $1000).

Functionality: Customer/account management, deposits, withdrawals, dashboards, and search.

APIs: Retrieve balances and transaction details by customer ID or account number.

Batch Jobs: Interest calculation and e-statement generation.

XML: File loading and generation for transactions.

Technical Details

Microservices: Customer, Account, Transaction, API Gateway (Spring Boot).

IAM: Keycloak with Maker, Checker, Authorizer roles.

Database: MySQL for persistence.

Running the Application

Clone the repo.

Set up Keycloak with roles.

Start services:

./mvnw clean install
./mvnw spring-boot:run

Access via API Gateway.

*Future Enhancement*

Add frontend for interaction.

Advanced reporting dashboards.

Mobile banking support.
