package com.m_landalex.dataconvert.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "ID" )
	private Long id;
	@Version
	@Column( name = "VERSION")
	private int version;
	@Column( name = "USERNAME" )
	private String username;
	@Column( name = "PASSWORD" )
	private String password;
	@Column( name = "START" )
	private String start;
	@Column( name = "AKTIV" )
	private String aktiv;
	@Column( name = "USERROLE" )
	private String userRole;
	@OneToOne(mappedBy = "user")
	private EmployeeEntity employeeEntity;
	
}
