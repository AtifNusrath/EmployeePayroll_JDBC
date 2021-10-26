package com.bridgelabz.controller;

import com.bridgelabz.service.EmployeePayrollDBService;
import com.bridgelabz.service.EmployeePayrollService;

import java.text.ParseException;

public class EmployeepayrollMain {
    public static void main(String[] args) throws ParseException {
        EmployeePayrollDBService employeePayrollService=new EmployeePayrollDBService();
        employeePayrollService.createTable();
        employeePayrollService.insertEmployee();
        employeePayrollService.readData();
    }
}
