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
package com.impetus.ankush.common.framework;

import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * Cluster creation interface.
 * 
 * @author mayur
 */
public interface Clusterable {

	/**
	 * Validates cluster configuration.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean validate(ClusterConf clusterConf);

	/**
	 * Validates nodes against cluster configuration.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param nodeConfs
	 *            the node confs
	 * @return true, if successful
	 */
	boolean validate(ClusterConf clusterConf, List<NodeConf> nodeConfs);

	/**
	 * Persists cluster details.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean saveClusterDetails(ClusterConf clusterConf);

	/**
	 * Preprocess before deployment.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean preDeploy(ClusterConf clusterConf);

	/**
	 * Creates components configurations.
	 * 
	 * @param newClusterConf 
	 * 			  the new cluster conf.
	 * @param clusterConf
	 *            the old cluster conf.
	 * @return the map
	 */
	Map<String, Configuration> createConfigs(ClusterConf newClusterConf,
			ClusterConf oldClusterConf);

	/**
	 * Creates components configurations.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return the map
	 */
	Map<String, Configuration> createConfigs(ClusterConf clusterConf);

	/**
	 * Post deployment logic.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean postDeploy(ClusterConf clusterConf);

	/**
	 * Preprocessing before undeploy.
	 * 
	 * @param dbCluster
	 *            the db cluster
	 * @return the cluster conf
	 * @throws Exception
	 *             the exception
	 */
	ClusterConf preUndeploy(Cluster dbCluster) throws Exception;

	/**
	 * Postprocessing after unpdeloy.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 */
	void postUndeploy(ClusterConf clusterConf);

	/**
	 * Get the lastest error message of Cluster.
	 * 
	 * @return the error
	 */
	String getError();

	/**
	 * Pre processing before adding nodes to Cluster.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean preAddNodes(List<NodeConf> nodeConfs, ClusterConf clusterConf);

	/**
	 * Post processing after adding nodes to Cluster.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean postAddNodes(List<NodeConf> nodeConfs, ClusterConf clusterConf);

	/**
	 * Post processing after removal of nodes from Cluster.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean postRemoveNodes(List<NodeConf> nodeConfs, ClusterConf clusterConf);

	/**
	 * Get Cluster configuration from database.
	 * 
	 * @param dbCluster
	 *            the db cluster
	 * @return the cluster conf
	 */
	ClusterConf getClusterConf(Cluster dbCluster);

	/**
	 * Get nodes of the Cluster.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return the nodes
	 */
	List<NodeConf> getNodes(ClusterConf clusterConf);

	/**
	 * Saves nodes detail for the Cluster.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean saveNodeDetails(List<NodeConf> nodeConfs, ClusterConf clusterConf);

	/**
	 * Updates Cluster details.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean updateClusterDetails(ClusterConf clusterConf);

	/**
	 * Pre processing before removing nodes from Cluster.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	boolean preRemoveNodes(List<NodeConf> nodeConfs, ClusterConf clusterConf);
	
}
