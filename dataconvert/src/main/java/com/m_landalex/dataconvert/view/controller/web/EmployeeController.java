package com.m_landalex.dataconvert.view.controller.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.Part;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.data.User;
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
		Employee employee = (Employee) defaultService.fetchById(id);
		model.addAttribute("employee", employee);
		return "employees/show";
	}
	
	@RequestMapping(value = "/edits/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable Long id, Model model) {
		model.addAttribute("employee", defaultService.fetchById(id));
		return "employees/update";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String createForm(Model model) {
		Employee employee = Employee.builder().user(new User()).build();
		model.addAttribute("employee", employee);
		return "employees/update";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("employee")Employee employee, BindingResult bindingResult, 
			Model model, @RequestParam(value = "file", required = false) Part part) throws ResourceNullException, IOException {
		if(bindingResult.hasErrors()) {
			model.addAttribute("employee", employee);
			return "employees/update";
		}
		if(part != null) {
			try(InputStream inputStream = part.getInputStream()) {
				if(inputStream != null) {
					byte[] fileContent = inputStream.readAllBytes();
					employee.setPhoto(fileContent);
				}
			}
		}
		defaultService.save(employee);
		if(employee.getId() != null) {
			return "redirect:/employees/" + employee.getId();
		}
		return "redirect:/employees";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id) {
		defaultService.deleteById(id);
		return "redirect:/employees";
	}
	
	@RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
	@ResponseBody
	public byte[] downloadPhoto(@PathVariable("id") Long id) {
		return ((Employee)defaultService.fetchById(id)).getPhoto();
	}
	
}
