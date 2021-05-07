package com.m_landalex.dataconvert.controller.restcontroller;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
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
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.DefaultService;
import com.m_landalex.dataconvert.view.controller.rest.RestEmployeeController;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class RestEmployeeControllerTest {
	
	@Mock
	private DefaultService mockedDefaultService;
	private RestEmployeeController employeeController;
	private List<AbstractObject> listEmployee = new ArrayList<>();

	@Before
	public void setUp() throws MalformedURLException {
		employeeController = new RestEmployeeController();
		User user = User.builder().username("TestUserName_1").password("12345").start(LocalDate.now()).aktiv(true)
				.userRole(List.of(Role.builder().role("DEVELOPER").build())).build();
		user.setId(1L);
		user.setVersion(0);
		Employee employee = Employee.builder().firstName("TestFirstName_1").lastName("TestLastName_1")
				.birthDate(LocalDate.of(2000, 10, 10)).jobStartInTheCompany(LocalDate.of(2019, 10, 10))
				.companyAffiliation(0).webSite(new URL("http://test_1.com/")).user(user).build();
		employee.setId(1L);
		employee.setVersion(0);
		listEmployee.add(employee);
	}

	@Test
	public void fetchAllEmployeeTest() {
		Mockito.when(mockedDefaultService.fetchAll()).thenReturn(listEmployee);

		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);

		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("listAllEmployees", mockedDefaultService.fetchAll());
		@SuppressWarnings("unchecked")
		List<AbstractObject> returnedList = (List<AbstractObject>) extendedModelMap.get("listAllEmployees");

		assertEquals(1, returnedList.size());
	}
	
	@Test
	public void createEmployeeTest() throws ResourceNullException, MalformedURLException {
		User user = User.builder().username("TestUserName_2").password("12345").start(LocalDate.now()).aktiv(true)
				.userRole(List.of(Role.builder().role("ADMINISTRATOR").build())).build();
		user.setId(2L);
		user.setVersion(0);
		Employee employee = Employee.builder().firstName("TestFirstName_2").lastName("TestLastName_2")
				.birthDate(LocalDate.of(2000, 10, 10)).jobStartInTheCompany(LocalDate.of(2019, 10, 10))
				.companyAffiliation(0).webSite(new URL("http://test_2.com/")).user(user).build();
		employee.setId(2L);
		employee.setVersion(0);
		
		Mockito.when(mockedDefaultService.save(employee)).thenAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployee.add(employee);
				return employee;
			}
		});
		
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("createEmployee", employeeController.createEmployee(employee));
		
		Employee returnedEmployee = (Employee) extendedModelMap.get("createEmployee");
		assertEquals("TestFirstName_2", returnedEmployee.getFirstName());
		assertEquals("TestLastName_2", returnedEmployee.getLastName());
		assertEquals(LocalDate.of(2000, 10, 10), returnedEmployee.getBirthDate());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createEmployeeTestShouldThrowIllegalArgumentExceptionTest() throws ResourceNullException {
		Employee employee = null;
		Mockito.when(mockedDefaultService.save(employee)).thenThrow(IllegalArgumentException.class);
		
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeController.createEmployee(employee);
	}
	
	@Test(expected = NullPointerException.class)
	public void createEmployeeTestShouldThrowNullPointerExceptionTest()	throws MalformedURLException, ResourceNullException {
		User user = null;
		Mockito.when(mockedDefaultService.save(Mockito.any(Employee.class))).thenThrow(NullPointerException.class);

		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);

		employeeController.createEmployee(Employee.builder().firstName("TestFirstName_2").lastName("TestLastName_2")
				.birthDate(LocalDate.of(2000, 10, 10)).jobStartInTheCompany(LocalDate.of(2019, 10, 10))
				.companyAffiliation(0).webSite(new URL("http://test_2.com/")).user(user).build());
	}
	
	@Test
	public void deleteAllEmployeesTest() {
		Mockito.doAnswer(new Answer<List<AbstractObject>>() {

			@Override
			public List<AbstractObject> answer(InvocationOnMock invocation) throws Throwable {
				listEmployee.clear();
				return listEmployee;
			}
		}).when(mockedDefaultService).deleteAll();
		
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeController.deleteAllEmployees();
		assertEquals(0, listEmployee.size());
	}
	
	@Test
	public void deleteEmployeeByIdTest() {
		Mockito.doAnswer(new Answer<AbstractObject>() {

			@Override
			public AbstractObject answer(InvocationOnMock invocation) throws Throwable {
				return listEmployee.remove(0);
			}
		}).when(mockedDefaultService).deleteById(Mockito.anyLong());
		
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeController.deleteEmployeeById(1L);
		assertEquals(0, listEmployee.size());
	}

	@Test
	public void fetchEmployeeByIdTest() {
		Mockito.when(mockedDefaultService.fetchById(Mockito.anyLong())).thenReturn(listEmployee.get(0));
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("fetchEmployeeById", employeeController.fetchEmployeeById(Mockito.anyLong()));
		Employee returnedEmployee = (Employee) extendedModelMap.get("fetchEmployeeById");
		
		assertEquals("TestFirstName_1", returnedEmployee.getFirstName());
		assertEquals("TestLastName_1", returnedEmployee.getLastName());
		assertEquals(LocalDate.of(2000, 10, 10), returnedEmployee.getBirthDate());
	}
	
	@Test
	public void updateEmployeeByIdTest() throws MalformedURLException, ResourceNullException {
		User user = User.builder().username("TestUserName_2").password("12345").start(LocalDate.now()).aktiv(true)
				.userRole(List.of(Role.builder().role("ADMINISTRATOR").build())).build();
		Employee employee = Employee.builder().firstName("TestFirstName_2").lastName("TestLastName_2")
				.birthDate(LocalDate.of(2000, 10, 10)).jobStartInTheCompany(LocalDate.of(2019, 10, 10))
				.companyAffiliation(0).webSite(new URL("http://test_2.com/")).user(user).build();
		
		Mockito.doAnswer(new Answer<AbstractObject>() {

			@Override
			public AbstractObject answer(InvocationOnMock invocation) throws Throwable {
				Employee returnedEmployee = (Employee) listEmployee.get(0);
				returnedEmployee.setFirstName(employee.getFirstName());
				returnedEmployee.setLastName(employee.getLastName());
				returnedEmployee.setBirthDate(employee.getBirthDate());
				returnedEmployee.setJobStartInTheCompany(employee.getJobStartInTheCompany());
				returnedEmployee.setCompanyAffiliation(employee.getCompanyAffiliation());
				returnedEmployee.setWebSite(employee.getWebSite());
				returnedEmployee.setVersion(returnedEmployee.getVersion() + 1);
				listEmployee.remove(0);
				listEmployee.add(returnedEmployee);
				return returnedEmployee;
			}
		}).when(mockedDefaultService).save(employee);
		
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		employeeController.updateEmployeeById(employee);
		Employee updatedEmployee = (Employee) listEmployee.get(0);
		
		assertEquals(1, listEmployee.size());
		assertEquals("TestFirstName_2", updatedEmployee.getFirstName());
		assertEquals("TestLastName_2", updatedEmployee.getLastName());
	}
	
	@Test(expected = RuntimeException.class)
	public void updateEmployeeByIdShouldThrowRuntimeExceptionTest() throws ResourceNullException {
		Mockito.doThrow(RuntimeException.class).when(mockedDefaultService).save(Mockito.any(Employee.class));
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeController.updateEmployeeById(new Employee());
	}
	
}
