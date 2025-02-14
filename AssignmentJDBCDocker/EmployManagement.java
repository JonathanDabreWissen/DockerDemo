
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import java.sql.*;

interface EmpDAO {

   public void createRecord();
   public void displayAllRecords();
}
class EmployeeRecord {
    public static void createRecord(String name, int age, int salary, String designation, String department) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/demodb", "postgres", "tiger");
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO employee(name, age, salary, designation, department) VALUES(?, ?, ?, ?, ?)");

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, salary);
            pstmt.setString(4, designation);
            pstmt.setString(5, department);
            pstmt.execute();

            pstmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void displayAllRecords() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/demodb", "postgres", "tiger");
            
            // Use RowSet instead of ResultSet
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.setCommand("SELECT * FROM employee");
            rowSet.execute(con);
            

            while (rowSet.next()) {
                System.out.println("EmpID: " + rowSet.getInt(1));
                System.out.println("Name: " + rowSet.getString(2));
                System.out.println("Age: " + rowSet.getInt(3));
                System.out.println("Salary: " + rowSet.getInt(4));
                System.out.println("Designation: " + rowSet.getString(5));
                System.out.println("Department: " + rowSet.getString(6));
                System.out.println();
            }

            rowSet.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void displayRecord(int empId) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/demodb", "postgres", "tiger");
            
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.setCommand("SELECT * FROM employee WHERE eid = ?");
            rowSet.setInt(1, empId);
            rowSet.execute(con);

            if (rowSet.next()) {
                System.out.println("EmpID: " + rowSet.getInt(1));
                System.out.println("Name: " + rowSet.getString(2));
                System.out.println("Age: " + rowSet.getInt(3));
                System.out.println("Salary: " + rowSet.getInt(4));
                System.out.println("Designation: " + rowSet.getString(5));
                System.out.println("Department: " + rowSet.getString(6));
                System.out.println();
            } else {
                System.out.println("Employee not found.");
            }

            rowSet.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateSalary(int eid, int newSalary) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/demodb", "postgres", "tiger");

            PreparedStatement pstmt = con.prepareStatement("UPDATE employee SET salary = ? WHERE eid = ?");
            pstmt.setInt(1, newSalary);
            pstmt.setInt(2, eid);
            pstmt.execute();

            pstmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteRecord(int eid) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/demodb", "postgres", "tiger");

            PreparedStatement pstmt = con.prepareStatement("DELETE FROM employee WHERE eid = ?");
            pstmt.setInt(1, eid);
            pstmt.execute();

            pstmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
abstract class Employ{
    int empId = 0;
    String name;
    String designation;
    String department;
    int salary;
    int age;

    static int employCount = 1;

    public void display() {
        System.out.println("\nHere are the employ details");
        System.out.println("Employ ID: " + empId);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Designation: " + designation);
        System.out.println("Salary: " + salary);
        System.out.println();
    }

    public static boolean validAge(int age) {

        if (age < 21 || age > 60) {
            return false;
        }
        return true;
    }

    public static boolean validName(String name) {
        String nameRegex = "^[A-Z][a-z]* [A-Z][a-z]*$";

        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(name);
        if (nameMatcher.find()) {
            return true;
        }

        return false;
    }

    Employ(String name, int age, String designation, int salary) {
        
        employCount++;
        this.name = name;
        this.age = age;
        this.designation = designation;
        this.salary = salary;
        this.department = "IT";

        EmployeeRecord.createRecord(name, age, salary, designation, "IT");
        System.out.println("\nCreated Employ Successfully");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Employ ID: " + empId);
        System.out.println("Designation: " + designation);
        System.out.println("Base Salary: " + salary);
        System.out.println();

    }

    abstract void raiseSalary(int empId);

}
final class Clerk extends Employ {

    Clerk(String name, int age) {
        super( name, age, "Clerk", 25000);
    }

    Clerk(String name, int age, int salary){
        super(name, age, "Clerk", salary);

    }

    void raiseSalary(int empId) {
        System.out.println("\nRaised salary of " + this.name + " ,Employ ID: " + this.empId);

        this.salary = this.salary + 1000;
        EmployeeRecord.updateSalary(this.empId, this.salary);
        System.out.println("Salary now: " + this.salary);

    }
}
final class Programmer extends Employ {

