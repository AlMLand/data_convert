package com.m_landalex.dataconvert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RestClientConfig.class })
public class RestClientUserTest {

	private static final Logger logger = LoggerFactory.getLogger(RestClientUserTest.class);
	private static final String URL_GET_ALL_USERS = "http://localhost:8080/dataconvert/users/listUsers";
	private static final String URL_GET_USER_BY_ID = "http://localhost:8080/dataconvert/users/{id}";
	private static final String URL_CREATE_USER = "http://localhost:8080/dataconvert/users/";
	private static final String URL_PUT_USER = "http://localhost:8080/dataconvert/users/{id}";
	private static final String URL_DELETE_USER = "http://localhost:8080/dataconvert/users/{id}";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		assertNotNull(restTemplate);
	}
	
	@Test
	public void testListUsers() {
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USERS, User[].class);
		for(User user : returnedArray) {
			logger.info("TEST FIND ALL : --> {}", user);
		}
		assertEquals(2, returnedArray.length);
	}
	
	@Test
	public void testFindById() {
		User returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 2);
		logger.info("TEST FIND USER BY ID : --> {}", returnedUser);
		assertEquals("Diamant", returnedUser.getUsername());
	}
	
	@Test
	public void testCreateUser() {
		User newUser = User.builder()
				.username("Max")
				.password(123)
				.start(LocalDate.now())
				.aktiv(false)
				.userRole(Role.SUPPORTER)
				.build();
		restTemplate.postForObject(URL_CREATE_USER, newUser, User.class);
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USERS, User[].class);
		for(User user : returnedArray) {
			logger.info("TEST CREATE USER : --> {}", user);
		}
		assertEquals(3, returnedArray.length);
	}
	
	@Test
	public void testUpdateUser() {
		User returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		returnedUser.setUsername("HuHu");
		restTemplate.put(URL_PUT_USER, returnedUser, 1);
		returnedUser = restTemplate.getForObject(URL_GET_USER_BY_ID, User.class, 1);
		assertEquals("HuHu", returnedUser.getUsername());
	}
	
	@Test
	public void testDeleteUser() {
		restTemplate.delete(URL_DELETE_USER, 3);
		User[] returnedArray = restTemplate.getForObject(URL_GET_ALL_USERS, User[].class);
		assertEquals(2, returnedArray.length);
	}
	
}
