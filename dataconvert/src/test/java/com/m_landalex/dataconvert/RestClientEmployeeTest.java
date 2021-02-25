package com.m_landalex.dataconvert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.m_landalex.dataconvert.configuration.restclient.RestClientConfig;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RestClientConfig.class })
public class RestClientEmployeeTest {

	private static final Logger logger = LoggerFactory.getLogger(RestClientEmployeeTest.class);
	private static final String URL_GET_ALL_EMPLOYEES = "http://localhost:8080/dataconvert/employees/listEmployees";
	private static final String URL_GET_EMPLOYEE_BY_ID = "http://localhost:8080/dataconvert/employees/{id}";
	private static final String URL_POST_EMPLOYEE = "http://localhost:8080/dataconvert/employees/";
	private static final String URL_PUT_EMPLOYEE_BY_ID = "http://localhost:8080/dataconvert/employees/{id}";
	private static final String URL_DELETE_EMPLOYEE_BY_ID = "http://localhost:8080/dataconvert/employees/{id}";

	@Autowired
	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		assertNotNull(restTemplate);
	}

	@Test
	public void testFindAll() {
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		for (Employee employee : returnedArray) {
			logger.info("TEST FIND ALL : --> {}", employee);
		}
		assertTrue(returnedArray.length == 1);
	}

	@Test
	public void testFindById() {
		Employee returnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 1);
		logger.info("TEST FIND BY ID : --> {}", returnedEmployee);
		assertNotNull(returnedEmployee);
		assertEquals("Connor", returnedEmployee.getFirstName());
	}
	
	@Test
	public void testDeleteEmployee() {
		restTemplate.delete(URL_DELETE_EMPLOYEE_BY_ID, 2);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		for(Employee employee : returnedArray) {
			logger.info("TEST DELETE : --> {}", employee);
		}
		assertEquals(1, returnedArray.length);
	}
	
	@Test
	public void testCreateEmployee() {
		Employee newEmployee = Employee.builder()
				.firstName("Aa")
				.lastName("Bb")
				.birthDate(LocalDate.of(2000, 10, 20))
				.jobStartInTheCompany(LocalDate.of(2020, 05, 05))
				.companyAffiliation(0)
				.webSite(null)
				.user(User.builder()
						.username("Yy")
						.password(123)
						.start(LocalDate.now())
						.aktiv(false)
						.userRole(Role.DEVELOPER)
						.build())
				.build();
		newEmployee = restTemplate.postForObject(URL_POST_EMPLOYEE, newEmployee, Employee.class);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		for(Employee employee : returnedArray) {
			logger.info("TEST CREATE : --> {}", employee);
		}
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	public void testUpdateEmployee() {
		Employee returnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 1);
		returnedEmployee.setFirstName("Habib");
		restTemplate.put(URL_PUT_EMPLOYEE_BY_ID, returnedEmployee, 1);
		Employee modifiedReturnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 1);
		logger.info("TEST UPDATE : --> {}", returnedEmployee);
		assertEquals("Habib", modifiedReturnedEmployee.getFirstName());
	}
	
}
