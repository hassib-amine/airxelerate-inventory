# Flight Inventory Management System

A RESTful API for managing flight inventory with JWT-based authentication, role-based access control, and comprehensive CRUD operations. Built with Spring Boot, this application provides a secure and scalable solution for flight data management.

## 🚀 Features

- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Admin and User roles with different permissions
- **Flight CRUD Operations**: Create, Read, Update (soft delete), and List flights
- **Pagination & Sorting**: Efficient data retrieval with pagination support
- **Soft Delete**: Flights are soft-deleted (marked as deleted) rather than permanently removed
- **Caching**: In-memory caching for improved performance on flight listings
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Comprehensive Testing**: Unit tests for services
- **Data Validation**: Request validation with clear error messages

## 🛠️ Tech Stack

- **Java 21**: Modern Java features
- **Spring Boot 4.0.0**: Application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database abstraction layer
- **MySQL**: Relational database
- **JWT (jjwt 0.12.3)**: Token-based authentication
- **MapStruct**: Object mapping
- **Lombok**: Boilerplate code reduction
- **Swagger/OpenAPI**: API documentation
- **Maven**: Dependency management and build tool

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (or compatible database)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd demo
```

### 2. Database Setup

Create a MySQL database for the application:

```sql
CREATE DATABASE airxelerate_inventory_db;
```

Or the application will create it automatically if your MySQL user has the necessary permissions (see configuration below).

### 3. Configuration

The application uses environment-specific configuration. For local development, create or update `src/main/resources/application-local.yml`:

```yaml
airxelerate:
  inventory:
    datasource:
      url: jdbc:mysql://localhost:3306/airxelerate_inventory_db?createDatabaseIfNotExist=true&serverTimezone=UTC
      username: user
      password: password
    jpa:
      ddl-auto: update
      show-sql: true
      format-sql: true
    jwt:
      secret: 578c4249e79647b6d1dc9fe66a8c170e16a1a66df47fe6e85ff17516febb0740
      expiration: 3600000
    server:
      port: 9090
    logging:
      level:
        com.airxelerate.inventory: DEBUG
        org.springframework.security: DEBUG
```

**Important**: Update the database credentials (`username` and `password`) to match your MySQL setup.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

#### Option 1: Using Maven

```bash
mvn spring-boot:run
```

#### Option 2: Using IDE

Run the `FlightInventoryApplication` class directly from your IDE.


```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:9090** (or the port configured in your `application-local.yml`).

## 🔐 Default Users

The application automatically creates two default users on startup:

| Username | Password | Role | Permissions |
|----------|----------|------|-------------|
| `admin` | `admin123` | ADMIN | Full access (create, read, delete flights) |
| `user` | `user123` | USER | Read-only access (view flights) |

**⚠️ Security Note**: Change these default credentials in production!

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:9090/swagger-ui.html
- **OpenAPI JSON**: http://localhost:9090/api-docs

## 🔌 API Endpoints

### Authentication

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

### Flight Management

All flight endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

#### Create Flight (Admin Only)
```http
POST /api/flights
Authorization: Bearer <token>
Content-Type: application/json

{
  "carrierCode": "AA",
  "flightNumber": "1234",
  "flightDate": "2025-01-15",
  "origin": "JFK",
  "destination": "LAX"
}
```

**Validation Rules:**
- `carrierCode`: 2 uppercase letters (e.g., "AA", "BA")
- `flightNumber`: 4 digits (e.g., "1234")
- `flightDate`: Valid date (format: YYYY-MM-DD)
- `origin`: 3 uppercase letters (e.g., "JFK", "LAX")
- `destination`: 3 uppercase letters (e.g., "JFK", "LAX")

#### Get Flight by ID
```http
GET /api/flights/{id}
Authorization: Bearer <token>
```

#### Get All Flights (Paginated)
```http
GET /api/flights?page=0&size=20&sort=id,asc
Authorization: Bearer <token>
```

**Query Parameters:**
- `page`: Page number (0-indexed, default: 0)
- `size`: Page size (default: 20, max: 100)
- `sort`: Sort field and direction (default: id,asc)

**Example:**
```http
GET /api/flights?page=0&size=10&sort=flightDate,desc
```

