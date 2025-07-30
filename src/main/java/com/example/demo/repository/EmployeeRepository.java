package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    List<Employee> findByDepartmentId(Long departmentId);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND e.joiningDate <= :fiveYearsAgo")
    List<Employee> findEmployeesInDepartmentWithTenure(@Param("departmentId") Long departmentId, 
                                                      @Param("fiveYearsAgo") LocalDate fiveYearsAgo);
} 