package com.m_landalex.dataconvert.petsistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.dataconvert.domain.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

}
