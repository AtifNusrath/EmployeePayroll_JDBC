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
    Scanner scanner = new Scanner(System.in);

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll?useSSL=false";
        String userName = "root";
        String password = "Mohammed@22";
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
            insertStmt.setDouble(2, scanner.nextDouble());
            System.out.println("Enter the Employee start date: ");
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
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id,name,salary,startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}
