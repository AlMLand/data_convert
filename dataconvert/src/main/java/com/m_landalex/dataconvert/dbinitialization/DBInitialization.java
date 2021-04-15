package com.m_landalex.dataconvert.dbinitialization;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.formatter.ApplicationConversionServiceFactoryBean;
import com.m_landalex.dataconvert.service.DefaultService;

@Service
@Profile("!test")
public class DBInitialization {

	@Autowired
	@Qualifier(value = "employeeService")
	private DefaultService defaultService;
	@Autowired
	private ApplicationConversionServiceFactoryBean applicationConversionServiceFactoryBean;
	
	@PostConstruct
	public void init() throws ParseException, MalformedURLException, ResourceNullException {

		for(int i = 1; i < 10; i++) {
			
			defaultService.save(Employee.builder().firstName("Firstname_" + i).lastName("Lastname_" + i)
				.birthDate(applicationConversionServiceFactoryBean.getLocalDateFormatter().parse( "2000-0" + i + "-0" + i , Locale.GERMAN))
				.jobStartInTheCompany(applicationConversionServiceFactoryBean.getLocalDateFormatter().parse("2022-0" + i + "-0" + i, Locale.GERMAN))
				.companyAffiliation(0).description("description_" + i).photo(null).webSite(new URL( "http://employee_" + i + ".com/"))
				.user(User.builder().username("Username_" + i)
						.password(applicationConversionServiceFactoryBean.getIntegerFormatter().parse("12345", Locale.GERMAN))
						.start(applicationConversionServiceFactoryBean.getLocalDateFormatter().parse("2022-0" + i + "-0" + i, Locale.GERMAN))
						.aktiv(applicationConversionServiceFactoryBean.getBooleanFormatter().parse("true", Locale.GERMAN))
						.userRole(Role.DEVELOPER).build())
				.build());
		
		}

	}

}