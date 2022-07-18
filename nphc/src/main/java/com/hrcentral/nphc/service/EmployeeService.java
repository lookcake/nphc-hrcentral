package com.hrcentral.nphc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hrcentral.nphc.model.Employee;

@Component
public interface EmployeeService {

	public List<Employee> saveAll(List<Employee> employeelist) throws Exception;

	public Optional<Employee> findById(String id);
	
}
