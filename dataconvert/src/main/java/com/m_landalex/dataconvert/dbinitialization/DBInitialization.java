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
				.username("Chicken")
				.password(applicationConversionServiceFactoryBean
						.getIntegerFormatter().parse("12345", Locale.GERMAN))
				.start(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2025-10-10",Locale.GERMAN))
				.aktiv(applicationConversionServiceFactoryBean
						.getBooleanFormatter().parse("true", Locale.GERMAN))
				.userRole(Role.DEVELOPER)
				.build();
		userValidator.validateUser(user);
		
		Employee employee = Employee.builder()
				.firstName("Connor")
				.lastName("McGregor")
				.birthDate(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2001-01-01", Locale.GERMAN))
				.jobStartInTheCompany(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2010-04-04", Locale.GERMAN))
				.companyAffiliation(0)
				.webSite(new URL("http://connor_mcgregor.com/"))
				.user(user)
				.build();
		employeeValidatorClassTypeService.validateEmployee(employee);
		employeeService.save(employee);
		
		user = User.builder()
				.username("Diamant")
				.password(applicationConversionServiceFactoryBean
						.getIntegerFormatter().parse("67890", Locale.GERMAN))
				.start(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2032-08-08",Locale.GERMAN))
				.aktiv(applicationConversionServiceFactoryBean
						.getBooleanFormatter().parse("true", Locale.GERMAN))
				.userRole(Role.ADMINISTRATOR)
				.build();
		userValidator.validateUser(user);
		
		employee = Employee.builder()
				.firstName("Dustin")
				.lastName("Poirier")
				.birthDate(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("1993-04-06", Locale.GERMAN))
				.jobStartInTheCompany(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2018-01-15", Locale.GERMAN))
				.companyAffiliation(0)
				.webSite(new URL("http://dustin_poirier.com/"))
				.user(user)
				.build();
		employeeValidatorClassTypeService.validateEmployee(employee);
		employeeService.save(employee);
	}

}
