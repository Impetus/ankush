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
package com.impetus.ankush.agent.sigar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Uptime;
import org.hyperic.sigar.Who;

import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.CommandExecutor;
import com.impetus.ankush.agent.utils.Result;

/**
 * The Class SigarNodeInfoProvider.
 * 
 * @author Hokam Chauhan
 */
public class SigarNodeInfoProvider {

	/** The log. */
	private static final AgentLogger LOGGER = new AgentLogger(
			SigarNodeInfoProvider.class);

	/** The Constant SLEEP. */
	public static final char SLEEP = 'S';

	/** The Constant RUN. */
	public static final char RUN = 'R';

	/** The Constant STOP. */
	public static final char STOP = 'T';

	/** The Constant ZOMBIE. */
	public static final char ZOMBIE = 'Z';

	/** The Constant IDLE. */
	public static final char IDLE = 'D';

	/** The Constant HUNDRED. */
	public static final int HUNDRED = 100;

	/** The Constant NUMBER_1024. */
	public static final int NUMBER_1024 = 1024;

	/** The sigar. */
	private Sigar sigar;

	private DecimalFormat dFormatter = new DecimalFormat("##.##");

	/**
	 * Instantiates a new sigar node info provider.
	 */
	public SigarNodeInfoProvider() {
		sigar = new Sigar();
	}

