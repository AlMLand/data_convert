package com.m_landalex.dataconvert.configuration.httpremotingserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

import com.m_landalex.dataconvert.service.DefaultService;

@Configuration
public class HttpInvokerConfigEmployee {

	@Autowired
	@Qualifier(value = "employeeService")
	private DefaultService defaultService;
	
	@SuppressWarnings("deprecation")
	@Bean(name = "/httpInvoker/defaultService/employee")
	public HttpInvokerServiceExporter httpInvokerServiceExporter() {
		HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setService(defaultService);
		exporter.setServiceInterface(DefaultService.class);
		return exporter;
	}
	
}
