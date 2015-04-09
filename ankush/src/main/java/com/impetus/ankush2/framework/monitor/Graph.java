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
package com.impetus.ankush2.framework.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.SSHConnection;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.AuthConfig;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.ganglia.GangliaConstants;
import com.impetus.ankush2.logger.AnkushLogger;

public class Graph {

	private final AnkushLogger logger = new AnkushLogger(Graph.class);

	public enum Type {
		cpu, network, memory, load
	}

	public enum StartTime {
		lasthour, lastday, lastweek, lastmonth, lastyear
	};

	private static final String LIVE_DATA_INTERVAL = "-s now-1800 -e now-30 ";
	private static final String LIVE_DATA_UPDATE_INTERVAL = "-s now-45 -e now-30 ";

	private static HashMap<Integer, String> timeMap;
	private AuthConfig authConf;
	private String rrdpath;
	private String host;
	/** The all files. */
	private List<String> allFiles;

	static {
		timeMap = new HashMap<Integer, String>();
		timeMap.put(StartTime.lasthour.ordinal(), "-s now-1h ");
		timeMap.put(StartTime.lastday.ordinal(), "-s now-1d ");
		timeMap.put(StartTime.lastweek.ordinal(), "-s now-1w ");
		timeMap.put(StartTime.lastmonth.ordinal(), "-s now-1m ");
		timeMap.put(StartTime.lastyear.ordinal(), "-s now-1y ");
	}

	static Map<String, String> metricsMap = new HashMap<String, String>();

	static {
		// cpu metrics display name.
		metricsMap.put("cpu_idle", "CPU Idle");
		metricsMap.put("cpu_wio", "CPU wio");
		metricsMap.put("cpu_system", "CPU System");
		metricsMap.put("cpu_nice", "CPU Nice");
		metricsMap.put("cpu_user", "CPU User");

		// disk metrics display name.
		metricsMap.put("disk_free", "Disk Space Available");
		metricsMap.put("disk_total", "Total Disk Space");

		// memory metrics display name.
		metricsMap.put("mem_total", "Total Memory");
		metricsMap.put("mem_buffers", "Memory Buffers");
		metricsMap.put("mem_shared", "Shared Memory");
		metricsMap.put("mem_free", "Free Memory");
		metricsMap.put("mem_cached", "Cached Memory");

		// load metrics display name.
		metricsMap.put("load_one", "One Minute Load Average");
		metricsMap.put("load_five", "Five Minute Load Average");
		metricsMap.put("load_fifteen", "Fifteen Minute Load Average");

		// process metrics display name
		metricsMap.put("proc_total", "Total Processes");
		metricsMap.put("proc_run", "Total Running Processes");

		// network metrics display name.
		metricsMap.put("bytes_in", "Bytes Received");
		metricsMap.put("bytes_out", "Bytes Sent");
		metricsMap.put("pkts_in", "Packets Received");
		metricsMap.put("pkts_out", "Packets Sent");

		// process metrics display name
		metricsMap.put("swap_total", "Total Swap Memory");
		metricsMap.put("swap_free", "Free Swap Memory");

	}

	private static String getMetricsName(String fileName) {
		fileName = fileName.replace(".rrd", "");
		if (metricsMap.containsKey(fileName)) {
			return metricsMap.get(fileName);
		}
		return fileName;
	}

	public Graph(ClusterConfig clusterConf) throws AnkushException {
		this.authConf = clusterConf.getAuthConf();
		ComponentConfig gangliaConf = clusterConf.getComponents().get(
				Constant.Component.Name.GANGLIA);
		if (gangliaConf == null) {
			throw new AnkushException("Could not get "
					+ Constant.Component.Name.GANGLIA + " component");
		}
		this.host = (String) gangliaConf
				.getAdvanceConfStringProperty(GangliaConstants.ClusterProperties.GMETAD_HOST);
		this.rrdpath = FileNameUtils
				.convertToValidPath(gangliaConf
						.getAdvanceConfStringProperty(GangliaConstants.ClusterProperties.RRD_FILE_PATH))
				+ FileNameUtils
						.convertToValidPath(gangliaConf
								.getAdvanceConfStringProperty(GangliaConstants.ClusterProperties.GANGLIA_CLUSTER_NAME));

	}

