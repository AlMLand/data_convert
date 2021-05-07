package com.m_landalex.dataconvert.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

	private MockMvc mockMvc;
	private DefaultService mockedDefaultService;
	@Autowired
	private EmployeeController employeeController;
	private Employee employeeTEST1, employeeTEST2;
	
	@Before
	public void setUp() throws MalformedURLException {
		mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
		mockedDefaultService = Mockito.mock(EmployeeService.class);
		// manually injections
		ReflectionTestUtils.setField(employeeController, "defaultService", mockedDefaultService);
		
		employeeTEST1 = Employee.builder().firstName("FirstnameTEST1").lastName("LastnameTEST1")
				.birthDate(LocalDate.of(2000, 01, 01))
				.jobStartInTheCompany(LocalDate.of(2022, 01, 01))
				.companyAffiliation(0).description("descriptionTEST1").photo(null)
				.webSite(new URL("http://employeeTEST1.com/"))
				.user(User.builder().username("UsernameTEST1").password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
						.start(LocalDate.of(2022, 01, 01))
						.aktiv(true)
						.userRole(List.of(Role.builder().role("ADMINISTRATOR").build())).build())
				.build();
				employeeTEST1.setId(Long.valueOf(1));
		employeeTEST2 = Employee.builder().firstName("FirstnameTEST2").lastName("LastnameTEST2")
				.birthDate(LocalDate.of(2000, 02, 02))
				.jobStartInTheCompany(LocalDate.of(2022, 02, 02))
				.companyAffiliation(0).description("descriptionTEST2").photo(null)
				.webSite(new URL("http://employeeTEST2.com/"))
				.user(User.builder().username("UsernameTEST2").password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
						.start(LocalDate.of(2022, 02, 02))
						.aktiv(true)
						.userRole(List.of(Role.builder().role("ADMINISTRATOR").build())).build())
				.build();
				employeeTEST2.setId(Long.valueOf(2));
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void findAll_ShouldAddEmployeeToModelAndRenderEMployeeListView() throws Exception {
		Mockito.when(mockedDefaultService.fetchAll()).thenReturn(List.of(employeeTEST1, employeeTEST2));
		
		this.mockMvc.perform(get("/employees"))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/list"))
				.andDo(print())
				.andExpect(forwardedUrl("employees/list"))
				.andExpect(model().attribute("employees", Matchers.hasSize(2)))
				.andExpect(model().attribute("employees", Matchers.hasItem(
						Matchers.allOf(
								Matchers.hasProperty("id", Matchers.is(1L)),
								Matchers.hasProperty("firstName", Matchers.is("FirstnameTEST1")),
								Matchers.hasProperty("lastName", Matchers.is("LastnameTEST1"))
								)
						)))
				.andExpect(model().attribute("employees", Matchers.hasItem(
						Matchers.allOf(
								Matchers.hasProperty("id", Matchers.is(2L)),
								Matchers.hasProperty("firstName", Matchers.is("FirstnameTEST2")),
								Matchers.hasProperty("lastName", Matchers.is("LastnameTEST2"))
								)
						)));
		
		Mockito.verify(mockedDefaultService, Mockito.times(1)).fetchAll();
		Mockito.verifyNoMoreInteractions(mockedDefaultService);
	}
	
}
