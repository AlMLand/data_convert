package com.m_landalex.dataconvert.converter;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.User;
import com.m_landalex.dataconvert.domain.UserEntity;
import com.m_landalex.dataconvert.formatter.ApplicationConversionServiceFactoryBean;

@Component
public class UserToUserEntityConverter implements Converter<User, UserEntity> {

	@Autowired
	private ApplicationConversionServiceFactoryBean applicationConversionServiceFactoryBean;

	@Override
	public UserEntity convert(User source) {
		if (source != null) {
			UserEntity userEntity = new UserEntity();
			userEntity.setId(source.getId());
			userEntity.setUsername(source.getUsername());
			userEntity.setPassword(applicationConversionServiceFactoryBean.getIntegerFormatter()
					.print(source.getPassword(), Locale.GERMAN));
			userEntity.setStart(applicationConversionServiceFactoryBean.getLocalDateFormatter().print(source.getStart(),
					Locale.GERMAN));
			userEntity.setAktiv(applicationConversionServiceFactoryBean.getBooleanFormatter().print(source.getAktiv(),
					Locale.GERMAN));
			userEntity.setUserRole(applicationConversionServiceFactoryBean.getEnumFormatter()
					.print(source.getUserRole(), Locale.GERMAN));
			userEntity.setVersion(source.getVersion());
			return userEntity;
		} else {
			return null;
		}
	}

}
