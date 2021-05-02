package com.m_landalex.dataconvert.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.domain.RoleEntity;

@Component
public class RoleToRoleEntityConverter implements Converter<Role, RoleEntity> {

	@Override
	public RoleEntity convert(Role source) {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setId(source.getId());
		roleEntity.setVersion(source.getVersion());
		roleEntity.setRole(source.getRole());
		return roleEntity;
	}

}
