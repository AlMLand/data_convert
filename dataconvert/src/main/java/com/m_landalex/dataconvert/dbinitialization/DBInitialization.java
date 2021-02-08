package com.m_landalex.dataconvert.dbinitialization;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.formatter.ApplicationConversionServiceFactoryBean;
import com.m_landalex.dataconvert.service.EmployeeService;
import com.m_landalex.dataconvert.validator.EmployeeValidatorClassTypeService;
import com.m_landalex.dataconvert.validator.UserValidator;

@Service
public class DBInitialization {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ApplicationConversionServiceFactoryBean applicationConversionServiceFactoryBean;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private EmployeeValidatorClassTypeService employeeValidatorClassTypeService;

	@PostConstruct
	public void init() throws MalformedURLException, ResourceNullException, ParseException {
		User user = User.builder()
				.username("Timur")
				.password(applicationConversionServiceFactoryBean
						.getIntegerFormatter().parse("12345", Locale.GERMAN))
				.start(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2022-10-10",Locale.GERMAN))
				.aktiv(applicationConversionServiceFactoryBean
						.getBooleanFormatter().parse("true", Locale.GERMAN))
				.userRole(Role.DEVELOPER)
				.build();
		userValidator.validateUser(user);
		
		Employee employee = Employee.builder()
				.firstName("Al")
				.lastName("M_land")
				.birthDate(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2001-01-01", Locale.GERMAN))
				.webSite(new URL("http://alm_land.com/"))
				.user(user)
				.build();
		employeeValidatorClassTypeService.validateEmployee(employee);
		employeeService.save(employee);
	}

}
