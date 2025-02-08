# **Event Management System API**
A Spring Boot-based RESTful web service for managing events, participants, and organizers with role-based access control.

---

## **Table of Contents**
1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Getting Started](#getting-started)
4. [Running the Application](#running-the-application)
5. [API Documentation](#api-documentation)
6. [API Endpoints](#api-endpoints)
7. [Authentication & Authorization](#authentication--authorization)
8. [Database Configuration](#database-configuration)
9. [Testing](#testing)

---

## **Features**
✅ User authentication & authorization (JWT)  
✅ Event creation, update, and deletion  
✅ Participant management for events  
✅ Role-based access control (Admin, Participant, Organizer)  
✅ RESTful API with Swagger documentation  
✅ File upload & download support  
✅ PostgreSQL/MySQL database integration  
✅ Global Exception Handling with custom error responses  
✅ Unit & Integration Testing (100% coverage)

---

## **Tech Stack**
- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA
- **Database:** PostgreSQL/MySQL
- **Build Tool:** Maven
- **Testing:** JUnit, Mockito
- **API Documentation:** Swagger (Springdoc OpenAPI)

---

## **Getting Started**

### **Prerequisites**
- Java 21
- Maven
- PostgreSQL (or MySQL)
- IntelliJ IDEA (Recommended)

### **Clone the Repository**
```bash
git clone https://github.com/tarektarho/novi-eindopdracht-event-management-system.git
cd novi-eindopdracht-event-management-system
```

### **Configure Environment Variables**
Create a `.env` file and add the following:
```ini
DB_URL=jdbc:postgresql://localhost:5432/EventManagementSystem
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
jwt.SecretKey=eengeheimesleuteldieniemandmagwetenenhijmoetheelerglangencomplexzijnomtevoldoenaanallenormenenwaardeninjavaland
jwt.Audience=eventManagementSystem-api.com
```
Also, create `.env.example` to guide other developers.

---

## **Running the Application**

### **Using IntelliJ IDEA**
1. Open the project in IntelliJ IDEA.
2. Ensure Maven is installed and configured.
3. Start the application using intelliJ IDEA by running the `EventManagementSystemApplication` class.

---

## **API Documentation**
Swagger UI is available at:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

API Docs:  
[http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## **API Endpoints**

### **Authentication API**
| Method | Endpoint | Access |
|--------|---------|--------|
| **POST** | `/api/v1/authenticate` | Public |
| **GET** | `/api/v1/authenticated` | Any authenticated user |

### **User API**
| Method     | Endpoint | Access                        |
|------------|---------|-------------------------------|
| **GET**    | `/api/v1/users/{username}` | Admin, Organizer                       |
| **PUT**    | `/api/v1/users/{username}` | Admin, Organizer              |
| **DELETE** | `/api/v1/users/{username}` | Admin                         |
| **PATCH**  | `/api/v1/users/{username}/ticket/{ticketId}` | Admin, Organizer              |
| **GET**    | `/api/v1/users/{username}/roles` | Admin                         |
| **PATCH**  | `/api/v1/users/{username}/roles` | Admin                         |
| **PATCH**  | `/api/v1/users/{username}/roles/{role}` | Admin                         |
| **GET**    | `/api/v1/users/{username}/photo` | Admin, Organizer, Participant |
| **POST**   | `/api/v1/users/{username}/photo` | Admin, Organizer, Participant |
| **GET**    | `/api/v1/users/all` | Admin, Organizer              |
| **POST**   | `/api/v1/users/create` | Public                        |

### **Event API**
| Method     | Endpoint | Access                        |
|------------|---------|-------------------------------|
| **GET**    | `/api/v1/events/{id}` | Admin, Organizer, Participant |
| **PUT**    | `/api/v1/events/{id}` | Admin, Organizer              |
| **DELETE** | `/api/v1/events/{id}` | Admin, Organizer              |
| **PATCH**  | `/api/v1/events/{eventId}/remove-tickets` | Admin, Organizer              |
| **PATCH**  | `/api/v1/events/{eventId}/remove-participants` | Admin, Organizer              |
| **PATCH**  | `/api/v1/events/{eventId}/remove-feedback` | Admin, Organizer              |
| **PATCH**  | `/api/v1/events/{eventId}/assign-participants` | Admin, Organizer              |
| **PATCH**  | `/api/v1/events/{eventId}/add-tickets` | Admin, Organizer              |
| **PATCH**  | `/api/v1/events/{eventId}/add-feedback` | Admin, Organizer, Participant            |
| **POST**   | `/api/v1/events/create` | Admin, Organizer              |
| **GET**    | `/api/v1/events/organizer/{username}` | Admin, Organizer, Participant |
| **GET**    | `/api/v1/events/all` | Admin, Organizer, Participant |

### **Ticket API**
| Method | Endpoint | Access                        |
|--------|---------|-------------------------------|
| **GET** | `/api/v1/tickets/{id}` | Admin, Organizer, Participant |
| **PUT** | `/api/v1/tickets/{id}` | Admin, Organizer              |
| **DELETE** | `/api/v1/tickets/{id}` | Admin, Organizer              |
| **POST** | `/api/v1/tickets/create` | Admin, Organizer              |
| **GET** | `/api/v1/tickets/user/{username}` | Admin, Organizer, Participant |
| **GET** | `/api/v1/tickets/event/{eventId}` | Admin, Organizer, Participant |
| **GET** | `/api/v1/tickets/all` | Admin, Organizer, Participant            |

### **Feedback API**
| Method | Endpoint | Access                        |
|--------|---------|-------------------------------|
| **GET** | `/api/v1/feedback/{id}` | Admin, Organizer, Participant |
| **PUT** | `/api/v1/feedback/{id}` | Admin, Organizer              |
| **DELETE** | `/api/v1/feedback/{id}` | Admin, Organizer              |
| **POST** | `/api/v1/feedback/{feedbackId}/user/{username}` | Admin, Organizer, Participant |
| **POST** | `/api/v1/feedback/{feedbackId}/event/{eventId}` | Admin, Organizer, Participant |
| **POST** | `/api/v1/feedback/submit` | Admin, Organizer, Participant |
| **GET** | `/api/v1/feedback/user/{username}` | Admin, Organizer, Participant |
| **GET** | `/api/v1/feedback/event/{eventId}` | Admin, Organizer, Participant |
| **GET** | `/api/v1/feedback/all` | Admin, Organizer, Participant                       |

---

## **Authentication & Authorization**

### **Public Endpoints:**
- `/api/v1/authenticate`
- Swagger Docs (`/api-docs/**`, `/swagger-ui/**`, `/v3/api-docs/**`)

### **Protected Endpoints:**
- **Admin:** Can access all `/api/v1/**` endpoints.
- **Organizer:** Manages events, tickets, and feedback.
- **Participant:** Limited to viewing events and tickets, and submitting feedback.

JWT Authentication is required for all protected endpoints. Users must include a Bearer token in the `Authorization` header:
```http
Authorization: Bearer <JWT_TOKEN>
```

---

## **Database Configuration**

Database properties are stored in `.env`:
```ini
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```
For local development:
```ini
spring.jpa.hibernate.ddl-auto=create
spring.sql.init.mode=always
```

---

## **Testing**
Run unit and integration tests using the IDE.

---
