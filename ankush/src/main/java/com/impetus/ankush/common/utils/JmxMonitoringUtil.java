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
package com.impetus.ankush.common.utils;

import java.util.Iterator;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.AuthConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class JmxMonitoringUtil.
 * 
 * @author Akhil
 */
public class JmxMonitoringUtil {

	/** The Constant JmxTransScripFileName. */
	public final static String JmxTransScripFileName = "jmxtrans.sh";
	/** The ankushConf Reader. */
	private static ConfigurationReader ankushConf = AppStoreWrapper
			.getAnkushConfReader();

	/** The Constant jmxCommandServiceStart. */
	private final static String jmxCommandServiceStart = JmxMonitoringUtil.ankushConf
			.getStringValue("jmx.command.service.start");

	/** The Constant jmxCommandServiceStop. */
	private final static String jmxCommandServiceStop = JmxMonitoringUtil.ankushConf
			.getStringValue("jmx.command.service.stop");

	/** The Constant jmxCommandServiceRestart. */
	private final static String jmxCommandServiceRestart = JmxMonitoringUtil.ankushConf
			.getStringValue("jmx.command.service.restart");

	/** The Constant jmxCommandServiceStatus. */
	private final static String jmxCommandServiceStatus = JmxMonitoringUtil.ankushConf
			.getStringValue("jmx.command.service.status");

	/**
	 * Configure jmx port.
	 * 
	 * @param processName
	 *            the process name
	 * @param connection
	 *            the connection
	 * @param filePath
	 *            the file path
	 * @param jmxPort
	 *            the jmx port
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	public static boolean configureJmxPort(String processName,
			SSHExec connection, String filePath, int jmxPort, String password) {
		try {
			final String portText = "JMX_PORT_" + processName.toUpperCase();
			final String targetText = JmxMonitoringUtil.ankushConf
					.getStringValue("jmx." + processName.toLowerCase()
							+ ".targetText");
			String replacementText = JmxMonitoringUtil.ankushConf
					.getStringValue("jmx." + processName.toLowerCase()
							+ ".replacementText");
			replacementText = replacementText.replaceAll(portText,
					String.valueOf(jmxPort));
			Result res = null;
			final AnkushTask updateFile = new ReplaceText(targetText,
					replacementText, filePath, true, password);
			res = connection.exec(updateFile);
			if (!res.isSuccess) {
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Copy jmx trans json.
	 * 
	 * @param connection
	 *            the connection
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @param componentName
	 *            the component name
	 * @param processName
	 *            the process name
	 * @param jmxPort
	 *            the jmx port
	 * @param privateIp
	 *            the private ip
	 * @return true, if successful
	 */
	public static boolean copyJmxTransJson(SSHExec connection, String userName,
			String password, String componentName, String processName,
			String jmxPort, String privateIp) {
		return copyJmxTransJson(connection, userName, password, componentName,
				processName, jmxPort, privateIp, null);
	}

	public static boolean copyJmxTransJson(SSHExec connection,
			AuthConfig authConfig, String componentName, String processName,
			int jmxPort, String agentHomeDir) {
		return copyJmxTransJson(connection, authConfig.getUsername(),
				authConfig.getPassword(), componentName, processName,
				String.valueOf(jmxPort), agentHomeDir, null, null, null);
	}

	/**
	 * Copy jmx trans json.
	 * 
	 * @param connection
	 *            the connection
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @param componentName
	 *            the component name
	 * @param processName
	 *            the process name
	 * @param jmxPort
	 *            the jmx port
	 * @param privateIp
	 *            the private ip
	 * @param jsonTemplateSourcePath
	 *            the source path for jmx json template
	 * @return true, if successful
	 */
	public static boolean copyJmxTransJson(SSHExec connection, String userName,
			String password, String componentName, String processName,
			String jmxPort, String privateIp, String jsonTemplateSourcePath) {
		return copyJmxTransJson(connection, userName, password, componentName,
				processName, jmxPort, privateIp, jsonTemplateSourcePath, null,
				null);
	}

