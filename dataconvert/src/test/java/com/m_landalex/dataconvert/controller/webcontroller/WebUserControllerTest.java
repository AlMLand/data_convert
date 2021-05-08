package com.m_landalex.dataconvert.controller.webcontroller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.m_landalex.dataconvert.configuration.webconfiguration.WebConfig;
import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.service.DefaultService;
import com.m_landalex.dataconvert.service.UserService;
import com.m_landalex.dataconvert.view.controller.web.UserController;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class WebUserControllerTest {
	
	@Autowired
	private UserController userController;
	private DefaultService mockedDefaultService;
	private User userTEST1, userTEST2;
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		mockedDefaultService = mock(UserService.class);
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
		userTEST1 = User.builder().username("UsernameTEST1")
					.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
					.start(LocalDate.of(2022, 01, 01)).aktiv(true)
					.userRole(List.of(Role.builder().role("ADMINISTRATOR").build())).build();
		userTEST1.setId(Long.valueOf(1));
		userTEST2 = User.builder().username("UsernameTEST2")
				.password("$2y$12$hIN5ajwLnQEKfMPHOyKxv.Hzk2v3yk2O1qaEAsDk/3KKD12LvOll.")
				.start(LocalDate.of(2022, 01, 01)).aktiv(false)
				.userRole(List.of(Role.builder().role("OFFICE").build())).build();
		userTEST2.setId(Long.valueOf(2));
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void list_ShouldAddUserToModelAndRenderUserListView() throws Exception {
		when(mockedDefaultService.fetchAll()).thenReturn(List.of(userTEST1, userTEST2));
		
		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("users/list"))
				.andExpect(forwardedUrl("users/list"))
				.andExpect(model().attribute("users", hasSize(2)))
				.andExpect(model().attribute("users", hasItem(
							allOf(
									hasProperty("id", is(1L)),
									hasProperty("username", is("UsernameTEST1")),
									hasProperty("aktiv", is(true))
									)
						)))
				.andExpect(model().attribute("users", hasItem(
							allOf(
									hasProperty("id", is(2L)),
									hasProperty("username", is("UsernameTEST2")),
									hasProperty("aktiv", is(false))
									)
						)));
		
		verify(mockedDefaultService, times(1)).fetchAll();
		verifyNoMoreInteractions(mockedDefaultService);
	}
	
	@Test
	@WithMockUser(username = "TESTER", password = "12345", authorities = {"ADMINISTRATOR"})
	public void show_UserFound_ShouldAddUserToModelAndRenderViewUserView() throws Exception {
		when(mockedDefaultService.fetchById(anyLong())).thenReturn(userTEST2);
		
		mockMvc.perform(get("/users/{id}", 2L))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(view().name("users/show"))
				.andExpect(forwardedUrl("users/show"))
				.andExpect(model().attribute("user", hasProperty("id", is(2L))))
				.andExpect(model().attribute("user", hasProperty("username", is("UsernameTEST2"))))
				.andExpect(model().attribute("user", hasProperty("aktiv", is(false))));
		
		verify(mockedDefaultService, times(1)).fetchById(anyLong());
		verifyNoMoreInteractions(mockedDefaultService);
	}

}
