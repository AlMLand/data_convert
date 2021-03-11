package com.m_landalex.dataconvert.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.DefaultService;
import com.m_landalex.dataconvert.view.controller.UserController;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class UserControllerTest {

	@Mock 
	private DefaultService mockedDefaultService;
	private UserController userController;
	private List<AbstractObject> listUsers = new ArrayList<>();
	
	@Before
	public void setUp() {
		userController = new UserController();
		User user = User.builder().username("Test_1").password(123).start(LocalDate.now()).aktiv(false)
				.userRole(Role.DEVELOPER).build();
		user.setId(1L);
		user.setVersion(0);
		listUsers.add(user);
	}
	
	@Test
	public void createUserTest() throws ResourceNullException {
		User newUser = User.builder().username("Test_2").password(12345).start(LocalDate.now()).aktiv(false)
				.userRole(Role.SUPPORTER).build();
		newUser.setId(2L);
		newUser.setVersion(0);
		
		Mockito.when(mockedDefaultService.save(newUser)).thenAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				listUsers.add(newUser);
				return newUser;
			}
		});
		
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("createUser", userController.createUser(newUser));
		User returnedUser = (User) extendedModelMap.get("createUser");
		
		assertEquals(2, listUsers.size());
		assertNotNull(returnedUser);
		assertEquals("Test_2", returnedUser.getUsername());
		assertEquals(Integer.valueOf(12345), returnedUser.getPassword());
	}
	
	@Test(expected = RuntimeException.class)
	public void createUserShouldThrowRuntimeExceptionTest() throws ResourceNullException {
		User newUser = null;
		Mockito.when(mockedDefaultService.save(newUser)).thenThrow(RuntimeException.class);
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
		User returnedUser = userController.createUser(newUser);
		
		assertNull(returnedUser);
	}
	
	@Test
	public void fetchAllUsersTest() {
		Mockito.when(mockedDefaultService.fetchAll()).thenReturn(listUsers);
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("fetchAllUsers", userController.fetchAllUsers());
		@SuppressWarnings("unchecked")
		List<AbstractObject> returnedList = (List<AbstractObject>) extendedModelMap.get("fetchAllUsers");
		
		assertNotNull(returnedList);
		assertEquals(1, returnedList.size());
	}
	
	@Test
	public void deleteAllUsersTest() {
		Mockito.doAnswer(new Answer<AbstractObject>() {

			@Override
			public AbstractObject answer(InvocationOnMock invocation) throws Throwable {
				listUsers.clear();
				return null;
			}
		}).when(mockedDefaultService).deleteAll();
		
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		userController.deleteAllUsers();
		
		assertEquals(0, listUsers.size());
	}
	
	@Test
	public void fetchUserByIdTest() {
		Mockito.when(mockedDefaultService.fetchById(Mockito.anyLong())).thenReturn(listUsers.get(0));
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("fetchUserById", userController.fetchUserById(Mockito.anyLong()));
		User returnedUser = (User) extendedModelMap.get("fetchUserById");
		
		assertNotNull(returnedUser);
		assertEquals("Test_1", returnedUser.getUsername());
		assertEquals(Integer.valueOf(123), returnedUser.getPassword());
	}
	
	@Test
	public void updateUserByIdTest() throws ResourceNullException {
		User newUser = User.builder().username("Test_2").password(12345).start(LocalDate.now()).aktiv(false)
				.userRole(Role.SUPPORTER).build();
		Mockito.doAnswer(new Answer<AbstractObject>() {

			@Override
			public AbstractObject answer(InvocationOnMock invocation) throws Throwable {
				User returnedUser = (User) listUsers.get(0);
				returnedUser.setUsername(newUser.getUsername());
				returnedUser.setPassword(newUser.getPassword());
				returnedUser.setStart(newUser.getStart());
				returnedUser.setAktiv(newUser.getAktiv());
				returnedUser.setUserRole(newUser.getUserRole());
				listUsers.clear();
				listUsers.add(returnedUser);
				return null;
			}
		}).when(mockedDefaultService).save(newUser);
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
		userController.updateUserById(newUser);

		User updatedUser = (User) listUsers.get(0);
		assertEquals(1, listUsers.size());
		assertEquals("Test_2", updatedUser.getUsername());
		assertEquals(false, updatedUser.getAktiv());
	}
	
	@Test
	public void deleteUserByIdTest() {
		User returnedUser = (User) listUsers.get(0);
		Mockito.doAnswer(new Answer<AbstractObject>() {

			@Override
			public AbstractObject answer(InvocationOnMock invocation) throws Throwable {
				listUsers.remove(0);
				return null;
			}
		}).when(mockedDefaultService).deleteById(returnedUser.getId());
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
		userController.deleteUserById(returnedUser.getId());
		
		assertNotNull(listUsers);
		assertEquals(0, listUsers.size());
	}
	
}
