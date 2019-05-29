package com.bc;

import junit.framework.TestCase;

/**
 * ancestor for each TestCase, which will be created dynamically
 */
public abstract class NamedTestCase extends TestCase{
	private final String name;
	
	public NamedTestCase(String methodName, String originalName){
		super(methodName);
		this.name=originalName;
	}

	public String getName() {
		return name;
	}
	
}
