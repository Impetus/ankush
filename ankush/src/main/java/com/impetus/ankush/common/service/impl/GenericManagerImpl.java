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
package com.impetus.ankush.common.service.impl;

import static java.lang.Integer.MAX_VALUE;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.impetus.ankush.common.dao.GenericDao;
import com.impetus.ankush.common.service.GenericManager;

/**
 * This class serves as the Base class for all other Managers - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * 
 * @param <T>
 *            a type variable
 * @param <P>
 *            the primary key for that type
 * 
 */
public class GenericManagerImpl<T, P extends Serializable> implements
		GenericManager<T, P> {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	/** GenericDao instance, set by constructor of child classes. */
	protected GenericDao<T, P> dao;

	/**
	 * Instantiates a new generic manager impl.
	 */
	public GenericManagerImpl() {
		// do nothing constructor
	}

	/**
	 * Instantiates a new generic manager impl.
	 * 
	 * @param genericDao
	 *            the generic dao
	 */
	public GenericManagerImpl(GenericDao<T, P> genericDao) {
		this.dao = genericDao;
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAll(String... orderBy) {
		return this.getAll(0, MAX_VALUE, orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAll(int start, int maxResults, String... orderBy) {
		return dao.getAll(start, maxResults, orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public int getAllCount() {
		return dao.getAllCount();
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T get(P id) {
		return dao.get(id);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T getGuarded(P id) {
		return dao.getGuarded(id);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T getReference(P id) {
		return dao.getReference(id);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public boolean exists(P id) {
		return dao.exists(id);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T save(T object) {
		return dao.save(object);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void remove(P id) {
		dao.remove(id);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByPropertyValue(Map<String, Object> propertyValueMap,
			String... orderBy) {
		return this.getAllByPropertyValue(propertyValueMap, 0, MAX_VALUE,
				orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByPropertyValue(Map<String, Object> propertyValueMap,
			int start, int maxResults, String... orderBy) {
		return dao.getAllByPropertyValue(propertyValueMap, start, maxResults,
				orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public int getAllByPropertyValueCount(Map<String, Object> propertyValueMap) {
		return dao.getAllByPropertyValueCount(propertyValueMap);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T getByPropertyValue(Map<String, Object> propertyValueMap) {
		return dao.getByPropertyValue(propertyValueMap);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T getByPropertyValue(String property, Object value) {
		return this.getByPropertyValue(Collections
				.singletonMap(property, value));
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T getByPropertyValueGuarded(Map<String, Object> propertyValueMap) {
		List<T> results = this.getAllByPropertyValue(propertyValueMap);
		return results.isEmpty() ? null : results.get(0);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public T getByPropertyValueGuarded(String property, Object value) {
		return this.getByPropertyValueGuarded(Collections.singletonMap(
				property, value));
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByPropertyValue(String property, Object value,
			String... orderBy) {
		return this.getAllByPropertyValue(property, value, 0, MAX_VALUE,
				orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByPropertyValue(String property, Object value,
			int start, int maxResults, String... orderBy) {
		return this.getAllByPropertyValue(
				Collections.singletonMap(property, value), start, maxResults,
				orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public int getAllByPropertyValueCount(String property, Object value) {
		return this.getAllByPropertyValueCount(Collections.singletonMap(
				property, value));
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByNamedQuery(String queryName,
			Map<String, Object> propertyValueMap) {
		return this.getAllByNamedQuery(queryName, propertyValueMap, 0,
				MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByNamedQuery(String queryName,
			Map<String, Object> propertyValueMap, int start, int maxResults) {
		return dao.getAllByNamedQuery(queryName, propertyValueMap, start,
				maxResults);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllOfOrMatch(Map<String, Object> queryMap,
			String... orderBy) {
		return this.getAllOfOrMatch(queryMap, 0, MAX_VALUE, orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllOfOrMatch(Map<String, Object> queryMap, int start,
			int maxResults, String... orderBy) {
		return dao.getAllOfOrMatch(queryMap, start, maxResults, orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public int getAllOfOrMatchCount(Map<String, Object> queryMap) {
		return dao.getAllOfOrMatchCount(queryMap);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByDisjunctionveNormalQuery(
			List<Map<String, Object>> disjunctionMaps, String... orderBy) {
		return this.getAllByDisjunctionveNormalQuery(disjunctionMaps, 0,
				MAX_VALUE, orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<T> getAllByDisjunctionveNormalQuery(
			List<Map<String, Object>> disjunctionMaps, int start,
			int maxResults, String... orderBy) {
		return dao.getAllByDisjunctionveNormalQuery(disjunctionMaps, start,
				maxResults, orderBy);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public int getAllByDisjunctionveNormalQueryCount(
			List<Map<String, Object>> disjunctionMaps) {
		return dao.getAllByDisjunctionveNormalQueryCount(disjunctionMaps);
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public int deleteAllByPropertyValue(String property, Object value) {
		return this.deleteAllByPropertyValue(Collections.singletonMap(property,
				value));
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public int deleteAllByPropertyValue(Map<String, Object> propertyValueMap) {
		return dao.deleteAllByPropertyValue(propertyValueMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.GenericManager#getAllByNativeQuery(
	 * java.lang.String)
	 */
	@Override
	public List<T> getAllByNativeQuery(String sql) {
		return dao.getAllByNativeQuery(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.GenericManager#executeNativeQuery(java
	 * .lang.String)
	 */
	@Override
	public int executeNativeQuery(String sql) {
		return dao.executeNativeQuery(sql);
	}

	@Override
	public List getCustomQuery(String queryString) {
		return dao.getCustomQuery(queryString);
	}
}
