package com.m_landalex.dataconvert.controller.restcontroller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m_landalex.dataconvert.configuration.webconfiguration.WebConfig;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.service.DefaultService;
import com.m_landalex.dataconvert.service.EmployeeService;
import com.m_landalex.dataconvert.view.controller.rest.RestEmployeeController;

@ActiveProfiles("test3")
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class RestEmployeeControllerIntegrationTest {

	@Autowired private RestEmployeeController restEmployeeController;
	@Autowired private ObjectMapper objectMapper;
	private DefaultService mockedDefaultService;
	private Employee employeeTEST1, employeeTEST2;
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws MalformedURLException {
		mockMvc = MockMvcBuilders.standaloneSetup(restEmployeeController).build();
		mockedDefaultService = mock(EmployeeService.class);
		// manually injections
		ReflectionTestUtils.setField(restEmployeeController, "defaultService", mockedDefaultService);
		
		Role role = Role.builder().role("ADMINISTRATOR").build();
		role.setId(Long.valueOf(1));
		User user = User.builder().username("UsernameTEST1")
				.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
				.start(LocalDate.of(2022, 01, 01)).aktiv(true).userRole(List.of(role)).build();
		user.setId(Long.valueOf(1));
		employeeTEST1 = Employee.builder().firstName("FirstnameTEST1").lastName("LastnameTEST1")
				.birthDate(LocalDate.of(2000, 01, 01)).jobStartInTheCompany(LocalDate.of(2022, 01, 01))
				.companyAffiliation(0).description("descriptionTEST1").photo("photoTEST".getBytes())
				.webSite(new URL("http://employeeTEST1.com/"))
				.user(user)
				.build();
		employeeTEST1.setId(Long.valueOf(1));
		User user2 = User.builder().username("UsernameTEST2")
				.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
				.start(LocalDate.of(2022, 02, 02)).aktiv(true).userRole(List.of(role)).build();
		user2.setId(Long.valueOf(2));
		employeeTEST2 = Employee.builder().firstName("FirstnameTEST2").lastName("LastnameTEST2")
				.birthDate(LocalDate.of(2000, 02, 02)).jobStartInTheCompany(LocalDate.of(2022, 02, 02))
				.companyAffiliation(0).description("descriptionTEST2").photo("photoTEST".getBytes())
				.webSite(new URL("http://employeeTEST2.com/"))
				.user(user2)
				.build();
		employeeTEST2.setId(Long.valueOf(2));
	}
	
	@Test
	public void createEmployee_WhenValidInputThenReturn200_VerifyingDeserializedFromHTTPRequest() throws Exception {
		mockMvc.perform(post("/rest/employees/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeTEST1)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void createEmployee_WhenFirstnameAndUserNullValueThenReturn400_VerifyingValidation() throws JsonProcessingException, Exception {
		Employee employeeNotValid = Employee.builder().firstName(null).lastName("LastnameTEST1")
				.birthDate(LocalDate.of(2000, 01, 01)).jobStartInTheCompany(LocalDate.of(2022, 01, 01))
				.companyAffiliation(0).description("descriptionTEST1").photo("photoTEST".getBytes())
				.webSite(new URL("http://employeeTEST1.com/")).user(null).build();
		employeeNotValid.setId(Long.valueOf(1));
		
		mockMvc.perform(post("/rest/employees/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeNotValid)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void createEmployee_WhenValidInputThenMapToBuisnessModel() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/employees/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeTEST1)))
		.andExpect(status().isOk());
		
		ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
		verify(mockedDefaultService, times(1)).save(employeeCaptor.capture());
		Employee employee = employeeCaptor.getValue();
		
		assertEquals("FirstnameTEST1", employee.getFirstName());
		assertEquals("LastnameTEST1", employee.getLastName());
		assertEquals(LocalDate.of(2000, 01, 01), employee.getBirthDate());
	}
	
	@Test
	public void createEmployee_WhenValidInputThenReturnEmployeeResource_VerifyingSerializedToHTTPResponse() throws JsonProcessingException, Exception {
		MvcResult mvcResult = mockMvc.perform(post("/rest/employees/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeTEST1)))
		.andExpect(status().isOk())
		.andReturn();
		
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(employeeTEST1);
		
		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
	}
	
	@Test
	public void fetchAllEmployees_SchouldReturnTwoEmployees() throws Exception {
		when(mockedDefaultService.fetchAll()).thenReturn(List.of(employeeTEST1, employeeTEST2));
		
		mockMvc.perform(get("/rest/employees/"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", Matchers.hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].firstName", is("FirstnameTEST1")))
			.andExpect(jsonPath("$[0].lastName", is("LastnameTEST1")))
			.andExpect(jsonPath("$[1].id", is(2)))
			.andExpect(jsonPath("$[1].firstName", is("FirstnameTEST2")))
			.andExpect(jsonPath("$[1].lastName", is("LastnameTEST2")));
		
		verify(mockedDefaultService, times(1)).fetchAll();
		verifyNoMoreInteractions(mockedDefaultService);
	}
	
}
