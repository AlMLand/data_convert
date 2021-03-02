package com.m_landalex.dataconvert.service;

import java.util.List;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.exception.ResourceNullException;

public interface DefaultService {

	AbstractObject save(AbstractObject object) throws ResourceNullException;

	AbstractObject fetchById(Long id);
	
	List<AbstractObject> fetchAll();

	void delete(AbstractObject object);
	
	void deleteAll();
	
	void updateCompanyAffiliation();
	
}