	/**
	 * Copy jmx trans json.
	 * 
	 * @param connection
	 *            the connection
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @param componentName
	 *            the component name
	 * @param processName
	 *            the process name
	 * @param jmxPort
	 *            the jmx port
	 * @param privateIp
	 *            the private ip
	 * @param jsonTemplateSourcePath
	 *            the source path for jmx json template
	 * @param prmMap
	 *            the parameter map for jmx json template
	 * @return true, if successful
	 */
	public static boolean copyJmxTransJson(SSHExec connection, String userName,
			String password, String componentName, String processName,
			String jmxPort, String agentHomeDir, String privateIp,
			String jsonTemplateSourcePath, Map prmMap) {
		try {
			final String jsonTemplatePath_Source = jsonTemplateSourcePath == null ? (AppStoreWrapper
					.getResourcePath()
					+ JmxMonitoringUtil.ankushConf
							.getStringValue("jmxtrans.json.template.path")
					+ processName.toLowerCase() + ".json")
					: jsonTemplateSourcePath;
			final String jmxTransInstallPath = agentHomeDir
					+ JmxMonitoringUtil.ankushConf
							.getStringValue("jmxtrans.installation.relative.path");
			final String jsonTemplatePath_Destination = jmxTransInstallPath
					+ JmxMonitoringUtil.ankushConf
							.getStringValue("jmxtrans.json.filename.prefix")
					+ processName.toLowerCase() + ".json";

			connection.uploadSingleDataToServer(jsonTemplatePath_Source,
					jsonTemplatePath_Destination);

			String targetText = JmxMonitoringUtil.ankushConf
					.getStringValue("jmxtrans.json.jmxport.prefix")
					+ processName.toUpperCase();
			String replacementText = jmxPort;
			Result res = null;
			AnkushTask updateJmxJsonFile = new ReplaceText(targetText,
					replacementText, jsonTemplatePath_Destination, false,
					password);
			res = connection.exec(updateJmxJsonFile);
			if (!res.isSuccess) {
				return false;
			}

			targetText = JmxMonitoringUtil.ankushConf
					.getStringValue("jmxtrans.script.template.resultalias");
			replacementText = JmxMonitoringUtil.getReplacementText_ResultAlias(
					componentName, processName);
			updateJmxJsonFile = new ReplaceText(targetText, replacementText,
					jsonTemplatePath_Destination, false, password);
			res = connection.exec(updateJmxJsonFile);
			if (!res.isSuccess) {
				return false;
			}
			if (prmMap != null) {
				Iterator itr = prmMap.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String) itr.next();
					targetText = "${" + key + "}";
					replacementText = (String) prmMap.get(key);
					updateJmxJsonFile = new ReplaceText(targetText,
							replacementText, jsonTemplatePath_Destination,
							false, password);
					res = connection.exec(updateJmxJsonFile);
					if (!res.isSuccess) {
						return false;
					}
				}
			}
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	public static boolean copyJmxTransJsonForComponent(SSHExec connection,
			AuthConfig authConfig, String componentName, String processName,
			String jmxPort, String agentHomeDir) {
		return copyJmxTransJson(connection, authConfig.getUsername(),
				authConfig.getPassword(), componentName, processName,
				String.valueOf(jmxPort), agentHomeDir,
				authConfig.getPrivateKey(), null, null);
	}

	/**
	 * Gets the jmx trans command.
	 * 
	 * @param scriptFilePath
	 *            the script file path
	 * @param password
	 *            the password
	 * @param action
	 *            the action
	 * @return the jmx trans command
	 */
	public static String getJmxTransCommand(String scriptFilePath,
			String password, Constant.JmxTransServiceAction action) {

		final StringBuilder command = new StringBuilder();
		String echoPass = "";
		if (password != null) {
			echoPass = "echo '" + password + "' | sudo -S ";
		}
		command.append(echoPass).append("sh " + scriptFilePath + " ");

		if (action.equals(Constant.JmxTransServiceAction.START)) {
			command.append(JmxMonitoringUtil.jmxCommandServiceStart);
		} else if (action.equals(Constant.JmxTransServiceAction.STOP)) {
			command.append(JmxMonitoringUtil.jmxCommandServiceStop);
		} else if (action.equals(Constant.JmxTransServiceAction.RESTART)) {
			command.append(JmxMonitoringUtil.jmxCommandServiceRestart);
		} else if (action.equals(Constant.JmxTransServiceAction.STATUS)) {
			command.append(JmxMonitoringUtil.jmxCommandServiceStatus);
		}

		return command.toString();
	}

	/**
	 * Gets the process list.
	 * 
	 * @param componentName
	 *            the component name
	 * @return the process list
	 */
	public static String[] getProcessList(String componentName) {
		final String confKey = "java.process.list."
				+ componentName.toLowerCase();
		final String processes = JmxMonitoringUtil.ankushConf
				.getStringValue(confKey);
		if (processes != null) {
			return processes.split(",");
		} else {
			return null;
		}
	}

	/**
	 * Gets the replacement text_ result alias.
	 * 
	 * @param componentName
	 *            the component name
	 * @param processName
	 *            the process name
	 * @return the replacement text_ result alias
	 */
	private static String getReplacementText_ResultAlias(String componentName,
			String processName) {
		return componentName.toLowerCase() + "_" + processName.toLowerCase();
	}
}
