package com.m_landalex.dataconvert.data;

import java.net.URL;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {
	
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private LocalDate birthDate;
	private URL webSite;
	@NotNull
	private User user;

}
