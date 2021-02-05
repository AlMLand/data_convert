package com.m_landalex.dataconvert.data;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

	private String username;
	private Integer password;
	private LocalDate start;
	private Boolean aktiv;
	private Role userRole;
	
}
