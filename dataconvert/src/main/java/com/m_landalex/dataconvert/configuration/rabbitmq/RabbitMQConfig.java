package com.m_landalex.dataconvert.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@ComponentScan(basePackages = "com.m_landalex.dataconvert")
public class RabbitMQConfig {

	private final String queueName = "statusCreatingEmployee";
	private final String exchangeName = "createdOrNotCreated";
	
	@Bean
	public CachingConnectionFactory connectionFactory() {
		return new CachingConnectionFactory("127.0.0.1");
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setReplyTimeout(2000);
		template.setRoutingKey(queueName);
		template.setExchange(exchangeName);
		return template;
	}
	
	@Bean
	public Queue statusCreatingEmployee() {
		return new Queue(queueName, true);
	}

	@Bean
	public DirectExchange createdOrNotCreated() {
		return new DirectExchange(exchangeName, true, false);
	}
	
	@Bean
	public Binding dataBinding(DirectExchange exchange, Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin() {
		RabbitAdmin admin = new RabbitAdmin(connectionFactory());
		admin.declareQueue(statusCreatingEmployee());
		admin.declareExchange(createdOrNotCreated());
		admin.declareBinding(dataBinding(createdOrNotCreated(), statusCreatingEmployee()));
		return admin;
	}
	
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrentConsumers(5);
		return factory;
	}
	
}
