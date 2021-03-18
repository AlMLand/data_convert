package com.m_landalex.dataconvert.data;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
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

	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" ) 
	@Size(min = 2, max = 50, message = "{javax.validation.constraints.Size.message}" ) 
	private String username;
	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" )
	private Integer password;
	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" ) 
	@FutureOrPresent( message = "{javax.validation.constraints.FutureOrPresent.message}" ) 
	private LocalDate start;
	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" )
	private Boolean aktiv;
	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" )
	private Role userRole;
	
}
