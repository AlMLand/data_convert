package com.m_landalex.dataconvert.dbinitialization;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m_landalex.dataconvert.converter.StringToLocalDateConverter;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.EmployeeService;

@Service
public class DBInitialization {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private StringToLocalDateConverter stringToLocalDateConverter;
	
	@PostConstruct
	public void init() throws MalformedURLException, ResourceNullException {
		employeeService.save(Employee.builder()
				.firstName("Al")
				.lastName("M_land")
				.birthDate(stringToLocalDateConverter.convert("2000-01-01"))
				.webSite(new URL("http://alm_land.com/"))
				.build());
	}
	
}
