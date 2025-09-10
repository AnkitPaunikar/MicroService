Quarkus Microservices Project
A Quarkus-based microservices architecture demonstrating two RESTful services — rest-number and rest-book — containerized and deployed using Docker Compose.

Summary
This repository showcases a microservices architecture built with Quarkus, designed to illustrate independent services working collaboratively.

rest-number: Generates ISBN-13 and ISBN-10 numbers.

rest-book: Creates and persists book records. It communicates with rest-number for ISBN generation and includes fault tolerance with fallback to ensure resilience when services are unavailable.

This setup demonstrates key microservices principles: modularity, fault tolerance, containerization, and orchestration.

Table of Contents
Getting Started

Project Structure

Quarkus vs Spring Boot

Microservices Overview

Architecture Diagram

Fault Tolerance and Resilience

Building Docker Images

Running Containers

Docker Compose Setup

cURL Usage Examples

Contributing

License

Getting Started
Prerequisites
Ensure the following tools are installed on your system:

Java 17+

Maven 3.6+

Docker & Docker Compose

Service Ports & Configuration
Each service defines its own HTTP port in application.properties:

rest-number → quarkus.http.port=8701

rest-book → quarkus.http.port=8702

Project Structure
text
quarkus-microservices/
│
├── rest-number/               # Microservice for ISBN generation
│   ├── src/                   
│   └── pom.xml
│
├── rest-book/                 # Microservice for book creation & persistence
│   ├── src/                   
│   └── pom.xml
│
├── docker-compose.yml         # Multi-container setup for orchestration
└── README.md                  # Project documentation
Quarkus vs Spring Boot
Feature	Quarkus	Spring Boot
Startup Time	Very fast (milliseconds)	Slower (seconds)
Memory Footprint	Extremely lightweight	Higher memory usage
Native Execution	GraalVM native image support	Limited native support
Developer Experience	Hot reload, fast dev cycles	Mature ecosystem, rich tooling
Target Use Case	Cloud-native, serverless	Enterprise monolith/microservices
Quarkus is chosen here for its fast startup and low resource usage, ideal for containerized cloud environments.

Microservices Overview
rest-number:

Stateless service exposing REST APIs to generate ISBN-10 and ISBN-13 numbers.

rest-book:

Manages book records and persists data.

Consumes rest-number to retrieve ISBNs dynamically.

Integrates fault tolerance mechanisms to handle dependent service failures seamlessly.

Architecture Diagram
text
flowchart TD
    A[Client] -->|REST API call| B[rest-book]
    B -->|Request ISBN generation| C[rest-number]
    B -->|Persist Book Data| D[Database]
    C -->|Return ISBN| B
This diagram shows how the rest-book service is the primary client-facing endpoint, relying on rest-number for ISBN generation and persisting data in its database.

Fault Tolerance and Resilience
The rest-book service implements resilience patterns powered by Quarkus’ fault tolerance annotations to ensure high availability and graceful degradation:

@Retry
Automatically retries failed calls to the rest-number service a configurable number of times before declaring failure.

@CircuitBreaker
Prevents retrying calls once a threshold of failures is reached, allowing the system to recover gracefully and avoid cascading failures.

@Fallback
Provides an alternative method when the dependent service is unavailable or retries are exhausted. For example, returning a default ISBN or a placeholder value to keep the user experience intact.

Example code snippet from rest-book:

java
@Retry(maxRetries = 3, delay = 1000)
@CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.75, delay = 2000)
@Fallback(fallbackMethod = "isbnFallback")
public String getIsbn() {
    return restNumberService.generateIsbn();
}

public String isbnFallback() {
    // Fallback logic when rest-number is unreachable
    return "000-0000000000";
}
These resilience patterns showcase a production-ready microservices approach, critical for real-world distributed systems.

Building Docker Images
Package and containerize each service with Maven and Quarkus Docker support:

bash
cd rest-number
./mvnw clean package -Dquarkus.container-image.build=true

cd ../rest-book
./mvnw clean package -Dquarkus.container-image.build=true
Running Containers
Run the services manually via Docker (if not using compose):

bash
docker run -i --rm -p 8701:8701 rest-number
docker run -i --rm -p 8702:8702 rest-book
Docker Compose Setup
Use Docker Compose for easy orchestration and network configuration between services:

bash
docker-compose up --build
cURL Usage Examples
Generate ISBNs (rest-number)
bash
curl http://localhost:8701/api/numbers/isbn13
curl http://localhost:8701/api/numbers/isbn10
Create a new book (rest-book)
bash
curl -X POST http://localhost:8702/api/books \
     -H "Content-Type: application/json" \
     -d '{"title": "Quarkus in Action", "author": "John Doe"}'
Retrieve all books
bash
curl http://localhost:8702/api/books
Contributing
Contributions are welcome! To contribute:

Fork this repository.

Create a feature branch (git checkout -b feature/my-enhancement).

Commit your changes and push the branch.

Open a pull request for review.

License
This project is licensed under the MIT License.

