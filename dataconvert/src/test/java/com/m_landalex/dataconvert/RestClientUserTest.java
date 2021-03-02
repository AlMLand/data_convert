package com.m_landalex.dataconvert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.m_landalex.dataconvert.configuration.restclient.RestClientConfig;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { RestClientConfig.class })
public class RestClientUserTest {

	private static final Logger logger = LoggerFactory.getLogger(RestClientUserTest.class);
	private static final String URL_POST_USER = "http://localhost:8080/dataconvert/rest/employees/users/";
	private static final String URL_GET_ALL_USERS = "http://localhost:8080/dataconvert/rest/employees/users/";
	private static final String URL_DELETE_ALL_USERS = "http://localhost:8080/dataconvert/rest/employees/users/";
	private static final String URL_GET_USER_BY_ID = "http://localhost:8080/dataconvert/rest/employees/users/{id}";
	private static final String URL_PUT_USER_BY_ID = "http://localhost:8080/dataconvert/rest/employees/users/{id}";
	private static final String URL_DELETE_USER_BY_ID = "http://localhost:8080/dataconvert/rest/employees/users/{id}";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() throws ResourceNullException {
		assertNotNull(restTemplate);
	}
	
	@Test
	public void a_testCreateUser() {
		User newUser = User.builder()
				.username("Connor").password(123).start(LocalDate.now()).aktiv(false).userRole(Role.SUPPORTER).build();
		restTemplate.postForObject(URL_POST_USER, newUser, User.class);
		newUser = User.builder()
				.username("Dustin").password(123).start(LocalDate.now()).aktiv(false).userRole(Role.SUPPORTER).build();
		restTemplate.postForObject(URL_POST_USER, newUser, User.class);
		
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USERS, User[].class);
		for(User user : returnedArray) {
			logger.info("TEST CREATE USER : --> {}", user);
		}
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	public void d_testFetchAllUsers() {
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USERS, User[].class);
		for(User user : returnedArray) {
			logger.info("TEST FIND ALL : --> {}", user);
		}
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	public void f_testDeleteAllUsers() {
		restTemplate.delete(URL_DELETE_ALL_USERS);
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USERS, User[].class);
		assertEquals(0, returnedArray.length);
	}
	
	@Test
	public void b_testFindUserById() {
		User returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		logger.info("TEST FIND USER BY ID : --> {}", returnedUser);
		assertEquals("Connor", returnedUser.getUsername());
	}
	
	@Test
	public void c_testUpdateUserById() {
		User returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		returnedUser.setUsername("TestUserName");
		restTemplate.put(URL_PUT_USER_BY_ID, returnedUser, 1);
		returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		assertEquals("TestUserName", returnedUser.getUsername());
	}
	
	@Test
	public void e_testDeleteUserById() {
		restTemplate.delete(URL_DELETE_USER_BY_ID, 1);
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USERS, User[].class);
		assertEquals(1, returnedArray.length);
	}
	
}
