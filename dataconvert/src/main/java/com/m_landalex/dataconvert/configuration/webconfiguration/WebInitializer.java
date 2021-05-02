package com.m_landalex.dataconvert.configuration.webconfiguration;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.m_landalex.dataconvert.configuration.AppServiceConfig;
import com.m_landalex.dataconvert.configuration.TransactionManagerConfig;
import com.m_landalex.dataconvert.configuration.httpremotingserver.HttpInvokerConfigEmployee;
import com.m_landalex.dataconvert.configuration.websecurity.SecurityConfig;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppServiceConfig.class, TransactionManagerConfig.class, 
			SecurityConfig.RestSecurityConfig.class, SecurityConfig.WebSecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class, HttpInvokerConfigEmployee.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter("utf-8", true);
		return new Filter[] { new HiddenHttpMethodFilter(), encodingFilter };
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(multipartConfigElement());
	}
	
	@Bean
	private MultipartConfigElement multipartConfigElement() {
		return new MultipartConfigElement(null, 5000000, 5000000, 0);
	}

}
