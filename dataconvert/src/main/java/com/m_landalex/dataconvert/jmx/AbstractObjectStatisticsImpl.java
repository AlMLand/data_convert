package com.m_landalex.dataconvert.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.service.EmployeeService;
import com.m_landalex.dataconvert.service.UserService;

@Component
public class AbstractObjectStatisticsImpl implements AbstractObjectStatistics {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private UserService userService;
	
	@Override
	public long getTotalEmployeesCount() {
		return employeeService.getTotalCount();
	}

	@Override
	public long getTotalUsersCount() {
		return userService.getTotalCount();
	}

}
