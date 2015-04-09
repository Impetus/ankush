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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.xml.XMLSerializer;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.ThresholdConf;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.Graph.StartTime;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.SSHConnection;

/**
 * @author hokam
 */
public class LiveGraph {

	private static final String COULD_NOT_FETCH_DATA_MSG = "Could not fetch the graph data.";

	private static final String COULD_NOT_EXECUTE_COMMAND_MSG = "Could not execute the command to fetch the graph data";

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(LiveGraph.class);

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

	/** second time call flag **/
	private boolean update = false;

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
	 * 
	 * @param hostname
	 * @param username
	 * @param authInfo
	 * @param authUsingPassword
	 * @param clusterName
	 */
	public LiveGraph(String hostname, String username, String password,
			String privateKey, String clusterName) {
		super();
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
	}

	/**
	 * 
	 * @param hostname
	 * @param username
	 * @param authInfo
	 * @param authUsingPassword
	 * @param clusterName
	 */
	public LiveGraph(String hostname, String username, String password,
			String privateKey, String clusterName, Boolean update) {
		super();
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
		this.update = update;

		// getting config reader object.
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

		// setting rrds directory path
		rrdsDirectory = CommonUtil.getUserHome(username)
				+ ankushConf.getStringValue("ganglia.rrds");
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
	public Map extractRRD(StartTime startTime, Cluster cluster)
			throws Exception {

		// rrd cluster directory.
		String clusterRrdDir = FileNameUtils.convertToValidPath(rrdsDirectory
				+ clusterName + "/__SummaryInfo__");

		return generateRrdJSON(startTime, clusterRrdDir, cluster);
	}

	/**
	 * Method to provide Data from cluster memory, cpu, load and network data
	 * for given time period.
	 * 
	 * @param startTime
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public Map fetchGraphJson(StartTime startTime, String type)
			throws Exception {
		// output.
		Map result = new HashMap();
		// rrd cluster directory.
		String clusterRrdDir = FileNameUtils.convertToValidPath(rrdsDirectory
				+ clusterName + "/__SummaryInfo__");
		// making connection.
		SSHConnection connection = new SSHConnection(this.hostname,
				this.username, this.authInfo, this.authUsingPassword);

		if (connection.isConnected()) {

			// json creation command using the rrdtool.
			StringBuilder command = new StringBuilder();
			command.append("cd " + clusterRrdDir).append(";rrdtool xport ")
					.append(timeMap.get(startTime.ordinal()));

			String unit = "%";
			// cpu graphs
			if (type.equalsIgnoreCase(Constant.Graph.Type.cpu.toString())) {
				command.append(getClusterCpu(false));
				// memory graphs
			} else if (type.equalsIgnoreCase(Constant.Graph.Type.memory
					.toString())) {
				command.append(getClusterMemory(false));
				// network graphs.
			} else if (type.equalsIgnoreCase(Constant.Graph.Type.network
					.toString())) {
				unit = "MB/s";
				command.append(getClusterNetwork(false));
				// load graphs.
			} else if (type.equalsIgnoreCase(Constant.Graph.Type.load
					.toString())) {
				command.append(getClusterLoad(false));
			} else {
				return result;
			}

			logger.debug(command.toString());

			/* Executing the command. */
			if (connection.exec(command.toString())) {
				String output = connection.getOutput();
				if (output == null) {
					throw new Exception(COULD_NOT_FETCH_DATA_MSG);
				} else {
					// puting json in result.
					Map map = (Map) new XMLSerializer().read(output.replaceAll(
							"NaN", "0"));
					map.put("unit", unit);
					result.put("json", map);
				}
			} else {
				throw new Exception(COULD_NOT_EXECUTE_COMMAND_MSG);
			}
		}
		return result;
	}

	/**
	 * Method to extract json from the rrd file.
	 * 
	 * @param startTime
	 *            the start time
	 * @param hostName
	 *            the ip
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	public Map extractRRD(String hostName, StartTime startTime, Cluster cluster)
			throws Exception {
		// node rrd folder.
		String nodeRrdDir = rrdsDirectory + clusterName + "/" + hostName + "/";

		return generateRrdJSON(startTime, nodeRrdDir, cluster);
	}

	/**
	 * @param startTime
	 * @param pattern
	 * @param result
	 * @param clusterRrdDir
	 * @param rrdDir
	 * @throws Exception
	 */
	private Map generateRrdJSON(StartTime startTime, String rrdDir,
			Cluster cluster) throws Exception {
		// map
		Map result = new HashMap();
		// making connection.
		SSHConnection connection = new SSHConnection(this.hostname,
				this.username, this.authInfo, this.authUsingPassword);

		if (connection.isConnected()) {

			// json creation command using the rrdtool.
			StringBuilder command = new StringBuilder();
			command.append("cd " + rrdDir).append(";rrdtool xport ")
					.append(timeMap.get(startTime.ordinal()))
					.append(getClusterCpu(true)).append(getClusterMemory(true))
					.append(getClusterNetwork(true))
					.append(getClusterLoad(true));

			logger.debug(command.toString());

			/* Executing the command. */
			if (connection.exec(command.toString())) {
				String output = connection.getOutput();
				if (output == null) {
					throw new Exception("Unable to fetch graph.");
				}

				// putting JSON in result.
				Map map = (Map) new XMLSerializer().read(output.replaceAll(
						"NaN", "0"));
				Map mata = (Map) map.get("meta");
				List legends = (List) mata.get("legend");
				List datas = (List) map.get("data");

				for (Object legend : legends) {
					result.put(legend, new ArrayList());
				}

				for (int i = 0; i < datas.size(); i++) {
					for (int j = 0; j < legends.size(); j++) {
						Map data = (Map) datas.get(i);
						List v = (List) data.get("v");
						((List) result.get(legends.get(j))).add(v.get(j));
					}
				}

				Map<String, ThresholdConf> tConf = cluster.getAlertsConf()
						.getThresholdsMap();
				List list = new ArrayList();

				for (Object legend : result.keySet()) {
					String[] label = legend.toString().split(" ");
					Map item = new HashMap();

					item.put("id", label[0]);
					List values = (List) result.get(legend);

					List<String> maxValueList = values.subList(
							values.size() - 52, values.size() - 2);

					String max = Collections.max(maxValueList,
							new Comparator<String>() {
								public int compare(String o1, String o2) {
									Double i1 = Double.parseDouble(o1);
									Double i2 = Double.parseDouble(o2);
									return Double.compare(i1, i2);
								}
							});

					Double maxValue = Double.parseDouble(max);
					if (maxValue < 100) {
						maxValue = 100D;
					}

					item.put("maxValue", Math.round(maxValue));
					if (update) {
						values = values.subList(values.size() - 3,
								values.size() - 2);
					} else {
						values = values.subList(values.size() - 51,
								values.size() - 2);
					}
					List<Long> doubleValues = new ArrayList<Long>();
					for (Object value : values) {
						Double doubleValue = Double.parseDouble(value
								.toString());
						if (doubleValue < 0) {
							doubleValue = 0d;
						}
						doubleValues.add(Math.round(doubleValue));
					}
					item.put("values", doubleValues);
					if (tConf.get(label[0]) == null) {
						item.put("normalValue", "40");
					} else {
						item.put("normalValue", tConf.get(label[0])
								.getWarningLevel());
					}
					item.put("label", label[0]);
					item.put("unit", label[1]);
					item.put("delay", 500);
					list.add(item);
				}
				return Collections.singletonMap("sparklineData", list);
			}
		}
		return Collections.emptyMap();
	}

	private String getClusterCpu(boolean unit) {
		StringBuilder command = new StringBuilder();
		command.append("DEF:num_nodes=cpu_user.rrd:num:AVERAGE ");
		command.append("DEF:cpu_idle=cpu_idle.rrd:sum:AVERAGE ");
		command.append("CDEF:cpu_usage=100,cpu_idle,num_nodes,/,- ");
		if (unit) {
			command.append("XPORT:cpu_usage:\"CPU %\" ");
		} else {
			command.append("XPORT:cpu_usage:\"CPU\" ");
		}
		return command.toString();
	}

	private String getClusterMemory(boolean unit) {
		StringBuilder command = new StringBuilder();
		command.append("DEF:mt=mem_total.rrd:sum:AVERAGE ")
				.append("DEF:mf=mem_free.rrd:sum:AVERAGE ")
				.append("DEF:mb=mem_buffers.rrd:sum:AVERAGE ")
				.append("DEF:mc=mem_cached.rrd:sum:AVERAGE ")
				.append("DEF:ms=mem_shared.rrd:sum:AVERAGE ")
				.append("CDEF:mu=mt,ms,-,mf,-,mc,-,mb,- ")
				.append("CDEF:memory=100,mu,*,mt,/ ");
		if (unit) {
			command.append("XPORT:memory:\"Memory %\" ");
		} else {
			command.append("XPORT:memory:\"Memory\" ");
		}

		return command.toString();
	}

	private String getClusterLoad(boolean unit) {
		// creating xport value.
		StringBuilder command = new StringBuilder();
		command.append("DEF:cpu_num=cpu_num.rrd:sum:AVERAGE ");
		command.append("DEF:load_one=load_one.rrd:sum:AVERAGE ");
		command.append("CDEF:load=load_one,cpu_num,/,100,* ");
		if (unit) {
			command.append("XPORT:load:\"Load %\" ");
		} else {
			command.append("XPORT:load:\"Load\" ");
		}
		return command.toString();
	}

	private String getClusterNetwork(boolean unit) {
		// creating xport value.
		StringBuilder command = new StringBuilder();
		command.append("DEF:bytes_in=bytes_in.rrd:sum:AVERAGE ");
		command.append("DEF:bytes_out=bytes_out.rrd:sum:AVERAGE ");
		command.append("CDEF:bytes=bytes_in,bytes_out,+, ");
		command.append("CDEF:kbs=bytes,1024,/ ");
		command.append("CDEF:mbs=kbs,1024,/ ");
		if (unit) {
			command.append("XPORT:mbs:\"Network MB/s\" ");
		} else {
			command.append("XPORT:mbs:\"Network\" ");
		}
		return command.toString();
	}
}
