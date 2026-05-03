
# SmartDoc – AI-Powered Document Intelligence Platform

SmartDoc is a production-grade RAG (Retrieval-Augmented Generation) application 
built with Spring Boot that allows users to upload PDF documents and ask 
questions about them using AI — getting accurate, context-aware answers.

## Features
- Upload PDF documents and process them instantly
- Ask natural language questions about your documents
- AI-powered answers using Llama via Spring AI
- Semantic search using Chroma vector database
- 10x faster ingestion using concurrent PDF processing with ExecutorService
- Fully containerized with Docker

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Backend | Java, Spring Boot, Spring AI |
| AI Model | Llama (Local) |
| Vector DB | Chroma |
| Database | PostgreSQL |
| DevOps | Docker, Docker Compose |
| Build | Maven |

## How It Works
```
User uploads PDF
      ↓
PDF parsed and split into chunks
      ↓
Chunks converted to vector embeddings
      ↓
Stored in Chroma vector database
      ↓
User asks a question
      ↓
Semantic similarity search → top chunks retrieved
      ↓
Chunks + question sent to Llama
      ↓
Accurate answer returned
```

## Getting Started

### Prerequisites
- Docker & Docker Compose
- Java 17+
- Maven

### Run with Docker
```bash
git clone https://github.com/nitishshukla121/SmartDoc.git
cd SmartDoc
cp .env.example .env
# Add your config in .env
docker compose up --build
```

### Run Locally
```bash
mvn spring-boot:run
```

## Environment Variables
Copy `.env.example` to `.env` and fill in:
```
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
```

## Author
**Nitish Shukla** — [GitHub](https://github.com/nitishshukla121) 
| [LinkedIn](https://linkedin.com/in/nitish121shukla)
```
