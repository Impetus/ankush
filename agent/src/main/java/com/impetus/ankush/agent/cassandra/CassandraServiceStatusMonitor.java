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
package com.impetus.ankush.agent.cassandra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;

public class CassandraServiceStatusMonitor extends Taskable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(CassandraServiceStatusMonitor.class);
	/** The conf. */
	private AgentConf conf;

	/** The client. */
	private AgentRestClient client = new AgentRestClient();

	/**
	 * Constructor for ServiceStatusMonitor.
	 * 
	 */
	public CassandraServiceStatusMonitor() {
		this.conf = new AgentConf();
	}

	@Override
	public void start() {
		LOGGER.info("CassandraServiceStatusMonitor start");
		try {
			updateServiceStatusInfo();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Update service status info.
	 */
	private void updateServiceStatusInfo() {

		final Boolean isSeedNode = conf.getBooleanValue(Constant.PROP_SEEDNODE);

		if (isSeedNode) {
			long period = conf
					.getIntValue(Constant.PROP_NAME_COMMON_UPDATE_TIME);
			final String techDataUrl = conf
					.getURL(Constant.PROP_NAME_MONITORING_URL);
			final String hostIp = conf.getStringValue(Constant.HOST_IP);
			final Integer jmxPort = conf.getIntValue(Constant.JMX_PORT);
			final String cassandraHome = conf
					.getStringValue(Constant.CASSANDRA_HOME);
			LOGGER.info("inside seenode : " + hostIp);
			Runnable topologyThread = new Runnable() {

				@Override
				public void run() {
					try {
						Map map = getCassandraJMXData(hostIp.trim(), jmxPort);
						
						map.put("@class",
								"com.impetus.ankush.cassandra.CassandraJMXData");
						
						LOGGER.info("techDataUrl : " + techDataUrl);
						LOGGER.info("map : " + map);
						client.sendData(map, techDataUrl);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}

				}
			};
			service.scheduleAtFixedRate(topologyThread, 0, period,
					TimeUnit.SECONDS);
		}
		
	}
	
	private Map getCassandraJMXData(String hostIp, Integer jmxPort){
		try {
			
			CassandraJMXData data = new CassandraJMXData(
					hostIp, jmxPort);
			Map map = new ObjectMapper().convertValue(data,
					HashMap.class);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean checkJmxData(Map map){
		try {
			boolean check = false;
			Iterator entries = map.entrySet().iterator();
			while (entries.hasNext()) {
			    Map.Entry entry = (Map.Entry) entries.next();
			    if(entry.getKey().equals("datacenters")){
			    	if(!((List)entry.getValue()).isEmpty()){
			    		check = check||true; 
			    	}
			    }
			}
			return check;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

}
