package com.m_landalex.dataconvert.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.domain.EmployeeEntity;

@Component
public class EmployeeToEmployeeEntityConverter implements Converter<Employee, EmployeeEntity> {

	@Autowired
	private UserToUserEntityConverter userToUserEntityConverter;
	
	public EmployeeEntity convert(Employee source) {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setFirstName(source.getFirstName());
		employeeEntity.setLastName(source.getLastName());
		employeeEntity.setBirthDate(source.getBirthDate());
		employeeEntity.setWebSite(source.getWebSite());
		employeeEntity.setUser(userToUserEntityConverter.convert(source.getUser()));
		return employeeEntity;
	}

}
