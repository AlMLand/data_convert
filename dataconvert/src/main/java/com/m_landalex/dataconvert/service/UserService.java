package com.m_landalex.dataconvert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.domain.UserEntity;
import com.m_landalex.dataconvert.petsistence.UserRepository;
import com.m_landalex.dataconvert.validator.UserValidator;

@Transactional
@Service
public class UserService implements DefaultService{
	
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	@Qualifier(value = "conversionServiceFactoryBean")
	private ConversionService conversionService;
	
	public <T extends AbstractObject> T save(T user) {
		userValidator.validateUser(((User)user));
		userRepository.save(conversionService.convert(user, UserEntity.class));
		return user;
	}
	
	@Transactional(readOnly = true)
	@Override
	public User fetchById(Long id) {
		return conversionService.convert(userRepository.findById(id).get(), User.class);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<User> fetchAll(){
		return userRepository.findAll()
				.stream()
				.map(userEntity -> conversionService.convert(userEntity, User.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}
	
	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Transactional( propagation = Propagation.NEVER )
	@Override
	public long getTotalCount() {
		return userRepository.count();
	}

	@Override
	public User fetchByUserName(String userName) {
		return conversionService.convert(userRepository.findByUsername(userName), User.class);
	}

}
