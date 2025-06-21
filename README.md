# PoC: FastPass System

## 1. Introduction

This document outlines a Proof of Concept (PoC) for a modernized FastPass system, built on a resilient and scalable **Microservice Architecture**. This PoC demonstrates how complex business logic can be broken down into smaller, manageable services that communicate efficiently and reliably.

The primary goal is to showcase a robust foundation using modern architectural patterns like **Domain-Driven Design (DDD)** and efficient communication protocols like **gRPC**. This approach ensures that as the system grows in complexity and user load, it remains maintainable, scalable, and easy to evolve.

## 2. Architectural Concepts

We have chosen two key architectural patterns for this PoC to address the complexities of a large-scale enterprise system.

### 2.1. Microservice Architecture

Instead of a single, monolithic application, we have decomposed the system into independent services, each with a specific business responsibility.

* **`ticket-management-service`**: The source of truth for all FastPass tickets. It handles their creation, persistence, and status updates.

* **`attraction-redemption-service`**: Responsible for the business process of a guest redeeming their ticket at a ride entrance.

**Benefits of this approach:**

* **Independent Deployment**: Each service can be updated and deployed without affecting others, leading to faster release cycles.

* **Scalability**: We can scale individual services based on demand. For example, if ticket redemption is high, we can scale up only the `attraction-redemption-service`.

* **Technology Flexibility**: Each service can be built with the technology best suited for its task.

* **Fault Isolation**: An issue in one service is less likely to bring down the entire system.

### 2.2. Domain-Driven Design (DDD) & Layered Architecture

To manage the complexity within each microservice, we have applied the principles of DDD. This places the focus on the core business logic—the "domain". Each service is structured into distinct layers:

1. **Domain Layer**: This is the heart of the service. It contains the business models (e.g., `FastPassTicket`), rules, and logic, completely isolated from any technical concerns.

2. **Application Layer**: This layer orchestrates the domain layer. It defines the application's use cases but contains no business logic itself. For example, it coordinates fetching a ticket from the repository and then calling a method on it.

3. **Infrastructure Layer**: This layer handles all external communication and technical details, such as REST controllers, gRPC clients/servers, and database interactions.

This separation ensures our core business logic is pure, easy to test, and independent of changing technologies like databases or web frameworks.

## 3. System Overview & Data Flow

The flow for redeeming a ticket is as follows:

1. A user issues a ticket via a REST call to the **`ticket-management-service`**.

2. When the user arrives at the ride, the turnstile (simulated by a REST call) contacts the **`attraction-redemption-service`**.

3. The **`attraction-redemption-service`** acts as a gRPC client. It makes a synchronous call to the **`ticket-management-service`** to get ticket details and validate them (e.g., is it for the right ride? Is it within the time window?).

4. If the ticket is valid, the **`attraction-redemption-service`** sends a second gRPC message to the **`ticket-management-service`** to notify it that the ticket has been redeemed.

5. The **`ticket-management-service`** updates the ticket's status in its PostgreSQL database to `REDEEMED`.

## 4. How to Run the Proof of Concept

### Prerequisites

* Java 21

* Gradle 8.x

* A running PostgreSQL instance

### Step 1: Set Up the PostgreSQL Database

This PoC requires a PostgreSQL database for the `ticket-management-service`.

### Step 2: Run the Services

Open two separate terminals from the root of the `fastpass-disney-demo` project.

**Terminal 1: Start the `ticket-management-service`**
This service runs the gRPC server on port `9090` and the REST server on `8080`.

```bash
./gradlew :ticket-management-service:bootRun
```

**Terminal 2: Start the `attraction-redemption-service`**
This service runs its REST server on port `8081`.

```bash
./gradlew :attraction-redemption-service:bootRun
```

## 5. Testing the System with Postman

A Postman collection is included to demonstrate the end-to-end flow.

### Run the Requests

Execute the requests in the collection in order. A test script in the "Issue Ticket" request will automatically set the `ticketId` and `attractionId` for the subsequent requests.

1. **Issue Ticket**: Creates a new FastPass ticket.

2. **Get Ticket Details**: Fetches the details of the newly created ticket.

3. **Redeem Ticket**: Simulates redeeming the ticket via the `attraction-redemption-service`, triggering the gRPC communication flow.

## 6. Next Steps & Production Considerations

This PoC demonstrates a robust architecture using **synchronous gRPC calls** for inter-service communication. This pattern is excellent for request-response flows where an immediate answer is required, such as validating a ticket.

However, for a real-world, large-scale system, we should consider evolving parts of this communication to be **asynchronous** and **event-driven**.

### From gRPC to Kafka: Enhancing Resilience

The call from the `attraction-redemption-service` to notify the `ticket-management-service` of a redemption is a perfect candidate for an asynchronous pattern using a message broker like **Apache Kafka**.

**Proposed Event-Driven Flow:**

1. The `attraction-redemption-service` still makes a synchronous gRPC call to validate the ticket.

2. Once validated, instead of making a second gRPC call to notify of redemption, it publishes a `TicketRedeemed` event to a Kafka topic.

3. The `ticket-management-service` (and any other interested service, like analytics) would subscribe to this topic. When it receives the event, it updates its database.

**Benefits of an Event-Driven Approach:**

* **Decoupling**: The redemption service no longer needs to know if the ticket service is available to complete its primary task. It just fires an event and moves on.

* **Resilience**: If the `ticket-management-service` is temporarily down, events will queue up in Kafka. Once the service is back online, it can process the backlog of redemptions without any data loss. This greatly improves the overall system's uptime and reliability.

* **Scalability & Extensibility**: New services can easily be added to listen for `TicketRedeemed` events without modifying the existing services.
