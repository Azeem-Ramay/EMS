package com.example.demo.repository;

import com.example.demo.entity.SalaryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalaryAdjustmentRepository extends JpaRepository<SalaryAdjustment, Long> {
    
    @Query("SELECT sa FROM SalaryAdjustment sa WHERE sa.departmentId = :departmentId AND sa.performanceScore = :performanceScore AND sa.createdAt >= :thirtyMinutesAgo")
    List<SalaryAdjustment> findRecentAdjustmentsByDepartmentAndScore(
            @Param("departmentId") Long departmentId,
            @Param("performanceScore") Integer performanceScore,
            @Param("thirtyMinutesAgo") LocalDateTime thirtyMinutesAgo);
    
    boolean existsByDepartmentIdAndPerformanceScoreAndCreatedAtAfter(
            Long departmentId, Integer performanceScore, LocalDateTime createdAt);
} 