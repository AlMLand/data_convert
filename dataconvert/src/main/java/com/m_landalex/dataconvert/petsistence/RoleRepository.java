package com.m_landalex.dataconvert.petsistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.dataconvert.domain.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}
