# Restaurant Management System

Production-ready Spring Boot REST API for managing restaurants, menus, orders, and staff with secure JWT-based authentication.

## Features
* Full restaurant lifecycle management (restaurants, menus, staff)
* Advanced menu browsing with pagination and filtering
* Complex order handling with multiple items per order
* Table-based checkout system (merge multiple orders into one transaction)
* Secure JWT-based authentication and role-based authorization (GUEST, STAFF, MANAGER, ADMIN)

## Highlights
* Layered architecture (Controller → Service → Repository)
* Stateless authentication using JWT with custom filter
* Input validation using Jakarta Validation (@Valid)
* Global exception handling with @RestControllerAdvice
* DTO-based API design with ModelMapper
* Database-level pagination for performance
* Clean and consistent REST API design
* Unit and integration test coverage
* PostgreSQL integration with persistent storage

## Tech
Java • Spring Boot • Spring Data JPA (Hibernate) • Spring Security (JWT) • PostgreSQL • Maven • JUnit • Mockito
