package com.bridgelabz.controller;

import com.bridgelabz.service.EmployeePayrollService;

import java.text.ParseException;

public class EmployeepayrollMain {
    public static void main(String[] args) throws ParseException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        employeePayrollService.createTable();
        employeePayrollService.insertEmployee();
        employeePayrollService.readData();
    }
}
