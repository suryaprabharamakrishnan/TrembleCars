# Trimble Cars Lease Management - Backend API

This is the backend service for **Trimble Cars**, a lease management platform built using Java and Spring Boot. It provides RESTful APIs for authentication, user management, roles, car leasing, and car registration.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Gradle (Groovy DSL)**
- **Spring Data JPA**
- **MySQL**
- **Spring Security + JWT (for auth)**
- **Swagger (springdoc-openapi)**

---

## Modules

- **Authentication**: Signup, Signin with JWT, Token refresh
- **User Management**: Register and manage users
- **Role Management**: Create, fetch, delete roles
- **Car Registration**: Car owner can register and track their cars
- **Lease Management**: Customers lease and return cars

---

## Authentication
- Signup and Signin use JWT token-based authentication
- Secure endpoints using Bearer token in Authorization header

---

## Swagger API Docs

Once the app is running, access the Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

---

## How to Run

### 1. Clone the repo
```bash
git clone https://github.com/your-username/trimble-cars-backend.git
cd trimble-cars-backend
```

### 2. Configure MySQL
Ensure a MySQL DB named `trimble_cars` exists.
Update `src/main/resources/application.properties`:
```properties
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build and Run
```bash
./gradlew clean build
./gradlew bootRun
```
