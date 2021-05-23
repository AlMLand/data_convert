package com.m_landalex.dataconvert.service;

import java.util.List;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.exception.ResourceNullException;

public interface DefaultService {

	<T extends AbstractObject> T save(T object) throws ResourceNullException;

	AbstractObject fetchById(Long id);

	<T extends AbstractObject> List<T> fetchAll();

	void deleteById(Long id);

	void deleteAll();

	long getTotalCount();

	default void updateCompanyAffiliation() {};
	
	default AbstractObject fetchByUserName(String userName) {return null;};

}
