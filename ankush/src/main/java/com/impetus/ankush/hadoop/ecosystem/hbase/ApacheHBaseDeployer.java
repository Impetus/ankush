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
package com.impetus.ankush.hadoop.ecosystem.hbase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.RemoveFromPathVariable;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.EcosystemServiceUtil;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;

/*
 * Class implementing the deployment of a cluster of Apache HBase.
 * 
 * @author Akhil
 */
/**
 * The Class ApacheHBaseDeployer.
 */
public class ApacheHBaseDeployer implements Deployable {

	/** The Constant LOG. */
	public static final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.HBASE, ApacheHBaseDeployer.class);

	public static final String REL_PATH_HBASE_CONF_DIR = "conf/";
	
	public static final String HBASE_FILE_REGIONSERVERS = "regionservers";
	
	public static final String REL_HDFS_PATH_FOR_HBASE= "hbase";
	
	public static final String DEFAULT_HBASE_MASTER_HTTP_PORT = "60000";
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		HBaseConf conf = (HBaseConf) config;
		LOG.setLoggerConfig(conf);
		LOG.debug("Deploying HBaseConf cluster - " + conf);
		LOG.info("Deploying HBase...");
		
		NodeConf hbaseMasterNode = conf.getHbaseMasterNode();
		HadoopConf hConf = (HadoopConf) conf.getClusterConf().getClusterComponents().get(Constant.Component.Name.HADOOP);
		if(hConf == null) {
			hConf = (Hadoop2Conf) conf.getClusterConf().getClusterComponents().get(Constant.Component.Name.HADOOP2);
		}
		
		if(!HadoopUtils.isPasswordlessSshAlreadyConfigured(hConf, hbaseMasterNode)) {
			LOG.info("Setting up Passwordless SSH from HBase Master node across all region server nodes...");
			boolean passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, hbaseMasterNode, new HashSet(conf.getHbaseRegionServerNodes()));
			if (!passwordlessStatus) {
				LOG.error(hbaseMasterNode, "Could not set Passwordless SSH from HBaseMaster node to region server nodes");
				return false;
			}	
		}
		
		String componentHome = conf.getInstallationPath() + "hbase-"
				+ conf.getComponentVersion();
		// setting componentHome in bean
		conf.setComponentHome(componentHome);

		Set<NodeConf> hbaseNodeList = new HashSet<NodeConf>(); 
		hbaseNodeList.addAll(conf.getHbaseRegionServerNodes());
		hbaseNodeList.add(conf.getHbaseMasterNode());
		
		boolean status = false;
