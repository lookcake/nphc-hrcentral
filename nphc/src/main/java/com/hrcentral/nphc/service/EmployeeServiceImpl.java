package com.hrcentral.nphc.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hrcentral.nphc.helper.CustomException;
import com.hrcentral.nphc.helper.ResponseMessage;
import com.hrcentral.nphc.helper.ResponseObject;
import com.hrcentral.nphc.model.Employee;
import com.hrcentral.nphc.repository.EmployeeRepository;

@Service()
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository repo;
	
	private static final Logger logger = LogManager.getLogger();
	
	@Override
	public List<Employee> saveAll(List<Employee> employeelist) throws Exception {
		List<Employee> response = repo.saveAll(employeelist);
		if(response.isEmpty() || response ==null) throw new CustomException(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST);
		return response;
	}

	
	@Override
	public Employee findById(String id) {
		Optional<Employee> result = repo.findById(id);
		if (!result.isPresent()) {
			throw new CustomException(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE);
		}
		return result.get();
	}




	public void validateCreateUserWithError(Employee employee) {

		validateExistingUserWithError(employee);
		validateDuplicateUserWithError(employee);
	}


	public void validateUpdateUserWithError(Employee employee) {

		validateNotExistingUserWithError(employee);
		validateDuplicateUserWithError(employee);

	}

	public void validateDeleteUserWithError(Employee employee) {
		validateNotExistingUserWithError(employee);
	}
	
	private void validateDuplicateUserWithError(Employee employee) {
		if (repo.existsById(employee.getId())) {
			throw new CustomException(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST);
		}		
	}


	private void validateExistingUserWithError(Employee employee) {
		if (repo.existsById(employee.getId())) {
			throw new CustomException(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST);
		}	
	}
	
	private void validateNotExistingUserWithError(Employee employee) {

		if (!repo.existsById(employee.getId())) {
			throw new CustomException(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE);
		}
	}



}
