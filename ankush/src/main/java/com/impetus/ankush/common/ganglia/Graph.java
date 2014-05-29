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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.xml.XMLSerializer;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.Graph.StartTime;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.SSHConnection;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class Graph.
 * 
 * @author hokam
 */
public class Graph {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(Graph.class);

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

	/** The password *. */
	private String password;

	/** The private *. */
	private String privateKey;

	/** The time map. */
	private static Map<Integer, String> timeMap = null;

	/** Is getting the cluster level graph *. */
	private boolean isClusterGraph = false;

	/** The all files. */
	private List<String> allFiles;

	// map for the time periods.
	static {
		timeMap = new HashMap<Integer, String>();
		timeMap.put(StartTime.lasthour.ordinal(), "-s now-1h ");
		timeMap.put(StartTime.lastday.ordinal(), "-s now-1d ");
		timeMap.put(StartTime.lastweek.ordinal(), "-s now-1w ");
		timeMap.put(StartTime.lastmonth.ordinal(), "-s now-1m ");
		timeMap.put(StartTime.lastyear.ordinal(), "-s now-1y ");
	}

	/** The time map. */
	private static Map<String, String> units = null;

	// filing units label map.
	static {
		units = new HashMap<String, String>();
		units.put("cpu", "%");
		units.put("mem", "KB");
		units.put("swap", "KB");
		units.put("disk_free", "GB");
		units.put("disk_total", "GB");
		units.put("bytes", "bytes/sec");
		units.put("pkts", "packets/sec");
	}

