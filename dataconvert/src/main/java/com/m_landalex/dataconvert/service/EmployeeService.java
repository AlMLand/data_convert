package com.m_landalex.dataconvert.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.domain.EmployeeEntity;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.petsistence.EmployeeRepository;

import lombok.Getter;

@Transactional
@Service
public class EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	@Getter
	public boolean done;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	@Qualifier(value = "conversionServiceFactoryBean")
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
	public Employee fetchById(Long id) {
		return conversionService.convert(employeeRepository.findById(id), Employee.class);
	}
	
	@Transactional(readOnly = true)
	public List<Employee> fetchAll(){
		return employeeRepository.findAll()
					.stream()
					.map(employeeEntity -> conversionService.convert(employeeEntity, Employee.class))
					.collect(Collectors.toList());
	}
	
	public void delete(Employee employee) {
		employeeRepository.delete(conversionService.convert(employee, EmployeeEntity.class));
	}
	
	@Scheduled(fixedRate = 10000)
	public void updateEmployeeCompanyAffiliation() {
		List<Employee> returnedList = fetchAll();
		returnedList.forEach(employee -> {
			int years = Period.between(employee.getJobStartInTheCompany(), LocalDate.now()).getYears();
			employee.setCompanyAffiliation(years);
			try {
				save(employee);
				done = true;
			} catch (ResourceNullException e) {
				logger.error("Resource in method updateEmployeeCompanyAffiliation() is null");
				e.printStackTrace();
			}
		});
	}
	
}
