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
package com.impetus.ankush.common.zookeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.client.FourLetterWordMain;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.tiles.NewTileInfo;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.utils.AnkushLogger;

public class ZookeeperClusterMonitor extends AbstractMonitor{

	AnkushLogger logger = new AnkushLogger(ZookeeperClusterMonitor.class);

	/** The Constant COMMAND_MNTR. */
	private static final String COMMAND_MNTR = "mntr";

	/** The Constant COMMAND_SRVR. */
	private static final String COMMAND_SRVR = "srvr";

	/** The Constant COMMAND_CONF. */
	private static final String COMMAND_CONF = "conf";

	/** The Constant SERVER_ID. */
	private static final String SERVER_ID = "serverId";

	/** The Constant SERVER_TYPE. */
	private static final String SERVER_TYPE = "serverType";

	/** The Constant ZOOKEEPER_NODE_INFO. */
	private static final String ZOOKEEPER_NODE_INFO = "zookeeperNodeInfo";

	/** The Constant CASSANDRA_FOLDER_CONF. */
	private static final String ZOOKEEPER_FOLDER_CONF = "conf/";

	/** The Constant CASSANDRA_FILE_YAML. */
	private static final String ZOOKEEPER_FILE_CFG = "zoo.cfg";

	/** The Constant ZNODE_DATA. */
	private static final String ZNODE_DATA = "znodeData";

	private void nodelist(){

		try{
			// Getting Zookeeper conf.
			ZookeeperConf conf = (ZookeeperConf) dbCluster.getClusterConf()
					.getClusterComponents().get(Constant.Technology.ZOOKEEPER);

			int clientPort = conf.getClientPort();
			List<NodeConf> zookeeperNodes = conf.getNodes();
			List<Map> zookeeperNodeInfo = new ArrayList<Map>();

			for(NodeConf zookeeperNode : zookeeperNodes){
				String nodeType = execFourLetterCmd(zookeeperNode.getPublicIp(), COMMAND_SRVR ,clientPort);
				String serverId = execFourLetterCmd(zookeeperNode.getPublicIp(), COMMAND_CONF ,clientPort);
				Map<String, Object> zookeeperNodeData = new HashMap<String, Object>();
				zookeeperNodeData.put(com.impetus.ankush2.constant.Constant.Keys.NODEIP, zookeeperNode.getPublicIp());
				zookeeperNodeData.put(SERVER_ID, serverId);
				zookeeperNodeData.put(SERVER_TYPE, nodeType);
				zookeeperNodeInfo.add(zookeeperNodeData);
			}

			result.put(ZOOKEEPER_NODE_INFO, zookeeperNodeInfo);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	private void commandlist(){
		// Getting Zookeeper conf.
		ZookeeperConf conf = (ZookeeperConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.ZOOKEEPER);
		try {
			List<String> commandList = new ArrayList<String>();
			commandList.add("conf");
			commandList.add("cons");
			commandList.add("crst");
			commandList.add("dump");
			commandList.add("envi");
			commandList.add("ruok");
			commandList.add("srst");
			commandList.add("srvr");
			commandList.add("stat");
			commandList.add("wchs");
			commandList.add("wchc");
			commandList.add("wchp");
			
			List<String> versionList = new ArrayList<String>(
					Arrays.asList(conf.getComponentVersion().split("\\.")));
			if(versionList.get(1).equalsIgnoreCase("4")){
				commandList.add("mntr");
			}
			result.put(com.impetus.ankush2.constant.Constant.Keys.COMMAND, commandList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while getting command list: "+ e.getMessage());
		}
	}

	public void runFourLetterCommand(){

		// Getting the ip address from parameter map.
		String ip = (String) parameterMap.get(com.impetus.ankush2.constant.Constant.Keys.IP);

		// Getting the ip address from parameter map.
		String command = (String) parameterMap.get(com.impetus.ankush2.constant.Constant.Keys.COMMAND);

		// Getting Zookeeper conf.
		ZookeeperConf conf = (ZookeeperConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.ZOOKEEPER);

		int clientPort = conf.getClientPort();

		try {
			logger.info("Zookeeper 4 Letter Command Execution ...");
			String commandOutput = FourLetterWordMain.send4LetterWord(ip, clientPort, command);

			result.put(com.impetus.ankush2.constant.Constant.Keys.OUT, commandOutput);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while executing Zookeeper 4 Letter Command: "+ e.getMessage());
		}
	}
	
	public String execFourLetterCmd(String host, String command, int clientPort ) {
		try {
			System.out.println("Zookeeper 4 Letter Command Execution ...");
			String commandOutput = FourLetterWordMain.send4LetterWord(host, clientPort, command);

			List<String> sysoutList = new ArrayList<String>(Arrays.asList(commandOutput.split("\n")));
			String data = new String();

			String escapeCharacter;
			String parameterName;

			if(command.equalsIgnoreCase(COMMAND_MNTR)){
				escapeCharacter = "\t";
				parameterName = "zk_server_state";
			}else if(command.equalsIgnoreCase(COMMAND_SRVR)){
				escapeCharacter = ":";
				parameterName = "Mode";
			}else if(command.equalsIgnoreCase(COMMAND_CONF)){
				escapeCharacter = "=";
				parameterName = "serverId";
			}else{
				System.out.println("inside else" );
				escapeCharacter = " ";
				parameterName = " ";
			}

			for(String outData: sysoutList){

				if(outData.contains(escapeCharacter)){
					if(outData.split(escapeCharacter)[0].trim().equalsIgnoreCase(parameterName)){
						data = outData.split(escapeCharacter)[1].trim();
						logger.info("data: " + data);
					}
				}
			}

			return data;
			//			return FourLetterWordMain.send4LetterWord(host, clientPort, command);

		} catch (Exception e) {
			System.out.println("Error while executing Zookeeper 4 Letter Command: "+ e.getMessage());
		}
		return "";
	}

	@Override
	public List<TileInfo> compNodeTiles(Cluster dbCluster, Map parameterMap, String nodeIP) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewTileInfo> componentSummarizedTiles(Cluster dbCluster) {
		// TODO Auto-generated method stub
		return null;
	}

}
