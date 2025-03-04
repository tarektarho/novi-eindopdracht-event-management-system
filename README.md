[![Java CI with Maven Build and Test](https://github.com/tarektarho/novi-eindopdracht-event-management-system/actions/workflows/maven.yml/badge.svg)](https://github.com/tarektarho/novi-eindopdracht-event-management-system/actions/workflows/maven.yml)

**Event Management System API**
A Spring Boot-based RESTful web service for managing events, tickets, feedback, participants, and organizers with
role-based access control.

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

1. User authentication & authorization (JWT)
2. Event creation, update, and deletion
3. Participant management for events
4. Feedback submission for events
5. Ticket creation, update, and deletion
6. Role-based access control (Admin, Participant, Organizer)
7. RESTful API with Swagger documentation
8. File upload & download support  
9. PostgreSQL database integration  
10. Global Exception Handling with custom error responses  
11. Unit & Integration Testing (100% coverage)

---

## **Tech Stack**

- **Backend:** Java 21, Spring Boot 3.3.5, Spring Security, Spring Data JPA
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **Testing:** JUnit, Mockito
- **API Documentation:** Swagger (Springdoc OpenAPI)

---

## **Getting Started**

### **Prerequisites**

- Java 21
- Maven
- PostgreSQL
- IntelliJ IDEA (Recommended)

### **Clone the Repository**

```bash
git clone https://github.com/tarektarho/novi-eindopdracht-event-management-system.git
cd novi-eindopdracht-event-management-system
```

---

## **Setting Up and Connecting the Database**

To run the **Event Management System API**, you need to configure a **PostgreSQL** database. Follow these steps to **download, install, and configure PostgreSQL and pgAdmin**, and connect it to the application.

---

## **Download & Install PostgreSQL and pgAdmin**

### **Download PostgreSQL**
1. Go to the [official PostgreSQL website](https://www.postgresql.org/download/).
2. Choose your operating system (Windows, macOS, or Linux).
3. Download and run the installer.

### **Install PostgreSQL**
1. Follow the installation wizard.
2. During setup, **choose a password for the PostgreSQL superuser (`postgres`)**.
3. Keep the default port **5432** unless you need a different one.
4. Install **pgAdmin** (it comes with the PostgreSQL installer).

### **Download pgAdmin** (Optional if not installed with PostgreSQL)
- If you need **pgAdmin** separately, download it from [pgAdmin Official Site](https://www.pgadmin.org/download/).

---

## **Configure the PostgreSQL Database**

### Open pgAdmin and Connect to PostgreSQL
1. Open **pgAdmin** from the start menu.
2. On the left panel, find the **"Servers"** section and click on **"PostgreSQL 16"** (or your version).
3. Enter the password you set during installation.


---

### **Configure Environment Variables**

Create a `.env` file if it does not exist and add the following:

```ini
DB_URL=jdbc:postgresql://localhost:5432/EventManagementSystem
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
jwt.SecretKey=eengeheimesleuteldieniemandmagwetenenhijmoetheelerglangencomplexzijnomtevoldoenaanallenormenenwaardeninjavaland
jwt.Audience=eventManagementSystem-api.com
```

---

## **Roles and test users**

- **Admin:**
    - **Username:** admin
    - **Password:** password
- **Organizer:**
    - **Username:** organizer
    - **Password:** password
- **Participant:**
    - **Username:** participant
    - **Password:** password

---

## **Running the Application**

### **Using IntelliJ IDEA**

1. Open the project in IntelliJ IDEA.
2. Ensure Maven is installed and configured.
3. Start the application using intelliJ IDEA by running the `EventManagementSystemApplication` class.

### **Using Maven**

Run the application using Maven:

```bash
mvn spring-boot:run
```

---

## **Testing with Postman**
Export file can be found in the root directory of the project inside docs -> postman -> event-management-system.postman_collection.json

You can import this file in Postman to test the API endpoints.

---

## **API Documentation**

Swagger UI is available at:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

API Docs (JSON):  
[http://localhost:8080/api-docs](http://localhost:8080/api-docs)

---

## **API Endpoints**

### **Authentication API**

| Method   | Endpoint                | Description                         | Access                 | Response                      |
|----------|-------------------------|-------------------------------------|------------------------|-------------------------------|
| **POST** | `/api/v1/authenticate`  | Authenticate a user                 | Public                 | `200 OK` / `401 Unauthorized` |
| **GET**  | `/api/v1/authenticated` | Retrieve authenticated user details | Any authenticated user | `200 OK`                      |

### **User API**

| Method     | Endpoint                                     | Description                   | Access                        | Response                                       |
|------------|----------------------------------------------|-------------------------------|-------------------------------|------------------------------------------------|
| **GET**    | `/api/v1/users/{username}`                   | Retrieve user details         | Admin, Organizer              | `200 OK` / `404 Not Found`                     |
| **PUT**    | `/api/v1/users/{username}`                   | Update user details           | Admin, Organizer              | `200 OK` / `400 Bad Request` / `404 Not Found` |
| **DELETE** | `/api/v1/users/{username}`                   | Delete a user                 | Admin                         | `204 No Content` / `404 Not Found`             |
| **POST**   | `/api/v1/users/{username}/ticket/{ticketId}` | Assign a ticket to a user     | Admin, Organizer              | `200 OK` / `400 Bad Request`                   |
| **GET**    | `/api/v1/users/{username}/roles`             | Retrieve user roles           | Admin                         | `200 OK`                                       |
| **POST**   | `/api/v1/users/{username}/roles`             | Add a role to a user          | Admin                         | `200 OK` / `400 Bad Request`                   |
| **DELETE** | `/api/v1/users/{username}/roles/{role}`      | Remove a role from a user     | Admin                         | `200 OK` / `400 Bad Request`                   |
| **GET**    | `/api/v1/users/{username}/photo`             | Retrieve user’s profile photo | Admin, Organizer, Participant | `200 OK` / `404 Not Found`                     |
| **POST**   | `/api/v1/users/{username}/photo`             | Upload user’s profile photo   | Admin, Organizer, Participant | `201 Created` / `400 Bad Request`              |
| **GET**    | `/api/v1/users`                              | Retrieve all users            | Admin, Organizer              | `200 OK`                                       |
| **POST**   | `/api/v1/users`                              | Create a new user             | Public                        | `201 Created` / `400 Bad Request`              |

### **Event API**

| Method     | Endpoint                              | Description                       | Access                        | Response                                                    |
|------------|---------------------------------------|-----------------------------------|-------------------------------|-------------------------------------------------------------|
| **GET**    | `/api/v1/events/{id}`                 | Retrieve event details            | Admin, Organizer, Participant | `200 OK` / `404 Not Found`                                  |
| **PUT**    | `/api/v1/events/{id}`                 | Update event details              | Admin, Organizer              | `200 OK` / `400 Bad Request` / `404 Not Found`              |
| **DELETE** | `/api/v1/events/{id}`                 | Delete an event                   | Admin, Organizer              | `204 No Content` / `404 Not Found`                          |
| **GET**    | `/api/v1/events`                      | Retrieve all events               | Admin, Organizer, Participant | `200 OK`                                                    |
| **POST**   | `/api/v1/events`                      | Create a new event                | Admin, Organizer              | `201 Created` / `400 Bad Request`                           |
| **POST**   | `/api/v1/events/{id}/tickets`         | Add tickets to an event           | Admin, Organizer              | `204 No Content` / `400 Bad Request`                        |
| **DELETE** | `/api/v1/events/{id}/tickets`         | Remove tickets from an event      | Admin, Organizer              | `200 OK (list of removed tickets)` / `400 Bad Request`      |
| **POST**   | `/api/v1/events/{id}/participants`    | Assign participants to an event   | Admin, Organizer              | `204 No Content` / `400 Bad Request`                        |
| **DELETE** | `/api/v1/events/{id}/participants`    | Remove participants from an event | Admin, Organizer              | `200 OK (list of removed participants)` / `400 Bad Request` |
| **POST**   | `/api/v1/events/{id}/feedback`        | Assign feedback for an event      | Admin, Organizer, Participant | `204 No Content` / `400 Bad Request`                        |
| **DELETE** | `/api/v1/events/{id}/feedback`        | Remove feedback from an event     | Admin, Organizer              | `200 OK (list of removed feedback)` / `400 Bad Request`     |
| **POST**   | `/api/v1/events/{id}/organizer`       | Assign organizer to event         | Admin, Organizer              | `204 No Content`                                            |
| **DELETE** | `/api/v1/events/{id}/organizer`       | Remove organizer from an event    | Admin, Organizer              | `200 OK` / `400 Bad Request`                                |
| **GET**    | `/api/v1/events/organizer/{username}` | Retrieve events by organizer      | Admin, Organizer, Participant | `200 OK`                                                    |

### **Ticket API**

| Method     | Endpoint                          | Description                         | Access                        | Response                                       |
|------------|-----------------------------------|-------------------------------------|-------------------------------|------------------------------------------------|
| **GET**    | `/api/v1/tickets/{id}`            | Retrieve ticket details             | Admin, Organizer, Participant | `200 OK` / `404 Not Found`                     |
| **PUT**    | `/api/v1/tickets/{id}`            | Update ticket details               | Admin, Organizer              | `200 OK` / `400 Bad Request` / `404 Not Found` |
| **DELETE** | `/api/v1/tickets/{id}`            | Delete a ticket                     | Admin, Organizer              | `204 No Content` / `404 Not Found`             |
| **POST**   | `/api/v1/tickets`                 | Create a new ticket                 | Admin, Organizer              | `201 Created` / `400 Bad Request`              |
| **GET**    | `/api/v1/tickets`                 | Retrieve all tickets                | Admin, Organizer, Participant | `200 OK`                                       |

### **Feedback API**

| Method     | Endpoint                                        | Description                    | Access                        | Response                           |
|------------|-------------------------------------------------|--------------------------------|-------------------------------|------------------------------------|
| **GET**    | `/api/v1/feedback/{id}`                         | Retrieve feedback by ID        | Admin, Organizer, Participant | `200 OK` / `404 Not Found`         |
| **PUT**    | `/api/v1/feedback/{id}`                         | Update feedback                | Admin, Organizer              | `200 OK` / `400 Bad Request`       |
| **DELETE** | `/api/v1/feedback/{id}`                         | Delete feedback                | Admin, Organizer              | `204 No Content` / `404 Not Found` |
| **POST**   | `/api/v1/feedback`                              | Submit feedback                | Admin, Organizer, Participant | `201 Created` / `400 Bad Request`  |
| **GET**    | `/api/v1/feedback/user/{username}`              | Retrieve feedback by user      | Admin, Organizer, Participant | `200 OK`                           |
| **GET**    | `/api/v1/feedback/event/{eventId}`              | Retrieve feedback for an event | Admin, Organizer, Participant | `200 OK`                           |
| **GET**    | `/api/v1/feedback`                              | Retrieve all feedback entries  | Admin, Organizer, Participant | `200 OK`                           |

---

## **Authentication & Authorization**

### **Public Endpoints:**

- `/api/v1/authenticate`
- Swagger Docs (`/api-docs/**`, `/swagger-ui/**`, `/v3/api-docs/**`)

### **Protected Endpoints:**

- **Admin:** Can access all `/api/v1/**` endpoints.
- **Organizer:** Manages events, tickets, and feedback.
- **Participant:** Limited to viewing events and tickets, and submitting feedback.

JWT Authentication is required for all protected endpoints. Users must include a Bearer token in the `Authorization`
header:

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

Run unit and integration tests using the IDE or Maven:

```bash
mvn test
```

---
