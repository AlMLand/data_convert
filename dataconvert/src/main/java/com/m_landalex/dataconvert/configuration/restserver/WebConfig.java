package com.m_landalex.dataconvert.configuration.restserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

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
	private ApplicationContext applicationContext;
	
	private static final String KEY_MY_STATISTIC = "bean:name=MyBeansStatistics";
	private static final String KEY_CUSTOM_STATISTIC = "bean:name=MyBeansStatisticsHibernate"; 

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
    /* ******************************************************************* */
    /*  GENERAL CONFIGURATION ARTIFACTS                                    */
    /* ******************************************************************* */
	
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
	
	@Bean
	AbstractObjectStatistics abstractObjectStatistics() {
		return new AbstractObjectStatisticsImpl();
	}
	
	@Bean
	public MBeanExporter jmxExporter() {
		MBeanExporter exporter = new MBeanExporter();
		exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
		Map<String, Object> beansToExport = new HashMap<String, Object>();
		beansToExport.put( KEY_MY_STATISTIC, abstractObjectStatistics() );
		beansToExport.put( KEY_CUSTOM_STATISTIC, customStatistics() );
		exporter.setBeans( beansToExport );
		exporter.afterPropertiesSet();
		return exporter;
	}
	
	@Bean
	Statistics statistics() {
		return entityManagerFactory.unwrap( SessionFactory.class ).getStatistics();
	}
	
	@Bean
	CustomStatistics customStatistics() {
		return new CustomStatistics();
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
	public ResourceBundleMessageSource resourceBundleMessageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("Messages");
		return source;
	}
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/").setCachePeriod(3600000);
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
	}
	
    /* **************************************************************** */
    /*  THYMELEAF-SPECIFIC ARTIFACTS                                    */
    /*  TemplateResolver <- TemplateEngine <- ViewResolver              */
    /* **************************************************************** */
	
	@Bean
	public SpringResourceTemplateResolver springResourceTemplateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(this.applicationContext);
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCacheable(true);
		return resolver;
	}
	
	@Bean
	public SpringTemplateEngine springTemplateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(springResourceTemplateResolver());
		engine.setEnableSpringELCompiler(true);
		return engine;
	}
	
	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(springTemplateEngine());
		return resolver;
	}
	
}
