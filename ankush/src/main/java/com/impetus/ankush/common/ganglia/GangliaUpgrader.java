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
package com.impetus.ankush.common.ganglia;

import java.util.ArrayList;
import java.util.List;

import com.impetus.ankush.common.framework.ComponentUpgrader;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * @author hokam
 * 
 */
public class GangliaUpgrader implements ComponentUpgrader {

	// Logger
	private static final AnkushLogger LOG = new AnkushLogger(
			GangliaUpgrader.class);

	@Override
	public List<String> getUpgradeCommands(String nodeIp,
			GenericConfiguration config) throws Exception {
		try {
			// Cast GenericConfiguration class object to GangliaConf class
			// object
			GangliaConf conf = (GangliaConf) config;

			// commands.
			List<String> commands = new ArrayList<String>();

			// TODO add the commands.

			// returning commands.
			return commands;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}
}
