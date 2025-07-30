package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_adjustments")
public class SalaryAdjustment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "department_id", nullable = false)
    private Long departmentId;
    
    @Column(name = "performance_score", nullable = false)
    private Integer performanceScore;
    
    @Column(name = "adjustment_date", nullable = false)
    private LocalDateTime adjustmentDate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Default constructor
    public SalaryAdjustment() {}
    
    // Constructor with fields
    public SalaryAdjustment(Long departmentId, Integer performanceScore) {
        this.departmentId = departmentId;
        this.performanceScore = performanceScore;
        this.adjustmentDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public LocalDateTime getAdjustmentDate() {
        return adjustmentDate;
    }
    
    public void setAdjustmentDate(LocalDateTime adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 