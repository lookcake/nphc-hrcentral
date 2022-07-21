package com.hrcentral.nphc.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Calendar;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.hrcentral.nphc.helper.CustomException;
import com.hrcentral.nphc.helper.ResponseMessage;
import com.hrcentral.nphc.model.Employee;
import com.hrcentral.nphc.repository.EmployeeRepository;

@SpringBootTest
public class EmployeeServiceTest {
	
	@InjectMocks
	private EmployeeServiceImpl service;
	
	@Mock
	private EmployeeRepository	repo;
	

	
	@Test
	public void testIdMandatoryValidation() {
		Employee empl = new Employee();
		empl.setLogin("loginfj");
		empl.setName("fanjie");
		empl.setSalary(1000.00);
		empl.setStartDate("2022-07-17");
		
		// when field mandatory fields not provided
		Throwable exception = assertThrows(CustomException.class,
				() -> service.validateMandatoryFieldWithError(empl));

		assertEquals("[id] is mandatory", exception.getMessage());
	}
	
	@Test
	public void testloginMandatoryValidation() {
		Employee empl = new Employee();
		empl.setId("f0001");
		empl.setName("fanjie");
		empl.setSalary(1000.00);
		empl.setStartDate("2022-07-17");
		
		// when field mandatory fields not provided
		Throwable exception = assertThrows(CustomException.class,
				() -> service.validateMandatoryFieldWithError(empl));

		assertEquals("[login] is mandatory", exception.getMessage());
	}
	
	@Test
	public void testMandatoryValidation() {
		Employee empl = new Employee();
		
		// when field mandatory fields not provided
		Throwable exception = assertThrows(CustomException.class,
				() -> service.validateMandatoryFieldWithError(empl));
		
		assertEquals("[id,login,name,salary] is mandatory", exception.getMessage());
		
	}
	
	@Test
	public void testStartDateValidation() {
		Employee empl = new Employee();
		empl.setId("f0001");
		empl.setLogin("loginfj");
		empl.setName("fanjie");
		empl.setSalary(1000.00);
		empl.setStartDate("2022/07/17");
		
		Throwable exception = assertThrows(CustomException.class, () -> service.validateFormatWithError(empl));

		assertEquals("Invalid startDate", exception.getMessage());
		
	}
	
	@Test
	public void testWhenEmployeeNotExsits() {
		Employee empl = new Employee();
		Mockito.when(repo.existsById(empl.getId())).thenReturn(false);

		Throwable exception = assertThrows(CustomException.class,
				() -> service.validateDeleteUserWithError(empl));

		assertEquals(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE, exception.getMessage());
	}
	
	@Test
	public void testWhenEmployeeLoginNotUnique() {
		Employee empl = new Employee();
		
		Mockito.when(repo.existsById(empl.getId())).thenReturn(true);

		Mockito.when(repo.existsByLoginAndIdNot(empl.getLogin(), empl.getId()))
				.thenReturn(true);

		Throwable exception = assertThrows(CustomException.class,
				() -> service.validateUpdateUserWithError(empl));

		assertEquals(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST, exception.getMessage());
		
	}
	
	@Test
	public void TestCrudUser() {
		
		Throwable exception = null;
		Employee empl = new Employee();
		empl.setId("f0002");
		empl.setLogin("harry");
		empl.setName("Potter Party");
		empl.setSalary(4000.0);
		empl.setStartDate("2022-07-18");

		// get //
		
		// when exployee does not exists
		Mockito.when(repo.findById(empl.getId())).thenReturn(Optional.empty());

		exception = assertThrows(CustomException.class, () -> service.findById(empl.getId()));
		
		assertEquals(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE, exception.getMessage());

		// create // 
		
		// when employee ID exists
		
		Mockito.when(repo.existsById(empl.getId())).thenReturn(true);

		exception = assertThrows(CustomException.class, ()-> service.save(empl));
		
		assertEquals(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST, exception.getMessage());
	

		// when valid data
		
		Mockito.when(repo.existsById(empl.getId())).thenReturn(false);

		assertDoesNotThrow(()-> service.save(empl));


		service.save(empl);

		

		// update //
		
		// when employee does not exists
		Mockito.when(repo.existsById(empl.getId())).thenReturn(false);
		
		exception = assertThrows(CustomException.class, () -> service.updateUserById(empl));

		assertEquals(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE, exception.getMessage());
		

		// when employye exists but Id already used
		Mockito.when(repo.existsById(empl.getId())).thenReturn(true);
		
		Mockito.when(repo.existsByLoginAndIdNot(empl.getLogin(), empl.getId())).thenReturn(true);

		exception = assertThrows(CustomException.class, () -> service.updateUserById(empl));

		assertEquals(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST, exception.getMessage());
	
		// delete //
		
		// when exployee does not exists
		Mockito.when(repo.existsById(empl.getId())).thenReturn(false);
		
		exception = assertThrows(CustomException.class, () -> service.deleteById(empl.getId()));

		assertEquals(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE, exception.getMessage());
	}


}
