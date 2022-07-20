package com.hrcentral.nphc.helper;


import org.springframework.stereotype.Service;

import com.hrcentral.nphc.model.Employee;
import com.hrcentral.nphc.utility.DateUtilImpl;

@Service
public class EmployeeValidator {
	
	DateUtilImpl dateUtil = new DateUtilImpl();

	public boolean isNullorEmptyId(String id) {
		if (id == null || id.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isNullorEmptyLogin(String login) {
		if (login == null || login.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isNullorEmptyName(String name) {
		if (name == null || name.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isNullSalary(Double salary) {
		if (salary == null) {
			return true;
		}
		return false;
	}

	public boolean isValidSalary(Double salary) {
		if (isNullSalary(salary) || salary < 0) {
			return false;
		}

		return true;
	}

	public boolean isValidStartDate(String startDate) {
		if (startDate == null) {
			return false;
		}else {
			try {
				boolean result = dateUtil.isValidDate(startDate);
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public Employee mergeUpdate(Employee empl) {
		Employee createEmpl = new Employee();

		if (!isNullorEmptyId(empl.getId())) {
			createEmpl.setId(empl.getId());
		}
		if (!isNullorEmptyLogin(empl.getLogin())) {
			createEmpl.setLogin(empl.getLogin());
		}

		if (!isNullorEmptyName(empl.getName())) {
			createEmpl.setName(empl.getName());
		}

		if (!isNullSalary(empl.getSalary())) {
			createEmpl.setSalary(empl.getSalary());
		}

		if (isValidStartDate(empl.getStartDate())) {
			createEmpl.setStartDate(empl.getStartDate());
		}

		return createEmpl;
	}
}
