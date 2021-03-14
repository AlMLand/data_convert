package com.m_landalex.dataconvert.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m_landalex.dataconvert.data.User;

@Service
public class UserValidator {

	private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

	@Autowired
	private Validator validator;

	public void validateUser(User user) {
		Set<ConstraintViolation<User>> returnedSet = validator.validate(user);
		if (!returnedSet.isEmpty()) {
			logger.info("Quantity of violations: {}", returnedSet.size());
			returnedSet.forEach(violation -> {
				logger.info("Validation for property: {}, with value: {}, with error message: {}",
						violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage());
			});
			throw new ConstraintViolationException("Quantity of violations: " + returnedSet.size() + ".", returnedSet);
		}
	}

}
