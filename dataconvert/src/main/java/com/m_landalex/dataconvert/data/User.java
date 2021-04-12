package com.m_landalex.dataconvert.data;

import static java.util.Comparator.comparing;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class User extends AbstractObject implements Serializable, Comparable<User> {

	private static final Comparator<User> COMPARATOR = comparing(User::getUserRole)
			.thenComparing(User::getUsername, String.CASE_INSENSITIVE_ORDER);
	
	@NotEmpty( message = "{javax.validation.constraints.NotEmpty.message}" ) 
	@Size(min = 2, max = 50, message = "{javax.validation.constraints.Size.message}" ) 
	private String username;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	private Integer password;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" ) 
	@FutureOrPresent( message = "{javax.validation.constraints.FutureOrPresent.message}" ) 
	private LocalDate start;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	private Boolean aktiv;
	@NotNull( message = "{javax.validation.constraints.NotNull.message}" )
	private Role userRole;
	
	@Override
	public int compareTo(User o) {
		return COMPARATOR.compare(this, o);
	}
	
}
