package com.m_landalex.dataconvert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.domain.UserEntity;
import com.m_landalex.dataconvert.petsistence.UserRepository;

@Transactional
@Service
public class UserService implements DefaultService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	@Qualifier(value = "conversionServiceFactoryBean")
	private ConversionService conversionService;
	
	public AbstractObject save(AbstractObject user) {
		userRepository.save(conversionService.convert(user, UserEntity.class));
		return user;
	}
	
	@Transactional(readOnly = true)
	public User fetchById(Long id) {
		return conversionService.convert(userRepository.findById(id).get(), User.class);
	}
	
	@Transactional(readOnly = true)
	public List<AbstractObject> fetchAll(){
		return userRepository.findAll()
				.stream()
				.map(userEntity -> conversionService.convert(userEntity, User.class))
				.collect(Collectors.toList());
	}
	
	public void delete(AbstractObject user) {
		userRepository.delete(conversionService.convert(user, UserEntity.class));
	}
	
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public void updateCompanyAffiliation() {}
	
}
