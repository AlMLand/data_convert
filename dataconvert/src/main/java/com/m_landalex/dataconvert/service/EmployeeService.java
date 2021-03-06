package com.m_landalex.dataconvert.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.data.Employee;
import com.m_landalex.dataconvert.domain.EmployeeEntity;
import com.m_landalex.dataconvert.exception.ResourceNullException;
import com.m_landalex.dataconvert.petsistence.EmployeeRepository;
import com.m_landalex.dataconvert.validator.EmployeeValidator;
import com.m_landalex.dataconvert.validator.UserValidator;

@Transactional
@Service
public class EmployeeService implements DefaultService{

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired private UserValidator userValidator;
	@Autowired private RabbitTemplate rabbitTemplate;
	@Autowired private EmployeeValidator employeeValidator;
	@Autowired private EmployeeRepository employeeRepository;
	
	@Autowired
	@Qualifier( value = "conversionServiceFactoryBean" )
	private ConversionService conversionService;

	@Override
	public <T extends AbstractObject> T save(T employee) throws ResourceNullException {
		if( employee == null ) {
			logger.error( "Employee argument is null" );
			rabbitTemplate.convertAndSend( "error" );
			throw new ResourceNullException( "Employee object is null" );
		}
		employeeValidator.validateEmployee( ( Employee ) employee );
		userValidator.validateUser( ( ( Employee )employee ).getUser() );
		employeeRepository.save( conversionService.convert( employee, EmployeeEntity.class ) );
		rabbitTemplate.convertAndSend( "succesful" );
		return employee;
	}
	
	@Transactional( readOnly = true )
	@Override
	public Employee fetchById( Long id ) {
		return conversionService.convert( employeeRepository.findById(id).get(), Employee.class );
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Employee> fetchAll(){
		return employeeRepository.findAll()
					.stream()
					.map( employeeEntity -> conversionService.convert( employeeEntity, Employee.class ) )
					.collect( Collectors.toList() );
	}
	
	@Override
	public void deleteById( Long id ) {
		employeeRepository.deleteById(id);
	}
	
	@Override
	public void deleteAll() {
		employeeRepository.deleteAll();
	}
	
	@Scheduled( fixedRate = 50000 )
	@Override
	public void updateCompanyAffiliation() {
		var returnedList = fetchAll();
		returnedList.forEach( employee -> {
			var years = Period.between( ( ( Employee ) employee ).getJobStartInTheCompany(), LocalDate.now() ).getYears();
			if( years < 0 ) {
				years = 0;
			}
				( ( Employee ) employee ).setCompanyAffiliation( years );
			try {
				save( employee );
			} catch ( ResourceNullException e ) {
				logger.error( "Resource in method updateEmployeeCompanyAffiliation() is null" );
				e.printStackTrace();
			}
		} );
	}

	@Transactional( propagation = Propagation.NEVER )
	@Override
	public long getTotalCount() {
		return employeeRepository.count();
	}

}
