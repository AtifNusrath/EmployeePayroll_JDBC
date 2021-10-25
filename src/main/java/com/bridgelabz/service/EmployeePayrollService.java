package com.bridgelabz.service;

import com.bridgelabz.exception.EmployeePayrollException;
import com.bridgelabz.model.EmployeePayrollData;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
    Connection connection = null;
    Statement statement = null;
    Scanner scanner = new Scanner(System.in);

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll?useSSL=false";
        String userName="root";
        String password="Mohammed@22";
        Connection connection;
        connection = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println(connection+" connection successful");
        return connection;
    }

    public void createTable() {
        try {
            connection = this.getConnection();
            statement = connection.createStatement();
            String query = "create table payroll_service(id int not null AUTO_INCREMENT,"
                    + "name varchar(50)not null,"
                    + "salary double not null,"
                    + "start date not null,"
                    + "primary key(id)"
                    + ");";
            statement.executeUpdate(query);
            System.out.println("Table created");
            connection.close();
        } catch (Exception e) {
            System.out.println("Table already exists");
        }
    }

    public void insertEmployee() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String insertQuery = "insert into payroll_service(name,salary,start) values (?,?,?)";
        try {
            connection = this.getConnection();
            statement = connection.createStatement();
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            System.out.println("Enter the Employee Name: ");
            insertStmt.setString(1, scanner.next());
            System.out.println("Enter the Employee Salary: ");
            insertStmt.setDouble(2,scanner.nextDouble());
            System.out.println("Enter the Employee start date: ");
//            Loca start = "12-08-2021";
  //        LocalDate startDate = ;
            insertStmt.setDate(3, new Date(dateFormat.parse(scanner.next()).getTime()));
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> readData() {
        String query="SELECT * from payroll_service;";
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            connection = this.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getDouble(3) + "\t" + resultSet.getDate(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public void updateDataUsingStatement() {
        System.out.println("Enter the Employee Name: ");
        String name = scanner.next();
        System.out.println("Enter Employee Updated Salary: ");
        double salary = scanner.nextDouble();
        String query = String.format("update payroll_service set salary = %.2f where name= '%s';", salary, name);
        try {
            connection = this.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created");
        }catch (SQLException e) {
            e.printStackTrace();        }
    }

//    public static void main(String[] args) {
//EmployeePayrollService emp = new EmployeePayrollService();
////        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:/Volumes/Production/Courses/Programs/JavaPrograms/TestDB/testjava.db");
////            Statement statement = conn.createStatement()) {
////            statement.execute("CREATE TABLE contacts (name TEXT, phone INTEGER, email TEXT)");
//        try {
//            Connection conn = emp.getConnection();
////            conn.setAutoCommit(false);
//            Statement statement = conn.createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS contacts " +
//                    " (name TEXT, phone INTEGER, email TEXT)");
//           statement.execute("INSERT INTO contacts (name, phone, email) " +
//                   "VALUES('Joe', 45632, 'joe@anywhere.com')");
////
////            statement.execute("INSERT INTO contacts (name, phone, email) " +
////                    "VALUES('Jane', 4829484, 'jane@somewhere.com')");
////
////            statement.execute("INSERT INTO contacts (name, phone, email) " +
////                    "VALUES('Fido', 9038, 'dog@email.com')");
////
//            statement.execute("SELECT * FROM contacts");
//            ResultSet results = statement.getResultSet();
//            while(results.next()) {
//                System.out.println(results.getString("name") + " " +
//                        results.getInt("phone") + " " +
//                        results.getString("email"));
//            }
//
//            results.close();
//
//            statement.close();
//            conn.close();
//
////            Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\databases\\testjava.db");
////            Class.forName("org.sql.JDBC");
//
//        } catch (SQLException e) {
//            System.out.println("Something went wrong: " + e.getMessage());
//        }
//    }

}
