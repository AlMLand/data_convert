package com.m_landalex.dataconvert.configuration.restserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.m_landalex.dataconvert.jmx.AbstractObjectStatistics;
import com.m_landalex.dataconvert.jmx.AbstractObjectStatisticsImpl;
import com.m_landalex.dataconvert.jmx.CustomStatistics;

@EnableWebMvc
@Configuration
@Profile( "!test" )
@ComponentScan(basePackages = "com.m_landalex.dataconvert")
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(objectMapper());
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return objectMapper;
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2HttpMessageConverter());
	}
	
	@Bean
	AbstractObjectStatistics abstractObjectStatistics() {
		return new AbstractObjectStatisticsImpl();
	}
	
	@Bean
	public MBeanExporter jmxExporter() {
		MBeanExporter exporter = new MBeanExporter();
		exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
		Map<String, Object> beansToExport = new HashMap<String, Object>();
		beansToExport.put( "bean:name=MyBeansStatistics", abstractObjectStatistics() );
		beansToExport.put( "bean:name=MyBeansStatisticsHibernate", customStatistics() );
		exporter.setBeans( beansToExport );
		exporter.afterPropertiesSet();
		return exporter;
	}
	
	@Bean
	CustomStatistics customStatistics() {
		return new CustomStatistics();
	}
	
	@Bean
	SessionFactory sessionFactory() {
		return entityManagerFactory.unwrap( SessionFactory.class );
	}
	
}
