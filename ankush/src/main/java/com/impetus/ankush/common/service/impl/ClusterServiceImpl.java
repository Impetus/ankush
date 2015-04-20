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
package com.impetus.ankush.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.service.ClusterService;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class ClusterServiceImpl.
 */
@Service("clusterService")
public class ClusterServiceImpl implements ClusterService {

	/** The log. */
	private AnkushLogger log = new AnkushLogger(ClusterServiceImpl.class);

	/** The cluster manager. */
	private GenericManager<Cluster, Long> clusterManager;

	/**
	 * Sets the new cluster manager.
	 * 
	 * @param clusterManager
	 *            the cluster manager
	 */
	@Autowired
	public void setNewClusterManager(
			@Qualifier(Constant.Manager.CLUSTER) GenericManager<Cluster, Long> clusterManager) {
		this.clusterManager = clusterManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.service.ClusterService#getDashboardInfo()
	 */
	@Override
	public List<Map<String, Object>> getDashboardInfo() {
		List<Map<String, Object>> dashboardInfo = new ArrayList<Map<String, Object>>();
		List<Cluster> clusters = clusterManager.getAll();
		Map<String, Object> newClsMp = new HashMap<String, Object>();
		for (Cluster ncls : clusters) {
			newClsMp = new HashMap<String, Object>();
			newClsMp.put("id", ncls.getId());
			newClsMp.put("name", ncls.getName());
			newClsMp.put("state", ncls.getState());
			newClsMp.put("technology", ncls.getTechnology());
			dashboardInfo.add(newClsMp);
		}
		return dashboardInfo;
	}

}
