package com.m_landalex.dataconvert.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.Role;
import com.m_landalex.dataconvert.domain.RoleEntity;

@Component
public class RoleEntityToRoleConverter implements Converter<RoleEntity, Role> {

	@Override
	public Role convert(RoleEntity source) {
		Role role = new Role();
		role.setId(source.getId());
		role.setVersion(source.getVersion());
		role.setRole(source.getRole());
		return role;
	}

}
