package com.example.demo.service;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.entity.Department;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        // Check if department with same code already exists
        if (departmentRepository.existsByCode(departmentDto.getCode())) {
            throw new IllegalArgumentException("Department with code " + departmentDto.getCode() + " already exists");
        }
        
        Department department = new Department();
        department.setName(departmentDto.getName());
        department.setCode(departmentDto.getCode());
        
        Department savedDepartment = departmentRepository.save(department);
        return convertToDto(savedDepartment);
    }
    
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return convertToDto(department);
    }
    
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        
        department.setName(departmentDto.getName());
        department.setCode(departmentDto.getCode());
        
        Department updatedDepartment = departmentRepository.save(department);
        return convertToDto(updatedDepartment);
    }
    
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        departmentRepository.deleteById(id);
    }
    
    private DepartmentDto convertToDto(Department department) {
        return new DepartmentDto(department.getId(), department.getName(), department.getCode());
    }
} 