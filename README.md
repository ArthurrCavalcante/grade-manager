# Grade Manager API

API REST para gerenciamento de notas acadêmicas, desenvolvida com Spring Boot 3 como projeto de portfólio.

## O que o projeto faz

Permite cadastrar alunos, disciplinas e notas, com relatório de desempenho por aluno (média, aprovações e reprovações). Inclui validações de negócio, tratamento de erros padronizado e dados de demonstração carregados automaticamente ao iniciar.

## Tecnologias

- Java 21 + Spring Boot 3.2
- Spring Data JPA + H2 (banco em memória)
- Bean Validation + Lombok
- JUnit 5 + Mockito + MockMvc

## Como rodar

Você precisa ter o Java 21 e o Maven instalados.

```bash
git clone https://github.com/ArthurrCavalcante/grade-manager.git
cd grade-manager
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

Se quiser visualizar o banco de dados, acesse `http://localhost:8080/h2-console` com a URL `jdbc:h2:mem:gradedb` e usuário `sa` (sem senha).

## Endpoints principais

| Método | Rota | O que faz |
|--------|------|-----------|
| GET | `/api/v1/students` | Lista todos os alunos |
| POST | `/api/v1/students` | Cadastra um aluno |
| GET | `/api/v1/courses` | Lista todas as disciplinas |
| POST | `/api/v1/courses` | Cadastra uma disciplina |
| GET | `/api/v1/grades` | Lista todas as notas |
| POST | `/api/v1/grades` | Registra uma nota |
| GET | `/api/v1/grades/report/student/{id}` | Relatório de desempenho do aluno |

## Exemplo rápido

Cadastrar um aluno:
```http
POST /api/v1/students
Content-Type: application/json

{
  "name": "Arthur Cavalcante",
  "registration": "2024001",
  "email": "arthur@ifce.edu.br"
}
```

Relatório de um aluno:
```http
GET /api/v1/grades/report/student/1
```
```json
{
  "student": { "name": "Arthur Cavalcante", "registration": "2024001" },
  "grades": [
    { "courseName": "Algorithms & Data Structures", "grade": 9.5, "approved": true }
  ],
  "average": 9.5,
  "approvedCount": 1,
  "failedCount": 0
}
```

## Testes

```bash
./mvnw test
```

## Autor

Arthur Cavalcante — [LinkedIn](https://linkedin.com/in/arthur-victor-cavalcante) · [GitHub](https://github.com/ArthurrCavalcante)
