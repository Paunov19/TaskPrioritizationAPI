# **Task Management API**
A RESTful API for managing tasks with features like sorting, filtering, and detailed task retrieval. This project uses Spring Boot with MySQL for data storage and includes comprehensive error handling and API documentation via Swagger.

## **Features**
Task CRUD Operations:
 - Create, read, update, and delete tasks.
 - Sorting and Filtering:
 - Sort tasks by priority or due date.
 - Filter tasks by completion status or priority.
 - Detailed Task Retrieval:
 - Fetch a task by its ID.
 - Retrieve all tasks with full details.

## **Setup Instructions**
**Prerequisites**
- Java 17
- Spring Boot
- Maven
- MySQL (or your preferred database)
- Swagger UI web interface
- IntelliJ IDEA (or any other Java IDE)
  
## **Steps to Run the Application**
**1. Clone the Repository**

git clone https://github.com/your-repo/task-management-api.git

cd task-management-api

**2.Set up the Database**

If MySQL is used, update application.properties with your database configuration(write your MySQL username and password:

spring.datasource.username=  **your_username**

spring.datasource.password=  **your_password**

If PostgreSQL is used, need to update the whole application.properties file with your database configuration(copy and paste it in the file, then write your PostgreSQL username and password):

spring.datasource.url = jdbc:mysql://${DATABASE_URL:localhost}:5432/taskPriorityDB?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8

spring.datasource.username = **your PostgreSQL username**

spring.datasource.password = **your PostgreSQL password**

spring.jpa.show-sql = true

spring.jpa.hibernate.ddl-auto = update

spring.jpa.properties.hibernate.format_sql=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

**3. Build and Run the Application**

mvn clean install

mvn spring-boot:run

or

run it from your IDE

**4. API Documentation**

Open the Swagger UI in your browser for interactive API documentation:

http://localhost:8080/swagger-ui.html

In Swagger, we can manually test all API endpoints. Each endpoint requires specific request data. Follow the details provided for each request to ensure you send the correct input.

## **External Libraries and Tools Used**
- Spring Boot: Simplifies application setup and development.
- Spring Data JPA: Provides database access and ORM functionalities.
- Lombok: Reduces boilerplate code for models (Getters, Setters, etc.).
- Swagger: Generates API documentation and allows interactive API testing.
- Mockito: Used for writing unit tests and mocking dependencies.
- JUnit 5: Test framework for unit and integration tests.
  
## **Future Improvements**
Add User Authentication and Authorization:

JWT Authentication will secure the API by verifying user identity and granting access based on roles.
  
Roles and Permissions:
- Admin: Full access to all endpoints (create, update, delete, manage users).
- User: Limited access to manage only their own tasks.
- Viewer: Read-only access to tasks.
  
Tokens will be required for all API requests, ensuring only authorized users can access resources.
