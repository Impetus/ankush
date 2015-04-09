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
package com.impetus.ankush.common.ganglia;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.xml.XMLSerializer;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant.Graph.StartTime;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.GangliaUtils;
import com.impetus.ankush.common.utils.SSHConnection;

/**
 * It is used to get the CPU and memory utilisation graphs for the node.
 * 
 * @author hokam
 * 
 */
public class NodeGraph {

	private static final String COULD_NOT_EXECUTE_COMMAND_MSG = "Could not execute the graph data fetch command.";

	private static final String COULD_NOT_FETCH_DATA_MSG = "Could not fetch the graph data.";

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

	/** The time map. */
	private static Map<Integer, String> timeMap = null;

	// host name map.
//	private Map<String, String> hostNames;

	/** RRD directory path **/
	private String rrdsDirectory;

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
		if (password != null && !password.isEmpty()) {
			this.authInfo = password;
			this.authUsingPassword = true;
		} else {
			this.authInfo = privateKey;
			this.authUsingPassword = false;
		}
		this.clusterName = clusterName;

		// getting config reader object.
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

		// setting rrds directory path
		rrdsDirectory = CommonUtil.getUserHome(username)
				+ ankushConf.getStringValue("ganglia.rrds");

		// fetching host names.
//		hostNames = GangliaUtils.getGangliaHostNames(hostname, username,
//				password, privateKey);
	}

	/**
	 * Method to extract json from the rrd file.
	 * hostNames = GangliaUtils.getGangliaHostNames(hostname, username,
//				password, privateKey);
	 * @param startTime
	 *            the start time
	 * @param hostName
	 *            the hostName
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	public Map extractRRD(StartTime startTime, String hostName) throws Exception {

		Map result = new HashMap();
		// rrd director for the given ip address.
		String rrdDir = rrdsDirectory + clusterName + "/" + hostName + "/";

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
					throw new Exception(COULD_NOT_FETCH_DATA_MSG);
				}

				// puting json in result.
				result.put("json",
						new XMLSerializer().read(output.replaceAll("NaN", "0")));
			} else {
				throw new Exception(
						COULD_NOT_EXECUTE_COMMAND_MSG);
			}
		}
		return result;
	}
}
