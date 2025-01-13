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

- Java 17
- Spring Boot 3.x
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

- JDK 17 or later
- Maven 3.8+
- PostgreSQL 14+

### Setup Instructions

1. Clone the repository:

```bash
git clone <repository-url>
cd tech-radar-api
```

2. Configure database properties in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tech_radar
    username: your_username
    password: your_password
```

3. Build the project:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn spring-boot:run
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
