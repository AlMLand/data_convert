package com.m_landalex.dataconvert.configuration.httpremotingclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.m_landalex.dataconvert.service.DefaultService;

@SuppressWarnings("deprecation")
@Configuration
public class HttpInvokerClientConfigEmployee {

	@Bean(name = "emploeeService")
	public DefaultService defaultService() {
		HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
		factoryBean.setServiceInterface(DefaultService.class);
		factoryBean.setServiceUrl("http://localhost:8080/dataconvert/httpInvoker/defaultService/employee");
		factoryBean.afterPropertiesSet();
		return (DefaultService) factoryBean.getObject();
	}
	
}
