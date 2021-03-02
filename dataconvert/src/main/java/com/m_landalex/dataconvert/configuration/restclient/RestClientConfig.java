package com.m_landalex.dataconvert.configuration.restclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RestClientConfig {
	
	@Bean
	public Credentials credentials() {
		return new UsernamePasswordCredentials("Friend", "12345");
	}
	
	@Bean
	public CredentialsProvider credentialProvider() {
		BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
		basicCredentialsProvider.setCredentials(AuthScope.ANY, credentials());
		return basicCredentialsProvider;
	}
	
	@Bean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		CloseableHttpClient closeableHttpClient = HttpClients
				.custom()
				.setDefaultCredentialsProvider(credentialProvider())
				.build();
		factory.setHttpClient(closeableHttpClient);
		return factory;
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(clientHttpRequestFactory());
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(mappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(converters);
		return restTemplate;
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter(objectMapper());
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.EAGER_DESERIALIZER_FETCH);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, false);
		return objectMapper;
	}

}
