package com.m_landalex.dataconvert.domain;

import java.net.URL;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "employee")
public class EmployeeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "ID" )
	private Long id;
	@Version
	@Column( name = "VERSION" )
	private int version;
	@Column( name = "FIRTSNAME" )
	private String firstName;
	@Column( name = "LASTNAME" )
	private String lastName;
	@Column( name = "BIRTHDATE" )
	private LocalDate birthDate;
	@Column( name = "JOBSTARTINTHECOMPANY" )
	private LocalDate jobStartInTheCompany;
	@Column( name = "COMPANYYFFILIATION" )
	private int companyAffiliation;
	@Column( name = "DESCRIPTION" )
	private String description;
	@Lob
	@Basic( fetch = FetchType.LAZY )
	@Column( name = "PHOTO" )
	private byte photo;
	@Column( name = "WEBSITE" )
	private URL webSite;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID", referencedColumnName = "id")
	private UserEntity user;
	
}
