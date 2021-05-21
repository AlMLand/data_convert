package com.m_landalex.dataconvert.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class AbstractObject {

	protected Long id;
	protected int version;
	
}
