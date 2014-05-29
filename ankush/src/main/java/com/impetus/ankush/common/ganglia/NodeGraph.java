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
package com.impetus.ankush.common.ganglia;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.xml.XMLSerializer;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.Graph.StartTime;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.SSHConnection;

/**
 * It is used to get the CPU and memory utilization graphs for the node.
 * 
 * @author hokam
 * 
 */
public class NodeGraph {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(NodeGraph.class);

	/** The hostname. */
	private String hostname;

	/** The username. */
	private String username;

	/** The auth info. */
	private String authInfo;

	/** The auth using password. */
	private boolean authUsingPassword;

	/** The cluster name. */
	private String clusterName;

	/** The password **/
	private String password;

	/** The private **/
	private String privateKey;

	/** The time map. */
	private static Map<Integer, String> timeMap = null;

	// map for the time periods.
	static {
		timeMap = new HashMap<Integer, String>();
		timeMap.put(StartTime.lasthour.ordinal(), "-s now-1h ");
		timeMap.put(StartTime.lastday.ordinal(), "-s now-1d ");
		timeMap.put(StartTime.lastweek.ordinal(), "-s now-1w ");
		timeMap.put(StartTime.lastmonth.ordinal(), "-s now-1m ");
		timeMap.put(StartTime.lastyear.ordinal(), "-s now-1y ");
	}

	/**
	 * Instantiates a new node graph.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param privateKey
	 *            the private key
	 * @param clusterName
	 *            the cluster name
	 */
	public NodeGraph(String hostname, String username, String password,
			String privateKey, String clusterName) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.privateKey = privateKey;
		if (password != null && !password.isEmpty()) {
			this.authInfo = password;
			this.authUsingPassword = true;
		} else {
			this.authInfo = privateKey;
			this.authUsingPassword = false;
		}
		this.clusterName = clusterName;
	}

	/**
	 * Method to extract json from the rrd file.
	 * 
	 * @param startTime
	 *            the start time
	 * @param ip
	 *            the ip
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	public Map extractRRD(StartTime startTime, String ip) throws Exception {

		Map result = new HashMap();
	
		// rrd director for the given ip address.
		String rrdDir = Constant.Graph.RRD_BASH_PATH + clusterName + "/"
				+ HostOperation.getAnkushHostName(ip) + "/";

		// making connection.
		SSHConnection connection = new SSHConnection(this.hostname,
				this.username, this.authInfo, this.authUsingPassword);

		if (connection.isConnected()) {

			// json creation command using the rrdtool.
			StringBuilder command = new StringBuilder();
			command.append("cd " + rrdDir).append(";rrdtool xport ")
					.append(timeMap.get(startTime.ordinal()))
					.append("DEF:cn=cpu_num.rrd:sum:AVERAGE ")
					.append("DEF:ci=cpu_idle.rrd:sum:AVERAGE ")
					.append("DEF:mt=mem_total.rrd:sum:AVERAGE ")
					.append("DEF:mf=mem_free.rrd:sum:AVERAGE ")
					.append("DEF:mb=mem_buffers.rrd:sum:AVERAGE ")
					.append("DEF:mc=mem_cached.rrd:sum:AVERAGE ")
					.append("DEF:ms=mem_shared.rrd:sum:AVERAGE ")
					.append("CDEF:mu=mt,ms,-,mf,-,mc,-,mb,- ")
					.append("CDEF:mem=100,mu,*,mt,/ ")
					.append("CDEF:cpu=100,ci,- ")
					.append("XPORT:cpu:\"CPU %\" ")
					.append("XPORT:mem:\"Memory %\" ");

			/* Executing the command. */
			if (connection.exec(command.toString())) {
				String output = connection.getOutput();
				if (output == null) {
					throw new Exception("Unable to fetch graph.");
				}

				// puting json in result.
				result.put("json",
						new XMLSerializer().read(output.replaceAll("NaN", "0")));
			}
		}
		return result;
	}
}