//		int nodeId = 0;
		Semaphore semaphore = new Semaphore(hbaseNodeList.size());
		try {

			// iterate over node list
			for (NodeConf nodeConf : hbaseNodeList) {

				semaphore.acquire();
//				++nodeId;
				// create worker node
				HBaseNodeWorker worker = new HBaseNodeWorker(semaphore, conf,
						nodeConf, false);
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(hbaseNodeList.size());
			status = status(new ArrayList(hbaseNodeList));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}

		if(!status) {
			LOG.error("HBase deployment failed");	
		}
		else {
			LOG.info("HBase deployment completed");
		}
		return status;
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		final HBaseConf conf = (HBaseConf) config;
		LOG.setLoggerConfig(conf);
		
		LOG.debug("Undeploying HBase with configuration - " + conf);
		LOG.info("Undeploying HBase...");

		try {
					// get the region server node list
			Set<NodeConf> nodeList = new HashSet<NodeConf>(); 
			nodeList.addAll(conf.getHbaseRegionServerNodes());
			nodeList.add(conf.getHbaseMasterNode());
			
			// remove the hbase setup from hbase nodes
			final Semaphore semaphore = new Semaphore(nodeList.size());

			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						// connect to region server node
						SSHExec connection = SSHUtils.connectToNode(
								nodeConf.getPublicIp(), conf.getUsername(),
								conf.getPassword(), conf.getPrivateKey());

						try {
							// if connected
							if (connection != null) {
								
								// Removing HBASE_HOME/bin From PATH
								LOG.info("Removing HBASE_HOME/bin from $PATH");
								AnkushTask ankushTask = new RemoveFromPathVariable(conf.getComponentHome() + "/bin", Constant.ETCENV_FILE, 
										conf.getPassword());
								if(!connection.exec(ankushTask).isSuccess) {
									LOG.error("Unable to remove $HBASE_HOME/bin from $PATH");
								}

								ankushTask = new Remove(conf.getComponentHome());
								if(connection.exec(ankushTask).isSuccess) {
									LOG.error("Unable to remove HBASE_HOME directory from the node");
								}
							}
						} catch (TaskExecFailException e) {
							LOG.error(e.getMessage(), e);
						} finally {
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
							nodeConf.setStatus(true);
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return true;
		} 
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		// start the hbase cluster
		HBaseConf conf = (HBaseConf) config;
		LOG.setLoggerConfig(conf);
		SSHExec connection = null;
		Result result = null;
		final String publicIp = conf.getHbaseMasterNode().getPublicIp();

		try {
			// connect to the hbase master node
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if the connection is established, run the start script for hbase
			if (connection != null) {
				String hbaseClusterStartCmd = "$HBASE_HOME/bin/" + Constant.Hadoop.FileName.SCRIPT_HBASE_START;
				CustomTask runHbase = new RunInBackground(hbaseClusterStartCmd);
				result = connection.exec(runHbase);

				if (result.isSuccess) {
					LOG.info(conf.getHbaseMasterNode().getPublicIp(),
							"HBase services started successfully.");
				} else {
					LOG.error(conf.getHbaseMasterNode(),
							"Could not start the HBase services");
				}
			}
		} catch (Exception e) {
			LOG.error(publicIp, e.getMessage(), e);
		} finally {
			if (connection != null) {
				// disconnect from the hbase master
				connection.disconnect();
			}
		}
		return result.isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		// stop the hbase cluster
		HBaseConf conf = (HBaseConf) config;
		LOG.setLoggerConfig(conf);
		SSHExec connection = null;
		Result result = null;
		try {
			// connect to the hbase master node
			connection = SSHUtils.connectToNode(conf.getHbaseMasterNode()
					.getPublicIp(), conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if the connection is established, run the start script for hbase
			if (connection != null) {
			
				String stopRegionServers = "$HBASE_HOME/bin/" 
											+ Constant.Hadoop.FileName.SCRIPT_DAEMONS_FILE_HBASE
											+ " stop regionserver";
				
				CustomTask stopHbaseRS = new RunInBackground(stopRegionServers);
				connection.exec(stopHbaseRS);
				
				String hbaseClusterStopCmd = "$HBASE_HOME/bin/" + Constant.Hadoop.FileName.SCRIPT_HBASE_STOP;
				CustomTask stopHbase = new RunInBackground(hbaseClusterStopCmd);
				result = connection.exec(stopHbase);
				
				// Bug Fix 2555 - Hbase Master service not being stopped
				// Stop HMaster process by killing the JPS process as well				
				stopHbase = new KillJPSProcessByName(Constant.RoleProcessName.getProcessName(Constant.Role.HBASEMASTER));
				result = connection.exec(stopHbase);
			}
		} catch (TaskExecFailException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (connection != null) {
				// disconnect from the hbase master
				connection.disconnect();
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#startServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		
		HBaseConf conf = (HBaseConf) config;
		LOG.setLoggerConfig(conf);
		SSHExec connection = null;
		for (NodeConf node : nodeList) {
			try {
				// connect to remote node
				connection = SSHUtils.connectToNode(node.getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				// if connected
				if (connection != null) {
					boolean result = EcosystemServiceUtil.manageHBaseServices(connection, conf, 
							Constant.RoleProcessName.getProcessName(Constant.Role.HBASEREGIONSERVER), Constant.ServiceAction.START);
					if(!result) {
						LOG.error(node, "Could not start " + Constant.Role.HBASEREGIONSERVER + " - " + node.getPublicIp());
					}
					LOG.info(node.getPublicIp()+" : Starting " + Constant.Role.HBASEREGIONSERVER + "  done.");
				}else{
					LOG.error(node, "Could not connect to node - " + node.getPublicIp());
				}
			} catch (Exception e) {
				LOG.error(e.getMessage());
				e.printStackTrace();
			} finally {
				if(connection != null){
					connection.disconnect();
				}
			}
		}
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stopServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		HBaseConf conf = (HBaseConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Adding nodes for HBase...");
		boolean status = false;

		boolean setupPasswordlessSSH = HadoopUtils.setupPasswordlessSSH(conf, conf.getHbaseMasterNode() , new HashSet(nodeList));
		if (!setupPasswordlessSSH) {
			LOG.error("Could not set Passwordless SSH from HBase Master("
						+ conf.getHbaseMasterNode().getPublicIp() +") to the newly added nodes.");
			return false;
		}
		
		Set<NodeConf> expectedNodeList =  new HashSet<NodeConf>();
		expectedNodeList.addAll(new HashSet<NodeConf>(nodeList));
		expectedNodeList.addAll(conf.getCompNodes());
		conf.setExpectedRSAfterAddOrRemove(expectedNodeList);
		
		Semaphore semaphore = new Semaphore(nodeList.size());
		try {
			// iterate over node list
			for (NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				HBaseNodeWorker worker = new HBaseNodeWorker(semaphore, conf, nodeConf, true); 
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
			
			status = status(nodeList);
			if(status) {
				status = HBaseNodeWorker.updateConfToNodes(conf, Constant.SyncConfigurationAction.ADD);	
			}
			
			if (!status) {
				conf.setExpectedRSAfterAddOrRemove(new HashSet<NodeConf>(conf.getHbaseRegionServerNodes()));
				LOG.error("Could not add nodes for HBase.");
				return false;
			}
			status = status(nodeList);
			if(status) {
				HBaseNodeWorker.updateConfObjectAfterAddRemoveNodes(conf);
			} else {
				conf.setExpectedRSAfterAddOrRemove(new HashSet<NodeConf>(conf.getHbaseRegionServerNodes()));
				LOG.error("Could not add nodes for HBase.");
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		if(status) {
			LOG.info("Nodes addition for HBase completed.");	
		} else {
			LOG.error("Could not add nodes for HBase.");
		}
		return status;
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return status(nodeList);
	}

	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            List<NodeConf>
	 * @return boolean
	 */
	private boolean status(List<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = nodeConf.getStatus() && status;
		}
		return status;
	}

}
