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
		Employee employee = new Employee();
		employee.setId(source.getId());
		employee.setFirstName(source.getFirstName());
		employee.setLastName(source.getLastName());
		employee.setBirthDate(source.getBirthDate());
		employee.setJobStartInTheCompany(source.getJobStartInTheCompany());
		employee.setCompanyAffiliation(source.getCompanyAffiliation());
		employee.setWebSite(source.getWebSite());
		employee.setUser(userEntityToUserConverter.convert(source.getUser()));
		employee.setVersion(source.getVersion());
		return employee;
	}

}
