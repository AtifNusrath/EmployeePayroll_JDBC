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
    private PreparedStatement employeePayrollDataStatement;

    private static EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null) {
            employeePayrollDBService = new EmployeePayrollDBService();
        }
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll?useSSL=false";
        String userName="root";
        String password="Mohammed@22";
        Connection connection;
        connection = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println(connection+" connection succesful");
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String query="SELECT * from payroll_service;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }


    public int updateEmployeeData(String name, double salary) {
        return this.updateDataUsingStatement(name, salary);
    }

    private int updateDataUsingStatement(String name, double salary) {
        String query = String.format("update payroll_service set salary = %.2f where name= '%s';", salary, name);
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList=null;
        if (this.employeePayrollDataStatement == null) {
            this.prepareStatementForEmployeeData();
        }
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
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

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql="SELECT * FROM payroll_service WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> getEmployeePayrollDataByDataRange(LocalDate startDate, LocalDate endDate) {
        String query = String.format("select * from payroll_service where start BETWEEN CAST('%s' as DATE) and CAST('%s' as DATE);", startDate.toString(), endDate.toString());
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

}
