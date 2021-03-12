package com.m_landalex.dataconvert.service.configuration;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.m_landalex.dataconvert.configuration.DatabaseAccessConfig;
import com.m_landalex.dataconvert.dbinitialization.DBInitialization;

@ComponentScan( basePackages = "com.m_landalex.dataconvert", excludeFilters = {
		@ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, value = DBInitialization.class ) } )
@Profile("test")
@Configuration
public class TestConfig {

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
