# ========================================================
# Dockerfile for Employee Management System (EMS)
# Multi-stage lightweight build
# ========================================================

# Stage 1: Compile and package
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY . .

RUN mkdir -p bin && \
    javac -encoding UTF-8 -cp "lib/mysql-connector-java.jar" -d bin \
    src/com/ems/*.java \
    src/com/ems/model/*.java \
    src/com/ems/dao/*.java \
    src/com/ems/dao/jdbc/*.java \
    src/com/ems/dao/memory/*.java \
    src/com/ems/service/*.java \
    src/com/ems/util/*.java \
    src/com/ems/ui/*.java && \
    echo "Manifest-Version: 1.0" > manifest.txt && \
    echo "Main-Class: com.ems.Main" >> manifest.txt && \
    echo "Class-Path: lib/mysql-connector-java.jar" >> manifest.txt && \
    jar cfm EmployeeManagementSystem.jar manifest.txt -C bin .

# Stage 2: Production JRE runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/EmployeeManagementSystem.jar .
COPY --from=builder /app/lib/ ./lib/
COPY --from=builder /app/db.properties .

ENTRYPOINT ["java", "-jar", "EmployeeManagementSystem.jar"]

