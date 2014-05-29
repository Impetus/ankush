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
package com.impetus.ankush.agent.hadoop;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;

/**
 * @author Akhil
 *
 */
public class UpdateNameNodeRole extends Taskable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(UpdateNameNodeRole.class);
	// agent conf.
	private AgentConf conf;
	// rest client.
	private AgentRestClient restClient;

	/**
	 * constructor.
	 */
	public UpdateNameNodeRole() {
		conf = new AgentConf();
		restClient = new AgentRestClient();
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.agent.action.Taskable#start()
	 */
	@Override
	public void start() {
		LOGGER.info("UpdateNameNodeRole Start");
		
		// namenode ip.
		final String nameNodeIp = conf
				.getStringValue(Constant.PROP_NAMENODE_HOST);

		// monitoring ankush url.
		final String url = conf.getURL(Constant.PROP_NAME_NAMENODE_ROLE);

		// job status update time.
		long period = conf
				.getIntValue(Constant.PROP_NAME_HADOOP_DATA_UPDATE_TIME);

		final String jmxDataServicePort = conf
				.getStringValue(Constant.PROP_NAMENODE_HTTP_PORT);

		Runnable monitorThread = new Runnable() {

			@Override
			public void run() {
				try {
					LOGGER.info("Monitor Thread");
					// getting json object.
					JSONObject objectData = getNameNodeRoleData(nameNodeIp, jmxDataServicePort);
					// sending the data.
					restClient.sendData(objectData, url);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		};
		service.scheduleAtFixedRate(monitorThread, 0, period, TimeUnit.SECONDS);
	}

	private JSONObject getNameNodeRoleData(String nameNodeIp, String jmxDataServicePort) {
		JSONObject data = new JSONObject();
		String beanNameFsNameSysten = "Hadoop:service=NameNode,name=FSNamesystem";
		try {
			AgentRestClient restClient = new AgentRestClient();
			// rest url for cluster metrics.
			String url = "http://localhost:" + jmxDataServicePort
					+ "/jmx?qry=" + beanNameFsNameSysten;
			// getting the metrics data from rest.
			String nameNodeData = restClient.getRequest(url);
			JSONObject jsonNameNodeData = (JSONObject) new JSONParser().parse(nameNodeData);
			
			List<JSONObject> beans = (List<JSONObject>) jsonNameNodeData.get("beans");
			JSONObject nnInfo = beans.get(0);
			String nnState = nnInfo.get("tag.HAState").toString();
			data.put("nnrole", nnState.toUpperCase());
			
		} catch (Exception e) {
			data.put("nnrole", "NULL");
			// logging error.
			LOGGER.error(e.getMessage(), e);
		}
		
		
		return data;
	}
}
