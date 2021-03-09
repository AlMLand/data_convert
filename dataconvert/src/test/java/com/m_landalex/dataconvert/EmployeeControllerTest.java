package com.m_landalex.dataconvert;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.DefaultService;
import com.m_landalex.dataconvert.service.EmployeeService;
import com.m_landalex.dataconvert.view.controller.EmployeeController;

public class EmployeeControllerTest {

	private List<AbstractObject> listEmployee = new ArrayList<>();

	@Before
	public void setUp() throws MalformedURLException {
		User user = User.builder().username("TestUserName_1").password(12345).start(LocalDate.now()).aktiv(true)
				.userRole(Role.DEVELOPER).build();
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
		DefaultService mockedDefaultService = Mockito.mock(EmployeeService.class, "TEST_FetchAllEmployeeTest");
		Mockito.when(mockedDefaultService.fetchAll()).thenReturn(listEmployee);

		EmployeeController employeeController = new EmployeeController();
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);

		ExtendedModelMap extendedModelMap = new ExtendedModelMap();
		extendedModelMap.addAttribute("listAllEmployees", mockedDefaultService.fetchAll());
		@SuppressWarnings("unchecked")
		List<AbstractObject> returnedList = (List<AbstractObject>) extendedModelMap.get("listAllEmployees");

		assertEquals(1, returnedList.size());
	}
	
	@Test
	public void createEmployeeTest() throws ResourceNullException, MalformedURLException {
		User user = User.builder().username("TestUserName_2").password(12345).start(LocalDate.now()).aktiv(true)
				.userRole(Role.ADMINISTRATOR).build();
		user.setId(2L);
		user.setVersion(0);
		Employee employee = Employee.builder().firstName("TestFirstName_2").lastName("TestLastName_2")
				.birthDate(LocalDate.of(2000, 10, 10)).jobStartInTheCompany(LocalDate.of(2019, 10, 10))
				.companyAffiliation(0).webSite(new URL("http://test_2.com/")).user(user).build();
		employee.setId(2L);
		employee.setVersion(0);
		
		DefaultService mockedDefaultService = Mockito.mock(EmployeeService.class, "TEST_CreateEmployeeTest");
		Mockito.when(mockedDefaultService.save(employee)).thenAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				listEmployee.add(employee);
				return employee;
			}
		});
		
		EmployeeController employeeController = new EmployeeController();
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
		DefaultService mockedDefaultService = Mockito.mock(EmployeeService.class, "TEST_CreateEmployeeTestShouldThrowIllegalArgumentExceptionTest");
		Mockito.when(mockedDefaultService.save(employee)).thenThrow(IllegalArgumentException.class);
		
		EmployeeController employeeController = new EmployeeController();
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeController.createEmployee(employee);
	}
	
	@Test(expected = NullPointerException.class)
	public void createEmployeeTestShouldThrowNullPointerExceptionTest()	throws MalformedURLException, ResourceNullException {
		User user = null;
		DefaultService mockedDefaultService = Mockito.mock(EmployeeService.class, "createEmployeeTestShouldThrowNullPointerExceptionTest");
		Mockito.when(mockedDefaultService.save(Mockito.any(Employee.class))).thenThrow(NullPointerException.class);

		EmployeeController employeeController = new EmployeeController();
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);

		employeeController.createEmployee(Employee.builder().firstName("TestFirstName_2").lastName("TestLastName_2")
				.birthDate(LocalDate.of(2000, 10, 10)).jobStartInTheCompany(LocalDate.of(2019, 10, 10))
				.companyAffiliation(0).webSite(new URL("http://test_2.com/")).user(user).build());
	}
	
	@Test
	public void deleteAllEmployeesTest() {
		DefaultService mockedDefaultService = Mockito.mock(EmployeeService.class, "TEST_deleteAllEmployeesTest");
		Mockito.doAnswer(new Answer<List<AbstractObject>>() {

			@Override
			public List<AbstractObject> answer(InvocationOnMock invocation) throws Throwable {
				listEmployee.clear();
				return listEmployee;
			}
		}).when(mockedDefaultService).deleteAll();
		
		EmployeeController employeeController = new EmployeeController();
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeController.deleteAllEmployees();
		assertEquals(0, listEmployee.size());
	}
	
}
