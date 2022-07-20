package com.hrcentral.nphc.helper;

public enum EmployeeEntityField {

	id, login, name, startDate, salary;

	public static boolean validValue(String value) {

		if (fromString(value) == null) {
			return false;
		}

		return true;
	}

	public String getValue() {

		String result = "";

		switch (this) {
		case startDate:
			result = "date";
			break;
		default:
			result = this.name().toString();
		}
		return result;
	}

	public static EmployeeEntityField fromString(String value) {

		try {
			for (EmployeeEntityField val : EmployeeEntityField.values()) {
				if (val.toString().equalsIgnoreCase(value)) {
					return val;
				}
			}
		} catch (Exception ex) {
		}
		return null;
	}
}
