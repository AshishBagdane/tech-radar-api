# Tech Radar - REST APIs (Stage 1)

A modern, scalable REST API service for managing technology radar data, built
with Spring Boot. This service provides comprehensive APIs for managing and
querying technology entries, quadrants, rings, and analytics data.

## Project Overview

The Tech Radar API service enables organizations to:

- Track and manage technology adoption across different categories
- Query and analyze technology distribution across quadrants and rings
- Monitor changes and updates to technology entries
- Support batch operations for efficient data management

## Technology Stack

- Java 21
- Spring Boot 3.4.1
- Spring Data JPA
- PostgreSQL (for data persistence)
- Lombok (for reducing boilerplate)
- Prometheus (for metrics)

## Key Features

### Core Functionality

- CRUD operations for technology entries
- Flexible querying with filtering and pagination
- Quadrant and ring-based analytics
- Change tracking and audit logging
- Batch operations support

### API Endpoints

#### Technology Management

- `GET /api/v1/technologies` - List technologies with filtering options
- `GET /api/v1/technologies/{id}` - Get specific technology details
- `POST /api/v1/technologies` - Add new technology
- `PUT /api/v1/technologies/{id}` - Update existing technology
- `DELETE /api/v1/technologies/{id}` - Remove technology

#### Quadrant Operations

- `GET /api/v1/quadrants` - List all quadrants with their technologies
- `GET /api/v1/quadrants/{quadrantType}/technologies` - Get technologies for
  specific quadrant

#### Ring Operations

- `GET /api/v1/rings` - List all rings with their technologies
- `GET /api/v1/rings/{ringType}/technologies` - Get technologies for specific
  ring

#### Analytics & Metrics

- `GET /api/v1/metrics/quadrant` - Get technology distribution by quadrant
- `GET /api/v1/metrics/ring` - Get technology distribution by ring
- `GET /api/v1/metrics/changes` - Get recent changes/updates

#### Batch Operations

- `POST /api/v1/technologies/batch` - Add multiple technologies
- `PUT /api/v1/technologies/batch` - Update multiple technologies

## Data Model

### Key Entities

#### Technology

- UUID id
- String name
- String description
- Quadrant quadrant (enum)
- Ring ring (enum)
- Map<String, String> metadata
- Audit fields (createdAt, updatedAt, version)

#### AuditLog

- UUID id
- String action
- String entityType
- String entityId
- String changes (JSON)
- LocalDateTime timestamp
- String performedBy
- String ipAddress

### Enums

- Quadrant: TECHNIQUES, TOOLS, PLATFORMS, LANGUAGES_AND_FRAMEWORKS
- Ring: ADOPT, TRIAL, ASSESS, HOLD
- ChangeType: ADDED, UPDATED, DELETED

## Getting Started

### Prerequisites

- JDK 21 or later
- Maven 3.8+
- PostgreSQL 14+

## Running the Tech Radar Application

This guide explains how to run the Tech Radar application using Spring Boot's
Docker Compose support.

### Prerequisites

- Java 21 or later
- Maven 3.8+
- Docker and Docker Compose
- Git

## Setup and Running

### 1. Clone the Repository

```bash
git clone git@github.com:AshishBagdane/tech-radar-api.git
cd tech-radar-api
```

### 2. Build the Application

```bash
./mvnw clean package -DskipTests
```

### 3. Run the Application

Simply run the application using Maven or Java:

```bash
# Using Maven
./mvnw spring-boot:run

# OR using Java
java -jar target/tech-radar-[version].jar
```

Spring Boot will automatically:

- Detect the `compose.yaml` file in the project root
- Start PostgreSQL and Redis Stack containers
- Configure all necessary environment variables
- Start the application

The application should be accessible at:

- Application: http://localhost:8080
- Redis Stack Manager: http://localhost:8001

### 4. Verify Services

Check if all services are running:

```bash
# Check running containers
docker ps

# Check application health
curl http://localhost:8080/actuator/health
```

### 5. Stopping the Application

When you stop the Spring Boot application (using Ctrl+C or any other method), it
will automatically:

- Stop the containers
- Preserve the data volumes
- Clean up the resources

## Configuration

### Default Properties

```properties
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=tech-radar
DB_USERNAME=tech
DB_PASSWORD=radar
# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
```

### Data Persistence

The configuration uses named volumes for data persistence:

- `postgres_data`: PostgreSQL data
- `redis_data`: Redis data

Data will persist between restarts unless you explicitly remove the volumes.

## Troubleshooting

### 1. Port Conflicts

If you see port conflict errors:

```bash
# Check what's using PostgreSQL port
lsof -i :5432

# Check what's using Redis ports
lsof -i :6379
lsof -i :8001
```

### 2. Container Issues

```bash
# View container logs
docker logs tech-radar-postgres-1
docker logs redis-stack

# Check container status
docker ps -a
```

### 3. Clean Start

If you need to start fresh:

```bash
# Stop the application
# Then remove containers and volumes
docker compose down -v

# Start the application again
./mvnw spring-boot:run
```

### 4. Common Issues

1. Database Connection:

- Verify PostgreSQL container is running
- Check logs for connection errors
- Ensure correct credentials in application.properties

2. Redis Connection:

- Verify Redis Stack container is running
- Check Redis Stack Manager at http://localhost:8001
- Review Redis connection logs

3. Volume Permissions:

- If you see permission errors, you might need to clean up volumes:

   ```bash
   docker compose down -v
   docker volume prune
   ```

## Security Best Practices

While security is a secondary concern, the following best practices are
implemented:

- SSL/TLS encryption for all communications
- Input validation and sanitization
- Exception handling to prevent information leakage
- Basic authentication for API access
- CORS configuration for frontend integration
- Audit logging for all changes

## Observability

The service includes basic observability features:

- Logging with correlation IDs
- Prometheus metrics integration
- Audit trail for all data changes
- Error tracking with stack traces (in development only)

## Development Guidelines

1. Follow SOLID principles and clean architecture practices
2. Use proper exception handling with custom exceptions
3. Implement comprehensive input validation
4. Write unit tests for all business logic
5. Document all public APIs using OpenAPI/Swagger
6. Follow consistent code formatting and naming conventions
7. Use meaningful commit messages

## Next Steps

The following features are planned for future iterations:

- Advanced search capabilities
- Caching implementation
- Rate limiting
- API versioning strategy
- Integration test suite
- Documentation enhancement

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process
for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE.md file for
details
