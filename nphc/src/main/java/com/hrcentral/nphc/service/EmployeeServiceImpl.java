package com.hrcentral.nphc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hrcentral.nphc.helper.CsvReader;
import com.hrcentral.nphc.helper.CustomException;
import com.hrcentral.nphc.helper.EmployeeEntityField;
import com.hrcentral.nphc.helper.EmployeeValidator;
import com.hrcentral.nphc.helper.ResponseMessage;
import com.hrcentral.nphc.helper.ResponseObject;
import com.hrcentral.nphc.model.Employee;
import com.hrcentral.nphc.repository.EmployeeRepository;

@Service()
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger logger = LogManager.getLogger();
	private static long FILE_SIZE_LIMIT_10MB = 10000000;

	@Autowired
	private EmployeeRepository repo;
	
	EmployeeValidator employeeValidator = new EmployeeValidator();

	/*
	 * status of update/create is based on the count of result;
	 */
	@Override
	public Integer uploadUserByCSV(MultipartFile file) {

		List<Employee> rowsDataList = new ArrayList<>();
		int result = 0;

		validateFileSizeLimit(file);

		String csvContent = csvFileToString(file);

		logger.info("filtered content: \n{}\n", csvContent);

		rowsDataList = CsvReader.toList(Employee.class, csvContent.getBytes());
		
		validateRowsList(rowsDataList);
		
		for (Employee empl : rowsDataList) {

			Optional<Employee> existingEmpl = repo.findById(empl.getId());
			if (existingEmpl.isPresent()) {
				Employee emplFromDb = existingEmpl.get();

				emplFromDb = employeeValidator.mergeUpdate(empl);

				Employee emplUpdate = repo.save(emplFromDb);
				if (!emplUpdate.equals(empl)) {
					result = 1;
				}
			} else {
				repo.save(empl);
				result = 1;
			}

		}
		if(result > 0 )return result;
		return null;
	}

	@Override
	public List<Employee> saveAll(List<Employee> employeelist) throws Exception {
		List<Employee> response = repo.saveAll(employeelist);
		if (response.isEmpty() || response == null)
			throw new CustomException(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST);
		return response;
	}

	@Override
	public void deleteById(String id) {
		Employee empl = new Employee();
		validateDeleteUserWithError(empl);
		repo.deleteById(id);
	}

	@Override
	public Optional<Employee> findById(String id) {
		Optional<Employee> result = repo.findById(id);
		if (!result.isPresent()) {
			throw new CustomException(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE);
		}
		return result;
	}

	@Override
	public void save(Employee employee) {
		validateCreateUserWithError(employee);
		saveOrUpdateUser(employee);
	}

	@Override
	public void updateUserById(Employee empl) {
		saveOrUpdateUser(empl);
	}

	private Employee saveOrUpdateUser(Employee empl) {

		StringJoiner errorList = new StringJoiner(",");
		StringJoiner sj = validateMandatoryField(empl);

		if (sj.length() > 0) {
			errorList.add(String.format(ResponseMessage.MSG_ERR_MANDATORY_FIELD, sj.toString()));
		}

		sj = validateFormat(empl);

		if (sj.length() > 0) {
			errorList.add(String.format(ResponseMessage.MSG_ERR_INVALID_FIELD, sj.toString()));
		}

		if (errorList.length() > 0) {
			throw new CustomException(errorList.toString());
		}

		Optional<Employee> empObj = repo.findById(empl.getId());

		Employee updateableEmpl = empl;

		if (empObj.isPresent()) {
			updateableEmpl = employeeValidator.mergeUpdate(empl);
		}

		repo.save(updateableEmpl);
		return updateableEmpl;
	}

	private StringJoiner validateFormat(Employee empl) {

		StringJoiner invalidField = new StringJoiner(",");

		if (!employeeValidator.isValidSalary(empl.getSalary())) {
			invalidField.add(EmployeeEntityField.salary.toString());
		}

		if (!employeeValidator.isValidStartDate(empl.getStartDate())) {
			invalidField.add(EmployeeEntityField.startDate.toString());
		}

		return invalidField;
	}

	private StringJoiner validateMandatoryField(Employee empl) {

		StringJoiner sj = new StringJoiner(",");

		if (employeeValidator.isNullorEmptyId(empl.getId())) {
			sj.add(EmployeeEntityField.id.toString());
		}

		if (employeeValidator.isNullorEmptyLogin(empl.getLogin())) {
			sj.add(EmployeeEntityField.login.toString());
		}

		if (employeeValidator.isNullorEmptyName(empl.getName())) {
			sj.add(EmployeeEntityField.name.toString());
		}

		if (employeeValidator.isNullSalary(empl.getSalary())) {
			sj.add(EmployeeEntityField.salary.toString());
		}

		return sj;
	}

	public void validateRowsList(List<Employee> emplList) {

		List<String> errorList = new ArrayList<>();

		errorList.addAll(validateDuplicateEmployee(emplList));

		IntStream.range(0, emplList.size()).forEach(idx -> {
			StringJoiner sj = validateMandatoryField(emplList.get(idx));

			if (sj.length() > 0) {
				errorList.add(String.format(ResponseMessage.MSG_ERR_MANDATORY_ROW_FIELD, idx + 2, sj.toString()));
			} else {

				sj = validateFormat(emplList.get(idx));

				if (sj.length() > 0) {
					errorList.add(String.format(ResponseMessage.MSG_ERR_INVALID_FORMAT_FIELD, idx + 2, sj.toString()));
				}
			}
		});

		if (errorList.size() > 0) {
			throw new CustomException(errorList.toString());
		}

		// database records validate
		IntStream.range(0, emplList.size()).forEach(idx -> {

			Employee validateUser = emplList.get(idx);

			if (repo.existsByLoginAndIdNot(validateUser.getLogin(), validateUser.getId())) {
				errorList.add(String.format(ResponseMessage.MSG_ERR_NOT_UNIQUE_ROW_FIELD, idx + 2, validateUser.getLogin()));
			}
		});

		if (errorList.size() > 0) {
			throw new CustomException(errorList.toString());
		}
	}
	
	
	private List<String> validateDuplicateEmployee(List<Employee> emplList) {

		Map<String, Integer> duplicateIdList = new HashMap<>();
		Map<String, Integer> duplicateLoginList = new HashMap<>();
		List<String> errorList = new ArrayList<>();

		emplList.stream().forEach(item -> {
			duplicateIdList.compute(item.getId().toLowerCase(), (key, val) -> val == null ? 1 : val + 1);
			duplicateLoginList.compute(item.getLogin().toLowerCase(), (key, val) -> val == null ? 1 : val + 1);
		});

		String duplicateList = duplicateIdList.entrySet().stream().filter(map -> map.getValue() > 1)
				.map(map -> map.getKey()).collect(Collectors.joining(","));

		if (!duplicateList.trim().isEmpty()) {
			errorList.add(String.format(ResponseMessage.MSG_ERR_NOT_UNIQUE_IDS, duplicateList));
		}

		String duplicateLogin = duplicateLoginList.entrySet().stream().filter(map -> map.getValue() > 1)
				.map(map -> map.getKey()).collect(Collectors.joining(","));

		if (!duplicateLogin.trim().isEmpty()) {
			errorList.add(String.format(ResponseMessage.MSG_ERR_NOT_UNIQUE_LOGINS, duplicateLogin));
		}

		return errorList;

	}

	public void validateCreateUserWithError(Employee empl) {

		validateExistingUserWithError(empl);
		validateDuplicateUserWithError(empl);
	}

	public void validateUpdateUserWithError(Employee empl) {

		validateNotExistingUserWithError(empl);
		validateDuplicateUserWithError(empl);

	}

	public void validateDeleteUserWithError(Employee empl) {
		validateNotExistingUserWithError(empl);
	}

	private void validateDuplicateUserWithError(Employee empl) {
		if (repo.existsById(empl.getId())) {
			throw new CustomException(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST);
		}
	}

	private void validateExistingUserWithError(Employee empl) {
		if (repo.existsById(empl.getId())) {
			throw new CustomException(ResponseMessage.MSG_ERR_EMPLOYEE_EXIST);
		}
	}

	private void validateNotExistingUserWithError(Employee empl) {

		if (!repo.existsById(empl.getId())) {
			throw new CustomException(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE);
		}
	}

	private void validateFileSizeLimit(MultipartFile file) {
		if (file.getSize() > FILE_SIZE_LIMIT_10MB) {
			throw new CustomException(ResponseMessage.MSG_ERR_FILE_SIZE_LIMIT_10MB);
		}
	}

	private String csvFileToString(MultipartFile file) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			return reader.lines().filter(line -> !line.startsWith("#"))
					.collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException ex) {
			throw new CustomException(ResponseMessage.MSG_ERR_FILE_READING);
		}
	}

}
