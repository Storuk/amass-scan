# Amass Scan

## Overview
This project consists of a frontend (React/Node.js), a backend (Spring Boot), and a PostgreSQL database, all containerized using Docker. The application is orchestrated using Docker Compose to simplify deployment and management.

## Prerequisites
Ensure you have the following installed on your system:
- (Docker and Docker Compose) or (Docker Desktop)

## Project Structure
```
.
├── amass-be-app/            # Backend Service (Spring Boot)
│   ├── Dockerfile
│   ├── src/
│   ├── target/
│   └── pom.xml
│
├── amass-fe-app/        # Frontend Service (React)
│   ├── Dockerfile
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── package-lock.json
│
├── docker-compose.yml   # Docker Compose Configuration
└── README.md            # Documentation
```

## Setup and Installation

### 1. Clone the Repository
```sh
git clone https://github.com/Storuk/amass-scan.git
cd amass-app
```

### 2. Build and Run with Docker Compose
```sh
docker-compose up --build
```
This command will:
- Start a PostgreSQL database
- Build and launch the backend service (Spring Boot)
- Build and launch the frontend service (React with Nginx)

### 3. Access the Application
- **Frontend:** [http://localhost:5173](http://localhost:5173)
- **Backend API:** [http://localhost:8080](http://localhost:8080)
- **PostgreSQL Database:** Available at `localhost:5433` with credentials:
    - Username: `user`
    - Password: `password`
    - Database: `amass_db`

## Docker Configuration Details

### Backend (`testTask/Dockerfile`)
The backend runs on OpenJDK 17 and includes the OWASP Amass tool.
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y \
    unzip \
    wget

RUN wget https://github.com/OWASP/Amass/releases/download/v4.2.0/amass_linux_amd64.zip
RUN unzip amass_linux_amd64.zip
RUN mv amass_Linux_amd64/amass /usr/local/bin/
RUN rm -rf amass_linux_amd64.zip amass_Linux_amd64

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### Frontend (`amass-fe-app/Dockerfile`)
The frontend is built using Node.js 18 and served using Nginx.
```dockerfile
FROM node:18-alpine AS build

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install --frozen-lockfile
COPY . .
RUN npm run build

FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Docker Compose Configuration (`docker-compose.yml`)
```yaml
version: '3.7'

services:
  postgres:
    image: postgres:13
    container_name: postgres-db
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: amass_db
    ports:
      - "5433:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  be_app:
    build: ./testTask/.
    container_name: be-app
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/amass_db
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: password
    depends_on:
      - postgres
    ports:
      - "8080:8080"

  fe_app:
    build: ./amass-fe-app/.
    container_name: fe-app
    depends_on:
      - be_app
    ports:
      - "5173:80"

volumes:
  postgres_data:
```

## Stopping the Application
To stop all running containers, use:
```sh
docker-compose down
```

## Troubleshooting
- **Port Conflicts:** Ensure ports `5173`, `8080`, and `5433` are available. Modify `docker-compose.yml` if necessary.
- **Database Issues:** Run `docker-compose down -v` to remove database volumes and reset PostgreSQL data.