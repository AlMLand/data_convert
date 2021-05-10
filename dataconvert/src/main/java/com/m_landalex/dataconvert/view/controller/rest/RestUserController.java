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
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.service.DefaultService;

@Controller
@RequestMapping(value = "/rest/employees/users")
public class RestUserController {

	@Autowired
	@Qualifier(value = "userService")
	private DefaultService defaultService;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public User createUser(@Valid @RequestBody User user) throws ResourceNullException {
		defaultService.save(user);
		return user;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<AbstractObject> fetchAllUsers(){
		return defaultService.fetchAll();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteAllUsers() {
		defaultService.deleteAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public AbstractObject fetchUserById(@PathVariable Long id) {
		return defaultService.fetchById(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void updateUserById(@Valid @RequestBody User user) throws ResourceNullException {
		defaultService.save(user);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUserById(@PathVariable Long id) {
		defaultService.deleteById(id);
	}
	
}
