package com.hrcentral.nphc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hrcentral.nphc.helper.CustomException;
import com.hrcentral.nphc.model.Employee;

@Component
public interface EmployeeService {

	public List<Employee> saveAll(List<Employee> employeelist) throws Exception;
	
	public void save(Employee employee);
	
	public Optional<Employee> findById(String id) throws CustomException;

	public void deleteById(String id) ;
	
	public void updateUserById(Employee employee);

	public Integer uploadUserByCSV(MultipartFile file);
	
}
