# Calculator Project with Kafka

This project consists of two microservices that communicate via Kafka:
- REST Module: Receives HTTP requests and sends calculation requests to Kafka
- Calculator Module: Processes calculations and sends results back via Kafka

## Prerequisites

- Java 17
- Maven
- Docker and Docker Compose

## Project Structure

```
project/
├── calculator-module/
├── rest-module/
└── docker-compose.yml
```

## Building the Project

1. Build the REST module:
```bash
cd rest-module
mvn clean package -DskipTests
```

2. Build the Calculator module:
```bash
cd calculator-module
mvn clean package -DskipTests
```

## Running with Docker

Start all services using Docker Compose:
```bash
docker compose -f docker-compose.yml up -d
```

To check the logs:
```bash
docker compose -f docker-compose.yml logs -f
```

## Testing the Application

You can test the calculator using cURL or Postman:

### Sum Operation
```bash
curl "http://localhost:8081/sum?a=10&b=20"
```

### Subtract Operation
```bash
curl "http://localhost:8081/subtract?a=20&b=10"
```

### Multiply Operation
```bash
curl "http://localhost:8081/multiply?a=10&b=20"
```

### Divide Operation
```bash
curl "http://localhost:8081/divide?a=20&b=5"
```

## API Endpoints

All endpoints accept two parameters: `a` and `b`

- `GET /sum` - Adds two numbers
- `GET /subtract` - Subtracts b from a
- `GET /multiply` - Multiplies two numbers
- `GET /divide` - Divides a by b

Response format:
```json
{
    "result": 30
}
```

## Stopping the Application

To stop all services:
```bash
docker compose -f docker-compose.yml down
```
