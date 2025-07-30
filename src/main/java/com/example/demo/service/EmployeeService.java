package com.example.demo.service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.SalaryAdjustmentDto;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.entity.SalaryAdjustment;
import com.example.demo.exception.DuplicateAdjustmentException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.SalaryAdjustmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private static final BigDecimal MAX_SALARY = new BigDecimal("20000000"); // ₹20,000,000 PKR
    private static final BigDecimal TENURE_BONUS_PERCENTAGE = new BigDecimal("0.05");
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private SalaryAdjustmentRepository salaryAdjustmentRepository;
    
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        // Check if employee with same email already exists
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new IllegalArgumentException("Employee with email " + employeeDto.getEmail() + " already exists");
        }
        
        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", employeeDto.getDepartmentId()));
        
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setSalary(employeeDto.getSalary());
        employee.setJoiningDate(employeeDto.getJoiningDate());
        employee.setDepartment(department);
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDto(savedEmployee);
    }
    
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return convertToDto(employee);
    }
    
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<EmployeeDto> getEmployeesByDepartment(Long departmentId) {
        // Check if department exists
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }
        
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        
        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", employeeDto.getDepartmentId()));
        
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setSalary(employeeDto.getSalary());
        employee.setJoiningDate(employeeDto.getJoiningDate());
        employee.setDepartment(department);
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDto(updatedEmployee);
    }
    
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee", "id", id);
        }
        employeeRepository.deleteById(id);
    }
    
    public void adjustSalary(SalaryAdjustmentDto adjustmentDto) {
        // Validate performance score
        if (adjustmentDto.getPerformanceScore() < 0 || adjustmentDto.getPerformanceScore() > 100) {
            throw new IllegalArgumentException("Performance score must be between 0 and 100");
        }
        
        // Check for duplicate adjustment within 30 minutes (idempotency)
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<SalaryAdjustment> recentAdjustments = salaryAdjustmentRepository
                .findRecentAdjustmentsByDepartmentAndScore(
                        adjustmentDto.getDepartmentId(),
                        adjustmentDto.getPerformanceScore(),
                        thirtyMinutesAgo);
        
        if (!recentAdjustments.isEmpty()) {
            throw new DuplicateAdjustmentException(
                    "Salary adjustment for department " + adjustmentDto.getDepartmentId() + 
                    " with performance score " + adjustmentDto.getPerformanceScore() + 
                    " has already been processed within the last 30 minutes");
        }
        
        // Check if department exists
        if (!departmentRepository.existsById(adjustmentDto.getDepartmentId())) {
            throw new ResourceNotFoundException("Department", "id", adjustmentDto.getDepartmentId());
        }
        
        // Get employees in the department
        List<Employee> employees = employeeRepository.findByDepartmentId(adjustmentDto.getDepartmentId());
        
        if (employees.isEmpty()) {
            logger.warn("No employees found in department with ID: {}", adjustmentDto.getDepartmentId());
            return;
        }
        
        // Calculate adjustment percentage based on performance score
        BigDecimal adjustmentPercentage = calculateAdjustmentPercentage(adjustmentDto.getPerformanceScore());
        
        // Calculate tenure bonus date (5 years ago)
        LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);
        
        // Get employees with tenure
        List<Employee> employeesWithTenure = employeeRepository.findEmployeesInDepartmentWithTenure(
                adjustmentDto.getDepartmentId(), fiveYearsAgo);
        
        for (Employee employee : employees) {
            BigDecimal newSalary = employee.getSalary();
            
            // Apply performance-based adjustment
            if (adjustmentPercentage.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal performanceIncrease = employee.getSalary().multiply(adjustmentPercentage);
                newSalary = newSalary.add(performanceIncrease);
            }
            
            // Apply tenure bonus
            if (employeesWithTenure.contains(employee)) {
                BigDecimal tenureBonus = employee.getSalary().multiply(TENURE_BONUS_PERCENTAGE);
                newSalary = newSalary.add(tenureBonus);
                logger.info("Applied tenure bonus for employee: {}", employee.getName());
            }
            
            // Apply salary cap
            if (newSalary.compareTo(MAX_SALARY) > 0) {
                newSalary = MAX_SALARY;
                logger.info("Applied salary cap for employee: {}", employee.getName());
            }
            
            // Update employee salary
            employee.setSalary(newSalary.setScale(2, RoundingMode.HALF_UP));
            employeeRepository.save(employee);
            
            logger.info("Salary adjusted for employee: {} from {} to {}", 
                    employee.getName(), employee.getSalary(), newSalary);
        }
        
        // Save the salary adjustment record for idempotency
        SalaryAdjustment salaryAdjustment = new SalaryAdjustment(
                adjustmentDto.getDepartmentId(), 
                adjustmentDto.getPerformanceScore());
        salaryAdjustmentRepository.save(salaryAdjustment);
        
        logger.info("Salary adjustment completed for department {} with performance score {}", 
                adjustmentDto.getDepartmentId(), adjustmentDto.getPerformanceScore());
    }
    
    public List<SalaryAdjustmentDto> getSalaryAdjustmentHistory() {
        return salaryAdjustmentRepository.findAll().stream()
                .map(this::convertToSalaryAdjustmentDto)
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getSalaryAdjustmentStats() {
        List<SalaryAdjustment> allAdjustments = salaryAdjustmentRepository.findAll();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAdjustments", allAdjustments.size());
        
        if (!allAdjustments.isEmpty()) {
            double avgPerformanceScore = allAdjustments.stream()
                    .mapToInt(SalaryAdjustment::getPerformanceScore)
                    .average()
                    .orElse(0.0);
            stats.put("averagePerformanceScore", avgPerformanceScore);
            
            long highPerformers = allAdjustments.stream()
                    .filter(adj -> adj.getPerformanceScore() >= 90)
                    .count();
            stats.put("highPerformanceAdjustments", highPerformers);
            
            long mediumPerformers = allAdjustments.stream()
                    .filter(adj -> adj.getPerformanceScore() >= 70 && adj.getPerformanceScore() < 90)
                    .count();
            stats.put("mediumPerformanceAdjustments", mediumPerformers);
            
            long lowPerformers = allAdjustments.stream()
                    .filter(adj -> adj.getPerformanceScore() < 70)
                    .count();
            stats.put("lowPerformanceAdjustments", lowPerformers);
        }
        
        return stats;
    }
    
    private BigDecimal calculateAdjustmentPercentage(Integer performanceScore) {
        if (performanceScore >= 90) {
            return new BigDecimal("0.15"); // 15% increase
        } else if (performanceScore >= 70) {
            return new BigDecimal("0.10"); // 10% increase
        } else {
            logger.warn("Performance score {} is below 70, no salary increase applied", performanceScore);
            return BigDecimal.ZERO; // No increase
        }
    }
    
    private EmployeeDto convertToDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary(),
                employee.getJoiningDate(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName()
        );
    }
    
    private SalaryAdjustmentDto convertToSalaryAdjustmentDto(SalaryAdjustment adjustment) {
        return new SalaryAdjustmentDto(
                adjustment.getDepartmentId(),
                adjustment.getPerformanceScore()
        );
    }
} 