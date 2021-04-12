package com.m_landalex.dataconvert.view.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.service.DefaultService;

@Controller
@RequestMapping( value = "/employees" )
public class EmployeeController {

	@Autowired
	@Qualifier( value = "employeeService" )
	private DefaultService defaultService;
	private MessageSource messageSource;
	
	@RequestMapping( method = RequestMethod.GET )
	public String list( Model model ) {
		List<Employee> returnedList = defaultService.fetchAll();
		model.addAttribute("employees", returnedList);
		return "employees/list";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model model) {
		Employee returnedEmployee = (Employee) defaultService.fetchById(id);
		model.addAttribute("returnedEmployee", returnedEmployee);
		return "employees/show";
	}
	
}
