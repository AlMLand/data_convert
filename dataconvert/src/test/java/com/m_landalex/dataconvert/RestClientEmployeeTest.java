package com.m_landalex.dataconvert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.m_landalex.dataconvert.configuration.httpremotingclient.HttpInvokerClientConfigEmployee;
import com.m_landalex.dataconvert.configuration.restclient.RestClientConfig;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.DefaultService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RestClientConfig.class, HttpInvokerClientConfigEmployee.class })
public class RestClientEmployeeTest {

	private static final Logger logger = LoggerFactory.getLogger(RestClientEmployeeTest.class);
	
	private static final String URL_POST_EMPLOYEE = "http://localhost:8080/dataconvert/rest/employees/";
	private static final String URL_GET_ALL_EMPLOYEES = "http://localhost:8080/dataconvert/rest/employees/";
	private static final String URL_DELETE_ALL_EMPLOYEES = "http://localhost:8080/dataconvert/rest/employees/";
	private static final String URL_GET_EMPLOYEE_BY_ID = "http://localhost:8080/dataconvert/rest/employees/{id}";
	private static final String URL_PUT_EMPLOYEE_BY_ID = "http://localhost:8080/dataconvert/rest/employees/{id}";
	private static final String URL_DELETE_EMPLOYEE_BY_ID = "http://localhost:8080/dataconvert/rest/employees/{id}";

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	@Qualifier(value = "emploeeService")
	private DefaultService defaultService;

	@Before
	public void setUp() throws MalformedURLException, ResourceNullException {
		User user = User.builder().username("Chicken").password(123).start(LocalDate.of(2025, 10, 10)).aktiv(false)
				.userRole(Role.DEVELOPER).build();
		Employee employee = Employee.builder().firstName("Connor").lastName("McGregor")
				.birthDate(LocalDate.of(2001, 01, 01)).jobStartInTheCompany(LocalDate.of(2010, 04, 04))
				.companyAffiliation(0).webSite(new URL("http://connor.com/")).user(user).build();
		defaultService.save(employee);

		user = User.builder().username("Diamant").password(123).start(LocalDate.of(2032, 8, 8)).aktiv(false)
				.userRole(Role.ADMINISTRATOR).build();
		employee = Employee.builder().firstName("Dustin").lastName("Poirier").birthDate(LocalDate.of(1993, 03, 03))
				.jobStartInTheCompany(LocalDate.of(2018, 01, 15)).companyAffiliation(0)
				.webSite(new URL("http://dustin.com/")).user(user).build();
		defaultService.save(employee);
		
		assertNotNull(restTemplate);
		logger.info("Start test .....");
	}
	
	@After
	public void tearDown() {
		defaultService.deleteAll();
		logger.info("End test .....");
	}

	@Test
	public void testCreateEmployee() {
		Employee newEmployee = Employee.builder().firstName("TEST").lastName("test").birthDate(LocalDate.of(2000, 10, 20))
				.jobStartInTheCompany(LocalDate.of(2020, 05, 05)).companyAffiliation(0).webSite(null)
				.user(User.builder().username("Yy").password(123).start(LocalDate.now()).aktiv(false)
						.userRole(Role.DEVELOPER).build())
				.build();
		newEmployee = restTemplate.postForObject(URL_POST_EMPLOYEE, newEmployee, Employee.class);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		for (Employee employee : returnedArray) {
			logger.info("TEST CREATE : --> {}", employee);
		}
		assertEquals(3, returnedArray.length);
	}

	@Test
	public void testFetchAllEmployees() {
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		for (Employee employee : returnedArray) {
			logger.info("TEST FIND ALL : --> {}", employee);
		}
		assertTrue(returnedArray.length == 2);
	}

	@Test
	public void testDeleteAllEmployee() {
		restTemplate.delete(URL_DELETE_ALL_EMPLOYEES);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		assertEquals(0, returnedArray.length);
	}

	@Test
	public void testFetchEmployeeById() {
		Employee returnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 7);
		logger.info("TEST FIND BY ID : --> {}", returnedEmployee);
		assertNotNull(returnedEmployee);
		assertEquals("Connor", returnedEmployee.getFirstName());
	}

	@Test
	public void testDeleteEmployeeById() {
		restTemplate.delete(URL_DELETE_EMPLOYEE_BY_ID, 3);
		Employee[] returnedArray = restTemplate.getForObject(URL_GET_ALL_EMPLOYEES, Employee[].class);
		for (Employee employee : returnedArray) {
			logger.info("TEST DELETE : --> {}", employee);
		}
		assertEquals(1, returnedArray.length);
	}

	@Test
	public void testUpdateEmployeeById() {
		Employee returnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 5);
		returnedEmployee.setFirstName("TEST");
		restTemplate.put(URL_PUT_EMPLOYEE_BY_ID, returnedEmployee, 1);
		Employee modifiedReturnedEmployee = restTemplate.getForObject(URL_GET_EMPLOYEE_BY_ID, Employee.class, 5);
		logger.info("TEST UPDATE : --> {}", returnedEmployee);
		assertEquals("TEST", modifiedReturnedEmployee.getFirstName());
	}

}
