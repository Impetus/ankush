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
package com.impetus.ankush.common.agent;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;

import org.apache.commons.io.FilenameUtils;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.HAService;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush2.agent.AgentConstant;
import com.impetus.ankush2.common.scripting.impl.AddConfProperty;
import com.impetus.ankush2.constant.Constant.Component;
import com.impetus.ankush2.constant.Constant.Strings;

/**
 * @author hokam
 * 
 */
public class AgentUtils {

	/**
	 * Gets the agent down message.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @return the agent down message
	 */
	public static String getAgentDownMessage(String nodeIp) {
		return "Agent Down on " + nodeIp + ": Could not process request.";

	}

	/**
	 * Method to add properties in agent configuration.
	 * 
	 * @param props
	 * @param connection
	 * @return
	 */
	public static boolean addProperties(SSHExec connection, Properties props) {
		try {
			// string buffer for configuration string.
			StringBuffer fileContent = new StringBuffer();
			// iterating over the props for creating configuration string.
			for (String key : props.stringPropertyNames()) {
				// appending key and value in string buffer.
				fileContent.append(key).append("=")
						.append(props.getProperty(key))
						.append(Strings.LINE_SEPERATOR);
			}
			// creating append file task.
			CustomTask task = new AppendFileUsingEcho(fileContent.toString(),
					Constant.Agent.AGENT_PROPERTY_FILE_PATH);
			// executing task and returning status.
			return connection.exec(task).rc == 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Adds the property to file.
	 * 
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 * @param nodeConf
	 *            the node conf
	 * @param filePath
	 *            the file path
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 * @return true, if successful
	 */
	public static boolean addPropertyToFile(GenericConfiguration conf,
			SSHExec connection, NodeConf nodeConf, String filePath,
			String propertyName, String propertyValue) {
		/** The log. */
		AnkushLogger LOG = new AnkushLogger(Component.Name.AGENT,
				AgentUtils.class);

		try {
			// Configuration manager to save the property file change records.
			ConfigurationManager confManager = new ConfigurationManager();
			Result res = null;
			AnkushTask addProperty = new AddConfProperty(propertyName,
					propertyValue, filePath, Constant.File_Extension.XML);
			res = connection.exec(addProperty);
			if (!res.isSuccess) {
				LOG.error("Could not add " + propertyName + " to " + filePath
						+ ".");
				return false;
			}
			// saving the Add Event for Audit Trail
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(), FilenameUtils.getName(filePath),
					nodeConf.getPublicIp(), propertyName, propertyValue);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		return true;
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
			taskFileContent.append(Strings.LINE_SEPERATOR);
			for (String className : classes) {
				// appending class name and line separator.
				taskFileContent.append(className)
						.append(Strings.LINE_SEPERATOR);
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
			taskFileContent.append(Strings.LINE_SEPERATOR);
			for (String className : classes) {
				// appending class name and line separator.
				taskFileContent.append(className)
						.append(Strings.LINE_SEPERATOR);
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

	/**
	 * Utility method to add Service XML configuration file.
	 * 
	 * @param connection
	 * @param serviceConfiguration
	 * @return
	 */
	private static boolean createServiceXML(SSHExec connection,
			ServiceConfiguration serviceConfiguration, String technology) {
		try {
			String outputFile = Constant.Agent.AGENT_SERVICE_CONF_FOLDER
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

	/**
	 * Utility method to add Service XML configuration file.
	 * 
	 * @param connection
	 * @param compServices
	 * @return
	 */
	public static boolean createServiceXML(SSHExec connection,
			List<ComponentService> services, String technology) {
		try {
			// component services configuration.
			ServiceConfiguration serviceConf = new ServiceConfiguration();
			// setting service
			serviceConf.setServices(services);
			// creating service XML.
			return createServiceXML(connection, serviceConf, technology);
		} catch (Exception e) {
			return false;
		}
	}

}
