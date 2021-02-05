package com.m_landalex.dataconvert.formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Component;

import com.m_landalex.dataconvert.data.Role;

@Component
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	private static final DateTimeFormatter DATE_TYPE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private Set<Formatter<?>> formatters = new HashSet<Formatter<?>>();

	@PostConstruct
	public void init() {
		formatters.add(getBooleanFormatter());
		formatters.add(getIntegerFormatter());
		formatters.add(getLocalDateFormatter());
		setFormatters(formatters);
	}
	
	public Formatter<Integer> getIntegerFormatter(){
		return new Formatter<Integer>() {

			@Override
			public String print(Integer object, Locale locale) {
				return object.toString();
			}

			@Override
			public Integer parse(String text, Locale locale) throws ParseException {
				return Integer.parseInt(text.trim());
			}
		};
	}
	
	public Formatter<LocalDate> getLocalDateFormatter(){
		return new Formatter<LocalDate>() {

			@Override
			public String print(LocalDate object, Locale locale) {
				return object.format(DATE_TYPE_FORMAT);
			}

			@Override
			public LocalDate parse(String text, Locale locale) throws ParseException {
				return LocalDate.parse(text.trim());
			}
		};
	}
	
	public Formatter<Boolean> getBooleanFormatter(){
		return new Formatter<Boolean>() {

			@Override
			public String print(Boolean object, Locale locale) {
				return object.toString();
			}

			@Override
			public Boolean parse(String text, Locale locale) throws ParseException {
				return Boolean.valueOf(text.trim());
			}
		};
	}
	
	public Formatter<Role> getEnumFormatter(){
		return new Formatter<Role>() {

			@Override
			public String print(Role object, Locale locale) {
				return object.toString();
			}

			@Override
			public Role parse(String text, Locale locale) throws ParseException {
				return Role.valueOf(text.trim());
			}
		};
	}
	
}
