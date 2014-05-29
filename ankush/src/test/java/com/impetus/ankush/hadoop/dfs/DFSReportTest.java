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
package com.impetus.ankush.hadoop.dfs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class DFSReportTest.
 *
 * @author bgunjan
 */
public class DFSReportTest {
	
	/** The dfs report. */
	DFSReport dfsReport;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void testSetUp() throws Exception {
		dfsReport = new DFSReport();
	}
	
	/**
	 * Test dfs report test.
	 */
	@Test
	public void testDFSReportTest() {
		assertNotNull(dfsReport);
	}
	
	/**
	 * Test get compiled.
	 */
	@Test
	public void testGetCompiled() {
		String compiled = "Thu Oct 4 20:40:32 UTC 2012";
		dfsReport.setCompiled(compiled);
		String val = dfsReport.getCompiled();
		assertNotNull(val);
		assertSame(compiled, dfsReport.getCompiled());
	}
	
	/**
	 * Test set compiled.
	 */
	@Test
	public void testSetCompiled() {
		String compiled = "Thu Oct 4 20:40:32 UTC 2012";
		dfsReport.setCompiled("Thu Oct 12 20:40:32 UTC 2012");

		assertNotNull(dfsReport.getCompiled());
		assertNotSame(compiled, dfsReport.getCompiled());
	}
	
	/**
	 * Test get version.
	 */
	@Test
	public void testGetVersion() {
		String version = "0.20.2 - revision 1393290 by hortonfo";
		dfsReport.setVersion(version);
		String val = dfsReport.getVersion();
		assertNotNull(val);
		assertSame(version, dfsReport.getVersion());
	}
	
	/**
	 * Test set version.
	 */
	@Test
	public void testSetVersion() {
		String version = "0.20.2 - revision 1393290 by hortonfo";
		dfsReport.setVersion("1.0.4 - revision 1393290 by hortonfo");

		assertNotNull(dfsReport.getVersion());
		assertNotSame(version, dfsReport.getVersion());
	}
	
	/**
	 * Test get configured capacity.
	 */
	@Test
	public void testGetConfiguredCapacity() {
		String configuredCapacity = "140.68GB";
		dfsReport.setConfiguredCapacity(configuredCapacity);
		String val = dfsReport.getConfiguredCapacity();
		assertNotNull(val);
		assertSame(configuredCapacity, dfsReport.getConfiguredCapacity());
	}
	
	/**
	 * Test set configured capacity.
	 */
	@Test
	public void testSetConfiguredCapacity() {
		String configuredCapacity = "140.68GB";
		dfsReport.setConfiguredCapacity("120.0GB");

		assertNotNull(dfsReport.getConfiguredCapacity());
		assertNotSame(configuredCapacity, dfsReport.getConfiguredCapacity());
	}
	
	/**
	 * Test get dfs used.
	 */
	@Test
	public void testGetDfsUsed() {
		String dfsUsed = "41.65MB";
		dfsReport.setDfsUsed(dfsUsed);
		String val = dfsReport.getDfsUsed();
		assertNotNull(val);
		assertSame(dfsUsed, dfsReport.getDfsUsed());
	}
	
	/**
	 * Test set dfs used.
	 */
	@Test
	public void testSetDfsUsed() {
		String dfsUsed = "41.65MB";
		dfsReport.setDfsUsed("32.65MB");

		assertNotNull(dfsReport.getDfsUsed());
		assertNotSame(dfsUsed, dfsReport.getDfsUsed());
	}
	
	/**
	 * Test get dfs remaining.
	 */
	@Test
	public void testGetDfsRemaining() {
		String dfsRemaining = "90.36GB";
		dfsReport.setDfsRemaining(dfsRemaining);
		String val = dfsReport.getDfsRemaining();
		assertNotNull(val);
		assertSame(dfsRemaining, dfsReport.getDfsRemaining());
	}
	
	/**
	 * Test set dfs remaining.
	 */
	@Test
	public void testSetDfsRemaining() {
		String dfsRemaining = "90.36GB";
		dfsReport.setDfsRemaining("80.36GB");

		assertNotNull(dfsReport.getDfsRemaining());
		assertNotSame(dfsRemaining, dfsReport.getDfsRemaining());
	}
	
	/**
	 * Test get non dfs used.
	 */
	@Test
	public void testGetNonDFSUsed() {
		String nonDFSUsed = "50.32GB";
		dfsReport.setNonDFSUsed(nonDFSUsed);
		String val = dfsReport.getNonDFSUsed();
		assertNotNull(val);
		assertSame(nonDFSUsed, dfsReport.getNonDFSUsed());
	}
	
