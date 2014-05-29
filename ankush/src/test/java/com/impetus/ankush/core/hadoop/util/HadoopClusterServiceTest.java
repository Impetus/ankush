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
package com.impetus.ankush.core.hadoop.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.AuthConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.HadoopServiceMonitor;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;

/**
 * The Class HadoopClusterServiceTest.
 *
 * @author bgunjan
 */
@ContextConfiguration(locations = {
		"classpath:/applicationContext-resources.xml",
		"classpath:/applicationContext-dao.xml",
		"classpath*:/applicationContext.xml",
		"classpath:**/applicationContext*.xml" })

@RunWith(PowerMockRunner.class)
@PrepareForTest(SSHUtils.class)
public class HadoopClusterServiceTest {
	
	/** The hadoop cluster service. */
	HadoopServiceMonitor hadoopServiceMonitor;
	
	/** The node ip. */
	String nodeIp;
	
	/** The service action. */
	String serviceAction;
	
	/** The service name. */
	String serviceName;
	
	/** The hadoop cluster conf. */
	HadoopClusterConf hadoopClusterConf;
	
	/** The connection. */
	SSHExec connection;

	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		Cluster cluster;
		hadoopServiceMonitor = EasyMock.createMock(HadoopServiceMonitor.class);
		connection = EasyMock.createMock(SSHExec.class);
		serviceName = "Hadoop";
		serviceAction = "start";
		
		cluster = EasyMock.createMock(Cluster.class);
		cluster = new Cluster();
		nodeIp = "192.168.145.61";
		hadoopClusterConf = new HadoopClusterConf();
		AuthConf authConf = new AuthConf();
		authConf.setPassword("impetus");
		authConf.setUsername("impetus");
		hadoopClusterConf.setAuthConf(authConf);
		hadoopClusterConf.setClusterId(1L);
		HadoopNodeConf hadoopNodeConf = new HadoopNodeConf();
		hadoopNodeConf.setDataNode(true);
		hadoopNodeConf.setNameNode(true);
		hadoopNodeConf.setId(1L);
		hadoopNodeConf.setPrivateIp(nodeIp);
		hadoopNodeConf.setPublicIp(nodeIp);
		List<HadoopNodeConf> hadoopNodeConfs = new ArrayList<HadoopNodeConf>();
		hadoopNodeConfs.add(hadoopNodeConf);
		HadoopConf hadoopConf = new HadoopConf();
		hadoopConf.setComponentHome("/home/hadoop/test");
		hadoopConf.setComponentVersion("1.0.4");
		hadoopConf.setDfsDataDir("/home/hadoop/test/dfs/data");
		hadoopConf.setHadoopConfDir("/home/hadoop/test/conf");
		hadoopConf.setHadoopTmpDir("/home/hadoop/test/tmp");
		hadoopConf.setInstallationPath("/home/hadoop/test/hes");
		
		hadoopConf.setUsername("impadmin");
		hadoopConf.setPassword("impetus");
		
		
		String ip = "192.168.41.83";
		HadoopNodeConf nodeConf = new HadoopNodeConf(ip, ip);
		
		Set<HadoopNodeConf> nodes = new HashSet<HadoopNodeConf>();
		nodes.add(nodeConf);
		
		hadoopConf.setNamenode(nodeConf);
		hadoopConf.setSlaves(nodes);
		hadoopConf.setSecondaryNamenode(nodeConf);
		
