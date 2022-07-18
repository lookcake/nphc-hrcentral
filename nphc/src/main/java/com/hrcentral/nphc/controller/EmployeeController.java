package com.hrcentral.nphc.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hrcentral.nphc.helper.ResponseMessage;
import com.hrcentral.nphc.helper.ResponseObject;
import com.hrcentral.nphc.model.Employee;
import com.hrcentral.nphc.service.EmployeeService;


@RepositoryRestController
public class EmployeeController<T> {

//	@Autowired
//	private EmployeeRepository repo;
	@Autowired
	private EmployeeService service;

	private static final Logger logger = LogManager.getLogger();
	
	@PostMapping(value ="/users/saveall")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveAll(@RequestBody List<Employee> employee) throws Exception {
		logger.info("List of Employee {}", employee );
		List<Employee> response = (List<Employee>) service.saveAll(employee);
		return ResponseEntity.ok().body(new ResponseObject(ResponseMessage.MSG_SUC_CREATED));
	}
	
	@GetMapping(value = "/users/{id}")
	public @ResponseBody ResponseEntity<Optional<Employee>> findById(@PathVariable String id) {
		
		logger.info("id: {}", id);
		Optional<Employee> result = service.findById(id);
		if(!result.isPresent()) {
//			return new ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD_INPUT);
			return new ResponseEntity(new ResponseObject(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD_INPUT),HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(HttpStatus.OK).body(result);
		
	}
	
	
	
	
	
}
