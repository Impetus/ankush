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

import com.impetus.ankush.AppStoreWrapper;

/**
 * A factory for creating Object objects.
 * 
 * @author mayur
 */
public class ObjectFactory {

	/**
	 * Gets the deployable priority.
	 * 
	 * @param className
	 *            the class name
	 * @return the deployable priority
	 */
	public static int getDeployablePriority(String className) {
		return AppStoreWrapper.getDeployablePriority(className);
	}

	/**
	 * Gets the instance by id.
	 * 
	 * @param id
	 *            the id
	 * @return the instance by id
	 */
	public static Deployable getInstanceById(String id) {
		return AppStoreWrapper.getDeployableInstanceById(id);
	}

	/**
	 * Gets the instance by id.
	 * 
	 * @param id
	 *            the id
	 * @return the instance by id
	 */
	public static String getConfigurationClassName(String id) {
		return AppStoreWrapper.getDeployableConfClassName(id);
	}

	/**
	 * Gets the clusterable instance by id.
	 * 
	 * @param id
	 *            the id
	 * @return the clusterable instance by id
	 */
	public static Clusterable getClusterableInstanceById(String id) {
		return AppStoreWrapper.getClusterableInstanceById(id);
	}

	/**
	 * Gets the monitorable instance by id.
	 * 
	 * @param id
	 *            the id
	 * @return the monitorable instance by id
	 */
	public static AbstractMonitor getMonitorableInstanceById(String id) {
		return AppStoreWrapper.getMonitorableInstanceById(id);
	}

	/**
	 * Gets the Serviceable instance by Service Name.
	 * 
	 * @param ServiceName
	 *            the Service Name 
	 * @return the Serviceable instance by Service Name
	 */
	public static ServiceMonitorable getServiceMonitorableInstanceByServiceName(String serviceName) {
		return AppStoreWrapper.getServiceMonitorableInstanceByServiceName(serviceName);
	}
}
