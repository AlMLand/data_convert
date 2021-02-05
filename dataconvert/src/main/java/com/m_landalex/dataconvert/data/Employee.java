package com.m_landalex.dataconvert.data;

import java.net.URL;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {
	
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private URL webSite;
	private User user;

}
