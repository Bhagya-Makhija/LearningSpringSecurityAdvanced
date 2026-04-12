# 🔐 Learning Spring Security Advanced

A hands-on project demonstrating multiple ways to implement authentication and authorization using Spring Security.

This project showcases the evolution of security in Spring Boot applications — from custom JWT implementation to OAuth2-based authentication.

---

## 🚀 Features

### 🔹 1. Custom JWT Authentication
- Implemented using `OncePerRequestFilter`
- Manual token generation & validation
- Stateless authentication
- Role-based access control

---

### 🔹 2. OAuth2 Resource Server (JWT)
- Uses Spring Security’s built-in support
- Cleaner and production-ready approach
- Token validation using framework configuration

---

### 🔹 3. OAuth2 Login (Social Login)
- Google Login
- GitHub Login
- Multi-provider support
- Account linking using a separate OAuthAccount entity

---

## 🧠 Key Learnings

- Difference between **custom JWT vs OAuth2 Resource Server**
- How Spring Security internally handles authentication
- Designing scalable authentication systems
- Handling real-world edge cases (e.g., GitHub email issue)
- Structuring security for production-ready applications

> Spring Security is the de-facto standard for securing Java applications and supports modern mechanisms like OAuth2 and JWT-based APIs :contentReference[oaicite:0]{index=0}

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- OAuth2 Client
- PostgreSQL
- JPA / Hibernate

---

## ⚙️ Setup Instructions

### 1. Clone the repo

```bash```
git clone https://github.com/Bhagya-Makhija/LearningSpringSecurityAdvanced.git
cd LearningSpringSecurityAdvanced
### 2. Configure database

Create PostgreSQL DB and update:
application-local.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password

### 3. Configure OAuth

Create:
application-secret.properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_SECRET
spring.security.oauth2.client.registration.github.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_SECRET

### 4. Run the application
mvn spring-boot:run

API Endpoints-
http://localhost:8080/api/v1/auth/signup
http://localhost:8080/api/v1/auth/login
http://localhost:8080/api/v1/login
http://localhost:8080/api/v1/user/habits
