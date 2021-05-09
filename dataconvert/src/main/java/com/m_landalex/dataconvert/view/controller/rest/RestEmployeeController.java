package com.m_landalex.dataconvert.view.controller.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.DefaultService;

@Controller
@RequestMapping(value = "/rest/employees")
public class RestEmployeeController {

	@Autowired
	@Qualifier(value = "employeeService")
	private DefaultService defaultService;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Employee createEmployee(@Valid @RequestBody Employee employee) throws ResourceNullException {
		defaultService.save(employee);
		return employee;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<AbstractObject> fetchAllEmployees() {
		return defaultService.fetchAll();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteAllEmployees() {
		defaultService.deleteAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public AbstractObject fetchEmployeeById(@PathVariable Long id) {
		return defaultService.fetchById(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void updateEmployeeById(@Valid @RequestBody Employee employee) throws ResourceNullException {
		defaultService.save(employee);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteEmployeeById(@PathVariable Long id) {
		defaultService.deleteById(id);
	}
	
}