		LinkedHashMap<String, GenericConfiguration> components = new LinkedHashMap<String, GenericConfiguration>();
		components.put(Constant.Component.Name.HADOOP, hadoopConf);
		hadoopClusterConf.setComponents(components);
		hadoopClusterConf.setNewNodes(hadoopNodeConfs);
		hadoopClusterConf.setNodes(hadoopNodeConfs);
		cluster.setClusterConf(hadoopClusterConf);
	}
	
	/**
	 * Manage hadoop service.
	 */
	@Test
	public void manageHadoopService() {
		HadoopConf hadoopConf = (HadoopConf) hadoopClusterConf
				.getComponents().get(Constant.Component.Name.HADOOP);

		String hadoopDaemonFile = "hadoop-daemon.sh ";
		String componentHome = hadoopConf.getInstallationPath() + "hadoop-"
				+ hadoopConf.getComponentVersion();
		Result res = null;
		try {
			// This is the way to tell PowerMock to mock all static methods of a given class
	        PowerMock.mockStatic(SSHUtils.class);
			// connect to remote node
			EasyMock.expect(SSHUtils.connectToNode(this.nodeIp,
					hadoopConf.getUsername(), hadoopConf.getPassword(),
					hadoopConf.getPrivateKey())).andReturn(connection);
			 // replay the class
	        PowerMock.replay(SSHUtils.class);
	        
			String serviceName = this.serviceName;
			StringBuffer command = new StringBuffer();
			command.append(componentHome).append("/bin")
					.append(hadoopDaemonFile).append(" ");
			command.append(this.serviceAction).append(" ")
					.append(serviceName.toLowerCase());

			CustomTask manageServices = new ExecCommand(command.toString());
			EasyMock.expect(connection.exec(manageServices)).andReturn(res);
		} catch (Exception e) {
		} finally {
			// Disconnect from node/machine
			if (connection != null)
				connection.disconnect();
		}
	}
	
	/**
	 * Manage agent start.
	 */
	@Test
	public void manageAgentStart() {
		String password = "impetus";
		Boolean value = true;
		String startAgent = "java -cp .ankush/agent/libs/*:.ankush/agent/libs/agent-0.1.jar com.impetus.ankush.agent.daemon.AnkushAgent";
		RunInBackground task = EasyMock.createMock(RunInBackground.class);
		startAgent = task.getCommand();
		// This is the way to tell PowerMock to mock all static methods of a given class
        PowerMock.mockStatic(SSHUtils.class);
        
		EasyMock.expect(SSHUtils.action(connection, password, startAgent)).andReturn(value);
		 // replay the class
        PowerMock.replay(SSHUtils.class);
	}
	
	/**
	 * Manage agent stop.
	 */
	@Test
	public void manageAgentStop() {
		String password = "impetus";
		CustomTask killProcess = EasyMock.createMock(KillJPSProcessByName.class);
		String stopAgent = killProcess.getCommand();
		// This is the way to tell PowerMock to mock all static methods of a given class
        PowerMock.mockStatic(SSHUtils.class);
        
		EasyMock.expect(SSHUtils.action(connection, password, stopAgent)).andReturn(true);

		// replay the class
        PowerMock.replay(SSHUtils.class);
	}
	
	/**
	 * Manage ganglia services.
	 */
	@Test
	public void manageGangliaServices() {
		String password = "impetus";
		// This is the way to tell PowerMock to mock all static methods of a given class
        PowerMock.mockStatic(SSHUtils.class);

		if (serviceName.equals("gmond") && serviceAction.equals("start")) {
			String startGmondCmd = "gmond --conf=.ankush/monitoring/conf/gmond.conf";
			EasyMock.expect(SSHUtils.action(password, connection, startGmondCmd)).andReturn(true);
		}
		if (serviceName.equals("gmetad") && serviceAction.equals("start")) {
			String startGmetadCmd = "gmetad --conf=.ankush/monitoring/conf/gmetad.conf";
			EasyMock.expect(SSHUtils.action(password, connection, startGmetadCmd)).andReturn(true);
		}
		if (serviceName.equals("gmond") && serviceAction.equals("stop")) {
			String stopGmondCmd = "killall -9 gmond";
			EasyMock.expect(SSHUtils.action(password, connection, stopGmondCmd)).andReturn(true);
		}
		if (serviceName.equals("gmetad") && serviceAction.equals("stop")) {
			String stopGmetadCmd = "killall -9 gmetad";
			EasyMock.expect(SSHUtils.action(password, connection, stopGmetadCmd)).andReturn(true);
		}
		// replay the class
        PowerMock.replayAll(SSHUtils.class);
	}
}
