package com.m_landalex.dataconvert.configuration;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = "com.m_landalex.dataconvert.service")
@EnableTransactionManagement
@Configuration
public class TransactionManagerConfig {

	@Autowired
	private EntityManagerFactory entityManagerFactory; 
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
}
