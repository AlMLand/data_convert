package com.m_landalex.dataconvert.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.m_landalex.dataconvert.configuration.AppServiceConfig;
import com.m_landalex.dataconvert.configuration.TransactionManagerConfig;
import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.service.configuration.TestConfig;

@ContextConfiguration(classes = { TestConfig.class, AppServiceConfig.class, TransactionManagerConfig.class } )
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class EmployeeServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceTest.class);
	
	@Autowired
	@Qualifier("employeeService")
	private DefaultService defaultService;
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@Test
	public void fetchAllTest() {
		List<AbstractObject> returnedList = defaultService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql", 
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@Test
	public void fetchByIdTest() {
		Employee returnedEmployee = (Employee) defaultService.fetchById(1L);
		assertNotNull(returnedEmployee);
		assertEquals("Test1_First_Name", returnedEmployee.getFirstName());
		assertEquals("Test1_Last_Name", returnedEmployee.getLastName());
		assertEquals(LocalDate.of(1985, 06, 05), returnedEmployee.getBirthDate());
		assertEquals(LocalDate.of(2018, 10, 10), returnedEmployee.getJobStartInTheCompany());
	}

}