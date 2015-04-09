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

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;

public class DBNodeManager {
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	private static AnkushLogger logger = new AnkushLogger(DBNodeManager.class);

	public Node getNode(String host) {
		try {
			return nodeManager.getByPropertyValueGuarded("publicIp", host);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Node getNode(Long nodeId) {
		try {
			return nodeManager.get(nodeId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Node saveNode(Node node) {
		try {
			return nodeManager.save(node);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void remove(String host) {
		try {
			// Remove node.
			nodeManager.deleteAllByPropertyValue("publicIp", host);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