	/**
	 * Instantiates a new graph.
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
	public Graph(String hostname, String username, String password,
			String privateKey, String clusterName) {
		super();
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
	 * @param pattern
	 *            the pattern
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	public Map extractRRD(StartTime startTime, String pattern) throws Exception {
		isClusterGraph = true;

		// rrd cluster directory.
		String clusterRrdDir = FileNameUtils
				.convertToValidPath(Constant.Graph.RRD_BASH_PATH + clusterName
						+ "/__SummaryInfo__");

		return generateRrdJSON(startTime, pattern, clusterRrdDir);
	}

	/**
	 * Extract all rrd.
	 * 
	 * @param startTime
	 *            the start time
	 * @param pattern
	 *            the pattern
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	public Map extractAllRRD(StartTime startTime, String pattern)
			throws Exception {
		isClusterGraph = true;

		// rrd cluster directory.
		String clusterRrdDir = FileNameUtils
				.convertToValidPath(Constant.Graph.RRD_BASH_PATH + clusterName
						+ "/__SummaryInfo__");

		return generateAllRrdJSON(startTime, pattern, clusterRrdDir);
	}

	/**
	 * Method to extract json from the rrd file.
	 * 
	 * @param ip
	 *            the ip
	 * @param startTime
	 *            the start time
	 * @param pattern
	 *            the pattern
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	public Map extractRRD(String ip, StartTime startTime, String pattern)
			throws Exception {
		isClusterGraph = false;

		// node rrd folder.
		String nodeRrdDir = Constant.Graph.RRD_BASH_PATH + clusterName + "/"
				+ HostOperation.getAnkushHostName(ip) + "/";

		return generateRrdJSON(startTime, pattern, nodeRrdDir);
	}

	/**
	 * Generate rrd json.
	 * 
	 * @param startTime
	 *            the start time
	 * @param pattern
	 *            the pattern
	 * @param rrdDir
	 *            the rrd dir
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	private Map generateRrdJSON(StartTime startTime, String pattern,
			String rrdDir) throws Exception {
		int i = 1;
		// map
		Map result = new HashMap();
		// making connection.
		SSHConnection connection = new SSHConnection(this.hostname,
				this.username, this.authInfo, this.authUsingPassword);

		String filesString = SSHUtils.getCommandOutput("cd " + rrdDir + ";ls",
				this.hostname, this.username, this.authInfo,
				this.authUsingPassword);
		String def = "";
		String xport = "";

		List<String> files = new ArrayList<String>(Arrays.asList(filesString
				.split("\n")));

		files.remove("cpu_speed.rrd");
		files.remove("cpu_num.rrd");
		files.remove("cpu_aidle.rrd");

		files = getMatchingFiles(pattern, files, false, false);
		// assigning unit as empty.
		String unit = "";
		// iterating over the files to for def and port strings also assigning
		// the unit for the metric.
		for (String file : files) {
			// iterating over the units.
			for (String labelKey : units.keySet()) {
				// if the requested metrics has the same start as declared unit
				// key then assigning the units from units map.
				if (file.startsWith(labelKey)) {
					unit = units.get(labelKey);
					break;
				}
			}

			// if metrics file start with cpu and its cluster graph call.
			if (file.startsWith("cpu_") && isClusterGraph) {
				def = getClusterCpuDef(files);
				xport = getClusterCpuXport(files);
				break;
			}

			// if metrics file start with mem and its cluster graph call.
			if (file.startsWith("mem_") && isClusterGraph) {
				def = getClusterMemoryDef(files);
				xport = getClusterMemoryXport(files);
				unit = "Bytes";
				break;
			}

			// if metrics file start with load and its cluster graph call.
			if (file.startsWith("load_") && isClusterGraph) {
				def = getClusterLoadDef(files);
				xport = getClusterLoadXport(files);
				unit = "Load/Procs";
				break;
			}

			// getting file name.
			String name = file.replace(".rrd", "");

			// get display name
			String displayName = Constant.GangliaMetricsName
					.getMetricsDisplayName(name);
			// if display name is null then assign it to original name
			if (displayName == null) {
				displayName = name;
			}
			// creating def.
			def += "DEF:a" + i + "=" + file.replace(" ", "\\ ")
					+ ":sum:AVERAGE ";
			// creating xport value.
			xport += "XPORT:a" + i++ + ":\"" + displayName + "\" ";
		}
		if (connection.isConnected()) {

			// json creation command using the rrdtool.
			StringBuilder command = new StringBuilder();
			command.append("cd " + rrdDir).append(";rrdtool xport ")
					.append(timeMap.get(startTime.ordinal())).append(def)
					.append(xport);

			logger.debug(command.toString());

			/* Executing the command. */
			if (connection.exec(command.toString())) {
				String output = connection.getOutput();
				if (output == null) {
					throw new Exception("Unable to fetch graph.");
				}

				// puting json in result.
				Map map = (Map) new XMLSerializer().read(output.replaceAll(
						"NaN", "0"));
				map.put("unit", unit);
				result.put("json", map);
			}
		}
		return result;
	}

	/**
	 * Cluster load export string.
	 * 
	 * @param files
	 * @return
	 */
	private String getClusterLoadXport(List<String> files) {
		// creating xport value.
		StringBuilder xport = new StringBuilder();
		xport.append("XPORT:load_one:\"1-Min Load\" ");
		xport.append("XPORT:num_nodes:\"Nodes\" ");
		xport.append("XPORT:proc_run:\"Running Processes\" ");
		xport.append("XPORT:cpu_num:\"CPUs\" ");
		return xport.toString();
	}

	/**
	 * Cluster Load Definition.
	 * 
	 * @param files
	 * @return
	 */
	private String getClusterLoadDef(List<String> files) {
		StringBuilder def = new StringBuilder();
		def.append("DEF:num_nodes=cpu_user.rrd:num:AVERAGE ");
		def.append("DEF:cpu_num=cpu_num.rrd:sum:AVERAGE ");
		def.append("DEF:proc_run=proc_run.rrd:sum:AVERAGE ");
		def.append("DEF:load_one=load_one.rrd:sum:AVERAGE ");
		return def.toString();
	}

	/**
	 * Generate all rrd json.
	 * 
	 * @param startTime
	 *            the start time
	 * @param pattern
	 *            the pattern
	 * @param rrdDir
	 *            the rrd dir
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	private Map generateAllRrdJSON(StartTime startTime, String pattern,
			String rrdDir) throws Exception {
		int i = 1;
		// map
		Map result = new HashMap();
		// making connection.
		SSHConnection connection = new SSHConnection(this.hostname,
				this.username, this.authInfo, this.authUsingPassword);

		String filesString = SSHUtils.getCommandOutput("cd " + rrdDir
				+ ";ls ankush-*/*" + pattern + "*", this.hostname,
				this.username, this.authInfo, this.authUsingPassword);
		String def = "";
		String xport = "";

		List<String> files = new ArrayList<String>(Arrays.asList(filesString
				.split("\n")));

		for (String file : files) {
			// getting file name.
			String name = file.replace(".rrd", "");
			// creating def.
			def += "DEF:a" + i + "=" + file + ":sum:AVERAGE ";
			// creating xport value.
			xport += "XPORT:a" + i++ + ":\"" + name.replace("_", " ") + "\" ";
		}
		if (connection.isConnected()) {

			// json creation command using the rrdtool.
			StringBuilder command = new StringBuilder();
			command.append("cd " + rrdDir).append(";rrdtool xport ")
					.append(timeMap.get(startTime.ordinal())).append(def)
					.append(xport);

			logger.debug(command.toString());

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

	/**
	 * Gets the cluster cpu def.
	 * 
	 * @param files
	 *            the files
	 * @return the cluster cpu def
	 */
	private String getClusterCpuDef(List<String> files) {
		StringBuilder def = new StringBuilder();
		def.append("DEF:num_nodes=cpu_user.rrd:num:AVERAGE ");
		def.append("DEF:cpu_user=cpu_user.rrd:sum:AVERAGE ");
		def.append("DEF:cpu_nice=cpu_nice.rrd:sum:AVERAGE ");
		def.append("DEF:cpu_system=cpu_system.rrd:sum:AVERAGE ");
		def.append("DEF:cpu_idle=cpu_idle.rrd:sum:AVERAGE ");
		def.append("CDEF:\"ccpu_user\"=cpu_user,num_nodes,/ ");
		def.append("CDEF:\"ccpu_nice\"=cpu_nice,num_nodes,/ ");
		def.append("CDEF:\"ccpu_system\"=cpu_system,num_nodes,/ ");
		def.append("CDEF:\"ccpu_idle\"=cpu_idle,num_nodes,/ ");

		if (files.contains("cpu_wio.rrd")) {
			def.append("DEF:\"cpu_wio\"=cpu_wio.rrd:sum:AVERAGE ");
			def.append("CDEF:\"ccpu_wio\"=cpu_wio,num_nodes,/ ");
		}
		return def.toString();
	}

	/**
	 * Gets the cluster memory def.
	 * 
	 * @param files
	 *            the files
	 * @return the cluster memory def
	 */
	private String getClusterMemoryDef(List<String> files) {
		StringBuilder def = new StringBuilder();
		def.append("DEF:mem_total=mem_total.rrd:sum:AVERAGE ");
		def.append("CDEF:cmem_total=mem_total,1024,* ");
		def.append("DEF:mem_shared=mem_shared.rrd:sum:AVERAGE ");
		def.append("CDEF:cmem_shared=mem_shared,1024,* ");
		def.append("DEF:mem_free=mem_free.rrd:sum:AVERAGE ");
		def.append("CDEF:cmem_free=mem_free,1024,* ");
		def.append("DEF:mem_cached=mem_cached.rrd:sum:AVERAGE ");
		def.append("CDEF:cmem_cached=mem_cached,1024,* ");
		def.append("DEF:mem_buffers=mem_buffers.rrd:sum:AVERAGE ");
		def.append("CDEF:cmem_buffers=mem_buffers,1024,* ");
		def.append("CDEF:cmem_used=cmem_total,cmem_shared,-,cmem_free,-,cmem_cached,-,cmem_buffers,- ");

		if (files.contains("swap_total.rrd")) {
			def.append("DEF:swap_total=swap_total.rrd:sum:AVERAGE ");
			def.append("DEF:swap_free=swap_free.rrd:sum:AVERAGE ");
			def.append("CDEF:cswap_total=swap_total,1024,* ");
			def.append("CDEF:cswap_free=swap_free,1024,* ");
			def.append("CDEF:cmem_swapped=cswap_total,cswap_free,- ");
		}
		return def.toString();
	}

	/**
	 * Gets the cluster cpu xport.
	 * 
	 * @param files
	 *            the files
	 * @return the cluster cpu xport
	 */
	private String getClusterCpuXport(List<String> files) {
		// creating xport value.
		StringBuilder xport = new StringBuilder();
		xport.append("XPORT:ccpu_user:\"User CPU\" ");
		xport.append("XPORT:ccpu_nice:\"Nice CPU\" ");
		xport.append("XPORT:ccpu_system:\"System CPU\" ");
		xport.append("XPORT:ccpu_idle:\"Idle CPU\" ");

		if (files.contains("cpu_wio.rrd")) {
			xport.append("XPORT:ccpu_wio:\"WAIT CPU\" ");
		}
		return xport.toString();
	}

	/**
	 * Gets the cluster memory xport.
	 * 
	 * @param files
	 *            the files
	 * @return the cluster memory xport
	 */
	private String getClusterMemoryXport(List<String> files) {
		// creating xport value.
		StringBuilder xport = new StringBuilder();
		xport.append("XPORT:cmem_shared:\"Memory Shared\" ");
		xport.append("XPORT:cmem_cached:\"Memory Cached\" ");
		xport.append("XPORT:cmem_buffers:\"Memory Buffered\" ");
		xport.append("XPORT:cmem_total:\"Total Memory\" ");
		xport.append("XPORT:cmem_used:\"Memory Used\" ");

		if (files.contains("swap_total.rrd")) {
			xport.append("XPORT:cmem_swapped:\"Memory Swapped\" ");
		}
		return xport.toString();
	}

	/**
	 * Gets the all files.
	 * 
	 * @param ip
	 *            the ip
	 * @return the all files
	 * @throws Exception
	 *             the exception
	 */
	public List<String> getAllFiles(String ip) throws Exception {

		if (allFiles == null) {
			// node rrd folder.
			String nodeRrdDir = Constant.Graph.RRD_BASH_PATH + clusterName
					+ "/";

			if (ip == null) {
				nodeRrdDir = nodeRrdDir + "__SummaryInfo__/";
			} else {
				nodeRrdDir = nodeRrdDir + HostOperation.getAnkushHostName(ip);
			}

			// file strings.
			String filesString = SSHUtils.getCommandOutput("cd " + nodeRrdDir
					+ ";ls ", hostname, username, authInfo, authUsingPassword);

			if (filesString == null) {
				return null;
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

	/**
	 * Gets the matching files.
	 * 
	 * @param pattern
	 *            the pattern
	 * @param Allfiles
	 *            the allfiles
	 * @return the matching files
	 */
	public List<String> getMatchingFiles(String pattern, List<String> Allfiles) {
		return getMatchingFiles(pattern, Allfiles, true, true);
	}

	/**
	 * Gets the matching files.
	 * 
	 * @param pattern
	 *            the pattern
	 * @param Allfiles
	 *            the allfiles
	 * @param format
	 *            the format
	 * @return the matching files
	 */
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

	/**
	 * Gets the pattern files.
	 * 
	 * @param ip
	 *            the ip
	 * @param pattern
	 *            the pattern
	 * @return the pattern files
	 * @throws Exception
	 *             the exception
	 */
	public List<String> getPatternFiles(String ip, String pattern)
			throws Exception {

		// node rrd folder.
		String nodeRrdDir = Constant.Graph.RRD_BASH_PATH + clusterName + "/"
				+ HostOperation.getAnkushHostName(ip);

		// file strings.
		String filesString = SSHUtils.getCommandOutput("cd " + nodeRrdDir
				+ ";ls " + pattern, this.hostname, this.username,
				this.authInfo, this.authUsingPassword);

		if (filesString == null) {
			return null;
		}
		List<String> files = new ArrayList(Arrays.asList(filesString
				.split("\n")));
		List<String> legends = new ArrayList<String>();

		files.remove("cpu_speed.rrd");
		files.remove("cpu_num.rrd");
		files.remove("cpu_aidle.rrd");

		for (String file : files) {
			legends.add(file.replace(".rrd", "").replace("_", " "));
		}

		return legends;
	}
}
