package com.bridgelabz;

import com.bridgelabz.model.EmployeePayrollData;
import com.bridgelabz.service.EmployeePayrollService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.service.EmployeePayrollService.IOService.DB_IO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeePayrollServiceTest {
    @Test
    public void givenEmployeePayrollInDB_whenRetrieved_shouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
        assertEquals(7, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws SQLException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("dravid",6000000.00);
        boolean result = employeePayrollService.checkEmployeeInSyncWithDB("dravid");
        assertTrue(result);
    }

    @Test
    public void givenDateRange_whenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData=
                employeePayrollService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        assertEquals(6,employeePayrollData.size());
    }

    @Test
    public void givenPayRollData_WhenAverageSalaryRetrievdByGender_ShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        System.out.println(averageSalaryByGender.get("M"));
        assertTrue(averageSalaryByGender.get("M").equals(3800000.00));
    }

    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.addEmployeePayroll("Atif", 5000000.00, LocalDate.now(), "M");
        boolean result = employeePayrollService.checkEmployeeInSyncWithDB("Atif");
        assertTrue(result);
    }


    @Test
    public void givenEmployeePayrollData_ShouldReturnTheAverageOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("AVG", "M");
        assertEquals(employeePayrollData1, 3000000.0, 0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheSumOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("SUM", "M");
        assertEquals(employeePayrollData1, 12000000.0, 0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheMaxOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("MAX", "M");
        assertEquals(employeePayrollData1, 6000000.0, 0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheMinOfTheSalariesOfGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("MIN", "M");
        assertEquals(employeePayrollData1, 1000000.0, 0.0);
    }

    @Test
    public void givenEmployeePayrollData_ShouldReturnTheCountOfTheEmployeeByGender() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        double employeePayrollData1 = employeePayrollService.performVariousOperations("COUNT", "M");
        assertEquals(employeePayrollData1, 4, 0.0);
    }

}
