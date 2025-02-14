# Use OpenJDK 17 as the base image
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy Java application file
COPY JdbcCrudApp.java .

# Copy PostgreSQL JDBC driver
COPY postgresql-42.7.4.jar .

# Compile the Java application with the JDBC driver
RUN javac -cp postgresql-42.7.4.jar JdbcCrudApp.java

# Run the Java application with PostgreSQL JDBC driver in classpath
CMD ["java", "-cp", ".:postgresql-42.7.4.jar", "JdbcCrudApp"]
