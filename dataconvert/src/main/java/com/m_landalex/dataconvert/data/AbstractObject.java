package com.m_landalex.dataconvert.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AbstractObject {

	protected Long id;
	protected int version;
	
}
