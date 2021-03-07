package com.m_landalex.dataconvert.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeCreateListener {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeCreateListener.class);

	@RabbitListener(containerFactory = "rabbitListenerContainerFactory", queues = "statusCreatingEmployee")
	public void getCreateStatus(String status) {
		if (status.equals("succesful")) {
			logger.info("The object Employee.class are succesful created. [ {} ]", status);
		} else if (status.equals("error")) {
			logger.info("The object Employee.class are not crated. [ {} ]", status);
		}
	}

}
