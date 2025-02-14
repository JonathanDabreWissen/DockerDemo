import java.sql.*;
import java.util.Scanner;

public class JdbcCrudApp {
    private static final String URL = "jdbc:postgresql://postgres-container:5432/mydatabase";
    private static final String USER = "myuser";
    private static final String PASSWORD = "mypassword";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to PostgreSQL!");

            // Create table if not exists
            Statement stmt = conn.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(100))";
            stmt.executeUpdate(createTable);
            System.out.println("Table checked/created!");

            while (true) {
                // Menu options
                System.out.println("\nMENU:");
                System.out.println("1. Add User");
                System.out.println("2. Display All Users");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter user name: ");
                        String name = scanner.nextLine();
                        String insertSQL = "INSERT INTO users (name) VALUES (?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                            pstmt.setString(1, name);
                            pstmt.executeUpdate();
                            System.out.println("User added successfully!");
                        }
                        break;

                    case 2:
                        String selectSQL = "SELECT * FROM users";
                        try (Statement selectStmt = conn.createStatement();
                             ResultSet rs = selectStmt.executeQuery(selectSQL)) {
                            System.out.println("User List:");
                            while (rs.next()) {
                                System.out.println("ID: " + rs.getInt("id") + " - Name: " + rs.getString("name"));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter user ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();

                        String updateSQL = "UPDATE users SET name = ? WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                            pstmt.setString(1, newName);
                            pstmt.setInt(2, updateId);
                            int rowsUpdated = pstmt.executeUpdate();
                            if (rowsUpdated > 0) {
                                System.out.println("User updated successfully!");
                            } else {
                                System.out.println("User not found!");
                            }
                        }
                        break;

                    case 4:
                        System.out.print("Enter user ID to delete: ");
                        int deleteId = scanner.nextInt();

                        String deleteSQL = "DELETE FROM users WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                            pstmt.setInt(1, deleteId);
                            int rowsDeleted = pstmt.executeUpdate();
                            if (rowsDeleted > 0) {
                                System.out.println("User deleted successfully!");
                            } else {
                                System.out.println("User not found!");
                            }
                        }
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


/*
//First Create a network
docker network create jdbc-network

// Below command for creating postgres-container
docker run -d --name postgres-container --network=jdbc-network -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=mydatabase -p 5432:5432 postgres

//Go into java file directory
//Run the below commands only if any change was made
docker build -t jdbc-app .
docker run -it --name jdbc-container --network=jdbc-network jdbc-app

//Remove the code container after use 
docker rm jdbc-container

//Start the container again
docker start -ai jdbc-container

//jdbc Container automatically stops but if you want to stop it 
docker stop jdbc-container

//Stop Postgres-container
docker stop postgres-container

//Start Postgres-container
docker start postgres-container


//Get inside the postgres container
docker exec -it postgres-container psql -U myuser -d mydatabase

// View all tables inside the postgres mydatabase database
\dt;

//Display all data of the table
SELECT * FROM users;

//Insert new record
INSERT INTO users (name) VALUES ('Alice');

//Update record
UPDATE users SET name = 'Alice Johnson' WHERE id = 2;

//Delete record
DELETE FROM users WHERE id = 3;

//quit
\q

//Exit the container
exit

//Push to docker hub
docker images

docker tag postgres jonathandabrewissen/empdb
docker tag jdbc-app jonathandabrewissen/empcode

docker push jonathandabrewissen/empdb
docker push jonathandabrewissen/empcode

docker pull jonathandabrewissen/empdb
docker pull jonathandabrewissen/empcode





 */