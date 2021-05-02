package com.m_landalex.dataconvert.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.m_landalex.dataconvert.data.AbstractObject;
import com.m_landalex.dataconvert.exception.ResourceNullException;

public interface DefaultService {

	<T extends AbstractObject> T save(T object) throws ResourceNullException;

	AbstractObject fetchById(Long id);

	AbstractObject fetchByUserName(String userName);

	<T extends AbstractObject> List<T> fetchAll();

	void deleteById(Long id);

	void deleteAll();

	void updateCompanyAffiliation();

	long getTotalCount();

	<T extends AbstractObject> Page<T> findAllByPage(Pageable pageable);

}
