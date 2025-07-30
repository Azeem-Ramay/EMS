package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDto {
    
    private Long id;
    
    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0")
    private BigDecimal salary;
    
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;
    
    @NotNull(message = "Department ID is required")
    private Long departmentId;
    
    private String departmentName;
    
    // Default constructor
    public EmployeeDto() {}
    
    // Constructor with fields
    public EmployeeDto(Long id, String name, String email, BigDecimal salary, LocalDate joiningDate, Long departmentId, String departmentName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public LocalDate getJoiningDate() {
        return joiningDate;
    }
    
    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }
    
    public Long getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
} 