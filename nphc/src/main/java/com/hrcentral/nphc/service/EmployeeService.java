package com.hrcentral.nphc.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hrcentral.nphc.model.Employee;

@Component
public interface EmployeeService {

	public List<Employee> saveAll(List<Employee> employeelist) throws Exception;

	public Employee findById(String id);
	
}
