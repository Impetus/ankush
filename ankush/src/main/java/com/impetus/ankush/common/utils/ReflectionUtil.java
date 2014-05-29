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
package com.impetus.ankush.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.Clusterable;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.ServiceMonitorable;

/**
 * The Class ReflectionUtil.
 * 
 * @author mayur
 */
public class ReflectionUtil {

	/** The log. */
	private static final AnkushLogger LOG = new AnkushLogger(
			ReflectionUtil.class);

	/**
	 * Gets the clusterable object.
	 * 
	 * @param className
	 *            the class name
	 * @return the clusterable object
	 */
	public static Clusterable getClusterableObject(String className) {
		return (com.impetus.ankush.common.framework.Clusterable) getObject(className);
	}

	/**
	 * Gets the deployable object.
	 * 
	 * @param className
	 *            the class name
	 * @return the deployable object
	 */
	public static Deployable getDeployableObject(String className) {
		return (com.impetus.ankush.common.framework.Deployable) getObject(className);
	}

	/**
	 * Gets the monitorable object.
	 * 
	 * @param className
	 *            the class name
	 * @return the monitorable object
	 */
	public static AbstractMonitor getMonitorableObject(String className) {
		return (com.impetus.ankush.common.framework.AbstractMonitor) getObject(className);
	}

	/**
	 * Gets the Serviceable object.
	 * 
	 * @param className
	 *            the class name
	 * @return the Serviceable object
	 */
	public static ServiceMonitorable getServiceMonitorableObject(String className) {
		return (ServiceMonitorable) getObject(className);
	}

	/**
	 * Gets the object.
	 * 
	 * @param className
	 *            the class name
	 * @return the object
	 */
	private static Object getObject(String className) {
		Object obj = null;
		try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> co = clazz.getConstructor();
			obj = co.newInstance(null);
		} catch (SecurityException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			LOG.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			LOG.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
		}

		return obj;
	}
}
