package com.m_landalex.dataconvert.dbinitialization;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
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
@Profile({"!test & !test2 & !test3 & !test4"})
public class DBInitialization {

	@Autowired
	@Qualifier(value = "employeeService")
	private DefaultService defaultService;
	@Autowired
	private ApplicationConversionServiceFactoryBean applicationConversionServiceFactoryBean;

	@PostConstruct
	public void init() throws ParseException, MalformedURLException, ResourceNullException {

		for (int i = 1; i < 4; i++) {

			Collection<Role> roles = List.of(Role.builder().role("ADMINISTRATOR").build());
			
			if (i == 2) {
				roles = List.of(Role.builder().role("OFFICE").build());
			} else if (i == 3) {
				roles = List.of(Role.builder().role("OFFICE_EDITOR").build());
			}

			// password: 12345
			defaultService.save(Employee.builder().firstName("Firstname" + i).lastName("Lastname_" + i)
					.birthDate(applicationConversionServiceFactoryBean.getLocalDateFormatter().parse("2000-0" + i + "-0" + i, Locale.GERMAN))
					.jobStartInTheCompany(applicationConversionServiceFactoryBean.getLocalDateFormatter().parse("2022-0" + i + "-0" + i, Locale.GERMAN))
					.companyAffiliation(0).description("description" + i).photo(null)
					.webSite(new URL("http://employee" + i + ".com/"))
					.user(User.builder().username("Username" + i).password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
							.start(applicationConversionServiceFactoryBean.getLocalDateFormatter().parse("2022-0" + i + "-0" + i, Locale.GERMAN))
							.aktiv(applicationConversionServiceFactoryBean.getBooleanFormatter().parse("true", Locale.GERMAN))
							.userRole(roles).build())
					.build());

		}

	}

}