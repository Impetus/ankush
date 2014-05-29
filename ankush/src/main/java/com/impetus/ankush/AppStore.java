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
package com.impetus.ankush;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class AppStore.
 */
public class AppStore {

	/** The store. */
	private static Map<String, Object> store = new HashMap<String, Object>();

	/**
	 * Sets the object.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public static void setObject(String key, Object value) {
		store.put(key, value);
	}

	/**
	 * Gets the object.
	 *
	 * @param key the key
	 * @return the object
	 */
	public static Object getObject(String key) {
		return store.get(key);
	}

	/**
	 * Method to destroy store map values.
	 */
	public static void destroyStore() {
		store.clear();
		store = null;
	}
	
}
