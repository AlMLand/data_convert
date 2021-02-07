package com.m_landalex.dataconvert.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.m_landalex.dataconvert.validator.EmployeeValidatorClassType;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = EmployeeValidatorClassType.class)
@Documented
public @interface CheckEmployee {

	String message() default "Employee should have first name, last name, birth date and user daten defined or the last name lenght must be between 2 and 50 ";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {}; 
	
}