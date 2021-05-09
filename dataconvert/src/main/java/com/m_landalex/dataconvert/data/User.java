package com.m_landalex.dataconvert.data;

import static java.util.Comparator.comparing;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	private static final Comparator<User> COMPARATOR = comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER);
	
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}") 
	@Size(min = 2, max = 50, message = "{javax.validation.constraints.Size.message}") 
	private String username;
	@NotBlank(message = "{javax.validation.constraints.NotBlank.message}") 
	@Size(min = 5, max = 100, message = "{javax.validation.constraints.Size.message}") 
	private String password;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}") 
	@FutureOrPresent(message = "{javax.validation.constraints.FutureOrPresent.message}") 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate start;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private Boolean aktiv;
	@NotNull(message = "{javax.validation.constraints.NotNull.message}")
	private Collection<Role> userRole;
	
	@Override
	public int compareTo(User o) {
		return COMPARATOR.compare(this, o);
	}
	
}
