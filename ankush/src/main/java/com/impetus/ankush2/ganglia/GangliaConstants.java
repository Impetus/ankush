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
package com.impetus.ankush2.ganglia;

public interface GangliaConstants {

	static final String CONFIG_GANGLIA_RELATIVE_PATH = "config/ganglia/";

	static final String EXCEPTION_STRING = "Please view server logs for more details.";

	enum Ganglia_Services {
		gmond, GangliaMaster;
	}

	interface NodeProperties {
		// String GANGLIA_MASTER = "gangliaMaster";
	}

	interface ClusterProperties {
		String GANGLIA_PORT = "gangliaPort";

		String POLLING_INTERVAL = "pollingInterval";

		String GRID_NAME = "gridName";

		String RRD_FILE_PATH = "rrdFilePath";

		String GMOND_CONF_PATH = "gmondConfPath";

		String GMETAD_CONF_PATH = "gmetadConfPath";

		String GANGLIA_CLUSTER_NAME = "gangliaClustername";

		String SERVER_CONF_FOLDER = "serverConfFolder";

		String GMETAD_HOST = "gmetadHost";

		String GMOND_SET = "gmondSet";
	}

	interface ComponentType {
		String GMOND = "gmond";
		String GMETAD = "gmetad";
	}

	interface GangliaExecutables {
		String GMOND = "gmond";

		String GMETAD = "gmetad";
	}

	interface ConfigurationFiles {
		String GMOND_CONF = "gmond.conf";
		String GMETAD_CONF = "gmetad.conf";
	}
}
