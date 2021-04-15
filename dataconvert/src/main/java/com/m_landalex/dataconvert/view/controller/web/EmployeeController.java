package com.m_landalex.dataconvert.view.controller.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.DefaultService;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {

	@Autowired
	@Qualifier(value = "employeeService")
	private DefaultService defaultService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
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
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable Long id, Model model) {
		model.addAttribute("employeeToUpdate", defaultService.fetchById(id));
		return "employees/update";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("employeeToUpdate")Employee employee, BindingResult bindingResult, 
			Model model) throws ResourceNullException {
		if(bindingResult.hasErrors()) {
			model.addAttribute("employeeToUpdate", employee);
			return "employees/update";
		}
		defaultService.save(employee);
		return "redirect:/employees/" + employee.getId();
	}
	
}
