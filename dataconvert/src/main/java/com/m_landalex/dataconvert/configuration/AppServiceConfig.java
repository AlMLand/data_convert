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
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.m_landalex.dataconvert.converter.EmployeeEntityToEmployeeConverter;
import com.m_landalex.dataconvert.converter.EmployeeToEmployeeEntityConverter;
import com.m_landalex.dataconvert.converter.LocalDateToStringConverter;
import com.m_landalex.dataconvert.converter.RoleEntityToRoleConverter;
import com.m_landalex.dataconvert.converter.RoleToRoleEntityConverter;
import com.m_landalex.dataconvert.converter.StringToLocalDateConverter;
import com.m_landalex.dataconvert.converter.UserEntityToUserConverter;
import com.m_landalex.dataconvert.converter.UserToUserEntityConverter;

@EnableJpaRepositories(basePackages = "com.m_landalex.dataconvert.petsistence")
@ComponentScan(basePackages = "com.m_landalex.dataconvert")
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@Configuration
public class AppServiceConfig {
	
	@Value( "${h2.hibernate.dialect}" ) private String dialect;
	@Value( "${hibernate.show_sql}" ) private String showSql;
	@Value( "${hibernate.format_sql}" ) private String formatSql;
	@Value( "${hibernate.use_sql_comments}" ) private String useSqlComments;
	@Value( "${hibernate.max_fetch_depth}" ) private String maxFetchDepth;
	@Value( "${hibernate.jdbc.batch_size}" ) private String batchSize;
	@Value( "${hibernate.jdbc.fetch_size}" ) private String fetchSize;
	@Value( "${hibernate.hbm2ddl.auto}" ) private String ddlAuto;
	@Value( "${hibernate.jmx.enabled}" ) private String jmxEnabled;
	@Value( "${hibernate.generete_statistics}" ) private String genereteStatistics;
	@Autowired private StringToLocalDateConverter stringToLocalDateConverter; 
	@Autowired private LocalDateToStringConverter localDateToStringConverter;
	@Autowired private EmployeeEntityToEmployeeConverter employeeEntityToEmployeeConverter; 
	@Autowired private EmployeeToEmployeeEntityConverter employeeToEmployeeEntityConverter;
	@Autowired private UserEntityToUserConverter userEntityToUserConverter;
	@Autowired private UserToUserEntityConverter userToUserEntityConverter;
	@Autowired private RoleToRoleEntityConverter roleToRoleEntityConverter;
	@Autowired private RoleEntityToRoleConverter roleEntityToRoleConverter;
	@Autowired private DataSource dataSource;
	
	 @Bean
	 public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setIgnoreUnresolvablePlaceholders(true);
		return configurer;
	 }
	
	private Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty( "h2.hibernate.dialect", dialect );
		properties.setProperty( "hibernate.format_sql", formatSql );
		properties.setProperty( "hibernate.use_sql_comments", useSqlComments );
		properties.setProperty( "hibernate.max_fetch_depth", maxFetchDepth );
		properties.setProperty( "hibernate.jdbc.batch_size", batchSize );
		properties.setProperty( "hibernate.jdbc.fetch_size", fetchSize );
		properties.setProperty( "hibernate.hbm2ddl.auto", ddlAuto );
		properties.setProperty( "hibernate.jmx.enabled", jmxEnabled );
		properties.setProperty( "hibernate.generete_statistics", genereteStatistics );
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
		converters.add(roleToRoleEntityConverter);
		converters.add(roleEntityToRoleConverter);
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
