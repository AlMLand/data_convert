package com.m_landalex.dataconvert.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.m_landalex.dataconvert.service.DefaultService;

public class AbstractObjectStatisticsImpl implements AbstractObjectStatistics {

	@Autowired
	@Qualifier( "employeeService" )
	private DefaultService employeeService;
	@Autowired
	@Qualifier( "userService" )
	private DefaultService userService;
	
	@Override
	public long getTotalEmployeesCount() {
		return employeeService.getTotalCount();
	}

	@Override
	public long getTotalUsersCount() {
		return userService.getTotalCount();
	}

}
