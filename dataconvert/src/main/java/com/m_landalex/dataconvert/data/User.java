package com.m_landalex.dataconvert.data;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

	@NotNull @Size(min = 2, max = 50) 
	private String username;
	@NotNull 
	private Integer password;
	@NotNull @FutureOrPresent(message = "Start date must be in the future") 
	private LocalDate start;
	@NotNull 
	private Boolean aktiv;
	@NotNull 
	private Role userRole;
	
}
