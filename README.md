# E-Commerce – Spring Boot Application

This project is a **Spring Boot backend application** for an E-Commerce 
It uses **Spring Security**, **JPA**, **MySQL**, and **JWT** for authentication and data management.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot 4.0.2
- Spring Security
- Spring Data JPA
- MySQL
- JWT (jwt)
- Mavin

---

## 📂 Requirements

Before running the project, make sure you have:

- Java JDK 17
- MySQL 8 or higher
- Mavin

---

## 🛠️ Database Setup

1. Start your MySQL server
2. Create the database:

```sql
CREATE DATABASE ecommerce_project;
```

## Configure DataBase Credentials

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_project
    username: root
    password: ahly_1907
    port    : 8081
```
⚠️ Make sure the username and password match your local MySQL setup.

---

## ▶️ Running the Application

### Using Maven (Recommended)

```bash
mvn spring-boot:run

```

---
⚠️ Make sure the port 8081 is free to use.



