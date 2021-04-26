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
		if (source != null) {
			User user = new User();
			user.setId(source.getId());
			user.setVersion(source.getVersion());
			user.setUsername(source.getUsername());
			user.setPassword(source.getPassword());
			try {
				user.setStart(applicationConversionServiceFactoryBean.getLocalDateFormatter().parse(source.getStart(),
						Locale.GERMAN));
				user.setAktiv(applicationConversionServiceFactoryBean.getBooleanFormatter().parse(source.getAktiv(),
						Locale.GERMAN));
				user.setUserRole(applicationConversionServiceFactoryBean.getEnumFormatter().parse(source.getUserRole(),
						Locale.GERMAN));
			} catch (ParseException e) {
				logger.error("Parse ERROR", e);
			}
			return user;
		} else {
			return null;
		}
	}

}
