package com.m_landalex.dataconvert.configuration.websecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(RestSecurityConfig.class);
	
	@Autowired
	protected void configureClobal(AuthenticationManagerBuilder authenticationManagerBuilder) {
		try {
			authenticationManagerBuilder
				.inMemoryAuthentication()
				.withUser("Friend")
				.password("12345")
				.roles("REMOTE");
		} catch (Exception e) {
			logger.error("Could not configure authentication", e);
		}
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
