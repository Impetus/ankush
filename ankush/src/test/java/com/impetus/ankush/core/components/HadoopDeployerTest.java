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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.AsyncExecutorService;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.impl.AsyncExecutorServiceImpl;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;

/**
 * The Class HadoopDeployerTest.
 *
 * @author bgunjan
 */
@ContextConfiguration(locations = {
		"classpath:/applicationContext-resources.xml",
		"classpath:/applicationContext-dao.xml",
		"classpath*:/applicationContext.xml",
		"classpath:**/applicationContext*.xml" })

@RunWith(PowerMockRunner.class)
@PrepareForTest(AppStoreWrapper.class)
public class HadoopDeployerTest {

	/** The hadoop conf. */
	HadoopConf hadoopConf; 
	
	/** The hadoop deployer. */
	Hadoop1Deployer hadoopDeployer;
	
	/** The conf manager. */
	ConfigurationManager confManager;
	
	/** The node conf. */
	HadoopNodeConf nodeConf;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	
	@Before
	public void setUp() throws Exception {
		@SuppressWarnings("unchecked")
		GenericManager<Configuration, Long> configurationManager = EasyMock.createMock(GenericManager.class);
		// This is the way to tell PowerMock to mock all static methods of a given class
        PowerMock.mockStatic(AppStoreWrapper.class);
		// connect to remote node
		EasyMock.expect(AppStoreWrapper.getManager(Constant.Manager.CONFIGURATION, Configuration.class)).andReturn(configurationManager);
		 // replay the class
        PowerMock.replay(AppStoreWrapper.class);

        confManager = EasyMock.createMock(ConfigurationManager.class);
		hadoopDeployer = EasyMock.createMock(Hadoop1Deployer.class); //
		
		// add additional set up code here
		AsyncExecutorService executor = new AsyncExecutorServiceImpl();

		String fn = "dependencies/test";
		String path = this.getClass().getClassLoader().getResource(fn).toString();
		int idx = path.lastIndexOf(fn);
		path = path.substring(0, idx);
		path = path.substring("file:".length());
		
		PowerMock.mockStatic(AppStoreWrapper.class);
		AppStoreWrapper.setExecutor(executor);
		String filePath = path + "ankush_constants.xml";
		ConfigurationReader confReader = new ConfigurationReader(filePath);
		AppStoreWrapper.setAnkushConfReader(confReader);
		PowerMock.replayAll(AppStoreWrapper.class);
		
		hadoopConf = new HadoopConf();
		
		hadoopConf.setComponentHome("/home/hadoop/test");
		hadoopConf.setComponentVersion("1.0.4");
		hadoopConf.setDfsDataDir("/home/hadoop/test/dfs/data");
		hadoopConf.setHadoopConfDir("/home/hadoop/test/conf");
		hadoopConf.setHadoopTmpDir("/home/hadoop/test/tmp");
		hadoopConf.setInstallationPath("/home/hadoop/test/hes");
		
		hadoopConf.setUsername("impadmin");
		hadoopConf.setPassword("impetus");
		
		
		String ip = "192.168.41.83";
		nodeConf = new HadoopNodeConf();
		nodeConf.setPrivateIp(ip);
		nodeConf.setPublicIp(ip);
		nodeConf.setNameNode(true);
		nodeConf.setDataNode(true);
		nodeConf.setSecondaryNameNode(true);
		
		Set<HadoopNodeConf> nodes = new HashSet<HadoopNodeConf>();
		nodes.add(nodeConf);
		
		hadoopConf.setNamenode(nodeConf);
		hadoopConf.setSlaves(nodes);
		hadoopConf.setSecondaryNamenode(nodeConf);
	}
	
	/**
	 * Test deploy success.
	 */
	@Test
	public void testDeploySuccess()
	{
		EasyMock.expect(this.hadoopDeployer.deploy(hadoopConf)).andReturn(true);
		EasyMock.replay(this.hadoopDeployer);
		
		boolean result = hadoopDeployer.deploy(hadoopConf);
		assertEquals(true, result);
	}
	
	/**
	 * Test to check if component name is not provide
	 * Run the boolean deploy(Configuration) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDeployFailure1() throws Exception {
		EasyMock.expect(this.hadoopDeployer.deploy(hadoopConf)).andReturn(false);
		EasyMock.replay(this.hadoopDeployer);

		boolean result = hadoopDeployer.deploy(hadoopConf);
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean start(Configuration) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testStartSuccess() throws Exception {
		EasyMock.expect(this.hadoopDeployer.start(hadoopConf)).andReturn(true);
		EasyMock.replay(this.hadoopDeployer);

		boolean result = this.hadoopDeployer.start(hadoopConf);
		assertEquals(true, result);
	}
	
	/**
	 * Run the boolean start(Configuration) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testStartFailure() throws Exception {
		EasyMock.expect(this.hadoopDeployer.start(hadoopConf)).andReturn(false);
		EasyMock.replay(this.hadoopDeployer);

		boolean result = this.hadoopDeployer.start(hadoopConf);
		assertEquals(false, result);
	}

	/**
	 * Run the boolean stop(Configuration) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testStopSuccess() throws Exception {
		EasyMock.expect(this.hadoopDeployer.stop(hadoopConf)).andReturn(true);
		EasyMock.replay(this.hadoopDeployer);

		boolean result = this.hadoopDeployer.stop(hadoopConf);
		assertEquals(true, result);
	}
	
	/**
	 * Run the boolean stop(Configuration) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testStopFailure() throws Exception {
		EasyMock.expect(this.hadoopDeployer.stop(hadoopConf)).andReturn(false);
		EasyMock.replay(this.hadoopDeployer);

		boolean result = this.hadoopDeployer.stop(hadoopConf);
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean undeploy(Configuration) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUndeploySuccess() throws Exception {
		EasyMock.expect(this.hadoopDeployer.undeploy(hadoopConf)).andReturn(true);
		EasyMock.replay(this.hadoopDeployer);

		boolean result = this.hadoopDeployer.undeploy(hadoopConf);
		assertEquals(true, result);
	}
	
	/**
	 * Run the boolean undeploy(Configuration) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testUndeployFailure() throws Exception {
		EasyMock.expect(this.hadoopDeployer.undeploy(hadoopConf)).andReturn(false);
		EasyMock.replay(this.hadoopDeployer);

		boolean result = this.hadoopDeployer.undeploy(hadoopConf);
		assertEquals(false, result);
	}
	
}
