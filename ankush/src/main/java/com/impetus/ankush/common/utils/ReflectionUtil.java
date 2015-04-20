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

import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class ReflectionUtil.
 * 
 * @author mayur
 */
public class ReflectionUtil {

	/** The log. */
	private static final AnkushLogger LOG = new AnkushLogger(
			ReflectionUtil.class);

	public static <S> S getObjectInstance(String className, Class<S> classType) {
		return (S) getObject(className);
	}

	public static com.impetus.ankush2.framework.Deployable getDeployerObject(
			String className) {
		return (com.impetus.ankush2.framework.Deployable) getObject(className);
	}

	public static com.impetus.ankush2.framework.monitor.AbstractMonitor getMonitorObject(
			String className) {
		return (com.impetus.ankush2.framework.monitor.AbstractMonitor) getObject(className);
	}

	public static com.impetus.ankush2.framework.Serviceable getServiceObject(
			String className) {
		return (com.impetus.ankush2.framework.Serviceable) getObject(className);
	}

	/**
	 * Gets the object.
	 * 
	 * @param className
	 *            the class name
	 * @return the object
	 */
	public static Object getObject(String className) {
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
