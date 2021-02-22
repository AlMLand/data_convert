package com.m_landalex.dataconvert.view.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.EmployeeService;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> listEmployees(){
		return employeeService.fetchAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Employee findEmployeeById(@PathVariable Long id) {
		return employeeService.fetchById(id);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Employee createEmployee(@RequestBody Employee employee) {
		try {
			employeeService.save(employee);
			logger.info("Employee is successful saved {}", employee);
		} catch (ResourceNullException e) {
			logger.error("Ressource not found {}", e);
		}
		return employee;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void updateEmployee(@RequestBody Employee employee) {
		try {
			employeeService.save(employee);
			logger.info("Employee is successful updated {}", employee);
		} catch (ResourceNullException e) {
			logger.error("Ressource not found {}", e);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteEmployee(@PathVariable Long id) {
		employeeService.delete(employeeService.fetchById(id));
	}
	
}
