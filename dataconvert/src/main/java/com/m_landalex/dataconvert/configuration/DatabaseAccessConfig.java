package com.m_landalex.dataconvert.configuration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

//@Profile("dev")
@Configuration
@ComponentScan(basePackages = "com.m_landalex.dataconvert")
public class DatabaseAccessConfig {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseAccessConfig.class);
	
	@Bean
	public DataSource dataSource() {
		try {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		} catch (Exception e) {
			logger.error("No datasource created");
			return null;
		}
	}
	
}
