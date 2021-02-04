package com.m_landalex.dataconvert.view;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.m_landalex.dataconvert.configuration.AppConfig;
import com.m_landalex.dataconvert.service.EmployeeService;
import com.m_landalex.dataconvert.service.UserService;

public class DemoRunFile {

	public static void main(String[] args) {
		
		GenericApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		EmployeeService employeeService = context.getBean(EmployeeService.class);
		employeeService.fetchAll().forEach(System.out::println);
		
		UserService userService = context.getBean(UserService.class);
		userService.fetchAll().forEach(System.out::println);
		
		context.close();
	}

}
