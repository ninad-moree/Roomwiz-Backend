# Roomwiz

## Overview
This is a backend application for a hotel booking system built with Java and Spring Boot. It allows users to book rooms and admins to manage room availability. The application includes role-based access control, where users have specific permissions based on their role, and secure authentication using JWT.

## Features

- User Roles:
  - Admin: Can add, delete, and edit room details.
  - User: Can view available rooms and make bookings.
- Secure Authentication: Uses JSON Web Tokens (JWT) to authenticate users and secure endpoints.
- Role-Based Access Control: Permissions are enforced based on user roles, with restricted access to specific endpoints.

## Technology Stack
- Java: Core programming language used for backend development.
- Spring Boot: Framework for building and structuring the RESTful API.
- Hibernate: ORM framework to manage database interactions.
- MySQL: Relational database to store user, booking, and room data.
- Maven: Build automation tool for managing dependencies.
- Spring Security: Provides security configurations for JWT-based authentication and role-based access control.
