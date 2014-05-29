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
package com.impetus.ankush.hadoop;

/**
 * @author Akhil
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.validator.IPAddressValidator;

/**
 * The Class GenericRackAwareness.
 */
public class GenericRackAwareness {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(GenericRackAwareness.class);

	/** The Constant strDefaultRack. */
	public final static String strDefaultRack = "/default-rack";

	/** The Constant strDefaultRack. */
	public final static String strDefaultDataCenter = "/default-dc";

	/** The node rack map. */
	// private HashMap<String, String> nodeRackMap;

	/** The node dc rack map. */
	private HashMap<String, HashMap<String, String>> nodeDcRackMap;

	/** The topology file contents. */
	private List<String> topologyFileContents;

	/** The errors. */
	private List<String> errors = new ArrayList<String>();

	/**
	 * Gets the node rack map.
	 * 
	 * @return the node rack map
	 */
	// public HashMap<String, String> getNodeRackMap() {
	// return nodeRackMap;
	// }
	//
	// /**
	// * Sets the node rack map.
	// *
	// * @param nodeRackMap the node rack map
	// */
	// public void setNodeRackMap(HashMap<String, String> nodeRackMap) {
	// this.nodeRackMap = nodeRackMap;
	// }

	/**
	 * Gets the topology file contents.
	 * 
	 * @return the topology file contents
	 */
	public List<String> getTopologyFileContents() {
		return topologyFileContents;
	}

	/**
	 * Sets the topology file contents.
	 * 
	 * @param fileContents
	 *            the new topology file contents
	 */
	public void setTopologyFileContents(List<String> fileContents) {
		this.topologyFileContents = fileContents;
	}

	/**
	 * Instantiates a new generic rack awareness.
	 * 
	 * @param rackMap
	 *            the rack map
	 * @throws Exception
	 *             the exception
	 */
	// public GenericRackAwareness(HashMap<String, String> rackMap) throws
	// Exception {
	// this.nodeRackMap = rackMap;
	// getNodeRackFileFromMap();
	// }

	/**
	 * Instantiates a new generic rack awareness.
	 * 
	 * @param filePathRackMap
	 *            the file path rack map
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws Exception
	 *             the exception
	 */
	public GenericRackAwareness(String filePathRackMap) throws IOException,
			Exception {
		List<String> fileContents = new ArrayList<String>();
		try {
			File rackFile = new File(filePathRackMap);
			fileContents = FileUtils.readLines(rackFile);
		} catch (Exception e) {
			logger.error("Unable to read Node Rack Mapping File");
		}
		nodeDcRackMap = new HashMap<String, HashMap<String, String>>();
		int lineNumber = 0;
		for (String strNodeRackMap : fileContents) {
			lineNumber++;
			List<String> nodeDetails = new ArrayList<String>(
					Arrays.asList(strNodeRackMap.split(":")));

			if (nodeDetails.size() == 0) {
				logger.error("Unable to read rack file: Invalid Entry - Line: "
						+ lineNumber);
				throw (new Exception(
						"Unable to read rack file: Invalid Entry - Line: "
								+ lineNumber));
			} else if (nodeDetails.size() == 1 || nodeDetails.size() > 3) {
				logger.error("Unable to read rack file: Invalid Entry for "
						+ nodeDetails.get(0) + " - Line: " + lineNumber);
				throw (new Exception(
						"Unable to read rack file: Invalid Entry for "
								+ nodeDetails.get(0) + " - Line: " + lineNumber));
			} else if (!IPAddressValidator.validate(nodeDetails.get(0))) {
				logger.error("Unable to read rack file: Invalid IP Address - "
						+ nodeDetails.get(0) + "  ( Line: " + lineNumber + " )");
				throw (new Exception(
						"Unable to read rack file: Invalid IP Address - "
								+ nodeDetails.get(0) + "  ( Line: "
								+ lineNumber + " )"));
			} else {
				HashMap<String, String> dcRackMap = new HashMap<String, String>();
				dcRackMap.put("datacenter", nodeDetails.get(1));
				if (nodeDetails.size() != 2) {
					dcRackMap.put("rack", nodeDetails.get(2));
				} else {
					dcRackMap.put("rack", "");
				}
				this.nodeDcRackMap.put(nodeDetails.get(0), dcRackMap);
			}
		}
	}

	/**
	 * Gets the rack info.
	 * 
	 * @param ipAddress
	 *            the ip address
	 * @return the rack info
	 */
	// public final String getRackInfo(String ipAddress) {
	// return this.nodeRackMap.get(ipAddress);
	// }

