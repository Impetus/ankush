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
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * It is used to get the hash map of nodes with rechanbility status as values.
 * 
 * @author hokam Chauhan
 * 
 */
public class NmapUtil {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(NmapUtil.class);

	/** The node pattern. */
	private String nodePattern;

	/**
	 * Instantiates a new nmap util.
	 *
	 * @param nodePattern the node pattern
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

		// making nmap command.
		StringBuffer sBuf = new StringBuffer("nmap");

		// taking temporary xml file for nmap result.
		String fileName = "nodeDetailsList_" + CommonUtil.getRandomReqId()
				+ ".xml";
		sBuf.append(" -oX ").append(fileName);
		sBuf.append(" -sP ");

		// adding node pattend.
		sBuf.append(nodePattern);

		int exitValue;
		File file = new File(fileName);
		try {
			// executing command.
			exitValue = CommandExecutor.exec(sBuf.toString());
			if (file.exists()) {
				try {
					// creting document builder factory for xml file parsing.
					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder docBuilder = dbf.newDocumentBuilder();
					Document doc = docBuilder.parse(fileName);
					NodeList nl = doc.getElementsByTagName("host");

					// iterating over all the host elements.
					for (int index = 0; index < nl.getLength(); ++index) {
						String ip = null;
						boolean isUp = false;

						// getting the ip address of host.
						NodeList nsl = ((Element) nl.item(index))
								.getElementsByTagName("address");
						if (nsl.getLength() > 0) {
							ip = ((Element) nsl.item(0)).getAttribute("addr");
						}

						// getting rechability status on host.
						nsl = ((Element) nl.item(index))
								.getElementsByTagName("status");
						if (nsl.getLength() > 0) {
							String state = ((Element) (nsl.item(0)))
									.getAttribute("state");
							isUp = state.equals("up");
						}

						result.put(ip, isUp);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			logger.debug("exitValue " + exitValue);
		} catch (IOException e1) {
			logger.error(e1.getMessage());
		} catch (InterruptedException e1) {
			logger.error(e1.getMessage());
		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}
		// deleting the temporary xml file.
		FileUtils.deleteQuietly(file);

		return result;
	}
}
