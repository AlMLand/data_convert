package com.m_landalex.dataconvert.configuration;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
public class AppServiceConfig {
	
	@Value("${h2.hibernate.dialect}")private String dialect;
	@Value("${hibernate.show_sql}")private String show_sql;
	@Value("${hibernate.format_sql}")private String format_sql;
	@Value("${hibernate.use_sql_comments}")private String use_sql_comments;
	@Value("${hibernate.max_fetch_depth}")private String max_fetch_depth;
	@Value("${hibernate.jdbc.batch_size}")private String batch_size;
	@Value("${hibernate.jdbc.fetch_size}")private String fetch_size;
	@Value("${hibernate.hbm2ddl.auto}")private String ddl_auto;
	@Autowired private StringToLocalDateConverter stringToLocalDateConverter; 
	@Autowired private LocalDateToStringConverter localDateToStringConverter;
	@Autowired private EmployeeEntityToEmployeeConverter employeeEntityToEmployeeConverter; 
	@Autowired private EmployeeToEmployeeEntityConverter employeeToEmployeeEntityConverter;
	@Autowired private UserEntityToUserConverter userEntityToUserConverter;
	@Autowired private UserToUserEntityConverter userToUserEntityConverter;
	@Autowired private DataSource dataSource;
	
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
		bean.setDataSource(dataSource);
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
	
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}

}
