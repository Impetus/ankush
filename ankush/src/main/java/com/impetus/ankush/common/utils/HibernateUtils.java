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
package com.impetus.ankush.common.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.impetus.ankush.AppStoreWrapper;

/**
 * The Class HibernateUtils.
 *
 * @author hokam
 */
public class HibernateUtils {

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	public static EntityManager getEntityManager() {
		EntityManagerFactory factory = getEntityManagerFactory();
		return factory.createEntityManager();
	}

	/**
	 * Gets the entity manager factory.
	 *
	 * @return the entity manager factory
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return AppStoreWrapper.getService("entityManagerFactory",
				EntityManagerFactory.class);
	}
}