#### Delete Flight (Admin Only - Soft Delete)
```http
DELETE /api/flights/{id}
Authorization: Bearer <token>
```

## 🧪 Testing

### Test Coverage

The project includes comprehensive unit tests for:
- **Service Layer**: `FlightServiceImplTest`, `AuthServiceImplTest`
## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/airxelerate/inventory/
│   │   ├── configuration/          # Spring configuration classes
│   │   │   ├── DataInitializer.java
│   │   │   ├── SecurityConfig.java
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/             # REST controllers
│   │   │   ├── auth/
│   │   │   │   └── AuthController.java
│   │   │   └── flight/
│   │   │       └── FlightController.java
│   │   ├── exception/              # Custom exceptions
│   │   │   ├── FlightNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── mapper/                 # MapStruct mappers
│   │   │   └── FlightMapper.java
│   │   ├── persistence/            # JPA entities and repositories
│   │   │   ├── entity/
│   │   │   │   ├── flight/
│   │   │   │   │   └── Flight.java
│   │   │   │   └── user/
│   │   │   │       ├── User.java
│   │   │   │       └── Role.java
│   │   │   └── repository/
│   │   │       ├── flight/
│   │   │       │   └── FlightJpaRepository.java
│   │   │       └── user/
│   │   │           └── UserJpaRepository.java
│   │   ├── security/               # Security components
│   │   │   ├── CustomUserDetailsService.java
│   │   │   ├── JwtTokenProvider.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   └── JwtAccessDeniedHandler.java
│   │   ├── service/                 # Business logic
│   │   │   ├── flight/
│   │   │   │   ├── FlightService.java
│   │   │   │   └── FlightServiceImpl.java
│   │   │   └── user/
│   │   │       ├── AuthService.java
│   │   │       └── AuthServiceImpl.java
│   │   ├── usecase/                 # DTOs (Request/Response)
│   │   │   ├── request/
│   │   │   │   ├── flight/
│   │   │   │   │   └── FlightRequest.java
│   │   │   │   └── user/
│   │   │   │       └── LoginRequest.java
│   │   │   └── response/
│   │   │       ├── common/
│   │   │       │   ├── PagedResponse.java
│   │   │       │   └── DeleteResponse.java
│   │   │       ├── flight/
│   │   │       │   └── FlightResponse.java
│   │   │       └── user/
│   │   │           └── AuthResponse.java
│   │   └── util/                    # Utility classes
│   │       ├── PaginationUtils.java
│   │       ├── ResponseUtils.java
│   │       └── SortUtils.java
│   └── resources/
│       ├── application.yml          # Main configuration
│       └── application-local.yml     # Local development config
└── test/
    └── java/com/airxelerate/inventory/
        ├── service/                 # Service layer tests
        │   ├── flight/
        │   │   └── FlightServiceImplTest.java
        │   └── user/
               └── AuthServiceImplTest.java

```

## 🔄 Caching

The application uses Spring's in-memory caching for the `getAllFlights` endpoint to improve performance. Cache is automatically invalidated when:
- A new flight is created
- A flight is deleted (soft delete)

Cache configuration can be customized in the application properties.

## 🗄️ Database Schema

### Flights Table
- `id`: Primary key (auto-generated)
- `carrier_code`: 2-letter airline code (e.g., "AA")
- `flight_number`: 4-digit flight number (e.g., "1234")
- `flight_date`: Date of the flight
- `origin`: 3-letter airport code (e.g., "JFK")
- `destination`: 3-letter airport code (e.g., "LAX")
- `deleted`: Soft delete flag (boolean)

**Unique Constraint**: `(carrier_code, flight_number, flight_date)`

### Users Table
- `id`: Primary key (auto-generated)
- `username`: Unique username
- `password`: BCrypt-encoded password
- `role`: User role (ROLE_ADMIN or ROLE_USER)
- `enabled`: Account enabled flag

## 🚨 Troubleshooting

### Port Already in Use
If port 9090 is already in use, change it in `application-local.yml`:
```yaml
airxelerate:
  inventory:
    server:
      port: 8080  # Change to available port
```
## 📄 License

This project is part of an assessment test for Axlab.

## 👤 Author

HASSIB Amine

---



