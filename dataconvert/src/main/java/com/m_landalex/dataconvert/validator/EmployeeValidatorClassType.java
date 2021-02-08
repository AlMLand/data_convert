package com.m_landalex.dataconvert.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.m_landalex.dataconvert.annotation.CheckEmployee;
import com.m_landalex.dataconvert.data.Employee;

public class EmployeeValidatorClassType implements ConstraintValidator<CheckEmployee, Employee> {

	@Override
	public void initialize(CheckEmployee checkEmployee) {
	}

	@Override
	public boolean isValid(Employee value, ConstraintValidatorContext context) {
		boolean result = false;
		if (value.getFirstName() != null & value.getLastName() != null & value.getBirthDate() != null
				& value.getJobStartInTheCompany() != null & value.getUser() != null
				& (value.getLastName().length() > 2 & value.getLastName().length() < 50)) {
			return result = true;
		}
		return result;
	}

}
