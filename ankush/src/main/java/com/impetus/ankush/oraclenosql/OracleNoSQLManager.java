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
package com.impetus.ankush.oraclenosql;

import oracle.kv.impl.admin.CommandServiceAPI;
import oracle.kv.impl.util.registry.RegistryUtils;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * Utility class to get command service API object.
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLManager {

	/** The log. */
	private static AnkushLogger log = new AnkushLogger(OracleNoSQLManager.class);

	/**
	 * Gets the admin client.
	 * 
	 * @param conf
	 *            the conf
	 * @return the admin client
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public static CommandServiceAPI getAdminClient(OracleNoSQLConf conf)
			throws AnkushException {
		for (OracleNoSQLNodeConf nodeConf : conf.getNodes()) {
			if (nodeConf.isAdmin()) {
				log.info("Getting admin client from " + nodeConf.getPublicIp());
				try {
					CommandServiceAPI cs = RegistryUtils.getAdmin(
							nodeConf.getPublicIp(), nodeConf.getRegistryPort());
					if (cs.getMasterHttpAddress().getHost()
							.equals(nodeConf.getPublicIp())) {
						return cs;
					} else {
						return RegistryUtils.getAdmin(cs.getMasterHttpAddress()
								.getHost(), nodeConf.getRegistryPort());
					}
				} catch (Exception e) {
					log.error("Getting admin client failed.", e);
				}
			}
		}

		throw new AnkushException("Could not get admin client.");
	}

}
