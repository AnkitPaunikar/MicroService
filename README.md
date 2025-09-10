```markdown
# MicroService

A **Quarkus-based** microservices project demonstrating two RESTful services **rest-number** and **rest-book** deployed via Docker Compose.  

---

## Summary

This repository contains two independent Quarkus microservices:

- **rest-number**: Generates ISBN-13 and ISBN-10 numbers.  
- **rest-book**: Creates and persists book records, leveraging the **rest-number** service for ISBN generation, with fault tolerance and fallback support.

---

## Table of Contents

- [Getting Started](#getting-started)  
- [Project Structure](#project-structure)  
- [Quarkus vs Spring Boot](#quarkus-vs-spring-boot)  
- [Microservices Overview](#microservices-overview)  
- [Architecture Diagram](#architecture-diagram)  
- [Building Docker Images](#building-docker-images)  
- [Running Containers](#running-containers)  
- [Docker Compose](#docker-compose)  
- [cURL Examples](#curl-examples)  
- [Contributing](#contributing)  
- [License](#license)  

---

## Getting Started

### Prerequisites

- Java 17+  
- Maven 3.6+  
- Docker & Docker Compose  

### Ports & Configuration

- **rest-number**: `application.properties` → `quarkus.http.port=8701`  
- **rest-book**: `application.properties` → `quarkus.http.port=8702`  

---

## Project Structure

```
MicroService/
├── rest-number/
│   ├── src/main/java/org/ankit/
│   │   ├── NumberResource.java
│   │   ├── IsbnNumbers.java
│   │   └── NumbersMicroservice.java
│   ├── pom.xml
│   └── README.md
├── rest-book/
│   ├── src/main/java/org/ankit/
│   │   ├── BookResource.java
│   │   ├── Book.java
│   │   ├── NumberProxy.java
│   │   └── IsbnThirteen.java
│   ├── pom.xml
│   └── README.md
└── store-docker-compose.yml
```

---

## Quarkus vs Spring Boot

| Feature              | Quarkus                                 | Spring Boot                       |
|----------------------|-----------------------------------------|-----------------------------------|
| Startup Time         | < 0.1s native                           | ~2s–5s                            |
| Memory Footprint     | Low (<50 MB native)                     | Higher (hundreds of MB)           |
| Native Executable    | First-class via GraalVM                 | Community extensions              |
| Dev Mode             | Live reload + Dev UI                    | DevTools auto-restart             |
| MicroProfile         | Built-in                                | Requires dependencies             |
| Ecosystem            | Growing                                 | Mature, extensive                 |

---

## Microservices Overview

- **rest-number**:  
  - Exposes `GET /api/numbers`  
  - Returns random ISBN-13, ISBN-10, timestamp  

- **rest-book**:  
  - Exposes `POST /api/book`  
  - Consumes form data, calls **rest-number** via `@RestClient`  
  - Retries (3×) and falls back to disk persistence  

---

## Building Docker Images

From the project root, run:

```
# Build rest-number image
cd rest-number
./mvnw package -Dquarkus.container-image.build=true \
  -Dquarkus.container-image.image=ankit/rest-number:0.0.1

# Build rest-book image
cd ../rest-book
./mvnw package -Dquarkus.container-image.build=true \
  -Dquarkus.container-image.image=ankit/rest-book:0.0.1
```

---

## Running Containers

```
# Run rest-number
docker run -d --name rest-number \
  -p 8701:8701 ankit/rest-number:0.0.1

# Run rest-book (link to rest-number)
docker run -d --name rest-book \
  -p 8702:8702 \
  -e NUMBER_PROXY_MP_REST_URI=http://rest-number:8701 \
  --link rest-number ankit/rest-book:0.0.1
```

---

## Docker Compose

Use the provided `store-docker-compose.yml`:

```
version: "0.0.1"
services:
  rest-number:
    image: "ankit/rest-number:0.0.1"
    ports:
      - "8701:8701"

  rest-book:
    image: "ankit/rest-book:0.0.1"
    ports:
      - "8702:8702"
    environment:
      - "NUMBER_PROXY_MP_REST_URI=http://rest-number:8701"
```

Run both services:

```
docker-compose -f store-docker-compose.yml up --build -d
```

---

## cURL Examples

**Generate ISBN Numbers**  
```
curl -X GET http://localhost:8701/api/numbers
```

**Create a Book**  
```
curl -X POST http://localhost:8702/api/book \
  -d "title=MyBook" \
  -d "author=JaneDoe" \
  -d "yearOfPublication=2025" \
  -d "genre=Fiction" \
  -H "Content-Type: application/x-www-form-urlencoded"
```

---
