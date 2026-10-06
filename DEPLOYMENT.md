# 🚀 Production Deployment Guide: Employee Management System (EMS)

This guide covers all deployment strategies supported by the **Employee Management System**, from standalone executable JAR bundles to multi-container Docker environments and CI/CD pipelines.

---

## 📦 Strategy 1: Standalone Executable JAR Deployment (Zero Setup)

The application includes an automated packaging pipeline that bundles compiled classes and configurations into an executable JAR.

### How to Package:
Run [`package.bat`](file:///C:/Users/mohda/OneDrive/Desktop/Engineers%20Project/Employee%20Management%20System/package.bat):
```powershell
.\package.bat
```

### Resulting Deployment Artifact (`dist/` folder):
```
dist/
├── EmployeeManagementSystem.jar    # Self-contained runnable JAR with Main-Class manifest
├── db.properties                   # Configurable database credentials
├── lib/
│   └── mysql-connector-java.jar    # Bundled JDBC driver
├── run.bat                         # 1-Click launcher for Windows
└── run.sh                          # 1-Click launcher for Linux / macOS
```

### How to Run Anywhere:
```bash
java -jar EmployeeManagementSystem.jar
```
*(Can be distributed as a single `.zip` file and executed on any system with Java installed).*

---

## 🐳 Strategy 2: Containerized Deployment (Docker & Docker Compose)

The project includes production-ready Docker configurations with a multi-stage Dockerfile and health-checked MySQL 8 service.

### Components:
- **`Dockerfile`**: Multi-stage build (compiles source with JDK 21 Alpine, then packages into a minimal JRE 21 Alpine image).
- **`docker-compose.yml`**: Defines two isolated network services:
  1. `mysql`: Official MySQL 8.0 image with auto-initialization (`schema.sql` mounted into `/docker-entrypoint-initdb.d/`) and automated health checks.
  2. `app`: The EMS Java container configured via 12-factor environment variables (`DB_URL`, `DB_USER`, `DB_PASSWORD`).

### How to Deploy with Docker:
1. Start Docker Desktop.
2. In the project folder, run:
```bash
docker compose up -d mysql
```
3. Once MySQL is healthy, launch the interactive EMS application:
```bash
docker compose run --rm app
```
*(Or on Windows, simply double-click [`docker-run.bat`](file:///C:/Users/mohda/OneDrive/Desktop/Engineers%20Project/Employee%20Management%20System/docker-run.bat)).*

---

## 🔄 Strategy 3: CI/CD Pipeline (GitHub Actions)

Located at [`.github/workflows/deploy.yml`](file:///C:/Users/mohda/OneDrive/Desktop/Engineers%20Project/Employee%20Management%20System/.github/workflows/deploy.yml):
- Automatically triggers on every `git push` or `pull_request` to `main`.
- Provisions OpenJDK 21 on Ubuntu runners.
- Compiles the entire codebase and verifies syntax and dependencies.
- Packages `EmployeeManagementSystem.jar` with production manifest.
- Publishes the `dist/` folder as a downloadable release artifact.

---

## 🎯 How to Explain Deployment to Recruiters

> **Recruiter Question:** *"How did you package and deploy this application?"*  
> **Your Answer:**  
> "I designed a multi-tier deployment strategy:  
> 1. **Packaging:** I created an automated build script that compiles the source code and packages it into an **executable Fat JAR** with embedded manifest metadata linking the MySQL JDBC driver.  
> 2. **Containerization:** For cloud readiness, I containerized the system using **Docker** with a multi-stage build to keep the image footprint minimal. I used **Docker Compose** to orchestrate both the Java application and MySQL 8 service, utilizing Docker health checks so the application only boots once MySQL is ready and schema is migrated.  
> 3. **Config Management:** I followed **12-Factor App** principles—database credentials can be passed seamlessly via environment variables or loaded from `db.properties`.  
> 4. **CI/CD:** I configured a **GitHub Actions** workflow that builds and generates a release bundle on every commit."

