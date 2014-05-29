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
package com.impetus.ankush.core.components;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;

/**
 * @author bgunjan
 *
 */
public class HadoopConfTest {
	private HadoopConf hadoopConfObj;
	private HadoopNodeConf nodeConf;
	private ArrayList<HadoopNodeConf> nodeList;
	private String namenode;
	private String node;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		hadoopConfObj = new HadoopConf();
		String publicIp = "192.168.145.61";
		String privateIp = "192.168.145.61";
		nodeConf = new HadoopNodeConf(publicIp, privateIp);
		nodeList = new ArrayList<HadoopNodeConf>();
		
		namenode = "192.168.145.61";
		node = "192.168.00.00";
		nodeList.add(nodeConf);
	}

	@Test
	public void test() {
	}
	
	@Test
	public void testHadoopConfTest() {
		assertNotNull(hadoopConfObj);
	}
	
	/**
	 * @return the hadoopConfDir
	 */
	@Test
	public void testGetHadoopConfDir() {
		String hadoopConfDir = "/home/test/hadoopConf/";
		hadoopConfObj.setHadoopConfDir(hadoopConfDir);
		String val = hadoopConfObj.getHadoopConfDir();
		assertNotNull(val);
		assertSame(hadoopConfDir, hadoopConfObj.getHadoopConfDir());
	}

	/**
	 * @param hadoopConfDir
	 *            the hadoopConfDir to set
	 */
	@Test
	public void testSetHadoopConfDir() {
		hadoopConfObj.setHadoopConfDir("/home/test/hadopConf");
		String hadoopConfDir = "/home/test1/hadoopConf1";

		hadoopConfObj.setHadoopConfDir(hadoopConfDir);

		assertNotSame("/home/test/hadopConf", hadoopConfObj.getHadoopConfDir());
		assertNotNull(hadoopConfObj.getHadoopConfDir());
	}

	/**
	 * @return the hadoopTmpDir
	 */
	@Test
	public void testGetHadoopTmpDir() {
		String hadoopTmpDir = "/home/test/hadoopTmp/";
		hadoopConfObj.setHadoopTmpDir(hadoopTmpDir);
		String val = hadoopConfObj.getHadoopTmpDir();
		assertNotNull(val);
		assertSame(hadoopTmpDir, hadoopConfObj.getHadoopTmpDir());
	}

	/**
	 * @param hadoopTmpDir
	 *            the hadoopTmpDir to set
	 */
	@Test
	public void testSetHadoopTmpDir() {
		this.hadoopConfObj.setHadoopTmpDir("/home/test/hadoopTemp");
		String hadoopTmpDir = "/home/test1/hadoopTmp1";
		hadoopConfObj.setHadoopTmpDir(hadoopTmpDir);
		
		assertNotNull(hadoopConfObj.getHadoopTmpDir());
		assertNotSame("/home/test/hadoopTemp", hadoopConfObj.getHadoopTmpDir());
	}

	/**
	 * @return the namenode
	 */
	@Test
	public void testGetNamenode() {
		hadoopConfObj.setNamenode(nodeConf);
		NodeConf val = hadoopConfObj.getNamenode();
		assertNotNull(val);
		assertSame(this.nodeConf, this.hadoopConfObj.getNamenode());
	}

	/**
	 * @param namenode
	 *            the namenode to set
	 */
	@Test
	public void testSetNamenode() {
		String namenodeIP = "192.168.41.61";
		HadoopNodeConf nnConf = new HadoopNodeConf(namenodeIP, namenodeIP);
		this.hadoopConfObj.setNamenode(nnConf);
		
		assertNotNull(hadoopConfObj.getNamenode());
		assertSame(nnConf.getPublicIp(), namenodeIP);
	}

	/**
	 * @param nodeConf
	 * @return
	 */
	@Test
	public void testIsNamenode() {
		this.hadoopConfObj.setNamenode(this.nodeConf);
		boolean val = this.namenode.equals(this.nodeConf.getPrivateIp());
		assertSame(true, val);
	}

	/**
	 * @return the slaves
	 */
	@Test
	public void testGetSlaves() {
		Set<HadoopNodeConf> slave = new HashSet<HadoopNodeConf>();
		slave.add(this.nodeConf);
		hadoopConfObj.setSlaves(slave);
		
		Set<HadoopNodeConf> slaveval = this.hadoopConfObj.getSlaves();
		assertNotNull(slaveval);
		assertSame(slave, slaveval);
	}

	/**
	 * @param slaves
	 *            the slaves to set
	 */
	@Test
	public void testSetSlaves() {
		Set<HadoopNodeConf> slaveList = new HashSet<HadoopNodeConf>();
		slaveList.add(new HadoopNodeConf(node, node));
		this.hadoopConfObj.setSlaves(slaveList);
		
		Set<HadoopNodeConf> expectedSlaveList = this.hadoopConfObj.getSlaves();
		assertNotNull(expectedSlaveList);
		assertSame(slaveList, expectedSlaveList);
		assertNotSame(this.nodeList, expectedSlaveList);
		
	}

	/**
	 * @return the rackAwarenessScript
	 */
	@Test
	public void testGetRackFileContent() {
		this.hadoopConfObj.setRackFileContent("192.168.145.176 /rack1/node1");
		String val = this.hadoopConfObj.getRackFileContent();
		assertNotNull(val);
	}

	/**
	 * @param rackAwarenessScript
	 *            the rackAwarenessScript to set
	 */
	@Test
	public void testSetRackFileContent() {
		String rackFileContent = "192.168.145.176 /rack1/node1";
		
		this.hadoopConfObj.setRackFileContent("192.168.145.176 /rack1/node2");
		String expectedVal = this.hadoopConfObj.getRackFileContent();
		assertNotNull(expectedVal);
		assertSame("192.168.145.176 /rack1/node2", expectedVal);
		assertNotSame(rackFileContent, expectedVal);
	}

	/**
	 * @return the dfsNameDir
	 */
	@Test
	public void testGetDfsNameDir() {
		String hadoopDFSDir = "/home/test/dfsNameDir";
		this.hadoopConfObj.setDfsNameDir(hadoopDFSDir);
		String expectedVal = this.hadoopConfObj.getDfsNameDir();
		
		assertNotNull(expectedVal);
		assertSame(hadoopDFSDir, expectedVal);
	}

	/**
	 * @param dfsNameDir
	 *            the dfsNameDir to set
	 */
	@Test
	public void testSetDfsNameDir() {
		this.hadoopConfObj.setDfsNameDir("/home/hadoop/dfs");
		
		String hadoopDFSDir = "/home/test/dfsNameDir";
		this.hadoopConfObj.setDfsNameDir(hadoopDFSDir);
		
		String expectedVal = this.hadoopConfObj.getDfsNameDir();
		
		assertNotNull(expectedVal);
		assertSame(hadoopDFSDir, expectedVal);
		assertNotSame("/home/hadoop/dfs", expectedVal);
	}

	/**
	 * @return the dfsDataDir
	 */
	@Test
	public void testGetDfsDataDir() {
		String hadoopDFSDir = "/home/test/dfsDataDir";
		this.hadoopConfObj.setDfsDataDir(hadoopDFSDir);
		String expectedVal = this.hadoopConfObj.getDfsDataDir();
		
		assertNotNull(expectedVal);
		assertSame(hadoopDFSDir, expectedVal);
	}

	/**
	 * @param dfsDataDir
	 *            the dfsDataDir to set
	 */
	@Test
	public void testSetDfsDataDir() {
		this.hadoopConfObj.setDfsNameDir("/home/hadoop/dfs/data");
		
		String hadoopDFSDir = "/home/test/dfsDataDir";
		this.hadoopConfObj.setDfsDataDir(hadoopDFSDir);
		
		String expectedVal = this.hadoopConfObj.getDfsDataDir();
		
		assertNotNull(expectedVal);
		assertSame(hadoopDFSDir, expectedVal);
		assertNotSame("/home/hadoop/dfs/data", expectedVal);
	}

	/**
	 * @return the includes3
	 */
	@Test
	public void testIsIncludes3() {
		this.hadoopConfObj.setIncludes3(true);
		boolean val = this.hadoopConfObj.isIncludes3();
		
		assertSame(true, val);
	}

	/**
	 * @param includes3
	 *            the includes3 to set
	 */
	@Test
	public void testSetIncludes3() {
		hadoopConfObj.setIncludes3(false);
		
		boolean includeS3 = true;
		this.hadoopConfObj.setIncludes3(includeS3);
		
		boolean expectedVal = this.hadoopConfObj.isIncludes3();
		assertSame(includeS3, expectedVal);
		assertNotSame(false, expectedVal);
	}

	/**
	 * @return the s3AccessKey
	 */
	@Test
	public void testGetS3AccessKey() {
		this.hadoopConfObj.setS3AccessKey("Ar34jkKJKHK");
		String val = this.hadoopConfObj.getS3AccessKey();
		
		assertNotNull(val);
		assertSame("Ar34jkKJKHK", val);
	}

	/**
	 * @param s3AccessKey
	 *            the s3AccessKey to set
	 */
	@Test
	public void testSetS3AccessKey() {
		this.hadoopConfObj.setS3AccessKey("Ar34jkKJKHKJSDJG89HJKD");
		
		String s3SecretKey = "Ar34jkKJKHKJSDJG89HJKDasa23213aAJDJ";
		this.hadoopConfObj.setS3AccessKey(s3SecretKey);
		
		String expectedVal = this.hadoopConfObj.getS3AccessKey();
		
		assertNotNull(expectedVal);
		assertSame(s3SecretKey, expectedVal);
		assertNotSame("Ar34jkKJKHKJSDJG89HJKD", expectedVal);
	}

	/**
	 * @return the s3SecretKey
	 */
	@Test
	public void testGetS3SecretKey() {
		this.hadoopConfObj.setS3SecretKey("Ar34jkKJKHKJKJKJHKSDa2323HJ");
		String val = this.hadoopConfObj.getS3SecretKey();
		
		assertNotNull(val);
		assertSame("Ar34jkKJKHKJKJKJHKSDa2323HJ", val);
	}

	/**
	 * @param s3SecretKey
	 *            the s3SecretKey to set
	 */
	@Test
	public void testSetS3SecretKey() {
		this.hadoopConfObj.setS3SecretKey("Ar34jkKJKHKJSDJG89HJKD");
		
		String s3SecretKey = "Ar34jkKJKHKJSDJG89HJKDasa23213aAJDJ";
		this.hadoopConfObj.setS3SecretKey(s3SecretKey);
		
		String expectedVal = this.hadoopConfObj.getS3SecretKey();
		
		assertNotNull(expectedVal);
		assertSame(s3SecretKey, expectedVal);
		assertNotSame("Ar34jkKJKHKJSDJG89HJKD", expectedVal);
	}

	/**
	 * @return the includes3n
	 */
	@Test
	public void testIsIncludes3n() {
		this.hadoopConfObj.setIncludes3n(true);
		boolean val = this.hadoopConfObj.isIncludes3n();
		assertSame(true, val);
	}

	/**
	 * @param includes3n
	 *            the includes3n to set
	 */
	@Test
	public void setIncludes3n() {
		this.hadoopConfObj.setIncludes3n(false);
		boolean includeS3n = true;
		this.hadoopConfObj.setIncludes3n(includeS3n);
		
		boolean expectedVal = this.hadoopConfObj.isIncludes3n();
		assertSame(includeS3n, expectedVal);
		assertNotSame(false, expectedVal);
	}

	/**
	 * @return the s3nAccessKey
	 */
	public void testGetS3nAccessKey() {
		String s3nAccessKey = "Ar34jkKJKHK";
		this.hadoopConfObj.setS3nAccessKey(s3nAccessKey);
		String val = this.hadoopConfObj.getS3nAccessKey();
		
		assertNotNull(val);
		assertSame(s3nAccessKey, val);
	}

	/**
	 * @param s3nAccessKey
	 *            the s3nAccessKey to set
	 */
	@Test
	public void setS3nAccessKey() {
		this.hadoopConfObj.setS3nAccessKey("Ar34jkKJKHKJSDJG89HJKD");
		
		String s3nAccessKey = "Ar34jkKJKHKJSDJG89HJKDasa23213aAJDJ";
		this.hadoopConfObj.setS3nAccessKey(s3nAccessKey);
		
		String expectedVal = this.hadoopConfObj.getS3nAccessKey();
		
		assertNotNull(expectedVal);
		assertSame(s3nAccessKey, expectedVal);
		assertNotSame("/home/hadoop/dfs/data", expectedVal);
	}

	/**
	 * @return the s3nSecretKey
	 */
	@Test
	public void testGetS3nSecretKey() {
		this.hadoopConfObj.setS3nSecretKey("Ar34jkKJKHKJSDJG89HJKD");
		String expectedVal = this.hadoopConfObj.getS3nSecretKey();
		
		assertNotNull(expectedVal);
		assertSame("Ar34jkKJKHKJSDJG89HJKD", expectedVal);
	}

	/**
	 * @param s3nSecretKey
	 *            the s3nSecretKey to set
	 */
	@Test
	public void setS3nSecretKey() {
		this.hadoopConfObj.setS3nAccessKey("Ar34jkKJKHKJSDJG89HJKD");
		
		String s3nSecretKey = "Ar34jkKJKHKJSDJG89HJKDasa23213aAJDJ";
		this.hadoopConfObj.setS3nAccessKey(s3nSecretKey);
		
		String expectedVal = this.hadoopConfObj.getS3nAccessKey();
		
		assertNotNull(expectedVal);
		assertSame(s3nSecretKey, expectedVal);
		assertNotSame("Ar34jkKJKHKJSDJG89HJKD", expectedVal);	}

	/**
	 * @return the dfsReplicationFactor
	 */
	@Test
	public void testGetDfsReplicationFactor() {
		this.hadoopConfObj.setDfsReplicationFactor(3);
		int val = this.hadoopConfObj.getDfsReplicationFactor();
		
		assertSame(3, val);
	}

	/**
	 * @param dfsReplicationFactor
	 *            the dfsReplicationFactor to set
	 */
	@Test
	public void setDfsReplicationFactor() {
		this.hadoopConfObj.setDfsReplicationFactor(1);
		int dfsReplicationFacrtor = 3;
		this.hadoopConfObj.setDfsReplicationFactor(dfsReplicationFacrtor);
		int val = this.hadoopConfObj.getDfsReplicationFactor();
		
		assertSame(dfsReplicationFacrtor, val);
		assertNotSame(1, val);
	}
}
