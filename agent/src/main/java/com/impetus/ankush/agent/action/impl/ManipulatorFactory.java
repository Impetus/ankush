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
package com.impetus.ankush.agent.action.impl;

import java.lang.reflect.Constructor;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.action.Manipulatable;

public class ManipulatorFactory {

	/**
	 * Gets the instance by id.
	 * 
	 * @param id
	 *            the id
	 * @return the instance by id
	 */
	public static Manipulatable getInstanceById(String id) {

		// Create agent conf.
		final AgentConf conf = new AgentConf();

		// Getting class name.
		final String className = conf.getProperties().getProperty(id);
		return ManipulatorFactory.getManipulatableObject(className);
	}

	/**
	 * Gets the actionable object.
	 * 
	 * @param className
	 *            the class name
	 * @return the actionable object
	 */
	private static Manipulatable getManipulatableObject(String className) {
		com.impetus.ankush.agent.action.Manipulatable obj = null;
		try {
			final Class<?> clazz = Class.forName(className);
			final Constructor<?> co = clazz.getConstructor();
			obj = (com.impetus.ankush.agent.action.Manipulatable) (co
					.newInstance(null));
		} catch (final Exception e) {
			System.err.println(e.getMessage());
		}
		return obj;
	}
}
