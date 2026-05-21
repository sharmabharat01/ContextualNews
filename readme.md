````markdown
# Contextual News Retrieval System

## Tech Stack

* Java 26
* Spring Boot 3.5
* PostgreSQL
* Spring Data JPA
* Jackson Streaming Parser
* WebClient
* OpenRouter LLM API
* Maven

---

# Features Implemented

* News ingestion from JSON dataset
* Streaming JSON parsing for memory-efficient large file processing
* Batch database persistence with flush/clear optimization
* Async LLM enrichment pipeline
* Dedicated thread pool for background enrichment
* Enrichment status tracking (`PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`)
* Contextual search API
* Source-based filtering
* Relevance score filtering
* Pagination support
* Database indexing for optimized querying
* Audit fields (`createdAt`, `updatedAt`)
* Structured API responses
* Validation support
* Production-style layered architecture

---

# Project Setup

## 1. Clone Project

```bash
git clone <repo-url>
cd contextual-news
````

---

# 2. Create PostgreSQL Database

Login to PostgreSQL:

```sql
CREATE DATABASE newsdb;
```

---

# 3. Add Dataset File

Place the dataset JSON file at:

```text
src/main/resources/data/news.json
```

The application automatically loads this file during startup.

You can replace `news.json` with any valid dataset following the same schema.

---

# 4. Update application.yml

File:

```text
src/main/resources/application.yml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/newsdb
    username: root
    password: root

  jpa:
    hibernate:
      ddl-auto: update

    show-sql: true

    properties:
      hibernate:
        format_sql: true

server:
  port: 8080

logging:
  level:
    root: INFO
    com.news: DEBUG

openai:
  api-key: YOUR_OPENROUTER_API_KEY
  model: meta-llama/llama-3-8b-instruct:free
  base-url: https://openrouter.ai/api/v1
```

---

# 5. Generate OpenRouter API Key

* Create account at:

```text
https://openrouter.ai
```

* Generate API key

* Replace:

```yaml
openai:
  api-key: YOUR_OPENROUTER_API_KEY
```

---

# 6. Run Application

Using Maven:

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

---

# Data Loading Flow

On startup:

1. JSON dataset is streamed using Jackson parser
2. Articles are processed in batches
3. Batches are persisted to PostgreSQL
4. Async LLM enrichment tasks are triggered
5. Summaries are updated in background threads

This avoids loading the full dataset into memory.

---

# Batch Processing Details

The ingestion pipeline uses:

* Streaming JSON parsing
* Configurable batch persistence
* Hibernate flush/clear optimization

Benefits:

* Low memory footprint
* Efficient large dataset processing
* Reduced Hibernate persistence context overhead

---

# Async Processing Details

LLM enrichment runs asynchronously using a dedicated thread pool.

Configuration:

* Core Threads: 5
* Max Threads: 10
* Queue Capacity: 500

Benefits:

* Non-blocking startup
* Parallel enrichment processing
* Controlled API request throughput
* Isolation from request-handling threads

---

# APIs

Base URL:

```text
http://localhost:8080/api/v1/news
```

---

## Search News

```bash
curl --location 'http://localhost:8080/api/v1/news/search' \
--header 'Content-Type: application/json' \
--data '{
  "query": "Bangladesh",
  "page": 0,
  "size": 10
}'
```

---

## Get By Source

```bash
curl "http://localhost:8080/api/v1/news/source?source=BBC&page=0&size=10"
```

---

## Get By Score

```bash
curl "http://localhost:8080/api/v1/news/score?minScore=0.5&page=0&size=10"
```

---

# Database Tables

## news_article

Stores:

* title
* description
* source name
* categories
* relevance score
* publication date
* latitude/longitude
* LLM summary
* enrichment status
* createdAt
* updatedAt

---

# Database Optimizations

Indexes added on:

* source_name
* relevance_score
* publication_date

These improve filtering and pagination performance.

---

# Geolocation Support

The system supports location-aware news storage using:

* Latitude
* Longitude

This enables future extensibility for:

* Nearby news retrieval
* Radius-based filtering
* Haversine distance calculations

---

# Important Notes

## First Startup

Initial startup may take time because:

* dataset ingestion occurs
* batch persistence executes
* async LLM enrichment starts
* multiple OpenRouter API calls are triggered

---

## Re-running Application

If database already contains data:

```text
News articles already loaded
```

will appear in logs.

---

# Sample Success Logs

```text
Loaded 2000 news articles successfully

LLM enrichment completed for article: article-id
```

---

# Production Improvements Considered

The project includes several production-oriented improvements:

* Async background enrichment
* Thread pool isolation
* Batch processing
* Streaming ingestion
* Database indexing
* Pagination
* Validation
* Structured logging
* Layered architecture
* DTO separation
* Audit timestamps

---

# Suggested Production Enhancements

Not implemented due to assignment scope:

* Redis caching
* Kafka event pipeline
* Retry mechanism for failed LLM calls
* Dead-letter queue for failed enrichments
* Rate limiting
* Circuit breaker
* Observability/metrics
* Dockerization
* Kubernetes deployment
* Elasticsearch integration
* Distributed tracing

---

# Build Project

```bash
mvn clean install
```

---

# Run Tests

```bash
mvn test
```