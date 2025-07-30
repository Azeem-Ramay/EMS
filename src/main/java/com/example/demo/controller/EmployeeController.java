package com.example.demo.controller;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.SalaryAdjustmentDto;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        List<EmployeeDto> employees = employeeService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(employees);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, 
                                                   @Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/adjust-salary")
    public ResponseEntity<String> adjustSalary(@Valid @RequestBody SalaryAdjustmentDto adjustmentDto) {
        employeeService.adjustSalary(adjustmentDto);
        return ResponseEntity.ok("Salary adjustment completed successfully");
    }
    
    @GetMapping("/salary-adjustments")
    public ResponseEntity<List<SalaryAdjustmentDto>> getSalaryAdjustmentHistory() {
        List<SalaryAdjustmentDto> adjustments = employeeService.getSalaryAdjustmentHistory();
        return ResponseEntity.ok(adjustments);
    }
    
    @GetMapping("/salary-adjustments/stats")
    public ResponseEntity<Map<String, Object>> getSalaryAdjustmentStats() {
        Map<String, Object> stats = employeeService.getSalaryAdjustmentStats();
        return ResponseEntity.ok(stats);
    }
} 