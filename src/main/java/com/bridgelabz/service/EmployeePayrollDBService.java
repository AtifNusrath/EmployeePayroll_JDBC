package com.bridgelabz.service;

import com.bridgelabz.model.EmployeePayrollData;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollDBService {
    Connection connection = null;
    Statement statement = null;
    PreparedStatement insertStmt;
    Scanner scanner = new Scanner(System.in);

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll?useSSL=false";
        String userName = "root";
        String password = "Mohammed@22";
        Connection connection;
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println(connection + " connection successful");
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
            insertStmt.setDouble(2, scanner.nextDouble());
            System.out.println("Enter the Employee start date[dd-MM-yyyy]: ");
            insertStmt.setDate(3, new Date(dateFormat.parse(scanner.next()).getTime()));
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> readData() {
        String query = "SELECT * from payroll_service;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            connection = this.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public void updateDataUsingStatement() {
        try {
            String query = "update payroll_service set salary = ? where name= ?;";
            connection = this.getConnection();
            statement = connection.createStatement();
            insertStmt = connection.prepareStatement(query);
            System.out.println("Enter the Employee Name: ");
            insertStmt.setString(1, scanner.next());
            System.out.println("Enter the Employee Update Salary: ");
            insertStmt.setDouble(2, scanner.nextDouble());
            insertStmt.executeUpdate();
            System.out.println("Record Updated\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
               System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getDouble(3) + "\t" + resultSet.getDate(4));
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
              employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public void prepareStatementForEmployeeData() {
        try {
            connection = this.getConnection();
            String sql="SELECT * FROM payroll_service WHERE name = ?";
            insertStmt = connection.prepareStatement(sql);
            System.out.println("Enter the Employee Name: ");
            insertStmt.setString(1, scanner.next());
            ResultSet resultSet = insertStmt.executeQuery();
            this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
