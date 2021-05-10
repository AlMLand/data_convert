package com.m_landalex.dataconvert.controller.restcontroller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m_landalex.dataconvert.configuration.webconfiguration.WebConfig;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.service.DefaultService;
import com.m_landalex.dataconvert.service.UserService;
import com.m_landalex.dataconvert.view.controller.rest.RestUserController;

@ActiveProfiles("test4")
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class RestUserControllerIntegrationTest {

	@Autowired private RestUserController restUserController;
	@Autowired private ObjectMapper objectMapper;
	private DefaultService mockedDefaultService;
	private User userTEST1, userTEST2;
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(restUserController).build();
		mockedDefaultService = mock(UserService.class);
		// manually injections
		ReflectionTestUtils.setField(restUserController, "defaultService", mockedDefaultService);
		
		Role role = Role.builder().role("ADMINISTRATOR").build();
		role.setId(Long.valueOf(1));
		userTEST1 = User.builder().username("UsernameTEST1")
				.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
				.start(LocalDate.of(2022, 01, 01)).aktiv(true).userRole(List.of(role)).build();
		userTEST1.setId(Long.valueOf(1));
		userTEST2 = User.builder().username("UsernameTEST2")
				.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
				.start(LocalDate.of(2022, 02, 02)).aktiv(true).userRole(List.of(role)).build();
		userTEST2.setId(Long.valueOf(2));
	}
	
	@Test
	public void createUser_WhenValidInputThenReturn200_VerifyingDeserializedFromHTTPRequest() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/rest/employees/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userTEST1)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void createUser_WhenFirstnameAndUserNullValueThenReturn400_VerifyingValidation() throws JsonProcessingException, Exception {
		User userNotValid = User.builder().username("a".repeat(200))
				.password("a".repeat(300))
				.start(LocalDate.of(2000, 01, 01)).aktiv(null).userRole(null).build();
		userNotValid.setId(Long.valueOf(1));
		
		mockMvc.perform(post("/rest/employees/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userNotValid)))
		.andExpect(status().isBadRequest());
		
		verifyNoInteractions(mockedDefaultService);
	}
	
	@Test
	public void createUser_WhenValidInputThenMapToBuisnessModel() throws JsonProcessingException, Exception {
		when(mockedDefaultService.save(Mockito.any(User.class))).thenReturn(userTEST1);
		
		mockMvc.perform(post("/rest/employees/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userTEST1)))
		.andExpect(status().isOk());
		
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(mockedDefaultService, timeout(1)).save(userCaptor.capture());
		User user = userCaptor.getValue();
		
		assertEquals("UsernameTEST1", user.getUsername());
		assertEquals("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.", user.getPassword());
		assertEquals(LocalDate.of(2022, 01, 01), user.getStart());
		assertEquals(true, user.getAktiv());
	}
	
}
