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
/**
 * 
 */
package com.impetus.ankush.agent.sigar;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.agent.sigar.SigarNodeInfoProvider;


/**
 * The Class SigarNodeInfoProviderTest.
 *
 * @author hokam.chauhan
 */
public class SigarNodeInfoProviderTest {
	
	/** The provider. */
	private SigarNodeInfoProvider provider; 
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		provider = new SigarNodeInfoProvider();
	}
	
	/**
	 * Test provider object existence.
	 */
	@Test
	public void testProviderObjectExistence() 	{
		assert(provider != null);
	}
	
	/**
	 * Test get node cpu info function.
	 */
	@Test
	public void testGetNodeCpuInfoFunction() {
		
		// Test the class type of returned object should be same
		assertEquals(ArrayList.class, provider.getNodeCpuInfos().getClass());
		
		// Test the function should not return the null value
		assert(provider.getNodeCpuInfos() != null);
	}
	
	/**
	 * Test get node memory info function.
	 */
	@Test
	public void testGetNodeMemoryInfoFunction() {
		
		// Test the class type of returned object should be same
		assertEquals(HashMap.class, provider.getNodeMemoryInfo().getClass());
		
		// Test the function should not return the null value
		assert(provider.getNodeMemoryInfo() != null);
	}
	
	/**
	 * Test get node network info function.
	 */
	@Test
	public void testGetNodeNetworkInfoFunction() {
		
		// Test the class type of returned object should be same
		assertEquals(ArrayList.class, provider.getNodeNetworkInfos().getClass());
		
		// Test the function should not return the null value
		assert(provider.getNodeNetworkInfos() != null);
	}
	
	/**
	 * Test get node swap info function.
	 */
	@Test
	public void testGetNodeSwapInfoFunction() {
		
		// Test the class type of returned object should be same
		assertEquals(HashMap.class, provider.getNodeSwapInfo().getClass());
		
		// Test the function should not return the null value
		assert(provider.getNodeSwapInfo() != null);
	}
	
	
	/**
	 * Test get node disk info function.
	 */
	@Test
	public void testGetNodeDiskInfoFunction() {
		
		// Test the class type of returned object should be same
		assertEquals(ArrayList.class, provider.getNodeDiskInfos().getClass());
		
		// Test the function should not return the null value
		assert(provider.getNodeDiskInfos() != null);
	}
	
	/**
	 * Test get node os info function.
	 */
	@Test
	public void testGetNodeOSInfoFunction() {
		
		// Test the class type of returned object should be same
		assertEquals(HashMap.class, provider.getNodeOSInfo().getClass());
		
		// Test the function should not return the null value
		assert(provider.getNodeOSInfo() != null);
	}
	
	
	/**
	 * Test get node uptime info function.
	 */
	@Test
	public void testGetNodeUptimeInfoFunction() {
		// Test the class type of returned object should be same
		assertEquals(HashMap.class, provider.getNodeUpTimeInfo().getClass());
		
		// Test the function should not return the null value
		assert(provider.getNodeUpTimeInfo() != null);
	}

}
