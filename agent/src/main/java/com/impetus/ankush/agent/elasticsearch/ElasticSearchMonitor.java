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
package com.impetus.ankush.agent.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;

/**
 * The Class ElasticSearchMonitor.
 */
public class ElasticSearchMonitor extends Taskable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(ElasticSearchMonitor.class);
	
	/** The agent conf. */
	private AgentConf agentConf;

	/** The rest client. */
	private AgentRestClient restClient;
	
	/** The http port. */
	private int httpPort;

	/**
	 * Instantiates a new elastic search monitor.
	 */
	public ElasticSearchMonitor() {
		agentConf = new AgentConf();
		restClient = new AgentRestClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.agent.action.Taskable#start()
	 */
	@Override
	public void start() {
		// httpPort of ElasticSearch
		httpPort = agentConf.getIntValue("HTTP_PORT");

		// period after which cluster data will be updated.
		final int period = agentConf.getIntValue("UPDATE_CLUSTER_DATA_TIME");

		// monitoring ankush url.
		final String url = agentConf.getURL(Constant.PROP_NAME_MONITORING_URL);

		Runnable monitorThread = new Runnable() {
			@Override
			public void run() {
				try {
					LOGGER.info("ElasticSearch Monitor Thread");

					ElasticSearchMonitoringData esMonitoringData = getESMonitoringDetail();
					Map map = new ObjectMapper().convertValue(esMonitoringData,
							HashMap.class);
					map.put("@class",
							"com.impetus.ankush.elasticsearch.ElasticSearchMonitoringData");
					restClient.sendData(map, url);
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (ParseException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		};
		// start the taskable service.
		service.scheduleAtFixedRate(monitorThread, 0, period, TimeUnit.SECONDS);
	}

	/**
	 * Gets the eS monitoring detail.
	 *
	 * @return the eS monitoring detail
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private ElasticSearchMonitoringData getESMonitoringDetail()
			throws IOException, ParseException {
		ElasticSearchMonitoringData esMonitoringData = new ElasticSearchMonitoringData();
		Map<String, Object> stats = getStats();
		esMonitoringData.setStats(stats);
		esMonitoringData.setClusterHealth(getClusterHealth());
		JSONObject clusterState = getClusterState();
		esMonitoringData.setClusterState(clusterState);
		esMonitoringData.setClusterStats(getClusterStats());
		esMonitoringData
				.setPendingClusterTasks(getPendingClusterTasks());
		esMonitoringData.setStatus(getStatus());
		esMonitoringData.setAliases(getAliases());
		esMonitoringData.setNodesStats(getNodesStats());
		esMonitoringData.setNodesInfo(getNodesInfo());
		esMonitoringData.setIndicesDetail(getIndicesDetailMap(stats));

		return esMonitoringData;
	}


	/**
	 * Gets the cluster health.
	 *
	 * @return the cluster health
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private JSONObject getClusterHealth() throws IOException,
			ParseException {
		LOGGER.info("Going to fetch Cluster Health data");
		// rest url for cluster health.
		String url = "http://localhost:" + this.httpPort + "/_cluster/health";
		// getting the data from rest.
		String data = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject jsonObject = (JSONObject) new JSONParser()
				.parse(data);
		return jsonObject;
	}

	/**
	 * Gets the cluster health.
	 *
	 * @return the cluster health
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private JSONObject getStats() throws IOException,
			ParseException {
		LOGGER.info("Going to fetch Stats");
		// rest url for cluster stats.
		String url = "http://localhost:" + this.httpPort + "/_stats";
		// getting the data from rest.
		String data = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject jsonObject = (JSONObject) new JSONParser()
				.parse(data);
		return jsonObject;
	}

	/**
	 * Gets the cluster state.
	 *
	 * @return the cluster state
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private JSONObject getClusterState() throws IOException,
			ParseException {
		LOGGER.info("Going to fetch Cluster State data");
		// rest url for cluster state.
		String url = "http://localhost:" + this.httpPort + "/_cluster/state";
		// getting the data from rest.
		String data = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject jsonObject = (JSONObject) new JSONParser()
				.parse(data);
		return jsonObject;
	}

	/**
	 * Gets the cluster stats.
	 *
	 * @return the cluster stats
	 */
	private JSONObject getClusterStats() {
		LOGGER.info("Going to fetch Cluster Stats data");
		// rest url for cluster stats.
		String url = "http://localhost:" + this.httpPort + "/_cluster/stats";
		JSONObject jsonObject = null;
		try {
			// getting the data from rest.
			String data = restClient.getRequest(url);

			// parsing string data in json object.
			jsonObject = (JSONObject) new JSONParser().parse(data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return jsonObject;
	}

	/**
	 * Gets the pending cluster tasks.
	 *
	 * @return the pending cluster tasks
	 */
	private JSONObject getPendingClusterTasks() {
		LOGGER.info("Going to fetch Pending Cluster Tasks  data");
		// rest url for pending cluster tasks.
		String url = "http://localhost:" + this.httpPort + "/_cluster/pending_tasks";
		JSONObject jsonObject = null;
		try {
			// getting the data from rest.
			String data = restClient.getRequest(url);

			// parsing string data in json object.
			jsonObject = (JSONObject) new JSONParser().parse(data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return jsonObject;
	}

	/**
	 * Gets the aliases.
	 *
	 * @return the aliases
	 */
	private JSONObject getAliases() {
		LOGGER.info("Going to fetch Aliases data");
		// rest url for aliases.
		String url = "http://localhost:" + this.httpPort + "/_aliases";
		JSONObject jsonObject = null;
		try {
			// getting the data from rest.
			String data = restClient.getRequest(url);

			// parsing string data in json object.
			jsonObject = (JSONObject) new JSONParser().parse(data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return jsonObject;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	private JSONObject getStatus() {
		LOGGER.info("Going to fetch Status data");
		// rest url for aliases.
		String url = "http://localhost:" + this.httpPort + "/_status";
		JSONObject jsonObject = null;
		try {
			// getting the data from rest.
			String data = restClient.getRequest(url);

			// parsing string data in json object.
			jsonObject = (JSONObject) new JSONParser().parse(data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return jsonObject;
	}

	/**
	 * Gets the nodes stats.
	 *
	 * @return the nodes stats
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private JSONObject getNodesStats() throws IOException,
			ParseException {
		LOGGER.info("Going to fetch Nodes Stats data");
		// rest url for nodes stats.
		String url = "http://localhost:"
				+ this.httpPort
				+ "/_cluster/nodes/stats?os&process&jvm&thread_pool&network&transport&http&fs";
		// getting the metrics data from rest.
		String data = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject jsonObject = (JSONObject) new JSONParser()
				.parse(data);
		return jsonObject;
	}

	/**
	 * Gets the nodes info.
	 *
	 * @return the nodes info
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	private JSONObject getNodesInfo() throws IOException,
			ParseException {
		LOGGER.info("Going to fetch Nodes Stats data");
		// rest url for nodes info.
		String url = "http://localhost:"
				+ this.httpPort
				+ "/_cluster/nodes?os&process&jvm&thread_pool&network&transport&http&plugin";
		// getting the data from rest.
		String data = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject jsonObject = (JSONObject) new JSONParser()
				.parse(data);
		return jsonObject;
	}

	/**
	 * Gets the indices detail map.
	 *
	 * @param stats the stats
	 * @return the indices detail map
	 * @throws ParseException the parse exception
	 */
	private Map<String, HashMap<String, Object>> getIndicesDetailMap(
			Map<String, Object> stats) throws ParseException {
		Map<String, Object> indices = (Map<String, Object>) stats
				.get("indices");
		Map<String, HashMap<String, Object>> indexDetailMap = new HashMap<String, HashMap<String, Object>>();
		String indicesString = "";
		if (indices.size() != 0) {
			for (String index : indices.keySet()) {
				String urlForStatus = "http://localhost:" + this.httpPort + "/"
						+ index + "/_status";
				String urlForStats = "http://localhost:" + this.httpPort + "/"
						+ index + "/_stats";
				String statusData = restClient.getRequest(urlForStatus);
				String statsData = restClient.getRequest(urlForStats);
				JSONObject indexStatusData = (JSONObject) new JSONParser()
						.parse(statusData);
				JSONObject indexStatsData = (JSONObject) new JSONParser()
						.parse(statsData);
				Map<String, Object> indexMapStatus = (Map<String, Object>) ((Map<String, Object>) indexStatusData
						.get("indices")).get(index);
				Map<String, Object> indexMapStats = (Map<String, Object>) ((Map<String, Object>) indexStatsData
						.get("indices")).get(index);
				Map<String, Object> shardsMapStats = (Map<String, Object>) indexStatsData
						.get("_shards");
				indexDetailMap.put(
						index,
						getIndexInfo(index,indexMapStatus,indexMapStats, shardsMapStats));
			}
		}
		return indexDetailMap;
	}

	/**
	 * Gets the index info.
	 *
	 * @param index the index
	 * @param indexStatusData the index status data
	 * @param indexStatsData the index stats data
	 * @param shardsMapStats the shards map stats
	 * @return the index info
	 * @throws ParseException the parse exception
	 */
	private HashMap<String, Object> getIndexInfo(String index,Map<String, Object> indexStatusData,
			Map<String, Object> indexStatsData,
			Map<String, Object> shardsMapStats) throws ParseException {
		HashMap<String, Object> indexMap = new HashMap<String, Object>();
		indexMap.put("tiles", indexTileData(indexStatsData, shardsMapStats));
		indexMap.put("health", indexHealth(index));
		indexMap.put("documents",
				indexDocument(index,indexStatusData));
		indexMap.put("operations", indexOperation(indexStatusData));
		indexMap.put("mergeActivity", indexMergeActivity(indexStatusData));
		indexMap.put("searchTotal", indexSearchTotal(indexStatsData));
		indexMap.put("getTotal", indexGetTotal(indexStatsData));
		indexMap.put("indexingTotal", indexIndexingTotal(indexStatsData));
		indexMap.put("shards", indexShards(indexStatusData));
		indexMap.put("aliases", indexAliases(index));
		return indexMap;
	}

	/**
	 * Index health.
	 *
	 * @param index the index
	 * @return the hash map
	 * @throws ParseException the parse exception
	 */
	private HashMap<String, Object> indexHealth(String index)
			throws ParseException {
		HashMap<String, Object> indexHealthMap = new HashMap<String, Object>();
		String url = "http://localhost:" + this.httpPort + "/_cluster/health/"
				+ index;
		String data = restClient.getRequest(url);
		JSONObject indexHealthData = (JSONObject) new JSONParser().parse(data);
		indexHealthMap.put(
				Constant.ElasticSearch.ClusterHealth_Table_Keys.STATUS,
				indexHealthData
						.get(Constant.ElasticSearch.ClusterHealth_Keys.STATUS));
		indexHealthMap
				.put(Constant.ElasticSearch.ClusterHealth_Table_Keys.NODES,
						indexHealthData
								.get(Constant.ElasticSearch.ClusterHealth_Keys.NUMBER_OF_NODES));
		indexHealthMap
				.put(Constant.ElasticSearch.ClusterHealth_Table_Keys.DATA_NODES,
						indexHealthData
								.get(Constant.ElasticSearch.ClusterHealth_Keys.NUMBER_OF_DATA_NODES));
		indexHealthMap
				.put(Constant.ElasticSearch.ClusterHealth_Table_Keys.PRIMARY_SHARDS,
						indexHealthData
								.get(Constant.ElasticSearch.ClusterHealth_Keys.ACTIVE_PRIMARY_SHARDS));
		indexHealthMap
				.put(Constant.ElasticSearch.ClusterHealth_Table_Keys.ACTIVE_SHARDS,
						indexHealthData
								.get(Constant.ElasticSearch.ClusterHealth_Keys.ACTIVE_SHARDS));
		indexHealthMap
				.put(Constant.ElasticSearch.ClusterHealth_Table_Keys.RELOCATING_SHARDS,
						indexHealthData
								.get(Constant.ElasticSearch.ClusterHealth_Keys.RELOCATING_SHARDS));
		indexHealthMap
				.put(Constant.ElasticSearch.ClusterHealth_Table_Keys.INITIALIZING_SHARDS,
						indexHealthData
								.get(Constant.ElasticSearch.ClusterHealth_Keys.INITIALIZING_SHARDS));
		indexHealthMap
				.put(Constant.ElasticSearch.ClusterHealth_Table_Keys.UNASSIGNED_SHARDS,
						indexHealthData
								.get(Constant.ElasticSearch.ClusterHealth_Keys.UNASSIGNED_SHARDS));

		return indexHealthMap;
	}

	/**
	 * Index document.
	 *
	 * @param index the index
	 * @param indexStatusData the index status data
	 * @return the hash map
	 * @throws ParseException the parse exception
	 */
	private HashMap<String, Object> indexDocument(String index,	Map<String, Object> indexStatusData) throws ParseException {
		HashMap<String, Object> documentMap = new HashMap<String, Object>();
		HashMap<String, Object> indexDocumentData = (HashMap<String, Object>) indexStatusData
				.get("docs");
		HashMap<String, Object> indexIndexData = (HashMap<String, Object>) indexStatusData
				.get("index");

		documentMap.put(Constant.ElasticSearch.Documents_Table_Keys.DOCS,
				indexDocumentData
						.get(Constant.ElasticSearch.Document_Keys.NUM_DOCS));
		documentMap.put(Constant.ElasticSearch.Documents_Table_Keys.MAX_DOCS,
				indexDocumentData
						.get(Constant.ElasticSearch.Document_Keys.MAX_DOCS));
		documentMap
				.put(Constant.ElasticSearch.Documents_Table_Keys.DELETED_DOCS,
						indexDocumentData
								.get(Constant.ElasticSearch.Document_Keys.DELETED_DOCS));
		documentMap
				.put(Constant.ElasticSearch.Documents_Table_Keys.PRIMARY_SIZE,
						indexIndexData
								.get(Constant.ElasticSearch.Document_Keys.PRIMARY_SIZE));
		documentMap.put(Constant.ElasticSearch.Documents_Table_Keys.TOTAL_SIZE,
				indexIndexData.get(Constant.ElasticSearch.Document_Keys.SIZE));
		return documentMap;
	}

	/**
	 * Index operation.
	 *
	 * @param indexStatusData the index status data
	 * @return the hash map
	 */
	private HashMap<String, Object> indexOperation(
			Map<String, Object> indexStatusData) {
		HashMap<String, Object> operationMap = new HashMap<String, Object>();
		HashMap<String, Object> indexRefreshData = (HashMap<String, Object>) indexStatusData
				.get("refresh");
		HashMap<String, Object> indexFlushData = (HashMap<String, Object>) indexStatusData
				.get("flush");
		operationMap.put(
				Constant.ElasticSearch.Operations_Table_Keys.REFRESH_TOTAL,
				indexRefreshData
						.get(Constant.ElasticSearch.Operations_Keys.TOTAL));
		operationMap
				.put(Constant.ElasticSearch.Operations_Table_Keys.REFRESH_TIME,
						indexRefreshData
								.get(Constant.ElasticSearch.Operations_Keys.TOTAL_TIME));
		operationMap.put(
				Constant.ElasticSearch.Operations_Table_Keys.FLUSH_TOTAL,
				indexFlushData
						.get(Constant.ElasticSearch.Operations_Keys.TOTAL));
		operationMap
				.put(Constant.ElasticSearch.Operations_Table_Keys.FLUSH_TIME,
						indexFlushData
								.get(Constant.ElasticSearch.Operations_Keys.TOTAL_TIME));
		return operationMap;
	}

	/**
	 * Index merge activity.
	 *
	 * @param indexStatusData the index status data
	 * @return the hash map
	 */
	private HashMap<String, Object> indexMergeActivity(
			Map<String, Object> indexStatusData) {
		HashMap<String, Object> mergeActivityMap = new HashMap<String, Object>();
		HashMap<String, Object> indexMergeData = (HashMap<String, Object>) indexStatusData
				.get("merges");
		mergeActivityMap.put(
				Constant.ElasticSearch.MergeActivity_Table_Keys.MERGE_TOTAL,
				indexMergeData
						.get(Constant.ElasticSearch.MergeActivity_Keys.TOTAL));
		mergeActivityMap
				.put(Constant.ElasticSearch.MergeActivity_Table_Keys.MERGE_TOTAL_TIME,
						indexMergeData
								.get(Constant.ElasticSearch.MergeActivity_Keys.TOTAL_TIME));
		mergeActivityMap
				.put(Constant.ElasticSearch.MergeActivity_Table_Keys.MERGE_TOTAL_DOCS,
						indexMergeData
								.get(Constant.ElasticSearch.MergeActivity_Keys.TOTAL_DOCS));
		mergeActivityMap
				.put(Constant.ElasticSearch.MergeActivity_Table_Keys.MERGE_TOTAL_SIZE,
						indexMergeData
								.get(Constant.ElasticSearch.MergeActivity_Keys.TOTAL_SIZE));
		return mergeActivityMap;
	}

	/**
	 * Index search total.
	 *
	 * @param indexStatsData the index stats data
	 * @return the hash map
	 */
	private HashMap<String, Object> indexSearchTotal(
			Map<String, Object> indexStatsData) {
		HashMap<String, Object> searchTotalMap = new HashMap<String, Object>();
		HashMap<String, Object> indexSearchTotalData = (HashMap<String, Object>) ((HashMap<String, Object>) indexStatsData
				.get("total")).get("search");
		searchTotalMap.put(
				Constant.ElasticSearch.SearchTotal_Table_Keys.QUERY_TOTAL,
				indexSearchTotalData
						.get(Constant.ElasticSearch.SearchTotal.QUERY_TOTAL));
		searchTotalMap.put(
				Constant.ElasticSearch.SearchTotal_Table_Keys.QUERY_TIME,
				indexSearchTotalData
						.get(Constant.ElasticSearch.SearchTotal.QUERY_TIME));
		searchTotalMap.put(
				Constant.ElasticSearch.SearchTotal_Table_Keys.FETCH_TOTAL,
				indexSearchTotalData
						.get(Constant.ElasticSearch.SearchTotal.FETCH_TOTAL));
		searchTotalMap.put(
				Constant.ElasticSearch.SearchTotal_Table_Keys.FETCH_TIME,
				indexSearchTotalData
						.get(Constant.ElasticSearch.SearchTotal.FETCH_TIME));
		return searchTotalMap;
	}

	/**
	 * Index get total.
	 *
	 * @param indexStatsData the index stats data
	 * @return the hash map
	 */
	private HashMap<String, Object> indexGetTotal(
			Map<String, Object> indexStatsData) {
		HashMap<String, Object> getTotalMap = new HashMap<String, Object>();
		HashMap<String, Object> indexGetTotalData = (HashMap<String, Object>) ((HashMap<String, Object>) indexStatsData
				.get("total")).get("get");
		getTotalMap.put(Constant.ElasticSearch.GetTotal_Table_Keys.GET_TOTAL,
				indexGetTotalData
						.get(Constant.ElasticSearch.GetTotal_Keys.TOTAL));
		getTotalMap.put(Constant.ElasticSearch.GetTotal_Table_Keys.GET_TIME,
				indexGetTotalData
						.get(Constant.ElasticSearch.GetTotal_Keys.GET_TIME));
		getTotalMap
				.put(Constant.ElasticSearch.GetTotal_Table_Keys.EXISTS_TOTAL,
						indexGetTotalData
								.get(Constant.ElasticSearch.GetTotal_Keys.EXISTS_TOTAL));
		getTotalMap.put(Constant.ElasticSearch.GetTotal_Table_Keys.EXISTS_TIME,
				indexGetTotalData
						.get(Constant.ElasticSearch.GetTotal_Keys.EXISTS_TIME));
		getTotalMap
				.put(Constant.ElasticSearch.GetTotal_Table_Keys.MISSING_TOTAL,
						indexGetTotalData
								.get(Constant.ElasticSearch.GetTotal_Keys.MISSING_TOTAL));
		getTotalMap
				.put(Constant.ElasticSearch.GetTotal_Table_Keys.MISSING_TIME,
						indexGetTotalData
								.get(Constant.ElasticSearch.GetTotal_Keys.MISSING_TIME));
		return getTotalMap;
	}

	/**
	 * Index indexing total.
	 *
	 * @param indexStatsData the index stats data
	 * @return the hash map
	 */
	private HashMap<String, Object> indexIndexingTotal(
			Map<String, Object> indexStatsData) {
		HashMap<String, Object> indexTotalMap = new HashMap<String, Object>();
		HashMap<String, Object> indexIndexTotalData = (HashMap<String, Object>) ((HashMap<String, Object>) indexStatsData
				.get("total")).get("indexing");
		indexTotalMap.put(
				Constant.ElasticSearch.IndexingTotal_Table_Keys.INDEX_TOTAL,
				indexIndexTotalData
						.get(Constant.ElasticSearch.IndexingTotal.INDEX_TOTAL));
		indexTotalMap.put(
				Constant.ElasticSearch.IndexingTotal_Table_Keys.INDEX_TIME,
				indexIndexTotalData
						.get(Constant.ElasticSearch.IndexingTotal.INDEX_TIME));
		indexTotalMap
				.put(Constant.ElasticSearch.IndexingTotal_Table_Keys.DELETE_TOTAL,
						indexIndexTotalData
								.get(Constant.ElasticSearch.IndexingTotal.DELETE_TOTAL));
		indexTotalMap.put(
				Constant.ElasticSearch.IndexingTotal_Table_Keys.DELETE_TIME,
				indexIndexTotalData
						.get(Constant.ElasticSearch.IndexingTotal.DELETE_TIME));
		return indexTotalMap;
	}

	/**
	 * Index shards.
	 *
	 * @param indexStatusData the index status data
	 * @return the list
	 */
	private List<Map<String, Object>> indexShards(
			Map<String, Object> indexStatusData) {
		List<Map<String, Object>> listToReturn = new ArrayList<Map<String, Object>>();
		Map<String, Object> shardsList = (Map<String, Object>) indexStatusData
				.get("shards");
		for (int i = 0; i < shardsList.size(); i++) {
			List list = (List) shardsList.get(i + "");
			Map<String, Object> shards = (Map<String, Object>) list.get(0);
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put(Constant.ElasticSearch.Shard_Table_Keys.SHARD,
					((Map<String, Object>) shards
							.get(Constant.ElasticSearch.Shard_Keys.ROUTING))
							.get(Constant.ElasticSearch.Shard_Keys.SHARD));
			newMap.put(Constant.ElasticSearch.Shard_Table_Keys.STATE,
					((Map<String, Object>) shards
							.get(Constant.ElasticSearch.Shard_Keys.ROUTING))
							.get(Constant.ElasticSearch.Shard_Keys.STATE));
			newMap.put(Constant.ElasticSearch.Shard_Table_Keys.PRIMARY,
					((Map<String, Object>) shards
							.get(Constant.ElasticSearch.Shard_Keys.ROUTING))
							.get(Constant.ElasticSearch.Shard_Keys.PRIMARY));
			newMap.put(Constant.ElasticSearch.Shard_Table_Keys.NO_DOCS,
					((Map<String, Object>) shards
							.get(Constant.ElasticSearch.Shard_Keys.DOCS))
							.get(Constant.ElasticSearch.Shard_Keys.NUM_DOCS));
			newMap.put(Constant.ElasticSearch.Shard_Table_Keys.SIZE,
					((Map<String, Object>) shards
							.get(Constant.ElasticSearch.Shard_Keys.INDEX))
							.get(Constant.ElasticSearch.Shard_Keys.SIZE));
			String nodeId = (String) ((Map<String, Object>) shards.get(Constant.ElasticSearch.Shard_Keys.ROUTING)).get(Constant.ElasticSearch.Shard_Keys.NODE); 
			newMap.put(Constant.ElasticSearch.Shard_Table_Keys.NODE,getNodeName(nodeId));
			listToReturn.add(newMap);
		}
		return listToReturn;
	}

	/**
	 * Gets the node name.
	 *
	 * @param nodeId the node id
	 * @return the node name
	 */
	private String getNodeName(String nodeId){
		try {
			JSONObject jsonObject = getNodesStats();
			Map<String, Object> nodes = (Map<String, Object>) jsonObject
					.get("nodes");
			Map<String, Object> node = (Map<String, Object>) nodes.get(nodeId);
			nodeId = (String) node.get("name");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		return nodeId;
	}

	/**
	 * Index aliases.
	 *
	 * @param index the index
	 * @return the hash map
	 * @throws ParseException the parse exception
	 */
	private HashMap<String, Object> indexAliases(String index)
			throws ParseException {
		HashMap<String, Object> aliasesMap = new HashMap<String, Object>();
		String url = "http://localhost:" + this.httpPort + "/" + index + "/_aliases";
		String data = restClient.getRequest(url);
		JSONObject jsonObject = (JSONObject) new JSONParser().parse(data);
		Map<String, Object> aliases = (Map<String, Object>) ((Map<String, Object>) jsonObject
				.get(index)).get("aliases");
		for (String key : aliases.keySet()) {
			Map<String, Object> aliasValue = (Map<String, Object>) aliases
					.get(key);
			aliasesMap.put(key, aliasValue);
		}
		return aliasesMap;
	}

	/**
	 * Index tile data.
	 *
	 * @param indexStatsData the index stats data
	 * @param shardsMapStats the shards map stats
	 * @return the hash map
	 */
	private HashMap<String, Object> indexTileData(
			Map<String, Object> indexStatsData,
			Map<String, Object> shardsMapStats) {
		HashMap<String, Object> indexTileData = new HashMap<String, Object>();
		Map<String, Object> indexPrimariesMap = ((Map<String, Object>) indexStatsData
				.get("primaries"));
		Map<String, Object> indexTotalMap = ((Map<String, Object>) indexStatsData
				.get("total"));
		indexTileData.put("Documents", ((Map<String, Object>) indexPrimariesMap
				.get("docs")).get("count"));
		indexTileData.put("Primary Size",
				((Map<String, Object>) indexPrimariesMap.get("store"))
						.get("size"));
		indexTileData.put("Total Size",
				((Map<String, Object>) indexTotalMap.get("store")).get("size"));
		indexTileData.put("Total Shards", shardsMapStats.get("total"));
		return indexTileData;
	}
}
