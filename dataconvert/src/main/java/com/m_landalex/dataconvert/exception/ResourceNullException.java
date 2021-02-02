package com.m_landalex.dataconvert.exception;

public class ResourceNullException extends Exception {

	private static final long serialVersionUID = 1L;

	private String text;
	
	public ResourceNullException(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "ResourceNullException [text=" + text + "]";
	}
	
}
