package com.m_landalex.dataconvert.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.configuration.TestConfig;

@ContextConfiguration(classes = { TestConfig.class, AppServiceConfig.class, TransactionManagerConfig.class } )
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class EmployeeServiceTest {
	
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
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@Test
	public void saveTest() throws MalformedURLException, ResourceNullException {
		Employee newEmployee = new Employee();
		newEmployee.setId(2L);
		newEmployee.setVersion(0);
		newEmployee.setFirstName("Test_save_new_employee_firstname");
		newEmployee.setLastName("Test_save_new_employee_lastname");
		newEmployee.setBirthDate(LocalDate.of(1987, 02, 02));
		newEmployee.setJobStartInTheCompany(LocalDate.of(2018, 04, 03));
		newEmployee.setCompanyAffiliation(0);
		newEmployee.setWebSite(new URL("http://test_new_employee.com/"));
		newEmployee.setUser(null);
		defaultService.save(newEmployee);
		
		List<AbstractObject> returnedList = defaultService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", blockCommentStartDelimiter = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@Test
	public void deleteAllTest() {
		defaultService.deleteAll();
		List<AbstractObject> returnedList = defaultService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(0, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD ) } )
	@Test
	public void deleteByIdTest() {
		defaultService.deleteById(1L);
		List<AbstractObject> returnedList = defaultService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(0, returnedList.size());
	}

}