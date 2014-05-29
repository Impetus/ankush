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
package com.impetus.ankush.hadoop.service;

import java.util.Map;

import com.impetus.ankush.hadoop.scheduler.SchedulerConfig;

/**
 * The Interface SchedulerConfigService.
 */
public interface SchedulerConfigService {

    /**
     * Save config.
     *
     * @param clusterId the cluster id
     * @param config the config
     * @return the map
     * @throws Exception the exception
     */
    Map saveConfig(Long clusterId, SchedulerConfig config) throws Exception;

    /**
     * Gets the config.
     *
     * @param clusterId the cluster id
     * @return the config
     * @throws Exception the exception
     */
    Map getConfig(Long clusterId) throws Exception;

}
