package com.m_landalex.dataconvert.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintViolationException;

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
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.configuration.TestConfig;

@ContextConfiguration( classes = { TestConfig.class, AppServiceConfig.class, TransactionManagerConfig.class } )
@RunWith( SpringJUnit4ClassRunner.class )
@ActiveProfiles( "test" )
public class UserServiceTest {

	@Autowired
	@Qualifier( "userService" )
	private DefaultService defaultService;
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD) } )
	@Test
	public void fetchAllTest() {
		List<AbstractObject> returnedList = defaultService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(2, returnedList.size());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD) } )
	@Test
	public void fetchByIdTest() {
		User returnedUser = (User) defaultService.fetchById(1L);
		assertNotNull(returnedUser);
		assertEquals("12345", returnedUser.getPassword());
	}
	
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD) } )
	@Test
	public void saveTest() throws ResourceNullException {
		User newUser = User.builder().username("Test_SAVE").password("12345").start(LocalDate.of(2032, 04, 02))
				.userRole(List.of(Role.builder().role("DEVELOPER").build())).aktiv(true).build();
		defaultService.save(newUser);
		List<AbstractObject> returnedList = defaultService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(3, returnedList.size());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void saveWithConstraintViolationExceptionTest() throws ResourceNullException {
		User newUser = User.builder().username("T").password("12345").start(LocalDate.of(2010, 04, 02))
				.userRole(List.of(Role.builder().role("DEVELOPER").build())).aktiv(true).build();
		defaultService.save(newUser);
	}

	/*
	 * Only users who have no relationship with any employee are erased
	 */
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD) } )
	@Test
	public void deleteAllTest() {
		defaultService.deleteAll();
		List<AbstractObject> returnedList = defaultService.fetchAll();
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	/*
	 * Only users who have no relationship with any employee are erased
	 */
	@SqlGroup( { @Sql( value = "classpath:db/test-data.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.BEFORE_TEST_METHOD ),
			@Sql( value = "classpath:db/clean-up.sql",
			config = @SqlConfig( encoding = "utf-8", separator = ";", commentPrefix = "--" ),
			executionPhase = ExecutionPhase.AFTER_TEST_METHOD) } )
	@Test
	public void deleteByIdTest() {
		defaultService.deleteById(2L);
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
	public void getTotalCountTest() {
		Long totalCount = defaultService.getTotalCount();
		assertNotNull(totalCount);
		assertEquals(Long.valueOf(2L), totalCount);
	}
	
}
