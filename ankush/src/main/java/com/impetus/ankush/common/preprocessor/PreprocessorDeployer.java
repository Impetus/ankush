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
package com.impetus.ankush.common.preprocessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;
import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.impl.PrependFile;
import com.impetus.ankush.common.scripting.impl.RemoveText;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;

/**
 * @author Akhil
 *
 */
public class PreprocessorDeployer implements Deployable {
	
	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.PREPROCESSOR, PreprocessorDeployer.class);
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		// TODO Auto-generated method stub
		PreprocessorConf conf = (PreprocessorConf) config;
		
		// setting logger config.
		LOG.setLoggerConfig(conf);
		// logging startup message.
		String message = "Starting Host Preprocessor Configuration";
		LOG.info(message + Constant.DOTS);
		
		// Starting Host Preprocessor.
		boolean status = deployNodes(conf, conf.getClusterNodeConfs());
		// logging end message.
		message = "Host Preprocessor Configuration";
		LOG.log(message, status);
		return status;
	}
	
	/**
	 * Deploy nodes.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConfs
	 *            the nodes
	 * @return true, if successful
	 */
	private boolean deployNodes(final PreprocessorConf conf, Set<NodeConf> nodeConfs) {
		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeConfs) {
				// acuiring the semaphore
				semaphore.acquire();
				// starting a thread to start pre-processing on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = createNode(nodeConf, conf);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(nodeConfs.size());
			status = status(nodeConfs);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}
	
	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            Set<NodeConf>
	 * @return boolean
	 */
	private boolean status(Set<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = status && nodeConf.getStatus();
		}
		return status;
	}
	
	/**
	 * Method createNode.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            PreprocessorConf
	 * @return boolean
	 */
	private boolean createNode(NodeConf nodeConf, PreprocessorConf conf) {
		SSHExec connection = null;
		boolean status = false;

		// Setting the start progress message for activity startup.
		String message = "Starting Host Preprocessor Configuration";
		LOG.info(nodeConf.getPublicIp(), message + Constant.DOTS);

		try {
			// Connection with node via SSH connection
			String password = conf.getPassword();
			String publicIp = nodeConf.getPublicIp();
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					password, conf.getPrivateKey());

			if (connection != null) {
				// Get etc host file contents for node
				List<String> etcFileContents = this.getEtcHostContents(conf.getClusterNodeConfs(), nodeConf.getPublicIp(), conf);
				
				// Add etc host entries.
				PrependFile prepFile = new PrependFile(etcFileContents, "/etc/hosts", conf.getPassword());
				status = connection.exec(prepFile).rc == 0;
				if (!status) {
					nodeConf.setStatus(false);
					LOG.error(nodeConf, "Could not add entries in /etc/hosts file.");
				}
				else{
					nodeConf.setStatus(true);
				}
			} else {
				nodeConf.setStatus(false);
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			nodeConf.setStatus(false);
			String error = e.getMessage();
			if (error == null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf, error, e);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		// Setting the end progress status for execution status
//		message = "Host Preprocessor Configuration";
//		LOG.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());

		return nodeConf.getStatus();
	}
	
	private List<String> getEtcHostContents(Set<NodeConf> nodeConfs, String publicIp, PreprocessorConf conf) {
		List<String> etcFileContents = new ArrayList<String>();
		for(NodeConf objConf : nodeConfs) {
			if(objConf.getPrivateIp() == null) {
				continue;
			}
			String ankushHostName = HostOperation.getAnkushHostName(objConf.getPrivateIp()); 
			String etcFileEntry = new String();
			etcFileEntry += objConf.getPrivateIp();
			etcFileEntry += "\t";
			etcFileEntry += ankushHostName;
			if(!objConf.getSystemHostName().isEmpty()) {
				etcFileEntry += "\t";
				etcFileEntry += objConf.getSystemHostName();	
			}
			
			if (objConf.getPublicIp().equals(publicIp)) {
				etcFileEntry += "\tlocalhost";
			}
			etcFileContents.add(etcFileEntry);
		}
		return etcFileContents;
	}
	
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		final PreprocessorConf conf = (PreprocessorConf) config;
		LOG.setLoggerConfig(conf);

		final Semaphore semaphore = new Semaphore(conf.getClusterNodeConfs().size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : conf.getClusterNodeConfs()) {
				// acuiring the semaphore.
				semaphore.acquire();
				// starting a thread for ganglia removal on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = removeNode(nodeConf, conf);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(conf.getClusterNodeConfs().size());
			status = status(conf.getClusterNodeConfs());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Method removeNode.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            PreprocessorConf
	 * @return boolean
	 */
	private boolean removeNode(NodeConf nodeConf, PreprocessorConf conf) {

		String message = "Removing ";
		boolean status = false;
		// Setting the start progress message for activity remove.
		LOG.info(nodeConf.getPublicIp(), message + Constant.DOTS);

		SSHExec connection = null;
		try {
			// Connection with node via SSH connection
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			if (connection != null) {
				List<String> etcFileContents = this.getEtcHostContents(conf.getClusterNodeConfs(), nodeConf.getPublicIp(), conf);
				RemoveText prepFile = new RemoveText(etcFileContents, "/etc/hosts", conf.getPassword());
				connection.exec(prepFile);
				status = connection.exec(prepFile).rc == 0;
				if (!status) {
					nodeConf.setStatus(false);
					LOG.error(nodeConf, "Could not remove entries in /etc/hosts file.");
				}
				else{
					nodeConf.setStatus(true);
				}
			}
			else {
				nodeConf.setStatus(true);
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			nodeConf.setStatus(false);
			String error = e.getMessage();
			if (error == null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf.getPublicIp(), error, e);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}

		// Setting the end progress status for execution status
		LOG.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());

		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		// Create HashSet with List.
		Set<NodeConf> nodes = new HashSet<NodeConf>(nodeList);
		
		// Create Preprocessor Conf.
		PreprocessorConf conf = (PreprocessorConf) config;
		
		// Set LOG configs.
		LOG.setLoggerConfig(conf);
		conf.getClusterNodeConfs().addAll(nodes);
		
		String message = "Starting Preprocessor Configuration";
		// log message.
		LOG.info(message + Constant.DOTS);
		// create ganglia nodes.
		
		boolean status = deployNodes(conf, nodes);
		if(status) {
			message = "Sync configuration to existing nodes";
			LOG.info(message + Constant.DOTS);	
			status = this.syncConfigurationToExistingNodes(conf, nodes, Constant.SyncConfigurationAction.ADD);
		}
		
		// log end message
		message = "Preprocessor Configuration";
		LOG.log(message, status);
		// returning the overall status of the action
		return status;
	}

	/**
	 * Updates /etc/hosts file to Existing Nodes
	 *
	 * @param conf
	 *            the Preprocessor Conf
	 * @param nodeConfs
	 *            the list of newly added / removed nodes
	 * @param action
	 *            the Constant.SyncConfigurationAction (ADD / REMOVE)
	 * @return true, if successful
	 */
	private boolean syncConfigurationToExistingNodes(final PreprocessorConf conf, Set<NodeConf> nodeConfs, final Constant.SyncConfigurationAction action) {
		
		List<NodeConf> nodeListAfterOperation = conf.getClusterConf().getNodeConfs();
		
		final Semaphore semaphore = new Semaphore(nodeListAfterOperation.size());
		boolean status = true;
		
		try {
			for (final NodeConf nodeConf : nodeListAfterOperation) {
				final List<String> etcHostContent = this.getEtcHostContents(nodeConfs, nodeConf.getPublicIp(), conf);
				// acuiring the semaphore
				semaphore.acquire();
				// starting a thread to start pre-processing on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = updateEtcHostFiles(nodeConf, conf, etcHostContent, action);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(nodeListAfterOperation.size());
			status = status(nodeConfs);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
		
	}
	
	/**
	 * Updates /etc/hosts file to Existing Nodes
	 *
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the Preprocessor Conf
	 * @param etcHostContent
	 *            the /etc/hosts file content as list of string
	 * @param action
	 *            the Constant.SyncConfigurationAction (ADD / REMOVE)
	 * @return true, if successful
	 */
	private boolean updateEtcHostFiles(NodeConf nodeConf, PreprocessorConf conf, List<String> etcHostContent, Constant.SyncConfigurationAction action){
		boolean status = true;
		SSHExec connection = null;
		
		// Setting the start progress message for activity startup.
		String message = "Sync Preprocessor Configuration to Node";
		LOG.info(nodeConf.getPublicIp(), message + Constant.DOTS);

		try {
			// Connection with node via SSH connection
			String password = conf.getPassword();
			String publicIp = nodeConf.getPublicIp();
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					password, conf.getPrivateKey());

			if (connection != null) {
				// Add etc host entries.
				if(action.equals(Constant.SyncConfigurationAction.ADD)) {
					PrependFile prepFile = new PrependFile(etcHostContent, "/etc/hosts", conf.getPassword());
					status = connection.exec(prepFile).rc == 0;	
				}
				else{
					RemoveText prepFile = new RemoveText(etcHostContent, "/etc/hosts", conf.getPassword());
					status = connection.exec(prepFile).rc == 0;
				}
				if (!status) {
					nodeConf.setStatus(false);
					LOG.error(nodeConf, "Could not add entries in /etc/hosts file.");
				}
				else{
					nodeConf.setStatus(true);
				}
			} else {
				nodeConf.setStatus(false);
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			nodeConf.setStatus(false);
			String error = e.getMessage();
			if (error == null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf, error, e);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		// Setting the end progress status for execution status
		message = "Sync Preprocessor Configuration to Node";
		LOG.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());

		return nodeConf.getStatus();
	}
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		// Create HashSet with List.
		Set<NodeConf> nodes = new HashSet<NodeConf>(nodeList);
		// Create Preprocessor conf.
		PreprocessorConf conf = (PreprocessorConf) config;
		// Set LOG configs.
		LOG.setLoggerConfig(conf);

		String message = "Removing Preprocessor configurations";
		// log message.
		LOG.info(message + Constant.DOTS);

		// removing Preprocessor configuration from nodes.
		for (NodeConf nodeConf : nodes) {
			nodeConf.setStatus(removeNode(nodeConf, conf));
		}
		boolean status = status(nodes);
		if(status) {
			message = "Sync configuration to existing nodes";
			LOG.info(message + Constant.DOTS);	
			status = this.syncConfigurationToExistingNodes(conf, nodes, Constant.SyncConfigurationAction.REMOVE);
		}
		
		// log end message
		LOG.log(message, status);
		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#startServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stopServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return true;
	}

}
