# 📚 Grade Manager API

A **RESTful API** for academic grade management, built with **Spring Boot 3**, following clean architecture principles and industry best practices.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2 |
| Persistence | Spring Data JPA + H2 |
| Validation | Jakarta Bean Validation |
| Build | Maven |
| Tests | JUnit 5 + Mockito + MockMvc |
| Utilities | Lombok |

---

## 🏗️ Architecture

```
src/
└── main/java/com/arthur/grademanager/
    ├── config/         # DataLoader (demo data)
    ├── controller/     # REST endpoints
    ├── dto/            # Request/Response records
    ├── exception/      # Custom exceptions + GlobalExceptionHandler
    ├── mapper/         # Entity ↔ DTO conversion
    ├── model/          # JPA entities
    ├── repository/     # Spring Data repositories
    └── service/        # Business logic
```

The project follows a **layered architecture** (Controller → Service → Repository) with clear separation of concerns.

**Design patterns used:**
- **Builder** — entity construction
- **Repository** — data access abstraction
- **DTO** — separates API contract from domain model
- **Global Exception Handler** — centralized error responses

---

## ✨ Features

- ✅ Full CRUD for **Students**, **Courses**, and **Grades**
- ✅ Student academic report with average, approved/failed counts
- ✅ Business rule validation (duplicate registration, email, grade per course)
- ✅ Bean Validation on all request bodies
- ✅ Structured JSON error responses
- ✅ Demo data loaded on startup

---

## 🛠️ Running Locally

**Prerequisites:** Java 21+, Maven 3.8+

```bash
# Clone the repository
git clone https://github.com/ArthurrCavalcante/grade-manager.git
cd grade-manager

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

Access the H2 console at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:gradedb`
- Username: `sa` | Password: *(empty)*

---

## 📡 API Endpoints

### Students
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/students` | List all students |
| GET | `/api/v1/students/{id}` | Get student by ID |
| POST | `/api/v1/students` | Create a student |
| PUT | `/api/v1/students/{id}` | Update a student |
| DELETE | `/api/v1/students/{id}` | Delete a student |

### Courses
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/courses` | List all courses |
| GET | `/api/v1/courses/{id}` | Get course by ID |
| POST | `/api/v1/courses` | Create a course |
| PUT | `/api/v1/courses/{id}` | Update a course |
| DELETE | `/api/v1/courses/{id}` | Delete a course |

### Grades
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/grades` | List all grades |
| GET | `/api/v1/grades/{id}` | Get grade by ID |
| GET | `/api/v1/grades/report/student/{id}` | Student academic report |
| POST | `/api/v1/grades` | Register a grade |
| PUT | `/api/v1/grades/{id}` | Update a grade |
| DELETE | `/api/v1/grades/{id}` | Delete a grade |

---

## 📋 Request / Response Examples

### Create a student
```http
POST /api/v1/students
Content-Type: application/json

{
  "name": "Arthur Cavalcante",
  "registration": "2024001",
  "email": "arthur@ifce.edu.br"
}
```
```json
{
  "id": 1,
  "name": "Arthur Cavalcante",
  "registration": "2024001",
  "email": "arthur@ifce.edu.br"
}
```

### Register a grade
```http
POST /api/v1/grades
Content-Type: application/json

{
  "studentId": 1,
  "courseId": 1,
  "value": 9.5,
  "date": "2024-06-15"
}
```

### Student academic report
```http
GET /api/v1/grades/report/student/1
```
```json
{
  "student": { "id": 1, "name": "Arthur Cavalcante", "registration": "2024001" },
  "grades": [
    { "courseName": "Algorithms & Data Structures", "courseCode": "ADS101", "grade": 9.5, "approved": true },
    { "courseName": "Database Systems", "courseCode": "DBS301", "grade": 7.5, "approved": true }
  ],
  "average": 8.5,
  "approvedCount": 2,
  "failedCount": 0
}
```

### Validation error response
```json
{
  "name": "Name is required",
  "email": "Invalid email format"
}
```

---

## 🧪 Running Tests

```bash
./mvnw test
```

Test coverage includes:
- **Unit tests** — `StudentService`, `GradeService` (Mockito)
- **Controller tests** — `StudentController` (MockMvc + WebMvcTest)

---

## 📁 Project Structure

```
grade-manager/
├── src/
│   ├── main/
│   │   ├── java/com/arthur/grademanager/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/arthur/grademanager/
│           ├── controller/
│           └── service/
├── pom.xml
└── README.md
```

---

## 👨‍💻 Author

**Arthur Cavalcante**
- GitHub: [@ArthurrCavalcante](https://github.com/ArthurrCavalcante)
- LinkedIn: [arthur-victor-cavalcante](https://linkedin.com/in/arthur-victor-cavalcante)
- Email: cavalcate2.arthur@gmail.com
