package com.m_landalex.dataconvert.controller.webcontroller;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.m_landalex.dataconvert.configuration.webconfiguration.WebConfig;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.service.DefaultService;
import com.m_landalex.dataconvert.service.EmployeeService;
import com.m_landalex.dataconvert.view.controller.web.EmployeeController;

@ActiveProfiles("test2")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class WebEmployeeControllerTest {

	@Autowired
	private EmployeeController employeeController;
	private Employee employeeTEST1, employeeTEST2;
	private DefaultService mockedDefaultService;
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws MalformedURLException {
		mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
		mockedDefaultService = mock(EmployeeService.class);
		// manually injections
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeTEST1 = Employee.builder().firstName("FirstnameTEST1").lastName("LastnameTEST1")
				.birthDate(LocalDate.of(2000, 01, 01)).jobStartInTheCompany(LocalDate.of(2022, 01, 01))
				.companyAffiliation(0).description("descriptionTEST1").photo(null)
				.webSite(new URL("http://employeeTEST1.com/"))
				.user(User.builder().username("UsernameTEST1")
						.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
						.start(LocalDate.of(2022, 01, 01)).aktiv(true)
						.userRole(List.of(Role.builder().role("ADMINISTRATOR").build())).build())
				.build();
		employeeTEST1.setId(Long.valueOf(1));
		employeeTEST2 = Employee.builder().firstName("FirstnameTEST2").lastName("LastnameTEST2")
				.birthDate(LocalDate.of(2000, 02, 02)).jobStartInTheCompany(LocalDate.of(2022, 02, 02))
				.companyAffiliation(0).description("descriptionTEST2").photo(null)
				.webSite(new URL("http://employeeTEST2.com/"))
				.user(User.builder().username("UsernameTEST2")
						.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
						.start(LocalDate.of(2022, 02, 02)).aktiv(true)
						.userRole(List.of(Role.builder().role("ADMINISTRATOR").build())).build())
				.build();
		employeeTEST2.setId(Long.valueOf(2));
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void list_ShouldAddEmployeeToModelAndRenderEmployeeListView() throws Exception {
		when(mockedDefaultService.fetchAll()).thenReturn(List.of(employeeTEST1, employeeTEST2));
		
		mockMvc.perform(get("/employees"))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/list"))
				.andExpect(forwardedUrl("employees/list"))
				.andExpect(model().attribute("employees", hasSize(2)))
				.andExpect(model().attribute("employees", hasItem(
						allOf(
							hasProperty("id", is(1L)),
							hasProperty("firstName", is("FirstnameTEST1")),
							hasProperty("lastName", is("LastnameTEST1"))
							)
						)))
				.andExpect(model().attribute("employees", hasItem(
						allOf(
							hasProperty("id", is(2L)),
							hasProperty("firstName", is("FirstnameTEST2")),
							hasProperty("lastName", is("LastnameTEST2"))
							)
						)));
		
		verify(mockedDefaultService, times(1)).fetchAll();
		verifyNoMoreInteractions(mockedDefaultService);
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void show_EmployeeFound_ShouldAddEmployeeToModelAndRenderViewEmployeeView() throws Exception {
		when(mockedDefaultService.fetchById(1L)).thenReturn(employeeTEST1);
		
		mockMvc.perform(get("/employees/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/show"))
				.andExpect(forwardedUrl("employees/show"))
				.andExpect(model().attribute("employee", hasProperty("id", is(1L))))
				.andExpect(model().attribute("employee", hasProperty("firstName", is("FirstnameTEST1"))))
				.andExpect(model().attribute("employee", hasProperty("lastName", is("LastnameTEST1"))));
		
		verify(mockedDefaultService, times(1)).fetchById(1L);
		verifyNoMoreInteractions(mockedDefaultService);
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void updateForm_EmployeeFound_ShouldAddEmployeeToModelAndRenderViewEmployeeView() throws Exception {
		when(mockedDefaultService.fetchById(1L)).thenReturn(employeeTEST1);
		
		mockMvc.perform(get("/employees/edits/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/update"))
				.andExpect(forwardedUrl("employees/update"))
				.andExpect(model().attribute("employee", hasProperty("id", is(1L))))
				.andExpect(model().attribute("employee", hasProperty("firstName", is("FirstnameTEST1"))))
				.andExpect(model().attribute("employee", hasProperty("lastName", is("LastnameTEST1"))));
		
		verify(mockedDefaultService, times(1)).fetchById(1L);
		verifyNoMoreInteractions(mockedDefaultService);
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void update_FirstNameAndLastNameAndUserAreFail_ShouldRenderFormViewAndReturnValidationErrorsForFirstnameAndLastnameAndUser()
			throws Exception {
		when(mockedDefaultService.save(Mockito.any(Employee.class))).thenReturn(employeeTEST1);
		
		mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", "a".repeat(100))
				.param("lastName", "b".repeat(100))
				.param("birthDate", LocalDate.of(2000, 01, 01).toString())
				.param("jobStartInTheCompany", LocalDate.of(2022, 01, 01).toString())
				.param("companyAffiliation", "0")
				.param("description", "descriptionTEST")
				.param("photo", "")
				.param("webSite", "http://employeeTEST1.com/")
				.param("user", "null"))
		.andExpect(status().isOk()).andDo(print())
		.andExpect(view().name("employees/update"))
		.andExpect(forwardedUrl("employees/update"))
		.andExpect(model().errorCount(3))
		.andExpect(model().attributeHasFieldErrors("employee", "firstName"))
		.andExpect(model().attributeHasFieldErrors("employee", "lastName"))
		.andExpect(model().attributeHasFieldErrors("employee", "user"))
		.andExpect(model().attribute("employee", hasProperty("id", nullValue())))
		.andExpect(model().attribute("employee", hasProperty("user", nullValue())));
		
		verify(mockedDefaultService, times(0)).save(Mockito.any(Employee.class));
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void update_CreateNewEmployee_ShouldAddEmployeeEntryAndRenderViewEmployeeEntryView() throws Exception {
		when(mockedDefaultService.save(Mockito.any(Employee.class))).thenReturn(employeeTEST1);
		
		mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", "FirstnameTEST1")
				.param("lastName", "LastnameTEST1")
				.param("birthDate", LocalDate.of(2000, 01, 01).toString())
				.param("jobStartInTheCompany", LocalDate.of(2022, 01, 01).toString())
				.param("companyAffiliation", "0")
				.param("description", "descriptionTEST")
				.param("photo", "")
				.param("webSite", "http://employeeTEST1.com/")
				.param("user.username", "UsernameTEST1")
				.param("user.password", "$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
				.param("user.start", LocalDate.of(2022, 01, 01).toString())
				.param("user.aktiv", "true")
				.param("user.userRole", "ADMINISTRATOR"))
		.andExpect(status().isFound())
		.andDo(print())
		.andExpect(view().name("redirect:/employees"))
		.andExpect(redirectedUrl("/employees"))
		.andExpect(model().errorCount(0))
		.andExpect(model().attribute("employee", hasProperty("firstName", is("FirstnameTEST1"))))
		.andExpect(model().attribute("employee", hasProperty("description", is("descriptionTEST"))));
		
		ArgumentCaptor<Employee> captorEmployee = ArgumentCaptor.forClass(Employee.class);
		verify(mockedDefaultService, times(1)).save(captorEmployee.capture());
		verifyNoMoreInteractions(mockedDefaultService);
		
		Employee employee = captorEmployee.getValue();
		assertNull(employee.getId());
		assertEquals("descriptionTEST", employee.getDescription());
		assertEquals(LocalDate.of(2000, 01, 01), employee.getBirthDate());
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void update_ModifiedExistingEmployee_ShouldAddEmployeeEntryAndRenderViewEmployeeEntryView() throws Exception {
		when(mockedDefaultService.save(Mockito.any(Employee.class))).thenReturn(employeeTEST1);
		
		mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "1")
				.param("version", "0")
				.param("firstName", "FirstnameTEST1")
				.param("lastName", "LastnameTEST1")
				.param("birthDate", LocalDate.of(2000, 01, 01).toString())
				.param("jobStartInTheCompany", LocalDate.of(2022, 01, 01).toString())
				.param("companyAffiliation", "0")
				.param("description", "descriptionTEST")
				.param("photo", "")
				.param("webSite", "http://employeeTEST1.com/")
				.param("user.username", "UsernameTEST1")
				.param("user.password", "$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
				.param("user.start", LocalDate.of(2022, 01, 01).toString())
				.param("user.aktiv", "true")
				.param("user.userRole", "ADMINISTRATOR"))
		.andExpect(status().isFound())
		.andDo(print())
		.andExpect(view().name("redirect:/employees/1"))
		.andExpect(redirectedUrl("/employees/1"))
		.andExpect(model().errorCount(0))
		.andExpect(model().attribute("employee", hasProperty("id", is(1L))))
		.andExpect(model().attribute("employee", hasProperty("firstName", is("FirstnameTEST1"))))
		.andExpect(model().attribute("employee", hasProperty("description", is("descriptionTEST"))));
		
		ArgumentCaptor<Employee> captorEmployee = ArgumentCaptor.forClass(Employee.class);
		verify(mockedDefaultService, times(1)).save(captorEmployee.capture());
		verifyNoMoreInteractions(mockedDefaultService);
		
		Employee employee = captorEmployee.getValue();
		assertNotNull(employee.getId());
		assertEquals("FirstnameTEST1", employee.getFirstName());
		assertEquals("ADMINISTRATOR", employee.getUser().getUserRole().toString()
				.subSequence(1, employee.getUser().getUserRole().toString().length() - 1));

	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void createForm_ShouldReturnNewEmptyEmployeeAndRenderView() throws Exception {
		mockMvc.perform(get("/employees/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("employees/update"))
			.andExpect(forwardedUrl("employees/update"))
			.andDo(print())
			.andExpect(model().errorCount(0))
			.andExpect(model().attributeDoesNotExist("id", "version", "firstName", "lastName", "birthDate", "jobStartInTheCompany", 
					"companyAffiliation", "description", "photo", "webSite", "user.username", "user.password",
					"user.start", "user.aktiv", "user.userRole"));
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void delete_ShouldRemoveOneEmployeeInCollectionAndRenderView() throws Exception {
		List<Employee> employees = new ArrayList<>();
		employees.add(employeeTEST1);
		employees.add(employeeTEST2);
		assertEquals(2, employees.size());
		
		doAnswer(new Answer<Employee>() {

			@Override
			public Employee answer(InvocationOnMock invocation) throws Throwable {
				employees.remove(0);
				return null;
			}
		}).when(mockedDefaultService).deleteById(Mockito.anyLong());
		
		mockMvc.perform(get("/employees/delete/{id}", 1L))
			.andExpect(status().isFound())
			.andDo(print())
			.andExpect(redirectedUrl("/employees"))
			.andExpect(view().name("redirect:/employees"));
		
		assertEquals(1, employees.size());
	}
	
}
