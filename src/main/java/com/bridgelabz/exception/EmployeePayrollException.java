package com.bridgelabz.exception;

import com.bridgelabz.model.EmployeePayrollData;
import com.bridgelabz.service.EmployeePayrollService;

import java.util.List;

public class EmployeePayrollException extends Throwable {
    public enum IOService{CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
    private List<EmployeePayrollData> employeePayrollList;

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioservice) {
        if (ioservice.equals(IOService.DB_IO)){
            this.employeePayrollList= new EmployeePayrollService().readData();
        }
        return employeePayrollList;
    }
}
