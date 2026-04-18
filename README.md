# Restaurant Management System

Spring Boot REST API for managing restaurant menus and orders.

## Features
* Restaurant, menu, and staff management
* Paginated and filtered menu listing
* Order creation with multiple items
* Table-based checkout combining multiple orders into a single transaction
* Role-based authorization (GUEST, STAFF, MANAGER, ADMIN)

## Highlights
* Clean layered architecture (Controller → Service → Repository)
* Pagination handled at database level
* DTO mapping (ModelMapper)
* OrderItems managed within Order (no redundant endpoints)

## Tech
Java • Spring Boot • Spring Data JPA (Hibernate) • Spring Security (JWT) • PostgreSQL • Maven • JUnit • Mockito