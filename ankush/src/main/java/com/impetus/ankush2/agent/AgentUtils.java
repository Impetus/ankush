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
package com.impetus.ankush2.agent;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;

public class AgentUtils {

	final static ConfigurationReader ankushConf = AppStoreWrapper
			.getAnkushConfReader();

	public final static String agentBundlePath = AppStoreWrapper
			.getResourcePath() + AnkushConstant.Agent.Keys.AGENT_TAR;

	public static boolean removeProcessJmxtransJSON(SSHExec connection,
			String process, String agentHomeDir) {
		try {
			String jsonFilePath = agentHomeDir + "jmxtrans/jmxJson_"
					+ process.toLowerCase() + ".json";
			AnkushTask removeFile = new Remove(jsonFilePath);
			return (connection.exec(removeFile).rc == 0);
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean createServiceXML(SSHExec connection,
			ServiceConfiguration serviceConfiguration, String technology,
			String agentHomeDir) {
		try {
			String outputFile = agentHomeDir
					+ AgentConstant.Relative_Path.SERVICE_CONF_DIR
					+ technology.replace(" ", "\\ ") + ".xml";
			// java XML context object.
			JAXBContext jc = JAXBContext
					.newInstance(ServiceConfiguration.class);
			// Creating marshaller
			Marshaller marshaller = jc.createMarshaller();
			// Setting output format
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// Creating string writer for getting XML object string to write.
			StringWriter stringWriter = new StringWriter();
			// Marshalling object.
			marshaller.marshal(serviceConfiguration, stringWriter);
			// XML content.
			String xmlString = stringWriter.getBuffer().toString();
			// clear file task.
			AnkushTask clearFile = new ClearFile(outputFile);
			// Creating technology XML file at services conf folder.
			AnkushTask task = new AppendFileUsingEcho(xmlString.replaceAll(
					"\\\"", "\\\\\""), outputFile);
			// return execution status.
			return (connection.exec(clearFile).rc == 0 && connection.exec(task).rc == 0);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean createServiceXML(SSHExec connection,
			List<ComponentService> services, String technology,
			String agentHomeDir) {
		try {
			// component services configuration.
			ServiceConfiguration serviceConf = new ServiceConfiguration();
			// setting service
			serviceConf.setServices(services);
			// creating service XML.
			return createServiceXML(connection, serviceConf, technology,
					agentHomeDir);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean removeServiceXml(SSHExec connection, String technology) {
		try {

			String outputFile = Constant.Agent.AGENT_SERVICE_CONF_FOLDER
					+ technology.replace(" ", "\\ ") + ".xml";
			AnkushTask removeFile = new Remove(outputFile);
			return connection.exec(removeFile).rc == 0;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getAgentLogDirectoryPath(ClusterConfig clusterConfig) {
		return clusterConfig.getAgentHomeDir() + "/logs/";
	}

	public static int nodeCountForAgentUpgrade(Cluster cluster) {
		int nodeCount = 0;
		if (!(cluster.getState().equals(
				Constant.Cluster.State.SERVER_CRASHED.toString())
				|| cluster.getState().equals(
						Constant.Cluster.State.ERROR.toString())
				|| cluster.getState().equals(
						Constant.Cluster.State.REMOVING.toString()) || cluster
				.getState().equals(Constant.Cluster.State.DEPLOYING.toString()))) {

			String clusterAgentVersion = cluster.getAgentVersion();
			// Set of nodes
			Set<Node> nodes = cluster.getNodes();

			// iterating over the nodes.
			for (Node node : nodes) {
				// node agent version
				String nodeAgentVersion = node.getAgentVersion();
				// if both version are not equal.
				if (!nodeAgentVersion.equals(clusterAgentVersion)) {
					nodeCount++;
				}
			}
		}
		return nodeCount;
	}

	// public static String getAgentBuildCommand(String agentHomeDir) {
	// StringBuilder command = new StringBuilder().append("java -cp ")
	// .append(agentHomeDir).append("libs/*:").append(agentHomeDir)
	// .append("libs/agent-0.1.jar")
	// .append(" com.impetus.ankush.agent.action.ActionHandler ");
	// return command.toString();
	// }

	public static String getActionHandlerCommand(String agentInstallDir) {
		StringBuilder sb = new StringBuilder("java -cp \"");
		sb.append(agentInstallDir).append("/.ankush/agent/libs/*\" -D")
				.append(AgentConstant.Key.JAVA_PROPERTY_AGENT_INSTALL_DIR)
				.append("=").append(agentInstallDir)
				.append(" com.impetus.ankush.agent.action.ActionHandler ");
		return sb.toString();
	}

	public static boolean addProperties(SSHExec connection,
			String agentHomeDir, Properties props) {
		try {
			// string buffer for configuration string.
			StringBuffer fileContent = new StringBuffer();
			// iterating over the props for creating configuration string.
			for (String key : props.stringPropertyNames()) {
				// appending key and value in string buffer.
				fileContent.append(key).append("=")
						.append(props.getProperty(key))
						.append(Constant.Strings.LINE_SEPERATOR);
			}
			// creating append file task.
			CustomTask task = new AppendFileUsingEcho(fileContent.toString()
					.trim(), agentHomeDir
					+ AgentConstant.Relative_Path.AGENT_CONF_FILE);
			// executing task and returning status.
			return connection.exec(task).rc == 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method to add the taskable class names in taskable conf file.
	 * 
	 * @param connection
	 * @param className
	 * @return
	 */
	public static boolean addTaskables(SSHExec connection, List<String> classes)
			throws AnkushException {
		try {
			// empty buffer string for taskable.
			StringBuffer taskFileContent = new StringBuffer();
			// appending the line separator.
			taskFileContent.append(Constant.Strings.LINE_SEPERATOR);
			for (String className : classes) {
				// appending class name and line separator.
				taskFileContent.append(className)
						.append(Constant.Strings.LINE_SEPERATOR);
			}
			// add task information in taskable conf.
			AnkushTask task = new AppendFileUsingEcho(
					taskFileContent.toString(),
					Constant.Agent.AGENT_TASKABLE_FILE_PATH);
			return connection.exec(task).rc == 0;
		} catch (Exception e) {
			throw new AnkushException("Could not add taskable classes to "
					+ Constant.Agent.AGENT_TASKABLE_FILE_PATH);
		}
	}

	/**
	 * Method to add the taskable class names in taskable conf file.
	 * 
	 * @param connection
	 * @param className
	 * @return
	 */
	public static boolean addTaskables(SSHExec connection,
			List<String> classes, String agentHomeDir) throws AnkushException {
		try {
			// empty buffer string for taskable.
			StringBuffer taskFileContent = new StringBuffer();
			// appending the line separator.
			taskFileContent.append(Constant.Strings.LINE_SEPERATOR);
			for (String className : classes) {
				// appending class name and line separator.
				taskFileContent.append(className)
						.append(Constant.Strings.LINE_SEPERATOR);
			}
			// add task information in taskable conf.
			AnkushTask task = new AppendFileUsingEcho(
					taskFileContent.toString(), agentHomeDir
							+ AgentConstant.Relative_Path.TASKABLE_FILE);
			return connection.exec(task).rc == 0;
		} catch (Exception e) {
			throw new AnkushException("Could not add taskable classes to "
					+ Constant.Agent.AGENT_TASKABLE_FILE_PATH);
		}
	}

}
