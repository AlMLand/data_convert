package com.m_landalex.dataconvert.controller.restcontroller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
	
	@Test
	public void createUser_WhenValidInputThenReturnUserResource_VerifyingSerializedToHTTPResponse() throws JsonProcessingException, Exception {
		MvcResult mvcResult = mockMvc.perform(post("/rest/employees/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userTEST1)))
				.andExpect(status().isOk())
				.andReturn();
		
		String expectedResponseBody = objectMapper.writeValueAsString(userTEST1);
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actualResponseBody);
	}
	
	@Test
	public void fetchAllUsers_WhenStatus200ThenReturnUsersResource_VerifyingSerializedToHTTPResponse() throws JsonProcessingException, Exception {
		when(mockedDefaultService.fetchAll()).thenReturn(List.of(userTEST1, userTEST2));
		
		MvcResult mvcResult = mockMvc.perform(get("/rest/employees/users/"))
				.andExpect(status().isOk())
				.andReturn();
		
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(List.of(userTEST1, userTEST2));
		
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actualResponseBody);
	}
	
	@Test
	public void fetchAllUsers_SchouldReturnTwoUsers() throws JsonProcessingException, Exception {
		when(mockedDefaultService.fetchAll()).thenReturn(List.of(userTEST1, userTEST2));
		
		mockMvc.perform(get("/rest/employees/users/"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].username", is("UsernameTEST1")))
		.andExpect(jsonPath("$[0].password", is("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")))
		.andExpect(jsonPath("$[0].start", is("2022-01-01")))
		.andExpect(jsonPath("$[1].username", is("UsernameTEST2")))
		.andExpect(jsonPath("$[1].password", is("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")))
		.andExpect(jsonPath("$[1].start", is("2022-02-02")));
		
		verify(mockedDefaultService, times(1)).fetchAll();
		verifyNoMoreInteractions(mockedDefaultService);
	}
	
	@Test
	public void deleteAllUsers_WhenStatus200ThenClearAllUsersInUsersLis() throws Exception {
		List<User> usersList = new ArrayList<>();
		usersList.add(userTEST1);
		usersList.add(userTEST2);
		assertEquals(2, usersList.size());
		
		doAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				usersList.clear();
				return null;
			}
		}).when(mockedDefaultService).deleteAll();
		
		mockMvc.perform(delete("/rest/employees/users/"))
				.andExpect(status().isOk());
		
		assertNotNull(usersList);
		assertEquals(0, usersList.size());
	}
	
	@Test
	public void fetchUserById_WhenStatus200ThenReturnUserResource_VerifyingSerializedToHTTPResponse() throws Exception {
		when(mockedDefaultService.fetchById(Mockito.anyLong())).thenReturn(userTEST1);
		
		MvcResult mvcResult = mockMvc.perform(get("/rest/employees/users/{id}", 1L))
				.andExpect(status().isOk())
				.andReturn();
		
		String actuylResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(userTEST1);
		
		assertThat(expectedResponseBody).isEqualToIgnoringWhitespace(actuylResponseBody);
	}
	
	@Test
	public void fetchUserById_UserFound_ShouldReturnFoundUser() throws Exception {
		when(mockedDefaultService.fetchById(Mockito.anyLong())).thenReturn(userTEST1);
		
		mockMvc.perform(get("/rest/employees/users/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.username", is("UsernameTEST1")))
				.andExpect(jsonPath("$.password", is("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")))
				.andExpect(jsonPath("$.start", is("2022-01-01")))
				.andExpect(jsonPath("$.aktiv", is(true)))
				.andExpect(jsonPath("$.userRole[0].id", is(1)))
				.andExpect(jsonPath("$.userRole[0].role", is("ADMINISTRATOR")));
	}
	
	@Test
	public void deleteUserById_WhenValidInputThenReturn200AndUsersListHasOneUser() throws Exception {
		Long count = Long.valueOf(1);
		List<User> usersList = new ArrayList<>();
		usersList.add(userTEST1);
		usersList.add(userTEST2);
		assertEquals(2, usersList.size());
		
		doAnswer(new Answer<User>() {

			@Override
			public User answer(InvocationOnMock invocation) throws Throwable {
				for(User user : usersList) {
					if(user.getId() == count) {
						usersList.remove(user);
					}
				}
				return null;
			}
		}).when(mockedDefaultService).deleteById(count);
		
		mockMvc.perform(delete("/rest/employees/users/{id}", 1L))
				.andExpect(status().isOk());
		
		assertNotNull(usersList);
		assertEquals(1, usersList.size());
	}
	
}
