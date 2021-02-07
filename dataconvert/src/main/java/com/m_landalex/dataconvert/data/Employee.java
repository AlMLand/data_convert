package com.m_landalex.dataconvert.data;

import java.net.URL;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.m_landalex.dataconvert.annotation.CheckEmployee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@CheckEmployee
public class Employee {
	
	@NotNull private String firstName;
	@NotNull @Size(min = 2, max = 50) private String lastName;
	@NotNull private LocalDate birthDate;
	private URL webSite;
	@NotNull private User user;

}
