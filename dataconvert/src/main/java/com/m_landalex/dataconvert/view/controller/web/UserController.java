package com.m_landalex.dataconvert.view.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.service.DefaultService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	@Qualifier(value = "userService")
	private DefaultService defaultService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<User> returnedList = defaultService.fetchAll();
		model.addAttribute("users", returnedList);
		return "users/list";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") long id, Model model) {
		User returnedUser = (User) defaultService.fetchById(id);
		model.addAttribute("returnedUser", returnedUser);
		return "users/show";
	}
	
}
