package com.m_landalex.dataconvert.dbinitialization;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.formatter.ApplicationConversionServiceFactoryBean;
import com.m_landalex.dataconvert.service.DefaultService;

@Service
public class DBInitialization {

	@Autowired
	@Qualifier(value = "employeeService")
	private DefaultService defaultService;
	@Autowired
	private ApplicationConversionServiceFactoryBean applicationConversionServiceFactoryBean;

	@PostConstruct
	public void init() throws ParseException, MalformedURLException, ResourceNullException {
		
		Employee employee = Employee.builder()
				.firstName("Connor")
				.lastName("McGregor")
				.birthDate(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2001-01-01", Locale.GERMAN))
				.jobStartInTheCompany(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2010-04-04", Locale.GERMAN))
				.companyAffiliation(0)
				.webSite(new URL("http://connor_mcgregor.com/"))
				.user(User.builder()
						.username("Chicken")
						.password(applicationConversionServiceFactoryBean
								.getIntegerFormatter().parse("12345", Locale.GERMAN))
						.start(applicationConversionServiceFactoryBean
								.getLocalDateFormatter().parse("2025-10-10",Locale.GERMAN))
						.aktiv(applicationConversionServiceFactoryBean
								.getBooleanFormatter().parse("true", Locale.GERMAN))
						.userRole(Role.DEVELOPER)
						.build())
				.build();
		defaultService.save(employee);
		
		employee = Employee.builder()
				.firstName("Dustin")
				.lastName("Poirier")
				.birthDate(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("1993-04-06", Locale.GERMAN))
				.jobStartInTheCompany(applicationConversionServiceFactoryBean
						.getLocalDateFormatter().parse("2018-01-15", Locale.GERMAN))
				.companyAffiliation(0)
				.webSite(new URL("http://dustin_poirier.com/"))
				.user(User.builder()
						.username("Diamant")
						.password(applicationConversionServiceFactoryBean
								.getIntegerFormatter().parse("67890", Locale.GERMAN))
						.start(applicationConversionServiceFactoryBean
								.getLocalDateFormatter().parse("2032-08-08",Locale.GERMAN))
						.aktiv(applicationConversionServiceFactoryBean
								.getBooleanFormatter().parse("true", Locale.GERMAN))
						.userRole(Role.ADMINISTRATOR)
						.build())
				.build();
		defaultService.save(employee);
	
	}

}