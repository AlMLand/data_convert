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

import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/listUsers", method = RequestMethod.GET)
	@ResponseBody
	public List<User> listUsers(){
		return userService.fetchAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public User findUserByid(@PathVariable Long id) {
		return userService.fetchById(id);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public User createUser(@RequestBody User user) {
		userService.save(user);
		return user;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public void updateUser(@RequestBody User user) {
		userService.save(user);
		logger.info("User with id {} is updated.", user.getId());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteUser(@PathVariable Long id) {
		userService.delete(userService.fetchById(id));
		logger.info("User with id {} is deleted.", id);
	}
}
