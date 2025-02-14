# JDBC Docker Setup and Deployment Guide

## Overview
This guide provides step-by-step instructions to set up a PostgreSQL database and a Java-based JDBC application inside Docker containers, manage them, and push the images to Docker Hub.

---

## 1. Create a Docker Network
To allow communication between containers, create a dedicated network:
```sh
docker network create jdbc-network
```

---

## 2. Set Up PostgreSQL Container
Run the following command to create a PostgreSQL container:
```sh
docker run -d --name postgres-container --network=jdbc-network \
    -e POSTGRES_USER=myuser \
    -e POSTGRES_PASSWORD=mypassword \
    -e POSTGRES_DB=mydatabase \
    -p 5432:5432 postgres
```

---

## 3. Build and Run the JDBC Application Container
Navigate to the Java project directory and build the Docker image:
```sh
docker build -t jdbc-app .
```
Run the container:
```sh
docker run -it --name jdbc-container --network=jdbc-network jdbc-app
```

> **Note:** Run the `docker build` command only if changes have been made to the Java code.

---

## 4. Manage Containers

### Start the JDBC Application Again
```sh
docker start -ai jdbc-container
```

### Remove the JDBC Container After Use
```sh
docker rm jdbc-container
```

### Stop the JDBC Container
```sh
docker stop jdbc-container
```

### Stop the PostgreSQL Container
```sh
docker stop postgres-container
```

### Start the PostgreSQL Container
```sh
docker start postgres-container
```

---

## 5. Access the PostgreSQL Database
To interact with the database inside the PostgreSQL container:
```sh
docker exec -it postgres-container psql -U myuser -d mydatabase
```

### Common PostgreSQL Commands
#### View all tables
```sql
\dt;
```
#### Display all records in the `users` table
```sql
SELECT * FROM users;
```
#### Insert a new record
```sql
INSERT INTO users (name) VALUES ('Alice');
```
#### Update a record
```sql
UPDATE users SET name = 'Alice Johnson' WHERE id = 2;
```
#### Delete a record
```sql
DELETE FROM users WHERE id = 3;
```
#### Exit PostgreSQL
```sql
\q
```
#### Exit the container
```sh
exit
```

---

## 6. Push Docker Images to Docker Hub
### Verify Existing Images
```sh
docker images
```

### Tag Images for Docker Hub
```sh
docker tag postgres jonathandabrewissen/empdb
docker tag jdbc-app jonathandabrewissen/empcode
```

### Push Images to Docker Hub
```sh
docker push jonathandabrewissen/empdb
docker push jonathandabrewissen/empcode
```

### Pull Images from Docker Hub (Verification)
```sh
docker pull jonathandabrewissen/empdb
docker pull jonathandabrewissen/empcode
```

---

## Conclusion
This guide outlines the steps to set up and manage JDBC and PostgreSQL containers in Docker, interact with the database, and push images to Docker Hub. Happy coding! 🚀

