package com.m_landalex.dataconvert.data;

import static java.util.Comparator.comparing;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

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
public class Employee extends AbstractObject implements Serializable, Comparable<Employee> {
	
	private static final Comparator<Employee> COMPARATOR = comparing(Employee::getBirthDate)
			.thenComparing(Employee::getLastName, String.CASE_INSENSITIVE_ORDER)
			.thenComparing(Employee::getFirstName, String.CASE_INSENSITIVE_ORDER)
			.thenComparingInt((Employee e) -> e.getCompanyAffiliation());
	
	@NotBlank( message = "{javax.validation.constraints.NotBlank.message}" )
	@Size( min = 2, max = 50, message = "{javax.validation.constraints.Size.message}" )
	private String firstName;
	@NotBlank( message = "{javax.validation.constraints.NotBlank.message}" ) 
	@Size( min = 2, max = 50, message = "{javax.validation.constraints.Size.message}" ) 
	private String lastName;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	@Past( message = "{javax.validation.constraints.Past.message}" )
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	@FutureOrPresent( message = "{javax.validation.constraints.FutureOrPresent.message}" )
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate jobStartInTheCompany;
	@Min( value = 0, message = "{javax.validation.constraints.Min.message}" )
	private int companyAffiliation;
	private String description;
	private byte[] photo;
	private URL webSite;
	@Valid
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	private User user;
	
	@Override
	public int compareTo(Employee o) {
		return COMPARATOR.compare(this, o);
	}

}
