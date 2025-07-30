package com.example.demo;

import com.example.demo.dto.DepartmentDto;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DemoApplicationTests {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private EmployeeService employeeService;

	@Test
	void contextLoads() {
		// Test that the application context loads successfully
		assertNotNull(departmentService);
		assertNotNull(employeeService);
	}

	@Test
	void testDepartmentCRUD() {
		// Create department
		DepartmentDto departmentDto = new DepartmentDto();
		departmentDto.setName("Test Department");
		departmentDto.setCode("TEST");

		DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);
		assertNotNull(createdDepartment.getId());
		assertEquals("Test Department", createdDepartment.getName());
		assertEquals("TEST", createdDepartment.getCode());

		// Get department by ID
		DepartmentDto retrievedDepartment = departmentService.getDepartmentById(createdDepartment.getId());
		assertEquals(createdDepartment.getId(), retrievedDepartment.getId());
		assertEquals(createdDepartment.getName(), retrievedDepartment.getName());

		// Update department
		DepartmentDto updateDto = new DepartmentDto();
		updateDto.setName("Updated Department");
		updateDto.setCode("UPD");

		DepartmentDto updatedDepartment = departmentService.updateDepartment(createdDepartment.getId(), updateDto);
		assertEquals("Updated Department", updatedDepartment.getName());
		assertEquals("UPD", updatedDepartment.getCode());

		// Delete department
		assertDoesNotThrow(() -> departmentService.deleteDepartment(createdDepartment.getId()));
	}

	@Test
	void testEmployeeCRUD() {
		// First create a department
		DepartmentDto departmentDto = new DepartmentDto();
		departmentDto.setName("Engineering");
		departmentDto.setCode("ENG");
		DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);

		// Create employee
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setName("John Doe");
		employeeDto.setEmail("john.doe@test.com");
		employeeDto.setSalary(new BigDecimal("75000.00"));
		employeeDto.setJoiningDate(LocalDate.of(2023, 1, 15));
		employeeDto.setDepartmentId(createdDepartment.getId());

		EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
		assertNotNull(createdEmployee.getId());
		assertEquals("John Doe", createdEmployee.getName());
		assertEquals("john.doe@test.com", createdEmployee.getEmail());
		assertEquals(createdDepartment.getId(), createdEmployee.getDepartmentId());

		// Get employee by ID
		EmployeeDto retrievedEmployee = employeeService.getEmployeeById(createdEmployee.getId());
		assertEquals(createdEmployee.getId(), retrievedEmployee.getId());
		assertEquals(createdEmployee.getName(), retrievedEmployee.getName());

		// Update employee
		EmployeeDto updateDto = new EmployeeDto();
		updateDto.setName("Jane Doe");
		updateDto.setEmail("jane.doe@test.com");
		updateDto.setSalary(new BigDecimal("80000.00"));
		updateDto.setJoiningDate(LocalDate.of(2023, 1, 15));
		updateDto.setDepartmentId(createdDepartment.getId());

		EmployeeDto updatedEmployee = employeeService.updateEmployee(createdEmployee.getId(), updateDto);
		assertEquals("Jane Doe", updatedEmployee.getName());
		assertEquals("jane.doe@test.com", updatedEmployee.getEmail());

		// Delete employee
		assertDoesNotThrow(() -> employeeService.deleteEmployee(createdEmployee.getId()));

		// Clean up department
		assertDoesNotThrow(() -> departmentService.deleteDepartment(createdDepartment.getId()));
	}
}
