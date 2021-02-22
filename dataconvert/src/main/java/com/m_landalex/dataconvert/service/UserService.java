package com.m_landalex.dataconvert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.domain.UserEntity;
import com.m_landalex.dataconvert.petsistence.UserRepository;

@Transactional
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	@Qualifier(value = "conversionServiceFactoryBean")
	private ConversionService conversionService;
	
	public User save(User user) {
		userRepository.save(conversionService.convert(user, UserEntity.class));
		return user;
	}
	
	@Transactional(readOnly = true)
	public User fetchById(Long id) {
		return conversionService.convert(userRepository.findById(id), User.class);
	}
	
	@Transactional(readOnly = true)
	public List<User> fetchAll(){
		return userRepository.findAll()
				.stream()
				.map(userEntity -> conversionService.convert(userEntity, User.class))
				.collect(Collectors.toList());
	}
	
	public void delete(User user) {
		userRepository.delete(conversionService.convert(user, UserEntity.class));
	}
	
}
