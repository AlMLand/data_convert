package com.m_landalex.dataconvert.configuration.websecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig {

	@Order(1)
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		
		@Autowired
		@Qualifier(value = "userDetailsServiceImpl")
		private UserDetailsService userDetailsService; 
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
			.antMatchers("/employees/**", "/users/**").authenticated()
			.antMatchers("/**").permitAll()
			.and()
			.formLogin()
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.and()
			.csrf().disable();
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authenticationProvider());
		}
		
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
			authenticationProvider.setPasswordEncoder(passwordEncoder());
			authenticationProvider.setUserDetailsService(userDetailsService);
			return authenticationProvider;
		}
		
	}
	
	@Order(2)
	@Configuration
	public static class RestSecurityConfig extends WebSecurityConfigurerAdapter {

		private static final Logger logger = LoggerFactory.getLogger(RestSecurityConfig.class);

		@Autowired
		protected void configureClobal(AuthenticationManagerBuilder authenticationManagerBuilder) {
			try {
				authenticationManagerBuilder.inMemoryAuthentication()
				.withUser("Friend")
				.password("12345")
				.roles("REMOTE");
			} catch (Exception e) {
				logger.error("Could not configure authentication {}", e);
			}
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.antMatchers("/rest/**").hasRole("REMOTE").anyRequest().authenticated()
			.and()
			.formLogin()
			.and()
			.httpBasic()
			.and()
			.csrf().disable();
		}

	}

}
