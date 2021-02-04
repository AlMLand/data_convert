package com.m_landalex.dataconvert.converter;

import java.text.ParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.domain.UserEntity;
import com.m_landalex.dataconvert.formatter.ApplicationConversionServiceFactoryBean;

@Component
public class UserEntityToUserConverter implements Converter<UserEntity, User> {

	private static final Logger logger = LoggerFactory.getLogger(UserEntityToUserConverter.class);
	
	@Autowired
	private ApplicationConversionServiceFactoryBean applicationConversionServiceFactoryBean;
	
	@Override
	public User convert(UserEntity source) {
		try {
			return User.builder()
					.username(source.getUsername())
					.password(applicationConversionServiceFactoryBean
									.getIntegerFormatter()
									.parse(source.getPassword(), Locale.GERMAN))
					.start(applicationConversionServiceFactoryBean
									.getLocalDateFormatter()
									.parse(source.getStart(), Locale.GERMAN))
					.aktiv(applicationConversionServiceFactoryBean
									.getBooleanFormatter()
									.parse(source.getAktiv(), Locale.GERMAN))
					.build();
		} catch (ParseException e) {
			logger.error("Not created a user object");
			e.printStackTrace();
			return null;
		}
	}

}
