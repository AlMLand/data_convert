package com.m_landalex.dataconvert.view.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	@Qualifier(value = "userService")
	private DefaultService defaultService;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public User createUser(@RequestBody User user) throws ResourceNullException {
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
	public void updateUserById(@RequestBody User user) throws ResourceNullException {
		defaultService.save(user);
		logger.info("User with id {} is updated.", user.getId());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUserById(@PathVariable Long id) {
		defaultService.deleteById(id);
		logger.info("User with id {} is deleted.", id);
	}
	
}
