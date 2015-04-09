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
package com.impetus.ankush.common.zookeeper;

import com.impetus.ankush.common.exception.RegisterClusterException;
import com.impetus.ankush.common.framework.ComponentJsonMapper;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.RegisterClusterConf;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush2.constant.Constant.Component;

/**
 * @author Akhil
 *
 */
public class ZookeeperJsonMapper implements ComponentJsonMapper {

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.ComponentJsonMapper#createComponentConfig(com.impetus.ankush.common.framework.config.RegisterClusterConf)
	 */
	@Override
	public GenericConfiguration createComponentConfig(
			RegisterClusterConf regClusterConf) throws Exception {
		try {
			ZookeeperConf obj = JsonMapperUtil.objectFromObject(regClusterConf
					.getComponents().get(Component.Name.ZOOKEEPER), ZookeeperConf.class);

			return obj;
		} catch (Exception e) {
			throw (e);
		}
	}
}
