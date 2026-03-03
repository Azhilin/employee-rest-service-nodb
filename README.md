# Employee REST Service (No DB)

A Spring Boot REST API for managing employee data using an in-memory store (no database required).

## Technologies

- Java 8
- Spring Boot 2.1.5
- Lombok
- Cucumber 4 (BDD testing)
- Allure (test reporting)
- Maven

## Prerequisites

- JDK 8+
- Maven 3.x

## Getting Started

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

The service starts on port **9090**.

## API Endpoints

Base URL: `http://localhost:9090`

| Method   | Endpoint          | Description                  |
|----------|-------------------|------------------------------|
| GET      | `/employee`       | Get all employees            |
| GET      | `/employee/{id}`  | Get employee by ID           |
| POST     | `/employee`       | Add a single employee        |
| POST     | `/employee/list`  | Add a list of employees      |
| PUT      | `/employee`       | Full update of an employee   |
| PATCH    | `/employee`       | Partial update of an employee|
| DELETE   | `/employee/{id}`  | Delete employee by ID        |
| DELETE   | `/employee`       | Delete all employees         |

### Employee Model

```json
{
  "id": 1,
  "name": "Tom",
  "passportNumber": "TM123456",
  "education": "University"
}
```

All fields are required when adding or fully updating an employee. For a partial update (`PATCH`), only the fields to be changed need to be provided.

### Examples

**Add an employee**
```bash
curl -X POST http://localhost:9090/employee \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Tom","passportNumber":"TM123456","education":"University"}'
```

**Get all employees**
```bash
curl http://localhost:9090/employee
```

**Get employee by ID**
```bash
curl http://localhost:9090/employee/1
```

**Update an employee**
```bash
curl -X PUT http://localhost:9090/employee \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Tom","passportNumber":"TM999999","education":"College"}'
```

**Delete an employee**
```bash
curl -X DELETE http://localhost:9090/employee/1
```

## Testing

Tests are written in Cucumber (BDD) and can be run with:

```bash
mvn test
```

To run tests with a specific tag:

```bash
mvn test -Dtags=@employee
```

### Allure Report

Generate and open the Allure test report after running tests:

```bash
mvn allure:serve
```

## Project Structure

```
src/
├── main/
│   ├── java/alex/enterprise/employeenodb/
│   │   ├── EmployeeRestServiceNodbApplication.java
│   │   ├── controller/
│   │   │   ├── EmployeeController.java
│   │   │   └── advice/
│   │   │       ├── RestAdviceHandler.java
│   │   │       └── ResponseMessage.java
│   │   ├── model/
│   │   │   └── Employee.java
│   │   ├── service/
│   │   │   └── EmployeeService.java
│   │   └── exception/
│   │       └── CustomRuntimeException.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/alex/enterprise/employeenodb/
    │   ├── spring/SpringIntegrationTest.java
    │   ├── employee/EmployeeDefStep.java
    │   ├── EmployeeTestRunner.java
    │   └── util/session/
    └── resources/
        ├── features/
        │   ├── employee.feature
        │   ├── employee1.feature
        │   └── employee2.feature
        └── allure.properties
```
