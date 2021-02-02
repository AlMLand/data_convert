package com.m_landalex.dataconvert.view;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.m_landalex.dataconvert.configuration.AppConfig;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.service.EmployeeService;

public class DemoRunFile {

	public static void main(String[] args) {
		
		GenericApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		EmployeeService service = context.getBean(EmployeeService.class);
		List<Employee> list = service.fetchAll();
		list.forEach(System.out::println);
		
		context.close();
	}

}