	/**
	 * Test set non dfs used.
	 */
	@Test
	public void testSetNonDFSUsed() {
		String nonDFSUsed = "50.32GB";
		dfsReport.setNonDFSUsed("22.32GB");

		assertNotNull(dfsReport.getNonDFSUsed());
		assertNotSame(nonDFSUsed, dfsReport.getNonDFSUsed());
	}
	
	/**
	 * Test get dfs used percent.
	 */
	@Test
	public void testGetDfsUsedPercent() {
		float dfsUsedPercent = 0.02891159f;
		dfsReport.setDfsUsedPercent(dfsUsedPercent);
		float val = dfsReport.getDfsUsedPercent();
		assertNotNull(val);
		assertNotSame(dfsUsedPercent, dfsReport.getDfsUsedPercent());
	}
	
	/**
	 * Test set dfs used percent.
	 */
	@Test
	public void testSetDfsUsedPercent() {
		float dfsUsedPercent = 0.02891159f;
		dfsReport.setDfsUsedPercent(0.59f);

		assertNotNull(dfsReport.getDfsUsedPercent());
		assertNotSame(dfsUsedPercent, dfsReport.getDfsUsedPercent());
	}
	
	/**
	 * Test get dfs remaining percent.
	 */
	@Test
	public void testGetDfsRemainingPercent() {
		float dfsRemainingPercent = 64.230515f;
		dfsReport.setDfsRemainingPercent(dfsRemainingPercent);
		float val = dfsReport.getDfsRemainingPercent();
		assertNotNull(val);
		assertNotSame(dfsRemainingPercent, dfsReport.getDfsRemainingPercent());
	}
	
	/**
	 * Test set dfs remaining percent.
	 */
	@Test
	public void testSetDfsRemainingPercent() {
		float dfsRemainingPercent = 64.230515f;
		dfsReport.setDfsRemainingPercent(44.230515f);

		assertNotNull(dfsReport.getDfsRemainingPercent());
		assertNotSame(dfsRemainingPercent, dfsReport.getDfsRemainingPercent());
	}
	
	/**
	 * Test get live nodes count.
	 */
	@Test
	public void testGetLiveNodesCount() {
		int liveNodesCount = 2;
		dfsReport.setLiveNodesCount(liveNodesCount);
		int val = dfsReport.getLiveNodesCount();
		assertNotNull(val);
		assertSame(liveNodesCount, dfsReport.getLiveNodesCount());
	}
	
	/**
	 * Test set live nodes count.
	 */
	@Test
	public void testSetLiveNodesCount() {
		int liveNodesCount = 6;
		dfsReport.setLiveNodesCount(4);

		assertNotNull(dfsReport.getLiveNodesCount());
		assertNotSame(liveNodesCount, dfsReport.getLiveNodesCount());
	}
	
	/**
	 * Test get dead nodes count.
	 */
	@Test
	public void testGetDeadNodesCount() {
		int deadNodesCount = 1;
		dfsReport.setDeadNodesCount(deadNodesCount);
		int val = dfsReport.getDeadNodesCount();
		assertNotNull(val);
		assertSame(deadNodesCount, dfsReport.getDeadNodesCount());
	}
	
	/**
	 * Test set dead nodes count.
	 */
	@Test
	public void testSetDeadNodesCount() {
		int deadNodesCount = 1;
		dfsReport.setDeadNodesCount(0);

		assertNotNull(dfsReport.getDeadNodesCount());
		assertNotSame(deadNodesCount, dfsReport.getDeadNodesCount());
	}
	
	/**
	 * Test get all nodes count.
	 */
	@Test
	public void testGetAllNodesCount() {
		int allNodesCount = 7;
		dfsReport.setAllNodesCount(allNodesCount);
		int val = dfsReport.getAllNodesCount();
		assertNotNull(val);
		assertSame(allNodesCount, dfsReport.getAllNodesCount());
	}
	
	/**
	 * Test set all nodes count.
	 */
	@Test
	public void testSetAllNodesCount() {
		int allNodesCount = 7;
		dfsReport.setAllNodesCount(6);

		assertNotNull(dfsReport.getAllNodesCount());
		assertNotSame(allNodesCount, dfsReport.getAllNodesCount());
	}
	
