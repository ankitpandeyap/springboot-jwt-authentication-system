SpringBoot-JWT-Authentication-System with OTP Verification

This backend system is built using Spring Boot and implements a secure authentication and authorization mechanism. It supports email-based OTP verification during registration, JWT token-based login, refresh token support, token blacklisting via Redis, and role-based access control.

🔧 Features

✅ User Registration with Email OTP Verification

✅ Login with JWT Token Generation

✅ Refresh Token and Access Token Handling

✅ Token Blacklisting using Redis

✅ Secure Logout Mechanism

✅ Role-Based Access Control (via annotations)

✅ Redis Docker Integration for Session and Token Management

✅ SMTP Integration for Sending OTPs (Email)

🔐 CORS Configuration for Frontend Communication

🌐 Ready for HTTPS and Secure Deployment

🧰 Tech Stack

Java 17

Spring Boot 3.x

Spring Security

Redis (via Docker)

MySQL

Jakarta Mail (SMTP)

JWT (Access and Refresh tokens)

Maven

📝 Prerequisites

Make sure you have the following installed:

Java 17+

Maven 3.8+

Docker (for Redis)

MySQL (Running locally or in Docker)

🐳 Redis Setup with Docker

Run the following command:

docker run --name redis-auth -p 6379:6379 -d redis

📧 SMTP Setup for OTP

Update your application.properties:

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

Make sure to use an App Password if you're using Gmail.

⚙️ Application Properties (Important Snippets)

jwt.secret=your-secret-key
redis.host=localhost
redis.port=6379


cors.allowed.origins=http://localhost:3000
cors.allowed.methods=GET,POST,PUT,DELETE
cors.allowed.headers=*
cors.allowed.credentials=true

📂 Module Breakdown

🔐 Authentication

OTP Registration

JWT Access/Refresh Token Login

Refresh Token Endpoint

Token Validation Endpoint

🧼 Logout

Access & Refresh token blacklisting using Redis

🧾 Token Management

Custom Filters for login, validation, refresh

Filters check for blacklisted tokens

🎯 Role-Based Access

Use @PreAuthorize("hasRole('ADMIN')") in controller methods

🚀 Deployment Readiness

To prepare for production:

✅ Enable HTTPS in Spring Boot

✅ Use Env Variables for Secrets

✅ Secure Redis with Auth

✅ Restrict CORS to production domains

✅ Move mail credentials to environment or secrets manager

✅ Add centralized logging (SLF4J + Logback)

🔜 To Do (Post-Frontend)



🧑‍💻 Author

Ankit PandeyGitHub • LinkedIn (https://www.linkedin.com/in/ankitpandeyap/)


