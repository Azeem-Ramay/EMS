package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class SalaryAdjustmentDto {
    
    @NotNull(message = "Department ID is required")
    private Long departmentId;
    
    @NotNull(message = "Performance score is required")
    @Min(value = 0, message = "Performance score must be at least 0")
    @Max(value = 100, message = "Performance score must be at most 100")
    private Integer performanceScore;
    
    // Default constructor
    public SalaryAdjustmentDto() {}
    
    // Constructor with fields
    public SalaryAdjustmentDto(Long departmentId, Integer performanceScore) {
        this.departmentId = departmentId;
        this.performanceScore = performanceScore;
    }
    
    // Getters and Setters
    public Long getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    
    public Integer getPerformanceScore() {
        return performanceScore;
    }
    
    public void setPerformanceScore(Integer performanceScore) {
        this.performanceScore = performanceScore;
    }
} 