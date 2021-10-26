package com.bridgelabz;

import com.bridgelabz.model.EmployeePayrollData;
import com.bridgelabz.service.EmployeePayrollService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.bridgelabz.service.EmployeePayrollService.IOService.DB_IO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_whenRetrived_shouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        assertEquals(4, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("dravid", 6000000.00);
        boolean result = employeePayrollService.checkEmployeeInSyncWithDB("dravid");
        assertTrue(result);
    }

    @Test
    public void givenEmployeePayrollDB_AbilityToRetrievAllTheEmployees_JoinedInParticularDataRanga() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
        List<EmployeePayrollData> employeePayrollDataByGivenDataRange = employeePayrollService.getEmployeePayrollDataByGivenDataRange(LocalDate.of(2018, 01, 01), LocalDate.now());
        assertEquals(employeePayrollDataByGivenDataRange.get(0),employeePayrollData.get(0));
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheAverageOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("AVG", "M");
        assertEquals(employeePayrollData1,3000000.0,0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheSumOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("SUM", "M");
        assertEquals(employeePayrollData1,12000000.0,0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheMaxOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("MAX", "M");
        assertEquals(employeePayrollData1,6000000.0,0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheMinOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("MIN", "M");
        assertEquals(employeePayrollData1,1000000.0,0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheCountOfTheEmployeeByGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("COUNT", "M");
        assertEquals(employeePayrollData1,4,0.0);
    }
}
