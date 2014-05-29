/*******************************************************************************
 * ===========================================================
 * Ankush : Big Data Cluster Management Solution
 * ===========================================================
 * 
 * (C) Copyright 2014, by Impetus Technologies
 * 
 * This is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation;
 * 
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/
package com.impetus.ankush.common.framework.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * The Class JavaConfTest.
 */
public class JavaConfTest {

	/**
	 * Test default java conf.
	 */
	@Test
	public void testDefaultJavaConf() {
		JavaConf result = new JavaConf();
		result.setJavaHomePath(null);
		assertNotNull(result);
		assertEquals(null, result.getJavaBinPath());
		assertEquals(null, result.getJavaBundle());
		assertEquals(null, result.getJavaHomePath());
		assertEquals(null, result.isInstall());
	}

	/**
	 * Test java conf.
	 */
	@Test
	public void testJavaConf() {
		Boolean install = true;
		String javaBundle = "java bundle path";
		String javaHomePath = "java home path";

		JavaConf result = new JavaConf();
		result.setInstall(install);
		result.setJavaBundle(javaBundle);
		result.setJavaHomePath(javaHomePath);

		assertNotNull(result);
		assertEquals(javaHomePath + "/bin/java", result.getJavaBinPath());
		assertEquals(javaBundle, result.getJavaBundle());
		assertEquals(javaHomePath + "/", result.getJavaHomePath());
		assertEquals(true, result.isInstall());
	}

	/**
	 * Test get java bin path_1.
	 */
	@Test
	public void testGetJavaBinPath_1() {
		JavaConf fixture = new JavaConf();
		fixture.setJavaHomePath("");
		fixture.setJavaBundle("");
		fixture.setInstall(new Boolean(true));

		String result = fixture.getJavaBinPath();

		// add additional test code here
		assertEquals("bin/java", result);
	}

}
