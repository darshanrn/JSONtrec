package com.dash.jsontrec.test;

import com.dash.supportlibs.RESTcaller;

import junit.framework.TestCase;

public class RESTtester extends TestCase {
	public RESTtester(String name) {
		super(name);		
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testReadURLAPIwithInvalidURL() {
		try {
			String value = RESTcaller.readUrl("http://nothing.abz");
			assertTrue("Read URL API should be null", value == null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testReadURLAPIwithValidURL() {
		try {
			String value = RESTcaller.readUrl("http://www.google.com");
			assertTrue("Read URL API should not be null", value != null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
