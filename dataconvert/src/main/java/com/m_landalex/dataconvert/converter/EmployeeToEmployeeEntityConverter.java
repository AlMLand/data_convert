package com.m_landalex.dataconvert.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.domain.EmployeeEntity;

@Component
public class EmployeeToEmployeeEntityConverter implements Converter<Employee, EmployeeEntity> {

	public EmployeeEntity convert(Employee source) {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setFirstName(source.getFirstName());
		employeeEntity.setLastName(source.getLastName());
		employeeEntity.setBirthDate(source.getBirthDate());
		employeeEntity.setWebSite(source.getWebSite());
		return employeeEntity;
	}

}
