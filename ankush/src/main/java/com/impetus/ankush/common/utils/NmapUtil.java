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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.exception.RegisterClusterException;
import com.impetus.ankush2.constant.Constant.Strings;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * It is used to get the hash map of nodes with rechanbility status as values.
 * 
 * @author hokam Chauhan
 * 
 */
public class NmapUtil {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(NmapUtil.class);

	/** The node pattern. */
	private String nodePattern;

	/**
	 * Instantiates a new nmap util.
	 * 
	 * @param nodePattern
	 *            the node pattern
	 */
	public NmapUtil(String nodePattern) {
		this.nodePattern = nodePattern;
	}

	/**
	 * Gets the node list with status.
	 * 
	 * @return the node list with status
	 */
	public Map<String, Boolean> getNodeListWithStatus() {
		// create result hash map.
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		if ((nodePattern != null) && !nodePattern.isEmpty()
				&& !nodePattern.equals(Constant.Strings.SPACE)) {
			// split the nodePattern on Comma
			String[] splittedNodeArray = this.nodePattern
					.split(Constant.Strings.SPACE);
			Boolean isReachable = false;
			for (String host : splittedNodeArray) {
				try {
					isReachable = InetAddress.getByName(host.trim())
							.isReachable(3000);
					result.put(host.trim(), isReachable);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return result;
	}

	/**
	 * Gets the host ipxml file path.
	 * 
	 * @param hostNameList
	 *            the host name list
	 * @return the host ipxml file path
	 */
	private String getHostIPXMLFilePath(List<String> hostNameList) {
		// convert list to Space seperated String
		String hostsString = org.apache.commons.lang3.StringUtils.join(
				hostNameList, Strings.SPACE);
		// making nmap command.
		StringBuffer sBuf = new StringBuffer("nmap");
		// taking temporary xml file for nmap result.
		String fileName = "hostIP_" + CommonUtil.getRandomReqId() + ".xml";
		sBuf.append(Strings.SPACE + "-oX" + Strings.SPACE).append(fileName);
		sBuf.append(Strings.SPACE + "-sn" + Strings.SPACE);

		// adding node pattend.
		sBuf.append(hostsString);

		File file = new File(fileName);
		try {
			// executing command.
			int exitValue = CommandExecutor.exec(sBuf.toString());
			if (exitValue == 0 && file.exists()) {
				return file.getAbsolutePath();
			}
		} catch (Exception e) {
			this.logger.error("Error in executing command :" + sBuf.toString()
					+ e.getMessage());
			return null;
		}
		return null;
	}

	/**
	 * Gets the host ip map.
	 * 
	 * @param hostNameList
	 *            the host name list
	 * @return the host ip map
	 */
	public Map<String, String> getHostIPMap(List<String> hostNameList)
			throws Exception {
		String xmlFilePath = getHostIPXMLFilePath(hostNameList);
		Map<String, String> hostIpMap = null;
		if (xmlFilePath == null) {
			throw new RegisterClusterException(
					"Couln't found nmap generated xml file for hostIP mapping.");
		} else {
			List<String> errorList = new ArrayList<String>();
			String errMsg = "Couldn't map IP for ";
			try {
				hostIpMap = getHostIPMapping(xmlFilePath);
				for (String hostName : hostNameList) {
					if (!hostIpMap.containsKey(hostName)) {
						errorList.add(errMsg + hostName);
					}
				}
				if (errorList.size() > 0) {
					throw new RegisterClusterException(errorList);
				}
			} catch (Exception e) {
				throw e;
			}
			return hostIpMap;
		}
	}

	/**
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	private static Map<String, String> getHostIPMapping(String filePath)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		// loading the XML document from a file
		DocumentBuilderFactory builderfactory = DocumentBuilderFactory
				.newInstance();
		builderfactory.setNamespaceAware(true);

		DocumentBuilder builder = builderfactory.newDocumentBuilder();
		File file = new File(filePath);
		Document xmlDocument = builder.parse(file);

		XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
		XPath xPath = factory.newXPath();

		// getting the name of the book having an isbn number == ABCD7327923
		XPathExpression hostXpath = xPath.compile("//host");

		XPathExpression ipXpath = xPath
				.compile("address[@addrtype='ipv4']/@addr");

		XPathExpression hostNameXpath = xPath
				.compile("hostnames/hostname[@type='user']/@name");

		NodeList nodeListHost = (NodeList) hostXpath.evaluate(xmlDocument,
				XPathConstants.NODESET);

		Map<String, String> hostIpMapping = new HashMap<String, String>();
		for (int index = 0; index < nodeListHost.getLength(); index++) {
			String ip = (String) ipXpath.evaluate(nodeListHost.item(index),
					XPathConstants.STRING);
			String host = (String) hostNameXpath.evaluate(
					nodeListHost.item(index), XPathConstants.STRING);

			hostIpMapping.put(host, ip);

		}
		// deleting the temporary xml file.
		FileUtils.deleteQuietly(file);
		return hostIpMapping;
	}
}
