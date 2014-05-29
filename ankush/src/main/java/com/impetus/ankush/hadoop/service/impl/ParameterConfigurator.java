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
package com.impetus.ankush.hadoop.service.impl;

import java.util.List;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.Parameter;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;

/**
 * The Class ParameterConfigurator.
 * 
 * @author hokam
 */
public class ParameterConfigurator {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(ParameterConfigurator.class);

	/**
	 * Adds the parameters.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param parameters
	 *            the parameters
	 * @param filePath
	 *            the file path
	 * @return true, if successful
	 */
	public boolean addParameters(Cluster cluster,
			final List<Parameter> parameters, final String filePath) {
		// hadoop cluster conf object.
		final ClusterConf clusterConf = cluster.getClusterConf();
		HadoopConf hConf = (HadoopConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.HADOOP);
		if (hConf == null) {
			hConf = (HadoopConf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
		}
		boolean status = true;
		// iterating over all the nodes.
		try {
			final Semaphore semaphore = new Semaphore(hConf.getCompNodes()
					.size());
			for (final NodeConf nodeConf : hConf.getCompNodes()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					/* (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						// connection reference.
						SSHExec connection = null;
						try {

							String hostname = nodeConf.getPublicIp();

							// iterating over the list to get remove all params
							// command.
							StringBuilder command = new StringBuilder();
							for (Parameter param : parameters) {
								// creating delete property task.
								AnkushTask addProperty = new AddConfProperty(
										param.getName(), param.getValue(),
										filePath, Constant.File_Extension.XML);
								// appending the command
								command.append(addProperty.getCommand())
										.append(";");
							}

							// connection object.
							connection = SSHUtils.connectToNode(hostname,
									clusterConf.getUsername(),
									clusterConf.getPassword(),
									clusterConf.getPrivateKey());

							nodeConf.setStatus(false);
							if (connection != null) {
								ExecCommand task = new ExecCommand(command
										.toString());
								Result rs = connection.exec(task);
								if (rs.rc != 0) {
									clusterConf.addError(
											nodeConf.getPublicIp(),
											"Unable to add the parameters to "
													+ filePath + " file on "
													+ nodeConf.getPublicIp()
													+ ".");
								}
								nodeConf.setStatus(rs.rc == 0);
							} else {
								clusterConf.addError(
										nodeConf.getPublicIp(),
										"Unable to connect to node "
												+ nodeConf.getPublicIp() + ".");
							}

						} catch (Exception e) {
							logger.error(e.getMessage());
						} finally {
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(hConf.getCompNodes().size());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return status;
	}

	/**
	 * Removes the parameters.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param parameters
	 *            the parameters
	 * @param filePath
	 *            the file path
	 * @return true, if successful
	 */
	public boolean removeParameters(Cluster cluster,
			final List<Parameter> parameters, final String filePath) {

		// cluster conf object.
		final ClusterConf clusterConf = cluster.getClusterConf();

		HadoopConf hConf = (HadoopConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.HADOOP);
		if (hConf == null) {
			hConf = (HadoopConf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
		}
		boolean status = true;
		// iterating over all the nodes.
		try {
			final Semaphore semaphore = new Semaphore(hConf.getCompNodes()
					.size());
			for (final NodeConf nodeConf : hConf.getCompNodes()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						nodeConf.setStatus(false);
						// connection reference.
						SSHExec connection = null;
						try {
							String hostname = nodeConf.getPublicIp();

							// iterating over the list to get remove all params
							// command.
							StringBuilder command = new StringBuilder();
							for (Parameter param : parameters) {
								// creating delete property task.
								AnkushTask deleteProperty = new DeleteConfProperty(
										param.getName(), filePath,
										Constant.File_Extension.XML);
								// appending the command
								command.append(deleteProperty.getCommand())
										.append(";");
							}

							// connection object.
							connection = SSHUtils.connectToNode(hostname,
									clusterConf.getUsername(),
									clusterConf.getPassword(),
									clusterConf.getPrivateKey());

							if (connection != null) {
								ExecCommand task = new ExecCommand(command
										.toString());
								Result rs = connection.exec(task);
								nodeConf.setStatus(rs.rc == 0);
							} else {
								clusterConf.addError(
										nodeConf.getPublicIp(),
										"Unable to connect to node "
												+ nodeConf.getPublicIp() + ".");
							}

						} catch (Exception e) {
							logger.error(e.getMessage());
						} finally {
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(hConf.getCompNodes().size());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return status;
	}

	/**
	 * Edits the parameters.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param parameters
	 *            the parameters
	 * @param filePath
	 *            the file path
	 * @return true, if successful
	 */
	public boolean editParameters(Cluster cluster,
			final List<Parameter> parameters, final String filePath) {
		// cluster conf object.
		final ClusterConf clusterConf = cluster.getClusterConf();

		HadoopConf hConf = (HadoopConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.HADOOP);
		if (hConf == null) {
			hConf = (HadoopConf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
		}
		boolean status = true;
		// iterating over all the nodes.
		try {
			final Semaphore semaphore = new Semaphore(hConf.getCompNodes()
					.size());
			for (final NodeConf nodeConf : hConf.getCompNodes()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						nodeConf.setStatus(false);
						// connection reference.
						SSHExec connection = null;
						try {
							String hostname = nodeConf.getPublicIp();

							// iterating over the list to get remove all params
							// command.
							StringBuilder command = new StringBuilder();
							for (Parameter param : parameters) {
								// creating delete property task.
								AnkushTask editProperty = new EditConfProperty(
										param.getName(), param.getValue(),
										filePath, Constant.File_Extension.XML);
								// appending the command
								command.append(editProperty.getCommand())
										.append(";");
							}

							// connection object.
							connection = SSHUtils.connectToNode(hostname,
									clusterConf.getUsername(),
									clusterConf.getPassword(),
									clusterConf.getPrivateKey());

							if (connection != null) {
								ExecCommand task = new ExecCommand(command
										.toString());
								Result rs = connection.exec(task);
								if (rs.rc != 0) {
									clusterConf.addError(
											nodeConf.getPublicIp(),
											"Unable to edit the parameters in "
													+ filePath + " file on "
													+ nodeConf.getPublicIp()
													+ ".");
								}
								nodeConf.setStatus(rs.rc == 0);
							} else {
								clusterConf.addError(
										nodeConf.getPublicIp(),
										"Unable to connect to node "
												+ nodeConf.getPublicIp() + ".");
							}

						} catch (Exception e) {
							logger.error(e.getMessage());
						} finally {
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(hConf.getCompNodes().size());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return status;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param fileName
	 *            the file name
	 * @return the parameters
	 */
	public List<Parameter> getParameters(Cluster cluster, String fileName) {
		return null;
	}

	/**
	 * Write xml to config.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param filePath
	 *            the file path
	 * @param fileContents
	 *            the file contents
	 */
	public void writeXMLToConfig(Cluster cluster, final String filePath,
			final String fileContents) {
		// cluster conf object.
		final ClusterConf clusterConf = cluster.getClusterConf();

		HadoopConf hConf = (HadoopConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.HADOOP);
		if (hConf == null) {
			hConf = (HadoopConf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
		}
		boolean status = true;
		// iterating over all the nodes.
		try {
			final Semaphore semaphore = new Semaphore(hConf.getCompNodes()
					.size());
			for (final NodeConf nodeConf : hConf.getCompNodes()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						nodeConf.setStatus(false);
						// connection reference.
						SSHExec connection = null;
						try {
							String hostname = nodeConf.getPublicIp();

							// iterating over the list to get remove all params
							// command.
							CustomTask task = new ClearFile(filePath);
							AppendFile appendTask = new AppendFile(
									fileContents, filePath);

							connection = SSHUtils.connectToNode(hostname,
									clusterConf.getUsername(),
									clusterConf.getPassword(),
									clusterConf.getPrivateKey());

							if (connection != null) {
								Result rs = connection.exec(task);
								rs = connection.exec(appendTask);
								if (rs.rc != 0) {
									clusterConf.addError(
											nodeConf.getPublicIp(),
											"Unable to write xml to "
													+ filePath + " file on "
													+ nodeConf.getPublicIp()
													+ ".");
								}
								nodeConf.setStatus(rs.rc == 0);
							} else {
								clusterConf.addError(
										nodeConf.getPublicIp(),
										"Unable to connect to node "
												+ nodeConf.getPublicIp() + ".");
							}

						} catch (Exception e) {
							logger.error(e.getMessage());
						} finally {
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(hConf.getCompNodes().size());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
