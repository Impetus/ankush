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
package com.impetus.ankush.hadoop.service;

import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.hadoop.config.Parameter;

/**
 * The Interface ParameterConfigService.
 */
public interface ParameterConfigService   {

	/**
	 * Gets the component config files.
	 *
	 * @param cluster the cluster
	 * @param params the params
	 * @return the component config files
	 */
	Map<String, Object> getComponentConfigFiles(Cluster cluster, Map<String, String> params);
	
	/**
	 * Gets the comp conf file params.
	 *
	 * @param cluster the cluster
	 * @param params the params
	 * @return the comp conf file params
	 */
	Map<String, List<Parameter>> getCompConfFileParams(Cluster cluster,
			Map<String, String> params);
	
	/**
	 * Update config file param.
	 *
	 * @param cluster the cluster
	 * @param parameterMap the parameter map
	 */
	void updateConfigFileParam(Cluster cluster, Map<String, Object> parameterMap);
}
