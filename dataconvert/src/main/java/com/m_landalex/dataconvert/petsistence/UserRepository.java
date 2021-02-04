package com.m_landalex.dataconvert.petsistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m_landalex.dataconvert.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