	/**
	 * Method getNodeMemoryInfo.
	 * 
	 * @return Map<Object,Object>
	 */
	public Map<Object, Object> getNodeMemoryInfo() {
		Map<Object, Object> nodeMemoryInfo = new HashMap<Object, Object>();
		Mem mem = null;

		try {
			mem = sigar.getMem();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		nodeMemoryInfo.put("total", mem.getTotal());
		nodeMemoryInfo.put("free", mem.getFree());
		nodeMemoryInfo.put("used", mem.getUsed());
		nodeMemoryInfo.put("actualFree", mem.getActualFree());
		nodeMemoryInfo.put("actualUsed", mem.getActualUsed());
		nodeMemoryInfo.put("freePercentage", mem.getFreePercent());
		nodeMemoryInfo.put("usedPercentage", mem.getUsedPercent());

		return nodeMemoryInfo;
	}

	/**
	 * Method getNodeCpuInfos.
	 * 
	 * @return List<Map<Object,Object>>
	 */
	public List<Map<Object, Object>> getNodeCpuInfos() {

		List<Map<Object, Object>> nodeCpuInfoList = new ArrayList<Map<Object, Object>>();
		Cpu[] cpuList = null;
		CpuInfo[] cpuInfoList = null;
		Map<Object, Object> nodeCpuInfo = null;

		try {
			cpuList = sigar.getCpuList();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		try {
			cpuInfoList = sigar.getCpuInfoList();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		for (int i = 0; i < cpuList.length; i++) {
			nodeCpuInfo = new HashMap<Object, Object>();
			nodeCpuInfo.put("vendor", cpuInfoList[i].getVendor());
			nodeCpuInfo.put("clock", cpuInfoList[i].getMhz());
			nodeCpuInfo.put("model", cpuInfoList[i].getModel());
			nodeCpuInfo.put("cores", cpuInfoList[i].getTotalCores());
			nodeCpuInfo.put("coresPerSocket",
					cpuInfoList[i].getCoresPerSocket());
			nodeCpuInfo.put("sockets", cpuInfoList[i].getTotalSockets());
			nodeCpuInfo.put("cacheSize", cpuInfoList[i].getCacheSize());
			nodeCpuInfo.put("idleTime", cpuList[i].getIdle());
			nodeCpuInfo.put("interruptHandlingTime", cpuList[i].getIrq());
			nodeCpuInfo.put("niceTime", cpuList[i].getNice());
			nodeCpuInfo.put("softRequestTime", cpuList[i].getSoftIrq());
			nodeCpuInfo.put("kernelTime", cpuList[i].getSys());
			nodeCpuInfo.put("cpuTime", cpuList[i].getTotal());
			nodeCpuInfo.put("userTime", cpuList[i].getUser());
			nodeCpuInfo.put("waitTime", cpuList[i].getWait());
			nodeCpuInfoList.add(nodeCpuInfo);
		}

		return nodeCpuInfoList;
	}

	/**
	 * Method getNodeSwapInfo.
	 * 
	 * @return Map<Object,Object>
	 */
	public Map<Object, Object> getNodeSwapInfo() {

		Swap swap = null;
		Map<Object, Object> nodeSwapInfo = new HashMap<Object, Object>();

		try {
			swap = sigar.getSwap();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		nodeSwapInfo.put("freeSystemSwap", swap.getFree());
		nodeSwapInfo.put("pageIn", swap.getPageIn());
		nodeSwapInfo.put("pageOut", swap.getPageOut());
		nodeSwapInfo.put("totalSystemSwap", swap.getTotal());
		nodeSwapInfo.put("usedSystemSwap", swap.getUsed());

		return nodeSwapInfo;
	}

	/**
	 * Method getNodeNetworkInfos.
	 * 
	 * @return List<Map<Object,Object>>
	 */
	public List<Map<Object, Object>> getNodeNetworkInfos() {

		List<Map<Object, Object>> nodeNetworkInfoList = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> nodeNetworkInfo = null;
		String[] netInterfaceList = null;
		NetInterfaceStat netInterfaceStat = null;

		try {
			netInterfaceList = sigar.getNetInterfaceList();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		for (String netInterface : netInterfaceList) {
			try {
				netInterfaceStat = sigar.getNetInterfaceStat(netInterface);

			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			nodeNetworkInfo = new HashMap<Object, Object>();
			nodeNetworkInfo.put("interfaceName", netInterface);
			nodeNetworkInfo.put("receivedBytes", netInterfaceStat.getRxBytes());
			nodeNetworkInfo.put("receivedDropped",
					netInterfaceStat.getRxDropped());
			nodeNetworkInfo.put("receivedErrors",
					netInterfaceStat.getRxErrors());
			nodeNetworkInfo.put("receivedFrame", netInterfaceStat.getRxFrame());
			nodeNetworkInfo.put("receivedOverruns",
					netInterfaceStat.getRxOverruns());
			nodeNetworkInfo.put("receivedPackets",
					netInterfaceStat.getRxPackets());
			nodeNetworkInfo.put("transmittedBytes",
					netInterfaceStat.getTxBytes());
			nodeNetworkInfo.put("transmittedDropped",
					netInterfaceStat.getTxDropped());
			nodeNetworkInfo.put("transmittedErrors",
					netInterfaceStat.getTxErrors());
			nodeNetworkInfo.put("transmittedCarrier",
					netInterfaceStat.getTxCarrier());
			nodeNetworkInfo.put("transmittedCollision",
					netInterfaceStat.getTxCollisions());
			nodeNetworkInfo.put("transmittedOverruns",
					netInterfaceStat.getTxOverruns());
			nodeNetworkInfo.put("transmittedPackets",
					netInterfaceStat.getTxPackets());
			nodeNetworkInfo.put("speed", netInterfaceStat.getSpeed());

			nodeNetworkInfoList.add(nodeNetworkInfo);
		}

		return nodeNetworkInfoList;
	}

	/**
	 * Method getNodeOSInfo.
	 * 
	 * @return Map<Object,Object>
	 */
	public Map<Object, Object> getNodeOSInfo() {
		OperatingSystem os = null;
		Map<Object, Object> nodeOSInfo = new HashMap<Object, Object>();

		os = OperatingSystem.getInstance();

		nodeOSInfo.put("arch", os.getArch());
		nodeOSInfo.put("cpuEndian", os.getCpuEndian());
		nodeOSInfo.put("dataModel", os.getDataModel());
		nodeOSInfo.put("description", os.getDescription());
		nodeOSInfo.put("machineName", os.getMachine());
		nodeOSInfo.put("patchlevel", os.getPatchLevel());
		nodeOSInfo.put("systemVersion", os.getVersion());
		nodeOSInfo.put("vendor", os.getVendor());
		nodeOSInfo.put("vendorCodeName", os.getVendorCodeName());
		nodeOSInfo.put("vendorVersion", os.getVendorVersion());

		return nodeOSInfo;
	}

	/**
	 * Method getNodeUpTimeInfo.
	 * 
	 * @return Map<Object,Object>
	 */
	public Map<Object, Object> getNodeUpTimeInfo() {

		Uptime upTime = null;
		Who[] who = null;
		Map<Object, Object> nodeUpTimeInfo = new HashMap<Object, Object>();
		double[] loadAverage = { 0, 0, 0 };

		try {
			upTime = sigar.getUptime();
			nodeUpTimeInfo.put("upTime", upTime.getUptime());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		try {
			who = sigar.getWhoList();
			String loggedUsers;
			List<String> userList = new ArrayList<String>();
			for (Who w : who) {
				userList.add(w.getUser());
			}
			loggedUsers = getDelimitedValues(userList, ",");

			nodeUpTimeInfo.put("loggedUsers", loggedUsers);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		try {
			loadAverage = sigar.getLoadAverage();
			nodeUpTimeInfo.put("loadAverage1", loadAverage[0]);
			nodeUpTimeInfo.put("loadAverage2", loadAverage[1]);
			nodeUpTimeInfo.put("loadAverage3", loadAverage[2]);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		nodeUpTimeInfo.put("cpuUsage", getCpuUsage());
		return nodeUpTimeInfo;
	}

	/**
	 * Gets the cpu usage.
	 * 
	 * @return the cpu usage
	 */
	private Object getCpuUsage() {
		try {
			// cpu percentage object.
			CpuPerc per = sigar.getCpuPerc();

			// getting the string value of percentage.
			String p = String.valueOf(per.getCombined() * 100.0);
			// getting index of the .
			int ix = p.indexOf('.') + 1;
			// getting only one digit after .
			return p.substring(0, ix) + p.substring(ix, ix + 1);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Method getDelimitedValues.
	 * 
	 * @param list
	 *            List
	 * @param delimiter
	 *            String
	 * @return String
	 */
	private static String getDelimitedValues(List list, String delimiter) {
		StringBuffer sBuf = new StringBuffer();
		int size = list.size();
		for (int index = 0; index < size; ++index) {
			if (index != 0) {
				sBuf.append(delimiter);
			}
			sBuf.append(list.get(index));
		}
		return sBuf.toString();
	}

	/**
	 * Method getNodeDiskInfos.
	 * 
	 * @return List<Map<Object,Object>>
	 */
	public List<Map<Object, Object>> getNodeDiskInfos() {

		List<Map<Object, Object>> nodeInfoList = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> nodeDiskInfo;
		FileSystem[] fileSystemList = null;
		FileSystemUsage fileSystemUsage = null;

		try {
			fileSystemList = sigar.getFileSystemList();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		for (FileSystem fileSystem : fileSystemList) {
			nodeDiskInfo = new HashMap<Object, Object>();
			nodeDiskInfo.put("deviceName", fileSystem.getDevName());
			nodeDiskInfo.put("dirName", fileSystem.getDirName());
			nodeDiskInfo.put("fileSystemEnvironmentType",
					fileSystem.getTypeName());
			nodeDiskInfo.put("fileSystemType", fileSystem.getSysTypeName());
			nodeDiskInfo.put("partitionFlags", fileSystem.getFlags());
			nodeDiskInfo.put("options", fileSystem.getOptions());

			fileSystemUsage = new FileSystemUsage();
			try {
				fileSystemUsage.gather(sigar, fileSystem.getDirName());

			} catch (SigarException e) {
				LOGGER.error(e.getMessage(), e);
			}
			nodeDiskInfo.put("availableMemory", fileSystemUsage.getAvail());
			nodeDiskInfo.put("freeMemory", fileSystemUsage.getFree());
			nodeDiskInfo.put("totalMemory", fileSystemUsage.getTotal());
			nodeDiskInfo.put("usedMemory", fileSystemUsage.getUsed());
			nodeDiskInfo.put("readBytes", fileSystemUsage.getDiskReadBytes());
			nodeDiskInfo.put("reads", fileSystemUsage.getDiskReads());
			nodeDiskInfo.put("writeBytes", fileSystemUsage.getDiskWriteBytes());
			nodeDiskInfo.put("writes", fileSystemUsage.getDiskWrites());

			nodeInfoList.add(nodeDiskInfo);
		}

		return nodeInfoList;
	}

	/**
	 * Method getProcessState.
	 * 
	 * @param state
	 *            char
	 * @return String
	 */
	private static String getProcessState(char state) {
		String strState;
		switch (state) {
		case SLEEP:
			strState = "Sleeping";
			break;
		case RUN:
			strState = "Running";
			break;
		case STOP:
			strState = "Terminated";
			break;
		case ZOMBIE:
			strState = "Zombie";
			break;
		case IDLE:
			strState = "Idle";
			break;
		default:
			strState = "Unknown";
			break;
		}
		return strState;
	}

	/**
	 * Method getNodeProcessInfo.
	 * 
	 * @return List<Map<Object,Object>>
	 */
	private List<Map<Object, Object>> getNodeProcessInfo() {
		List<Map<Object, Object>> nodeProcessInfos = new ArrayList<Map<Object, Object>>();
		try {
			long[] procIdList = sigar.getProcList();

			Mem mem = sigar.getMem();
			float totalMem = mem.getTotal() / NUMBER_1024;

			Result rs = new Result();

			for (long pid : procIdList) {
				try{

				ProcCpu procCpu = sigar.getProcCpu(pid);
				ProcMem procMem = sigar.getProcMem(pid);
				ProcState procState = sigar.getProcState(pid);
				String pName = procState.getName();
				double cpuUsage = 0;
				try {
					// executing jps command.
					String command = "ps -p " + pid + " -o%cpu";
					rs = CommandExecutor.executeCommand(command);
					if (rs.getExitVal() == 0) {
						String usage = rs.getOutput().split("\n")[1].trim();
						cpuUsage = Double.parseDouble(usage);
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
					cpuUsage = procCpu.getPercent();
				}

				double residentMemory = procMem.getResident() / NUMBER_1024;
				double sharedMemory = procMem.getShare() / NUMBER_1024;
				double virtualMemory = procMem.getSize() / NUMBER_1024;
				long threads = procState.getThreads();
				String state = getProcessState(procState.getState());
				double memUsage = (residentMemory / totalMem) * HUNDRED;
				Date startSince = new Date(procCpu.getStartTime());

				Map<Object, Object> nodeProcInfoStatus = new HashMap<Object, Object>();
				nodeProcInfoStatus.put("pid", pid);
				nodeProcInfoStatus.put("pName", pName);
				nodeProcInfoStatus.put("memUsage", dFormatter.format(memUsage));
				nodeProcInfoStatus.put("cpuUsage", cpuUsage);
				nodeProcInfoStatus.put("residentMemory", residentMemory);
				nodeProcInfoStatus.put("sharedMemory", sharedMemory);
				nodeProcInfoStatus.put("virtualMemory", virtualMemory);
				nodeProcInfoStatus.put("threads", threads);
				nodeProcInfoStatus.put("startSince", startSince);
				nodeProcInfoStatus.put("state", state);
				nodeProcInfoStatus.put("ioUsage", 0);
				nodeProcessInfos.add(nodeProcInfoStatus);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return nodeProcessInfos;
	}

	/**
	 * Method getNodeProcessCPUInfos.
	 * 
	 * @return List<Map<Object,Object>>
	 */
	public List<Map<Object, Object>> getSortedListByItem(final String item) {
		List<Map<Object, Object>> nodeProcessInfos = getNodeProcessInfo();
		try {
			Collections.sort(nodeProcessInfos,
					new Comparator<Map<Object, Object>>() {

						@Override
						public int compare(Map<Object, Object> arg1,
								Map<Object, Object> arg2) {
							Double d2 = Double.parseDouble(arg2.get(item)
									.toString());
							Double d1 = Double.parseDouble(arg1.get(item)
									.toString());
							return Double.compare(d2, d1);
						}

					});
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return nodeProcessInfos;
	}

	/**
	 * Get the Map of all metrics information.
	 * 
	 * @param topProcessCount
	 *            the top process count
	 * @return the node info
	 */
	public Map<String, Object> getNodeInfo(int topProcessCount) {

		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("memory", Collections.singletonList(getNodeMemoryInfo()));
		infoMap.put("cpu", getNodeCpuInfos());
		infoMap.put("disk", getNodeDiskInfos());
		infoMap.put("network", getNodeNetworkInfos());
		infoMap.put("swap", Collections.singletonList(getNodeSwapInfo()));
		infoMap.put("os", Collections.singletonList(getNodeOSInfo()));
		infoMap.put("uptime", Collections.singletonList(getNodeUpTimeInfo()));
		infoMap.put("processCPU",
				getSortedListByItem("cpuUsage").subList(0, topProcessCount));
		infoMap.put("processMemory",
				getSortedListByItem("memUsage").subList(0, topProcessCount));
		infoMap.put("processIO",
				getSortedListByItem("ioUsage").subList(0, topProcessCount));
		return infoMap;
	}
}
