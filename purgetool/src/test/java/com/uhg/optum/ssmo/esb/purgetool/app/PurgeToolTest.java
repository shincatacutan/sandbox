package com.uhg.optum.ssmo.esb.purgetool.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PurgeToolTest extends TestCase {
	public PurgeToolTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PurgeToolTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testPurgeTool() {
		assertTrue(true);
	}
}