	public final HashMap<String, String> getDcRackInfo(String ipAddress) {
		return this.nodeDcRackMap.get(ipAddress);
	}

	public String getDatacenter(String ipAddress) {
		String datacenter = "";
		if (this.nodeDcRackMap.containsKey(ipAddress)
				&& nodeDcRackMap.get(ipAddress).containsKey("datacenter")) {
			datacenter = this.nodeDcRackMap.get(ipAddress).get("datacenter");
		}
		return datacenter;
	}

	public String getRack(String ipAddress) {
		String rack = "";
		if (this.nodeDcRackMap.containsKey(ipAddress)
				&& nodeDcRackMap.get(ipAddress).containsKey("rack")) {
			rack = this.nodeDcRackMap.get(ipAddress).get("rack");
		}
		return rack;
	}

	/**
	 * Gets the node rack file from map.
	 * 
	 * @return the node rack file from map
	 * @throws Exception
	 *             the exception
	 */
	// private void getNodeRackFileFromMap() throws Exception
	// {
	// List<String> data = new ArrayList<String>();
	// for(String nodeIP : this.nodeRackMap.keySet())
	// {
	// if(!IPAddressValidator.validate(nodeIP)) {
	// throw (new
	// Exception("Unable to read Node Rack Map: Invalid IP Address - " +
	// nodeIP));
	// } else
	// {
	// String record = nodeIP + " " + this.nodeRackMap.get(nodeIP);
	// data.add(record);
	// }
	// }
	// }

	/**
	 * Gets the node rack map from file.
	 * 
	 * @return the node rack map from file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws Exception
	 *             the exception
	 */
	// private void getNodeRackMapFromFile() throws IOException, Exception {
	// int lineNumber = 0;
	// for (String strNodeRackMap : this.topologyFileContents) {
	// lineNumber++;
	// StringTokenizer st = new StringTokenizer(strNodeRackMap);
	// ArrayList<String> lstRecord = new ArrayList<String>();
	// if(st.countTokens() == 0){
	// throw (new Exception("Unable to read rack file: Invalid Entry - Line: " +
	// lineNumber));
	// }
	//
	// while (st.hasMoreElements()) {
	// String token = st.nextElement().toString();
	// lstRecord.add(token);
	// }
	// if(lstRecord.size() != 2) {
	// throw (new Exception("Unable to read rack file: Invalid Entry for " +
	// lstRecord.get(0) + " - Line: " + lineNumber));
	// }
	// else if(!IPAddressValidator.validate(lstRecord.get(0))) {
	// throw (new Exception("Unable to read rack file: Invalid IP Address - " +
	// lstRecord.get(0) + "  ( Line: " + lineNumber + " )"));
	// } else {
	// this.nodeRackMap.put(lstRecord.get(0), lstRecord.get(1));
	// }
	// }
	// }

	public static int getNodeCountForRack(Set<Node> nodeConfs, String rackInfo) {
		int nodeCount = 0;
		for (Node nodeConf : nodeConfs) {
			if (!nodeConf.getState().equals(Constant.Node.State.DEPLOYED)) {
				continue;
			}
			String rackId = getRackId(nodeConf);
			if (rackId.equals(rackInfo)) {
				nodeCount++;
			}
		}
		return nodeCount;
	}

	/**
	 * Gets the rack id.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @return the rack id
	 */
	public static String getRackId(Node nodeConf) {
		String dc = nodeConf.getNodeConf().getDatacenter();
		String rackId = nodeConf.getNodeConf().getRack();
		String rackInfo = new String();

		if (dc != null && !dc.isEmpty()) {
			if (!dc.contains("/")) {
				dc = "/" + dc;
			}
		} else {
			dc = "";
		}

		if (rackId != null && !rackId.isEmpty()) {
			if (!rackId.contains("/")) {
				rackId = "/" + rackId;
			}	
		} else {
			rackId = "";
		}
		

		rackInfo = dc + rackId;
		return rackInfo;
	}

	/**
	 * @return the nodeDcRackMap
	 */
	public HashMap<String, HashMap<String, String>> getNodeDcRackMap() {
		return nodeDcRackMap;
	}

	/**
	 * @param nodeDcRackMap
	 *            the nodeDcRackMap to set
	 */
	public void setNodeDcRackMap(
			HashMap<String, HashMap<String, String>> nodeDcRackMap) {
		this.nodeDcRackMap = nodeDcRackMap;
	}
}
