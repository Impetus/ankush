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
package com.impetus.ankush2.framework.utils;

import java.util.Map;

import com.impetus.ankush.AppStore;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.ReflectionUtil;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.Deployable;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.monitor.AbstractMonitor;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * A factory for creating Object objects.
 * 
 * @author mayur
 */
public class ObjectFactory {

	private static AnkushLogger logger = new AnkushLogger(ObjectFactory.class);

	private static Map getComponentConfiguration(String componentName) {
		try {
			Map<String, Object> compConfMap = (Map<String, Object>) AppStore
					.getObject(Constant.AppStore.COMPONENT_MAP);

			for (String deployerComponentName : compConfMap.keySet()) {
				if (componentName.startsWith(deployerComponentName)) {
					return (Map) compConfMap.get(deployerComponentName);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Gets the deployable priority.
	 * 
	 * @param className
	 *            the class name
	 * @return the deployable priority
	 */
	public static int getComponentPriority(String componentName) {
		try {
			String priority = (String) getComponentConfiguration(componentName)
					.get(Constant.AppStore.ComponentConf.Key.PRIORITY);
			return Integer.parseInt(priority);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return Integer.MAX_VALUE;
	}

	/**
	 * Gets the instance by id.
	 * 
	 * @param componentName
	 *            the id
	 * @return the instance by id
	 */
	public static Deployable getDeployerObject(String componentName) {
		try {
			String className = (String) getComponentConfiguration(componentName)
					.get(Constant.AppStore.ComponentConf.Key.DEPLOYER);
			Deployable deployerInstance = ReflectionUtil
					.getDeployerObject(className);
			deployerInstance.setComponentName(componentName);
			return deployerInstance;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static AbstractMonitor getMonitorObject(String componentName) {
		try {
			String className = (String) getComponentConfiguration(componentName)
					.get(Constant.AppStore.ComponentConf.Key.MONITOR);
			return ReflectionUtil.getMonitorObject(className);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Gets the instance by id.
	 * 
	 * @param componentName
	 *            the id
	 * @return the instance by id
	 */
	public static Serviceable getServiceObject(String componentName)
			throws AnkushException {
		try {
			String className = (String) getComponentConfiguration(componentName)
					.get(Constant.AppStore.ComponentConf.Key.SERVICE);
			Serviceable serviceInstance = ReflectionUtil
					.getServiceObject(className);
			if (className == null || className.isEmpty()
					|| serviceInstance == null) {
				throw new AnkushException("Could not find Serviceable class.");
			}
			serviceInstance.setComponentName(componentName);
			return serviceInstance;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
