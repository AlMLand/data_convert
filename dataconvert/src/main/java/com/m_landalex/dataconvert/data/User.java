package com.m_landalex.dataconvert.data;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@SuppressWarnings("serial")
@ToString(exclude = {"id","version"})
public class User implements Serializable{

	private Long id;
	private int version;
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