	/**
	 * Gets the all files.
	 * 
	 * @param hostName
	 *            the hostName
	 * @return the all files
	 * @throws Exception
	 *             the exception
	 */
	public List<String> getAllFiles(String hostName) throws Exception {

		if (allFiles == null) {
			// node rrd folder.
			String nodeRrdDir = getRRDPath(hostName);
			String filesString = null;

			try {
				// file strings.
				filesString = exeCommand("cd " + nodeRrdDir + ";ls *.rrd");
			} catch (Exception e) {
				logger.error("Could not run command." + e.getMessage());
			}

			// if files string is null.
			if (filesString == null) {
				// throw exception
				throw new Exception("Could not fetch graph tree data.");
			}
			allFiles = new ArrayList<String>(Arrays.asList(filesString
					.split("\n")));

			allFiles.remove("cpu_speed.rrd");
			allFiles.remove("cpu_num.rrd");
			allFiles.remove("cpu_aidle.rrd");
		}
		return allFiles;
	}

	/**
	 * Method to get legends by pattern.
	 * 
	 * @param ip
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public List<String> getLegends(String ip, String pattern) throws Exception {
		return getMatchingFiles(pattern, getAllFiles(ip), false, true);
	}
	
	private String exeCommand(String command) throws Exception {
		// Create connection
		SSHConnection connection = new SSHConnection(host,
				authConf.getUsername(), authConf.getPassword(),
				authConf.getPrivateKey());
		/* Executing the command. */
		if (connection.exec(command)) {
			if (connection.getOutput() == null) {
				throw new AnkushException("Invalid command: " + command + "\n"
						+ "Error :" + connection.getError());
			}
		}
		return connection.getOutput();
	}

	private JSON exeExportCommand(String command) throws Exception {
		try {
			// puting json in result.
			return new XMLSerializer().read(exeCommand(command).replaceAll(
					"NaN", "0"));
		} catch (Exception e) {
			logger.error("Could not run export command." + e.getMessage());
			throw new AnkushException("Coud not run export command.");
		}
	}

	private String getClusterCpu(String legend) {
		return new StringBuilder()
				.append("DEF:num_nodes=cpu_user.rrd:num:AVERAGE ")
				.append("DEF:cpu_idle=cpu_idle.rrd:sum:AVERAGE ")
				.append("CDEF:cpu_usage=100,cpu_idle,num_nodes,/,- ")
				.append("XPORT:cpu_usage:\"").append(legend).append("\" ")
				.toString();
	}

	private String getClusterMemory(String legend) {
		return new StringBuilder().append("DEF:mt=mem_total.rrd:sum:AVERAGE ")
				.append("DEF:mf=mem_free.rrd:sum:AVERAGE ")
				.append("DEF:mb=mem_buffers.rrd:sum:AVERAGE ")
				.append("DEF:mc=mem_cached.rrd:sum:AVERAGE ")
				.append("DEF:ms=mem_shared.rrd:sum:AVERAGE ")
				.append("CDEF:mu=mt,ms,-,mf,-,mc,-,mb,- ")
				.append("CDEF:memory=100,mu,*,mt,/ ").append("XPORT:memory:\"")
				.append(legend).append("\" ").toString();
	}

	private String getClusterLoad(String legend) {
		return new StringBuilder()
				.append("DEF:cpu_num=cpu_num.rrd:sum:AVERAGE ")
				.append("DEF:load_one=load_one.rrd:sum:AVERAGE ")
				.append("CDEF:load=load_one,cpu_num,/,100,* ")
				.append("XPORT:load:\"").append(legend).append("\" ")
				.toString();
	}

	private String getClusterNetwork(String legend) {
		return new StringBuilder()
				.append("DEF:bytes_in=bytes_in.rrd:sum:AVERAGE ")
				.append("DEF:bytes_out=bytes_out.rrd:sum:AVERAGE ")
				.append("CDEF:bytes=bytes_in,bytes_out,+, ")
				.append("CDEF:kbs=bytes,1024,/ ")
				.append("CDEF:mbs=kbs,1024,/ ").append("XPORT:mbs:\"")
				.append(legend).append("\" ").toString();

	}

	private String getRRDPath(String host) {
		return rrdpath + (host != null ? host : "__SummaryInfo__");
	}

	private Map addDataList(String unit, List<Integer> list) {
		Map item = new HashMap();
		item.put("values", list);
		item.put("maxValue", Collections.max(list));
		item.put("minValues", Collections.min(list));
		item.put("unit", unit);
		return item;
	}

	private String getClusterCpuDef(List<String> files) {
		StringBuilder sb = new StringBuilder()
				.append("DEF:num_nodes=cpu_user.rrd:num:AVERAGE ")
				.append("DEF:cpu_user=cpu_user.rrd:sum:AVERAGE ")
				.append("DEF:cpu_nice=cpu_nice.rrd:sum:AVERAGE ")
				.append("DEF:cpu_system=cpu_system.rrd:sum:AVERAGE ")
				.append("DEF:cpu_idle=cpu_idle.rrd:sum:AVERAGE ")
				.append("CDEF:\"ccpu_user\"=cpu_user,num_nodes,/ ")
				.append("CDEF:\"ccpu_nice\"=cpu_nice,num_nodes,/ ")
				.append("CDEF:\"ccpu_system\"=cpu_system,num_nodes,/ ")
				.append("CDEF:\"ccpu_idle\"=cpu_idle,num_nodes,/ ");

		if (files.contains("cpu_wio.rrd")) {
			sb.append("DEF:\"cpu_wio\"=cpu_wio.rrd:sum:AVERAGE ").append(
					"CDEF:\"ccpu_wio\"=cpu_wio,num_nodes,/ ");
		}

		return sb.toString();
	}

	private String getClusterMemoryDef(List<String> files) {
		StringBuilder sb = new StringBuilder()
				.append("DEF:mem_total=mem_total.rrd:sum:AVERAGE ")
				.append("CDEF:cmem_total=mem_total,1024,* ")
				.append("DEF:mem_shared=mem_shared.rrd:sum:AVERAGE ")
				.append("CDEF:cmem_shared=mem_shared,1024,* ")
				.append("DEF:mem_free=mem_free.rrd:sum:AVERAGE ")
				.append("CDEF:cmem_free=mem_free,1024,* ")
				.append("DEF:mem_cached=mem_cached.rrd:sum:AVERAGE ")
				.append("CDEF:cmem_cached=mem_cached,1024,* ")
				.append("DEF:mem_buffers=mem_buffers.rrd:sum:AVERAGE ")
				.append("CDEF:cmem_buffers=mem_buffers,1024,* ")
				.append("CDEF:cmem_used=cmem_total,cmem_shared,-,cmem_free,-,cmem_cached,-,cmem_buffers,- ");

		if (files.contains("swap_total.rrd")) {
			sb.append("DEF:swap_total=swap_total.rrd:sum:AVERAGE ")
					.append("DEF:swap_free=swap_free.rrd:sum:AVERAGE ")
					.append("CDEF:cswap_total=swap_total,1024,* ")
					.append("CDEF:cswap_free=swap_free,1024,* ")
					.append("CDEF:cmem_swapped=cswap_total,cswap_free,- ");
		}
		return sb.toString();
	}

	private String getClusterLoadDef() {
		return new StringBuilder()
				.append("DEF:num_nodes=cpu_user.rrd:num:AVERAGE ")
				.append("DEF:cpu_num=cpu_num.rrd:sum:AVERAGE ")
				.append("DEF:proc_run=proc_run.rrd:sum:AVERAGE ")
				.append("DEF:load_one=load_one.rrd:sum:AVERAGE ").toString();
	}

	private String getClusterCpuXport(List<String> files) {
		// creating xport value.
		StringBuilder sb = new StringBuilder()
				.append("XPORT:ccpu_user:\"User CPU\" ")
				.append("XPORT:ccpu_nice:\"Nice CPU\" ")
				.append("XPORT:ccpu_system:\"System CPU\" ")
				.append("XPORT:ccpu_idle:\"Idle CPU\" ");

		if (files.contains("cpu_wio.rrd")) {
			sb.append("XPORT:ccpu_wio:\"WAIT CPU\" ");
		}
		return sb.toString();
	}

	private String getClusterMemoryXport(List<String> files) {
		// creating xport value.
		StringBuilder sb = new StringBuilder()
				.append("XPORT:cmem_shared:\"Memory Shared\" ")
				.append("XPORT:cmem_cached:\"Memory Cached\" ")
				.append("XPORT:cmem_buffers:\"Memory Buffered\" ")
				.append("XPORT:cmem_total:\"Total Memory\" ")
				.append("XPORT:cmem_used:\"Memory Used\" ");

		if (files.contains("swap_total.rrd")) {
			sb.append("XPORT:cmem_swapped:\"Memory Swapped\" ");
		}
		return sb.toString();
	}

	private String getClusterLoadXport() {
		return new StringBuilder().append("XPORT:load_one:\"1-Min Load\" ")
				.append("XPORT:num_nodes:\"Nodes\" ")
				.append("XPORT:proc_run:\"Running Processes\" ")
				.append("XPORT:cpu_num:\"CPUs\" ").toString();

	}

	public List<String> getMatchingFiles(String pattern, List<String> Allfiles,
			boolean format, boolean removeExtenstion) {
		List<String> files = new ArrayList<String>();
		Pattern p = Pattern.compile(pattern);
		for (String file : Allfiles) {
			Matcher m = p.matcher(file);
			if (m.matches()) {
				if (removeExtenstion) {
					file = file.replace(".rrd", "");
				}
				if (format) {
					files.add(file.replace("_", " "));
				} else {
					files.add(file);
				}
			}
		}
		return files;
	}

	public Map nodeGraph(StartTime startTime, String hostName) throws Exception {
		// json creation command using the rrdtool.

		StringBuilder command = new StringBuilder();
		command.append("cd ").append(getRRDPath(hostName))
				.append("/ ;rrdtool xport ")
				.append(timeMap.get(startTime.ordinal()))
				.append("DEF:cn=cpu_num.rrd:sum:AVERAGE ")
				.append("DEF:ci=cpu_idle.rrd:sum:AVERAGE ")
				.append("DEF:mt=mem_total.rrd:sum:AVERAGE ")
				.append("DEF:mf=mem_free.rrd:sum:AVERAGE ")
				.append("DEF:mb=mem_buffers.rrd:sum:AVERAGE ")
				.append("DEF:mc=mem_cached.rrd:sum:AVERAGE ")
				.append("DEF:ms=mem_shared.rrd:sum:AVERAGE ")
				.append("CDEF:mu=mt,ms,-,mf,-,mc,-,mb,- ")
				.append("CDEF:mem=100,mu,*,mt,/ ").append("CDEF:cpu=100,ci,- ")
				.append("XPORT:cpu:\"CPU %\" ")
				.append("XPORT:mem:\"Memory %\" ");

		Map result = new HashMap();
		result.put("json", exeExportCommand(command.toString()));
		return result;
	}

	public Map clusterGraph(StartTime startTime, Type type) throws Exception {
		Map result = new HashMap();
		result.put("unit", "%");

		StringBuilder command = new StringBuilder().append("cd ")
				.append(getRRDPath(null)).append(" ;rrdtool xport ")
				.append(timeMap.get(startTime.ordinal()));

		switch (type) {
		case cpu:
			command.append(getClusterCpu("CPU"));
			break;
		case memory:
			command.append(getClusterMemory("Memory"));
			break;
		case network:
			command.append(getClusterNetwork("Network"));
			result.put("unit", "MB/s");
			break;
		case load:
			command.append(getClusterLoad("Load"));
			break;
		default:
			throw new AnkushException("Invalid cluster graph type.");
		}

		result.put("json", exeExportCommand(command.toString()));
		return result;
	}

	public Map liveGraph(boolean update, String host) throws Exception {

		StringBuilder command = new StringBuilder()
				.append("cd ")
				.append(getRRDPath(host))
				.append(" ;rrdtool xport ")
				.append(update ? LIVE_DATA_UPDATE_INTERVAL : LIVE_DATA_INTERVAL)
				.append(getClusterCpu("CPU %"))
				.append(getClusterMemory("Memory %"))
				.append(getClusterNetwork("Network MB/s"))
				.append(getClusterLoad("Load %"));

		// get xport command result
		Map xportResult = (Map) exeExportCommand(command.toString());
		List<Map<String, Object>> xportData = (List<Map<String, Object>>) xportResult
				.get("data");
		List<String> values;

		// Create blank data list
		List<Integer> cpuData = new ArrayList<Integer>();
		List<Integer> memoryData = new ArrayList<Integer>();
		List<Integer> networkData = new ArrayList<Integer>();
		List<Integer> loadData = new ArrayList<Integer>();

		// Convert string into Integer values
		for (Map<String, Object> data : xportData) {
			values = (List<String>) data.get("v");

			// Add values into array list
			cpuData.add((int) Math.round(Double.parseDouble(values.get(0))));
			memoryData.add((int) Math.round(Double.parseDouble(values.get(1))));
			networkData
					.add((int) Math.round(Double.parseDouble(values.get(2))));
			loadData.add((int) Math.round(Double.parseDouble(values.get(3))));
		}

		// Create result
		Map result = new HashMap();

		// Add cpu data
		result.put("CPU", addDataList("%", cpuData));
		result.put("Memory", addDataList("%", memoryData));
		result.put("Network", addDataList("%", networkData));
		result.put("Load", addDataList("%", loadData));

		return result;

	}

	public Map getGraph(StartTime startTime, String pattern, String host)
			throws Exception {

		// Get file list from pattern
		String filesString = SSHUtils.getCommandOutput(
				"cd " + getRRDPath(host) + ";ls",
				this.host,
				authConf.getUsername(),
				(authConf.getPassword() != null
						&& !authConf.getPassword().isEmpty() ? authConf
						.getPassword() : authConf.getPrivateKey()),
				authConf.getPassword() != null
						&& !authConf.getPassword().isEmpty());
		List<String> files = new ArrayList<String>(Arrays.asList(filesString
				.split("\n")));

		// ???
		files.remove("cpu_speed.rrd");
		files.remove("cpu_num.rrd");
		files.remove("cpu_aidle.rrd");

		files = getMatchingFiles(pattern, files, false, false);

		String def = "", xport = "";
		int i = 1;

		for (String file : files) {

			// if metrics file start with cpu and its cluster graph call.
			if (file.startsWith("cpu_") && host == null) {
				def = getClusterCpuDef(files);
				xport = getClusterCpuXport(files);
				break;
			}

			// if metrics file start with mem and its cluster graph call.
			if (file.startsWith("mem_") && host == null) {
				def = getClusterMemoryDef(files);
				xport = getClusterMemoryXport(files);
				break;
			}

			// if metrics file start with load and its cluster graph call.
			if (file.startsWith("load_") && host == null) {
				def = getClusterLoadDef();
				xport = getClusterLoadXport();
				break;
			}

			// creating def.
			def += "DEF:a" + i + "=" + file.replace(" ", "\\ ")
					+ ":sum:AVERAGE ";
			// creating xport value.
			xport += "XPORT:a" + i++ + ":\"" + getMetricsName(file) + "\" ";
		}

		// json creation command using the rrdtool.
		StringBuilder command = new StringBuilder().append("cd ")
				.append(getRRDPath(host)).append(" ;rrdtool xport ")
				.append(timeMap.get(startTime.ordinal())).append(def)
				.append(xport);

		Map result = new HashMap();
		result.put("json", exeExportCommand(command.toString()));

		return result;
	}
}
