package com.m_landalex.dataconvert.configuration;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.m_landalex.dataconvert.converter.EmployeeEntityToEmployeeConverter;
import com.m_landalex.dataconvert.converter.EmployeeToEmployeeEntityConverter;
import com.m_landalex.dataconvert.converter.LocalDateToStringConverter;
import com.m_landalex.dataconvert.converter.StringToLocalDateConverter;
import com.m_landalex.dataconvert.converter.UserEntityToUserConverter;
import com.m_landalex.dataconvert.converter.UserToUserEntityConverter;

@ComponentScan(basePackages = "com.m_landalex.dataconvert")
@EnableJpaRepositories(basePackages = "com.m_landalex.dataconvert.petsistence")
@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
	
	@Value("${h2.hibernate.dialect}")String dialect;
	@Value("${hibernate.show_sql}")String show_sql;
	@Value("${hibernate.format_sql}")String format_sql;
	@Value("${hibernate.use_sql_comments}")String use_sql_comments;
	@Value("${hibernate.max_fetch_depth}")String max_fetch_depth;
	@Value("${hibernate.jdbc.batch_size}")String batch_size;
	@Value("${hibernate.jdbc.fetch_size}")String fetch_size;
	@Value("${hibernate.hbm2ddl.auto}")String ddl_auto;
	@Autowired 
	private StringToLocalDateConverter stringToLocalDateConverter; 
	@Autowired
	private LocalDateToStringConverter localDateToStringConverter;
	@Autowired
	private EmployeeEntityToEmployeeConverter employeeEntityToEmployeeConverter; 
	@Autowired
	private EmployeeToEmployeeEntityConverter employeeToEmployeeEntityConverter;
	@Autowired
	private UserEntityToUserConverter userEntityToUserConverter;
	@Autowired
	private UserToUserEntityConverter userToUserEntityConverter;
	
	@Bean
	public DataSource dataSource() {
		try {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		} catch (Exception e) {
			logger.error("No datasource created");
			return null;
		}
	}
	
	private Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty("h2.hibernate.dialect", dialect);
		properties.setProperty("hibernate.format_sql", format_sql);
		properties.setProperty("hibernate.use_sql_comments", use_sql_comments);
		properties.setProperty("hibernate.max_fetch_depth", max_fetch_depth);
		properties.setProperty("hibernate.jdbc.batch_size", batch_size);
		properties.setProperty("hibernate.jdbc.fetch_size", fetch_size);
		properties.setProperty("hibernate.hbm2ddl.auto", ddl_auto);
		return properties;
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(dataSource());
		bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		bean.setPackagesToScan("com.m_landalex.dataconvert.domain");
		bean.setJpaProperties(getProperties());
		bean.afterPropertiesSet();
		return bean.getNativeEntityManagerFactory();
	}
	
	@Bean
	public ConversionServiceFactoryBean conversionServiceFactoryBean() {
		ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
		Set<Converter<?, ?>> converters = new HashSet<>();
		converters.add(employeeEntityToEmployeeConverter);
		converters.add(employeeToEmployeeEntityConverter);
		converters.add(userEntityToUserConverter);
		converters.add(userToUserEntityConverter);
		converters.add(stringToLocalDateConverter);
		converters.add(localDateToStringConverter);
		conversionServiceFactoryBean.setConverters(converters);
		conversionServiceFactoryBean.afterPropertiesSet();
		return conversionServiceFactoryBean;
	}

}
