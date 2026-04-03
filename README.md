# FlightBookingProject (Learning Sandbox)
This is my first major learning project created to explore the Java / Spring Boot ecosystem. It is a work-in-progress "sandbox" where I experimented with backend concepts.

## Project Status
Archived / Unmaintained: This project is currently not functional in a production sense.

Purpose: The goal was not to build a finished product, but to practice writing code, structuring packages, and integrating APIs.

Broken Integrations: External API calls (Amadeus) may fail due to expired credentials or architectural shifts during the learning process.

## What I focused on (Learning Goals):
Instead of looking at this as a working app, look at the code to see what I was practicing:

Spring Boot Basics: Setting up controllers, services, and repositories.

REST API Exploration: Learning how to send requests to the Amadeus API and handle JSON responses.

Security Experiments: Basic attempts at implementing JWT and Spring Security (see SecurityConfig.java).

Data Modeling: Using Hibernate/JPA to define relationships between Users, Flights, and Bookings.

Error Handling: Practicing global exception management with @ControllerAdvice.

## Reflections:
Looking back at this code, I now see many "bad" practices (lack of interface abstraction, incomplete logic in some services, etc.). However, this project served as the essential stepping stone that allowed me to build my later, more robust projects like the MunichWay Scooter Service.
