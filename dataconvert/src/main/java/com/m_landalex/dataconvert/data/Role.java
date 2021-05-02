package com.m_landalex.dataconvert.data;

import java.io.Serializable;
import java.util.Comparator;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractObject implements Serializable, Comparable<Role>{

	private static final Comparator<Role> COMPARATOR = Comparator.comparing(Role::getRole, String.CASE_INSENSITIVE_ORDER);
	
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}")
	private String role;

	@Override
	public int compareTo(Role o) {
		return COMPARATOR.compare(this, o);
	}

	@Override
	public String toString() {
		return role;
	}
	
}
