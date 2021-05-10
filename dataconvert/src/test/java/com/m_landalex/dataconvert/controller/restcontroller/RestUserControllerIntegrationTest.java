package com.m_landalex.dataconvert.controller.restcontroller;

import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
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

@ActiveProfiles("test4")
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class RestUserControllerIntegrationTest {

	@Autowired
	private UserController userController;
	private DefaultService mockedDefaultService;
	private User userTEST1, userTEST2;
	private MockMvc mockMvc;
	
	@Before
	private void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		mockedDefaultService = mock(UserService.class);
		// manually injections
		ReflectionTestUtils.setField(userController, "defaultService", mockedDefaultService);
		
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
	
	
	
}
