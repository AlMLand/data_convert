package com.m_landalex.dataconvert.configuration.webconfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.m_landalex.dataconvert.formatter.LocalDateFormatter;
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
	
	private static final String COOKIE_LOCAL_NAME = "locale";
	private static final String INTERCEPTOR_NAME = "lang";
	private static final String KEY_MY_STATISTIC = "bean:name=MyBeansStatistics";
	private static final String KEY_CUSTOM_STATISTIC = "bean:name=MyBeansStatisticsHibernate"; 

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
    /* ******************************************************************* */
    /*  GENERAL CONFIGURATION ARTIFACTS                                    */
    /* ******************************************************************* */
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations("/images/**");
		registry.addResourceHandler("/css/**").addResourceLocations("/styles/**");
		registry.addResourceHandler("/js/**").addResourceLocations("/scripts/**");
	}
	
    /* **************************************************************** */
    /*  MULTIPART RESOLVER                                              */
    /* **************************************************************** */
	
	@Bean
	StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
    /* **************************************************************** */
    /*  REST-SPECIFIC ARTIFACTS                                         */
    /* **************************************************************** */
	
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
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2HttpMessageConverter());
	}
	
    /* **************************************************************** */
    /*  STATISTICS-SPECIFIC ARTIFACTS                                         */
    /* **************************************************************** */
	
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
	
    /* **************************************************************** */
    /*  i18n MESSAGES FORMATTERS VALIDATOR - SPECIFIC ARTIFACTS         */
    /* **************************************************************** */
	
	@Bean
	public Validator validator() {
		final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}
	
	@Override
	public Validator getValidator() {
		return validator();
	}
	
	@Bean
	ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasenames("WEB-INF/i18n/messages");
		source.setDefaultEncoding("UTF-8");
		source.setFallbackToSystemLocale(false);
		return source;
	}
	
	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName(INTERCEPTOR_NAME);
		return interceptor;
	}

	@Bean
	ThemeChangeInterceptor themeChangeInterceptor() {
		return new ThemeChangeInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean
	CookieLocaleResolver cookieLocaleResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		resolver.setCookieMaxAge(3000);
		resolver.setCookieName(COOKIE_LOCAL_NAME);
		return resolver;
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new LocalDateFormatter());
	}
	
    /* **************************************************************** */
    /*  THYMELEAF-SPECIFIC ARTIFACTS                                    */
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