    Programmer(String name, int age) {
        super(name, age, "Programmer", 50000);
    }
    Programmer( String name, int age, int salary){
        super( name, age, "Programmer", salary);
    }

    void raiseSalary(int empId) {
        System.out.println("\nRaised salary of " + this.name + " ,Employ ID: " + this.empId);

        this.salary = this.salary + 2000;
        EmployeeRecord.updateSalary(this.empId, this.salary);
        System.out.println("Salary now: " + this.salary);

    }

}
final class Manager extends Employ {

    Manager(String name, int age) {
        super(name, age, "Manager", 100000);
    }

    Manager(String name, int age, int salary){
        super(name, age, "Manager", salary);
    }

    void raiseSalary(int empId) {
        System.out.println("\nRaised salary of " + this.name + " ,Employ ID: " + this.empId);

        this.salary = this.salary + 10000;
        EmployeeRecord.updateSalary(this.empId, this.salary);
        System.out.println("Salary now: " + this.salary);

    }
}

public class EmployManagement {


    public static void main(String[] args) {
        
        int choice=0;

        do {

            Scanner scanner = new Scanner(System.in);


            System.out.println("----------------------------------");
            System.out.println("\nMenu");
            System.out.println("1. Create Employ");
            System.out.println("2. Display Employs");
            System.out.println("3. Appraisal");
            System.out.println("4. Search Employ");
            System.out.println("5. Remove Employ");
            System.out.println("6. Exit");

            System.out.print("\nEnter your choice: ");

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format");
                scanner.nextLine();
                continue;
            }
            
            System.out.println("----------------------------------");


            switch (choice) {
                case 1-> {
                        // create employ
                        System.out.println("1. Clerk \n2. Programmer \n3. Manager \n4. Others \n5. Exit");
                        System.out.print("Enter your choice of Employ: ");
                        int empChoice;
                        try {
                            empChoice = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input format");
                            scanner.nextLine();
                            continue;
                        }

                        scanner.nextLine(); 

                        String empName;
                        int age;

                        System.out.print("Enter name: ");
                        empName = scanner.nextLine();

                        System.out.print("Enter age: ");
                        age = scanner.nextInt();

                        if (empChoice == 1) {
                            new Clerk(empName, age);
                        } else if (empChoice == 2) {
                            new Programmer(empName, age);
                        } else if (empChoice == 3) {
                            new Manager(empName, age);
                        } else if( empChoice == 4){
                            System.out.println("currently not available.");
                        }
                        else {
                            System.out.println("Check your choice.");
                        }

                    }
                
                case 2-> {
                        //Display
                        EmployeeRecord.displayAllRecords();
                    }

                case 3->{
                        //appraisal
                        System.out.print("Enter Id: ");
                        int empId = scanner.nextInt();
                        EmployeeRecord.displayRecord(empId);

                        System.out.println("Above are the employee details");

                        System.out.print("Enter new salary: ");
                        int newSalary = scanner.nextInt();

                        EmployeeRecord.updateSalary(empId, newSalary);
                        System.out.println("Salary Updated Successfully!!");
                        EmployeeRecord.displayRecord(empId);

                        System.out.println("");
                    }   
                    
                case 4 ->{
                        //search
                        System.out.print("Enter Id: ");
                        int empId = scanner.nextInt();
                        
                        EmployeeRecord.displayRecord(empId);
                    }
                
                case 5->{
                        //Remove
                        System.out.print("Enter Id: ");
                        int empId = scanner.nextInt();
                        EmployeeRecord.displayRecord(empId);

                        System.out.println("Above are the employee details, are you sure you want to delete? ");

                        System.out.print("Enter Yes/No: ");
                        scanner.nextLine();
                        String confirmation = scanner.nextLine();

                        if(confirmation.equalsIgnoreCase("Yes")){
                            EmployeeRecord.deleteRecord(empId);
                            System.out.println("Deleted Successfully!!");
                        }else{
                            continue;
                        }

                    }

                case 6->{
                        scanner.close();
                        System.out.println("Exiting the code");
                    }
                default ->{
                        System.out.println("Check your choice");
                    }
                    
            }
            
        } while (choice !=6);
    }
    
}

