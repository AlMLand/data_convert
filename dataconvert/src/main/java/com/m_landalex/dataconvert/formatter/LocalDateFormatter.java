package com.m_landalex.dataconvert.formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

public class LocalDateFormatter implements Formatter<LocalDate> {

	private static final DateTimeFormatter DATE_TYPE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Override
	public String print(LocalDate object, Locale locale) {
		return object.format(DATE_TYPE_FORMAT);
	}

	@Override
	public LocalDate parse(String text, Locale locale) throws ParseException {
		return LocalDate.parse(text.trim());
	}

}
