# Job Queue (Java 17, Javalin, Concurrency)

A minimal thread-safe job queue with a REST API for enqueue/dequeue and status.

## Tech
Java 17, Maven, Javalin, JUnit 5

## Run
```bash
mvn -q -DskipTests package
java -jar target/job-queue-1.0.0.jar
```
API: `POST /jobs` body: `{ "payload": "..." }`, `POST /jobs/take`, `GET /jobs/{id}`
