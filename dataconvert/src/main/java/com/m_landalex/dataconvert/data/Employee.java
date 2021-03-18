package com.m_landalex.dataconvert.data;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Employee extends AbstractObject implements Serializable{
	
	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" )
	@Size( min = 2, max = 50, message = "{javax.validation.constraints.Size.message}" )
	private String firstName;
	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" ) 
	@Size( min = 2, max = 50, message = "{javax.validation.constraints.Size.message}" ) 
	private String lastName;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	@Past( message = "{javax.validation.constraints.Past.message}" ) 
	private LocalDate birthDate;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	@FutureOrPresent( message = "{javax.validation.constraints.FutureOrPresent.message}" )
	private LocalDate jobStartInTheCompany;
	@Min( value = 0, message = "{javax.validation.constraints.Min.message}" )
	private int companyAffiliation;
	private String description;
	private byte[] photo;
	private URL webSite;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	private User user;

}
