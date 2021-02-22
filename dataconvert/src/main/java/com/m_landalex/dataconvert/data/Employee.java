package com.m_landalex.dataconvert.data;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.m_landalex.dataconvert.annotation.CheckEmployee;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@CheckEmployee
@SuppressWarnings("serial")
@ToString(exclude = {"id","version"})
public class Employee implements Serializable{
	
	private Long id;
	private int version;
	@NotNull
	private String firstName;
	@NotNull @Size(min = 2, max = 50) 
	private String lastName;
	@NotNull @Past(message = "Birth date must be in the past") 
	private LocalDate birthDate;
	@NotNull
	private LocalDate jobStartInTheCompany;
	@NotNull @Min(value = 0, message = "The experience of a new employee of the company must be equal to 0")
	private int companyAffiliation;
	private URL webSite;
	@NotNull 
	private User user;

}
