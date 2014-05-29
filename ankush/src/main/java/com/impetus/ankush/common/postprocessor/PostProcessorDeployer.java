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
package com.impetus.ankush.common.postprocessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush.common.utils.SSHUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class PostProcessorDeployer.
 * 
 * @author Akhil
 */
public class PostProcessorDeployer implements Deployable {

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	/** The log. */
	private final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.POSTPROCESSOR, PostProcessorDeployer.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List,
	 * com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		try{
			PostProcessorConf conf = (PostProcessorConf) config; 
			// setting logger config.
			LOG.setLoggerConfig(conf);
			// logging startup message.
			String message = "Starting Host Postprocessor Configuration";
			LOG.info(message + Constant.DOTS);
			// Starting Host Preprocessor.
			boolean status = deployNodes(conf, new HashSet(nodeList));
			// logging end message.
			message = "Host Postprocessor Configuration";
			LOG.log(message, status);
			return status;
		} catch (final Exception e) {
		this.LOG.error(e.getMessage(), e);
		return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		try {
			
			// TODO Auto-generated method stub
			PostProcessorConf conf = (PostProcessorConf) config; 
			// setting logger config.
			LOG.setLoggerConfig(conf);
			// logging startup message.
			String message = "Starting Host Postprocessor Configuration";
			LOG.info(message + Constant.DOTS);
			// Starting Host Preprocessor.
			boolean status = deployNodes(conf, conf.getClusterNodeConfs());
			// logging end message.
			message = "Host Postprocessor Configuration";
			LOG.log(message, status);
			return status;
		} catch (final Exception e) {
			this.LOG.error(e.getMessage(), e);
			return false;
		}
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
	private boolean deployNodes(final PostProcessorConf conf, Set<NodeConf> nodeConfs) {
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
	 * UnDeploy nodes.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConfs
	 *            the nodes
	 * @return true, if successful
	 */
	private boolean undeployNodes(final PostProcessorConf conf, Set<NodeConf> nodeConfs) {
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
						boolean status = removeNode(nodeConf, conf);
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
	 * Method createNode.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            PreprocessorConf
	 * @return boolean
	 */
	private boolean createNode(NodeConf nodeConf, PostProcessorConf conf) {
		SSHExec connection = null;
		boolean status = false;

		try {
			// Connection with node via SSH connection
			String password = conf.getPassword();
			String publicIp = nodeConf.getPublicIp();
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					password, conf.getPrivateKey());

			if (connection != null) {
				status = this.startJmxTrans(nodeConf, conf);
				
				if(!status) {
					return false;
				}
				status = this.addTaskableClassesToAgent(nodeConf, conf);
			} else {
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf, error, e);
			return false;
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		return status;
	}
	
	private boolean addTaskableClassesToAgent(NodeConf nodeConf, PostProcessorConf conf) {
		SSHExec connection = null;
		try {
			final String publicIp = nodeConf.getPublicIp();
			// connect to remote node
			this.LOG.debug("Connecting with node : " + publicIp);
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());
			
			boolean status = false;
			
			List<String> classes = new ArrayList<String>();
			// adding jps monitor
			classes.add(Constant.TaskableClass.JPS_MONITOR);
			// adding dir usage monitor.
			classes.add(Constant.TaskableClass.DIR_USAGE_MONITOR);
			
			// adding classes in agent taskables.
			status = AgentUtils.addTaskables(connection, classes);
			
			if(!status) {
				this.LOG.error(nodeConf, "Could not add taskable classes to Agent.");
				return false;
			}
			
		} catch (final Exception e) {
			this.LOG.error(nodeConf, "Could not add taskable classes to Agent.");
			LOG.error(e.getMessage());
			return false;
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.
	 * List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		try{
			PostProcessorConf conf = (PostProcessorConf) config; 
			// setting logger config.
			LOG.setLoggerConfig(conf);
			// logging startup message.
			String message = "Starting Host Postprocessor Configuration";
			LOG.info(message + Constant.DOTS);
			// Starting Host Preprocessor.
			boolean status = undeployNodes(conf, new HashSet(nodeList));
			// logging end message.
			message = "Host Postprocessor Configuration";
			LOG.log(message, status);
			return status;
		} catch (final Exception e) {
		this.LOG.error(e.getMessage(), e);
		return false;
		}
	}

	private boolean removeNode(NodeConf nodeConf, PostProcessorConf conf) {
		
		SSHExec connection = null;
		boolean status = false;

		try {
			// Connection with node via SSH connection
			String password = conf.getPassword();
			String publicIp = nodeConf.getPublicIp();
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					password, conf.getPrivateKey());

			if (connection != null) {
				status = this.stopJmxTrans(nodeConf, conf);
				nodeConf.setStatus(status);
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

		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {

		return true;
	}

	/**
	 * Start jmx trans.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean startJmxTrans(NodeConf nodeConf, PostProcessorConf conf) {
		try {
			SSHExec connection = null;
			final String publicIp = nodeConf.getPublicIp();
			// connect to remote node
			this.LOG.debug("Connecting with node : " + publicIp);
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			final String targetText_JmxOpts = this.ankushConf
					.getStringValue("jmx.opts.targetText");
			String replacementText_JmxOpts = this.ankushConf
					.getStringValue("jmx.opts.replacementText");
			final String filePath = conf.getJmxTransScriptFilePath();

			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(this.ankushConf
									.getStringValue("jmxtrans.script.template.gangliamasterip"),
									 conf.getGangliaMasterIp());
			
			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(this.ankushConf
									.getStringValue("jmxtrans.script.template.gangliaport"),
									this.ankushConf.getStringValue("ganglia.port"));
			
			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(this.ankushConf
									.getStringValue("jmxtrans.script.template.privateip"),
									nodeConf.getPrivateIp());

			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(this.ankushConf
									.getStringValue("jmxtrans.script.template.serveralias"),
									HostOperation.getAnkushHostName(nodeConf.getPrivateIp()));

			final AnkushTask jmxTask = new ReplaceText(targetText_JmxOpts,
					replacementText_JmxOpts, filePath, false,
					conf.getPassword());
			Result res = connection.exec(jmxTask);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Unable to update JMXTRANS_OPTS in JmxTrans script file");
				return false;
			}

			// Password is set to NULL to run the script command without sudo
			// option
			final String command = JmxMonitoringUtil.getJmxTransCommand(
					conf.getJmxTransScriptFilePath(), null,
					Constant.JmxTransServiceAction.START);
			final AnkushTask task = new RunInBackground(command);
			res = connection.exec(task);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not start jmxtrans service for JMX monitoring.");
				return false;
			}
		} catch (final Exception e) {
			LOG.error(nodeConf, "Could not start jmxtrans service for JMX monitoring.");
			return false;
		}
		nodeConf.setStatus(true);
		return true;
	}

	private boolean stopJmxTrans(NodeConf nodeConf, PostProcessorConf conf) {
		try {
			SSHExec connection = null;
			final String publicIp = nodeConf.getPublicIp();
			// connect to remote node
			this.LOG.debug("Connecting with node : " + publicIp);
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());
			Result res = null;
			final String command = JmxMonitoringUtil.getJmxTransCommand(
					conf.getJmxTransScriptFilePath(), null,
					Constant.JmxTransServiceAction.STOP);
			final AnkushTask task = new RunInBackground(command);
			res = connection.exec(task);
			if (!res.isSuccess) {
				this.LOG.error(nodeConf, "Could not stop jmxtrans service for JMX monitoring.");
			}
		} catch (final Exception e) {
			this.LOG.error(nodeConf, "Could not stop jmxtrans service for JMX monitoring.");
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#startServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            List<NodeConf>
	 * @return boolean
	 */
	private boolean status(Set<NodeConf> keySet) {
		boolean status = false;
		for (final NodeConf nodeConf : keySet) {
			status = nodeConf.getStatus() || status;
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stopServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		try {
			
			// TODO Auto-generated method stub
			PostProcessorConf conf = (PostProcessorConf) config; 
			// setting logger config.
			LOG.setLoggerConfig(conf);
			// logging startup message.
			String message = "Starting Host Postprocessor Undeployment";
			LOG.info(message + Constant.DOTS);
			// Starting Host Preprocessor.
			boolean status = undeployNodes(conf, conf.getClusterNodeConfs());
			// logging end message.
			message = "Host Postprocessor Undeployment";
			LOG.log(message, status);
			return status;
		} catch (final Exception e) {
			this.LOG.error(e.getMessage(), e);
			return false;
		}
	}

}