	/**
	 * Test get under replicated block count.
	 */
	@Test
	public void testGetUnderReplicatedBlockCount() {
		long underReplicatedBlockCount = 7L;
		dfsReport.setUnderReplicatedBlockCount(underReplicatedBlockCount);
		long val = dfsReport.getUnderReplicatedBlockCount();
		assertNotNull(val);
		assertSame(underReplicatedBlockCount, dfsReport.getUnderReplicatedBlockCount());
	}
	
	/**
	 * Test set under replicated block count.
	 */
	@Test
	public void testSetUnderReplicatedBlockCount() {
		long underReplicatedBlockCount = 7L;
		dfsReport.setUnderReplicatedBlockCount(6);

		assertNotNull(dfsReport.getUnderReplicatedBlockCount());
		assertNotSame(underReplicatedBlockCount, dfsReport.getUnderReplicatedBlockCount());
	}
	
	/**
	 * Checks if is in safemode.
	 */
	@Test
	public void isInSafemode() {
		boolean inSafemode = true;
		dfsReport.setInSafemode(inSafemode);
		boolean val = dfsReport.isInSafemode();
		assertNotNull(val);
		assertSame(inSafemode, dfsReport.isInSafemode());
	}
	
	/**
	 * Test set in safemode.
	 */
	@Test
	public void testSetInSafemode() {
		boolean inSafemode = true;
		dfsReport.setInSafemode(false);

		assertNotNull(dfsReport.isInSafemode());
		assertNotSame(inSafemode, dfsReport.isInSafemode());
	}
	
	/**
	 * Test get default replication.
	 */
	@Test
	public void testGetDefaultReplication() {
		int defaultReplication = 3;
		dfsReport.setDefaultReplication(defaultReplication);
		int val = dfsReport.getDefaultReplication();
		assertNotNull(val);
		assertSame(defaultReplication, dfsReport.getDefaultReplication());
	}
	
	/**
	 * Test set default replication.
	 */
	@Test
	public void testSetDefaultReplication() {
		int defaultReplication = 3;
		dfsReport.setDefaultReplication(2);

		assertNotNull(dfsReport.getDefaultReplication());
		assertNotSame(defaultReplication, dfsReport.getDefaultReplication());
	}
	
	/**
	 * Test get corrupted block count.
	 */
	@Test
	public void testGetCorruptedBlockCount() {
		long corruptedBlockCount = 7L;
		dfsReport.setCorruptedBlockCount(corruptedBlockCount);
		long val = dfsReport.getCorruptedBlockCount();
		assertNotNull(val);
		assertSame(corruptedBlockCount, dfsReport.getCorruptedBlockCount());
	}
	
	/**
	 * Test set corrupted block count.
	 */
	@Test
	public void testSetCorruptedBlockCount() {
		long corruptedBlockCount = 7L;
		dfsReport.setCorruptedBlockCount(6);

		assertNotNull(dfsReport.getCorruptedBlockCount());
		assertNotSame(corruptedBlockCount, dfsReport.getCorruptedBlockCount());
	}
	
	/**
	 * Test get default block size.
	 */
	@Test
	public void testGetDefaultBlockSize() {
		long defaultBlockSize = 7L;
		dfsReport.setDefaultBlockSize(defaultBlockSize);
		long val = dfsReport.getDefaultBlockSize();
		assertNotNull(val);
		assertSame(defaultBlockSize, dfsReport.getDefaultBlockSize());
	}
	
	/**
	 * Test set default block size.
	 */
	@Test
	public void testSetDefaultBlockSize() {
		long defaultBlockSize = 7L;
		dfsReport.setDefaultBlockSize(6);

		assertNotNull(dfsReport.getDefaultBlockSize());
		assertNotSame(defaultBlockSize, dfsReport.getDefaultBlockSize());
	}
	
	/**
	 * Test get missing block count.
	 */
	@Test
	public void testGetMissingBlockCount() {
		long missingBlockCount = 7L;
		dfsReport.setMissingBlockCount(missingBlockCount);
		long val = dfsReport.getMissingBlockCount();
		assertNotNull(val);
		assertSame(missingBlockCount, dfsReport.getMissingBlockCount());
	}
	
	/**
	 * Test set missing block count.
	 */
	@Test
	public void testSetMissingBlockCount() {
		long missingBlockCount = 7L;
		dfsReport.setMissingBlockCount(6);

		assertNotNull(dfsReport.getMissingBlockCount());
		assertNotSame(missingBlockCount, dfsReport.getMissingBlockCount());
	}
}
