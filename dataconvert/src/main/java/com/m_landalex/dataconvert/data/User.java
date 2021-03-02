package com.m_landalex.dataconvert.data;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class User extends AbstractObject implements Serializable{

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
