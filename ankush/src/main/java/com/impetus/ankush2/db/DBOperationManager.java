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
package com.impetus.ankush2.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import static java.lang.Integer.MAX_VALUE;

public class DBOperationManager {
	private static AnkushLogger logger = new AnkushLogger(
			DBOperationManager.class);

	/** The operation manager. */
	private GenericManager<Operation, Long> operationManager = AppStoreWrapper
			.getManager(Constant.Manager.OPERATION, Operation.class);

	public Operation saveOperation(Operation operation) {
		try {
			return operationManager.save(operation);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Operation getOperation(ClusterConfig clusterConf) {
		// property map for query.
		Map<String, Object> propMap = new HashMap<String, Object>();
		propMap.put("clusterId", clusterConf.getClusterId());
		propMap.put("operationId", clusterConf.getOperationId());

		// Getting last operation.
		List<Operation> operations = operationManager.getAllByPropertyValue(
				propMap, "-startedAt");

		// Returning first element
		if (operations != null && !operations.isEmpty()) {
			return operations.get(0);
		}
		return null;
	}

	public String getOperationStatus(Long clusterId, Long operationId) {
		try {
			Map<String, Object> propMap = new HashMap<String, Object>();
			propMap.put("clusterId", clusterId);
			propMap.put("operationId", operationId);

			// Getting last operation.
			List<Operation> operations = operationManager
					.getAllByPropertyValue(propMap);
			// Returning first element
			if (operations != null && !operations.isEmpty()) {
				return operations.get(0).getStatus();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Long getOperationDbId(ClusterConfig clusterConf) {
		if (getOperation(clusterConf) == null) {
			return null;
		}
		return getOperation(clusterConf).getId();
	}

	public List<Operation> getOperations(Long clusterId, String opName,
			Long operationId, String status) {
		return getOperations(getPropertyMap(clusterId, opName, operationId,
				status));
	}

	public Map<String, Object> getPropertyMap(Long clusterId, String opName,
			Long operationId, String status) {
		Map<String, Object> propMap = new HashMap<String, Object>();
		if (clusterId != null) {
			propMap.put(Constant.Keys.CLUSTERID, clusterId);
		}

		if (opName != null) {
			propMap.put(Constant.Keys.OPERATION_NAME, opName);
		}

		if (operationId != null) {
			propMap.put(Constant.Keys.OPERATIONID, operationId);
		}

		if (status != null) {
			propMap.put(Constant.Keys.STATUS, status);
		}
		return propMap;
	}

	private List<Operation> getOperations(Map<String, Object> propMap) {
		try {
			return operationManager.getAllByPropertyValue(propMap, 0,
					MAX_VALUE, "-operationId");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ArrayList<Operation>();
	}

	public long getNewOperationId(long clusterId) {
		try {
			String queryString = "select max(operationId) from Operation where clusterId="
					+ clusterId;
			List resultSet = AppStoreWrapper.getOperationManager()
					.getCustomQuery(queryString);
			if (resultSet != null && resultSet.size() > 0) {
				for (Iterator it = resultSet.iterator(); it.hasNext();) {
					if (it != null) {
						Long maxOperationId = (Long) it.next();
						return maxOperationId + 1;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getting new operation Id.", e);
		}
		logger.info("New operation id : " + 1);
		return 1;
	}
}
