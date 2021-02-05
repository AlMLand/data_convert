package com.m_landalex.dataconvert.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.domain.EmployeeEntity;

@Component
public class EmployeeEntityToEmployeeConverter implements Converter<EmployeeEntity, Employee> {

	@Autowired
	private UserEntityToUserConverter userEntityToUserConverter;
	
	public Employee convert(EmployeeEntity source) {
		return Employee.builder()
					.firstName(source.getFirstName())
					.lastName(source.getLastName())
					.birthDate(source.getBirthDate())
					.webSite(source.getWebSite())
					.user(userEntityToUserConverter.convert(source.getUser()))
					.build();
	}

}
