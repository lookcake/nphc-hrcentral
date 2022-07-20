package com.hrcentral.nphc.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hrcentral.nphc.helper.CustomException;
import com.hrcentral.nphc.helper.ResponseMessage;
import com.hrcentral.nphc.helper.ResponseObject;
import com.hrcentral.nphc.model.Employee;
import com.hrcentral.nphc.repository.EmployeeRepository;
import com.hrcentral.nphc.service.EmployeeService;

@RepositoryRestController
public class EmployeeController<T> {

	@Autowired
	private EmployeeRepository repo;

	@Autowired
	private EmployeeService service;

	private static final Logger logger = LogManager.getLogger();
	
	@RequestMapping(value = "/users/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<ResponseObject> uploadUserByCSV(@RequestParam("file") MultipartFile file){
		
		ResponseObject response = new ResponseObject(ResponseMessage.MSG_SUC_NO_CREATE);
		logger.info("file name: {}, file size: {}", file.getOriginalFilename(), file.getSize());
		Integer count = service.uploadUserByCSV(file);
		if( count > 0 ) {
			response.setMessage(ResponseMessage.MSG_SUC_CREATE_UPDATE);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	

	@PostMapping(value = "/users/saveall")
	@ResponseBody
	public ResponseEntity<ResponseObject> saveAll(@RequestBody List<Employee> employee) throws Exception {
		logger.info("List of Employee {}", employee);
		List<Employee> response = (List<Employee>) service.saveAll(employee);
		return ResponseEntity.ok().body(new ResponseObject(ResponseMessage.MSG_SUC_CREATED));
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/users")
	@ResponseBody
	public ResponseEntity<Employee> save(@RequestBody Employee employee) {
		logger.info("Employee {}", employee);
		try {
			service.save(employee);

		} catch (CustomException e) {
			return createReponseEntity((ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD + "-" + e),
					HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.status(HttpStatus.OK).body(employee);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/users/{id}")
	public @ResponseBody ResponseEntity<Optional<Employee>> findById(@PathVariable String id) {

		logger.info("id: {}", id);
		Optional<Employee> result = service.findById(id);
		if (!result.isPresent()) {
			return createReponseEntity(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD_INPUT, HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/users/{id}",method = { RequestMethod.PUT, RequestMethod.PATCH })
	public @ResponseBody ResponseEntity<Employee> update(@RequestBody Employee employee,@PathVariable String id) {
		logger.info("id: {}, Employee: {}",id , employee );
		try {
			service.updateUserById(employee);
		}catch(CustomException e){
			return createReponseEntity((ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD +"-"+ e),HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(HttpStatus.OK).body(employee);
	}

	@DeleteMapping(value = "/users/{id}")
	public @ResponseBody ResponseEntity<ResponseObject> deleteById(@PathVariable String id) {
		logger.info("id: {}", id);
		Employee empl = new Employee();
		empl.setId(id);
		if (!repo.existsById(id))
			return new ResponseEntity<ResponseObject>(
					new ResponseObject(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD_INPUT), HttpStatus.BAD_REQUEST);
		repo.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseMessage.MSG_SUC_DELETED));
	}

	@SuppressWarnings("unchecked")
	private ResponseEntity createReponseEntity(String message, HttpStatus status) {
		return new ResponseEntity(new ResponseObject(message), status);
	}

}
