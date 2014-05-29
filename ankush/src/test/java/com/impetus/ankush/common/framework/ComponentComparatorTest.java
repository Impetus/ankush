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
package com.impetus.ankush.common.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.impetus.ankush.common.framework.config.DeployableConf;

/**
 * The Class ComponentComparatorTest.
 */
public class ComponentComparatorTest {
	
	/**
	 * Test component comparator_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testComponentComparator_1()
		throws Exception {

		ComponentComparator result = new ComponentComparator();

		assertNotNull(result);
	}

	/**
	 * Test component comparator_2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testComponentComparator_2()
		throws Exception {
		boolean desc = true;

		ComponentComparator result = new ComponentComparator(desc);

		assertNotNull(result);
	}

	/**
	 * Test compare_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCompare_1()
		throws Exception {
		ComponentComparator fixture = new ComponentComparator(true);
		DeployableConf objectOne = null;
		DeployableConf objectTwo = null;

		int result = fixture.compare(objectOne, objectTwo);

		assertEquals(0, result);
	}

}
