package com.m_landalex.dataconvert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.domain.EmployeeEntity;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.petsistence.EmployeeRepository;

@Transactional
@Service
public class EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ConversionService conversionService;
	
	public Employee save(Employee employee) throws ResourceNullException {
		if(employee == null) {
			logger.error("employee argument is null");
			throw new ResourceNullException("employee object is null");
		}
		employeeRepository.save(conversionService.convert(employee, EmployeeEntity.class));
		return employee;
	}
	
	@Transactional(readOnly = true)
	public List<Employee> fetchAll(){
		return employeeRepository.findAll()
					.stream()
					.map(employeeEntity -> conversionService.convert(employeeEntity, Employee.class))
					.collect(Collectors.toList());
	}
	
}
