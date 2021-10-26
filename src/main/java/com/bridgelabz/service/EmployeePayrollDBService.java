package com.bridgelabz.service;

import com.bridgelabz.model.EmployeePayrollData;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollDBService {
    public Connection connection = null;
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
        connection = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println(connection+" connection successful");
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String query="SELECT * from payroll_service;";
        return this.getEmployeePayrollDataUsingDB(query);
    }

    public int updateEmployeeData(String name, double salary) throws SQLException {
        return this.updateDataUsingStatement(name, salary);
    }

    private int updateDataUsingStatement(String name, double salary) throws SQLException {
        String query = String.format("update payroll_service set salary = %.2f where name= '%s';", salary, name);
        try {
            connection = this.getConnection();
            statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch(SQLException e) {
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
            connection = this.getConnection();
            String sql="SELECT * FROM payroll_service WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
        String query = String.format("SELECT * FROM payroll_service WHERE START BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(query);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String query) {
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

    public Map<String, Double> getAverageSalaryByGender() {
        String query = "SELECT gender,AVG(salary) as avg_salary from payroll_service group by gender;";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try  {
            connection = this.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double avg_salary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender, avg_salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        int employeeId=-1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format("INSERT INTO payroll_service (name,gender,salary,start)" +
                "VALUES ('%s','%s','%s','%s')", name, gender, salary, Date.valueOf(startDate));
        try  {
            connection = this.getConnection();
            statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected==1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeId = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollData;
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
    public double performVariousOperationsOf(String average, String gender) {
        String query = String.format("select %s(salary),gender from payroll_service where gender = '%s' group by gender;",
                average, gender);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
