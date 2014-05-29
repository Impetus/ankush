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
package com.impetus.ankush.common.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impetus.ankush.common.dao.GenericDao;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * 
 * To register this class in your Spring context file, use the following XML.
 * 
 * @param <T>
 *            a type variable
 * @param <P>
 *            the primary key for that type
 */
public class GenericDaoJpa<T, P extends Serializable> implements
		GenericDao<T, P> {
	
	/** The Constant UNCHECKED. */
	private static final String UNCHECKED = "unchecked";

	/** The Constant SELECT_OBJ_FROM. */
	private static final String SELECT_OBJ_FROM = "select obj from ";

	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Logger log = LoggerFactory.getLogger(getClass());

	/** Entity manager, injected by Spring using @PersistenceContext annotation on setEntityManager(). */
	@PersistenceContext
	private EntityManager entityManager;
	
	/** The persistent class. */
	private final Class<T> persistentClass;

	/**
	 * Constructor that takes in a class to see which type of entity to persist.
	 * Use this constructor when subclassing or using dependency injection.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 */
	public GenericDaoJpa(final Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * Constructor that takes in a class to see which type of entity to persist.
	 * Use this constructor when subclassing or using dependency injection.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 * @param entityManager
	 *            the configured EntityManager for JPA implementation.
	 */
	public GenericDaoJpa(final Class<T> persistentClass,
			EntityManager entityManager) {
		this.persistentClass = persistentClass;
		this.entityManager = entityManager;
	}

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAll(int, int, java.lang.String[])
	 */
	public List<T> getAll(int start, int maxResults, String... orderBy) {
		Query query = this.entityManager.createQuery(getAllQueryString()
				+ createOrderByClause(orderBy));

		query.setFirstResult(start);
		query.setMaxResults(maxResults);

		return query.getResultList();
	}

	/**
	 * Gets the all query string.
	 *
	 * @return the all query string
	 */
	private String getAllQueryString() {
		return SELECT_OBJ_FROM + this.persistentClass.getName() + " obj";
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllCount()
	 */
	public int getAllCount() {
		return getCount(convertToCountQuery(getAllQueryString()),
				(List<Object>) null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings(UNCHECKED)
	public List<T> getAllDistinct() {
		return new ArrayList(new LinkedHashSet(getAll(0, Integer.MAX_VALUE)));
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(P id) {
		T entity = this.entityManager.find(this.persistentClass, id);

		if (entity == null) {
			String msg = "Uh oh, '" + this.persistentClass
					+ "' object with id '" + id + "' not found...";
			log.warn(msg);
			throw new EntityNotFoundException(msg);
		}
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public T getGuarded(P id) {
		return this.entityManager.find(this.persistentClass, id);
	}

	/**
	 * {@inheritDoc}
	 */
	public T getReference(P id) {
		try {
			T entity = this.entityManager
					.getReference(this.persistentClass, id);
			if (entity == null) {
				throw new RuntimeException();
			}
			return entity;
		} catch (Exception e) {
			String msg = "Uh oh, '" + this.persistentClass
					+ "' object with id '" + id + "' not found...";
			log.warn(msg);
			throw new EntityNotFoundException(msg);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists(P id) {
		T entity = this.entityManager.find(this.persistentClass, id);
		return entity != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public T save(T object) {
		return this.entityManager.merge(object);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(P id) {
		this.entityManager.remove(this.get(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings(UNCHECKED)
	public T getByPropertyValue(Map<String, Object> propertyValueMap) {
		return (T) createSelectQuery(propertyValueMap).getSingleResult();
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllByPropertyValue(java.util.Map, int, int, java.lang.String[])
	 */
	@SuppressWarnings(UNCHECKED)
	public List<T> getAllByPropertyValue(Map<String, Object> propertyValueMap,
			int start, int maxResults, String... orderBy) {
		Query query = createSelectQuery(propertyValueMap, orderBy);
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllByPropertyValueCount(java.util.Map)
	 */
	public int getAllByPropertyValueCount(Map<String, Object> propertyValueMap) {
		LinkedHashMap<String, Object> orderedMap = getOrderedMap(propertyValueMap);
		String selectQueryString = createSelectQueryString(orderedMap)
				.toString();
		return this
				.getCount(convertToCountQuery(selectQueryString), orderedMap);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllByNativeQuery(java.lang.String)
	 */
	public List<T> getAllByNativeQuery(String sql) {
		return entityManager.createNativeQuery(sql, persistentClass)
				.getResultList();
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#executeNativeQuery(java.lang.String)
	 */
	public int executeNativeQuery(String sql) {
		return entityManager.createNativeQuery(sql, persistentClass)
				.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	public int deleteAllByPropertyValue(Map<String, Object> propertyValueMap) {
		List<T> objects = getAllByPropertyValue(propertyValueMap, 0,
				Integer.MAX_VALUE);
		this.removeAll(objects);
		this.entityManager.flush();
		return objects.size();
	}

	/**
	 * Create a select query from the given property value map.
	 *
	 * @param propertyValueMap query key value
	 * @param orderBy the order by
	 * @return Query object
	 */
	private Query createSelectQuery(Map<String, Object> propertyValueMap,
			String... orderBy) {
		LinkedHashMap<String, Object> orderedMap = this
				.getOrderedMap(propertyValueMap);

		StringBuilder builder = createSelectQueryString(orderedMap).append(
				this.createOrderByClause(orderBy));

		Query query = this.entityManager.createQuery(builder.toString());

		// fill parameters
		this.assignQueryParameters(query, this.flattenParameterList(Collections
				.singletonList(orderedMap)));

		return query;
	}

	/**
	 * Creates the select query string.
	 *
	 * @param orderedMap the ordered map
	 * @return the string builder
	 */
	private StringBuilder createSelectQueryString(
			LinkedHashMap<String, Object> orderedMap) {
		return new StringBuilder(SELECT_OBJ_FROM)
				.append(this.persistentClass.getName()).append(" obj where ")
				.append(this.createAndClause(orderedMap, 1));
	}

	/**
	 * Removes the all.
	 *
	 * @param objects the objects
	 */
	private void removeAll(List<T> objects) {
		for (Object o : objects) {
			entityManager.remove(o);
		}
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllByNamedQuery(java.lang.String, java.util.Map, int, int)
	 */
	@SuppressWarnings(UNCHECKED)
	public List<T> getAllByNamedQuery(String queryName,
			Map<String, Object> propertyValueMap, int start, int maxResults) {
		Query query = getEntityManager().createNamedQuery(queryName);
		for (Entry<String, Object> entry : propertyValueMap.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		query.setFirstResult(start);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllOfOrMatch(java.util.Map, int, int, java.lang.String[])
	 */
	@SuppressWarnings(UNCHECKED)
	public List<T> getAllOfOrMatch(Map<String, Object> queryMap, int start,
			int maxResults, String... orderBy) {
		LinkedHashMap<String, Object> orderedMap = this.getOrderedMap(queryMap);

		StringBuilder builder = createSelectOrQueryString(orderedMap).append(
				this.createOrderByClause(orderBy));

		Query query = this.entityManager.createQuery(builder.toString());

		// fill parameters
		this.assignQueryParameters(query, this.flattenParameterList(Collections
				.singletonList(orderedMap)));
		query.setFirstResult(start);
		query.setMaxResults(maxResults);

		return query.getResultList();
	}

	/**
	 * Creates the select or query string.
	 *
	 * @param orderedMap the ordered map
	 * @return the string builder
	 */
	private StringBuilder createSelectOrQueryString(
			LinkedHashMap<String, Object> orderedMap) {
		return new StringBuilder(SELECT_OBJ_FROM)
				.append(this.persistentClass.getName()).append(" obj where ")
				.append(this.createOrClause(orderedMap, 1));
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllOfOrMatchCount(java.util.Map)
	 */
	public int getAllOfOrMatchCount(Map<String, Object> queryMap) {

		LinkedHashMap<String, Object> orderedMap = getOrderedMap(queryMap);

		return getCount(
				convertToCountQuery(createSelectOrQueryString(orderedMap)
						.toString()), orderedMap);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllByDisjunctionveNormalQuery(java.util.List, int, int, java.lang.String[])
	 */
	@SuppressWarnings(UNCHECKED)
	public List<T> getAllByDisjunctionveNormalQuery(
			List<Map<String, Object>> disjunctionMaps, int start,
			int maxResults, String... orderBy) {

		List<LinkedHashMap<String, Object>> orderedMaps = this
				.getOrderedMap(disjunctionMaps);

		StringBuilder builder = createDisjunctionNormalQueryString(orderedMaps)
				.append(this.createOrderByClause(orderBy));

		Query query = this.entityManager.createQuery(builder.toString());

		// fill parameters
		this.assignQueryParameters(query,
				this.flattenParameterList(orderedMaps));
		query.setFirstResult(start);
		query.setMaxResults(maxResults);

		return query.getResultList();
	}

	/**
	 * Creates the disjunction normal query string.
	 *
	 * @param orderedMaps the ordered maps
	 * @return the string builder
	 */
	private StringBuilder createDisjunctionNormalQueryString(
			List<LinkedHashMap<String, Object>> orderedMaps) {
		List<String> andClauses = new ArrayList<String>(orderedMaps.size());
		int startIndex = 1;
		for (LinkedHashMap<String, Object> queryMap : orderedMaps) {
			andClauses.add(this.createAndClause(queryMap, startIndex));
			startIndex += queryMap.size();
		}
		String disjunction = new StringBuilder("(")
				.append(StringUtils.join(andClauses, ") or (")).append(") ")
				.toString();

		StringBuilder builder = new StringBuilder(SELECT_OBJ_FROM)
				.append(this.persistentClass.getName()).append(" obj where ")
				.append(disjunction);
		return builder;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.dao.GenericDao#getAllByDisjunctionveNormalQueryCount(java.util.List)
	 */
	public int getAllByDisjunctionveNormalQueryCount(
			List<Map<String, Object>> disjunctionMaps) {
		List<LinkedHashMap<String, Object>> orderedDisjunctionMaps = this
				.getOrderedMap(disjunctionMaps);
		return getCount(
				convertToCountQuery(createDisjunctionNormalQueryString(
						orderedDisjunctionMaps).toString()),
				this.flattenParameterList(orderedDisjunctionMaps));
	}

	/**
	 * Gets the ordered map.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param maps the maps
	 * @return the ordered map
	 */
	private <K, V> List<LinkedHashMap<K, V>> getOrderedMap(List<Map<K, V>> maps) {
		List<LinkedHashMap<K, V>> orderedMaps = new ArrayList<LinkedHashMap<K, V>>(
				maps.size());
		for (Map<K, V> map : maps) {
			orderedMaps.add(this.getOrderedMap(map));
		}
		return orderedMaps;
	}

	/**
	 * Gets the ordered map.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param map the map
	 * @return the ordered map
	 */
	private <K, V> LinkedHashMap<K, V> getOrderedMap(Map<K, V> map) {
		return new LinkedHashMap<K, V>(map);
	}

	/**
	 * Flatten parameter list.
	 *
	 * @param parameterMaps the parameter maps
	 * @return the list
	 */
	private List<Object> flattenParameterList(
			List<LinkedHashMap<String, Object>> parameterMaps) {
		List<Object> parameterList = new ArrayList<Object>();
		for (HashMap<String, Object> parameterMap : parameterMaps) {
			for (Entry<String, Object> entry : parameterMap.entrySet()) {
				parameterList.add(entry.getValue());
			}
		}
		return parameterList;
	}

	/**
	 * Assign query parameters.
	 *
	 * @param query the query
	 * @param parameters the parameters
	 */
	private void assignQueryParameters(Query query, List<Object> parameters) {
		for (int i = 0; i < parameters.size(); i++) {
			query.setParameter(i + 1, parameters.get(i));
		}
	}

	/**
	 * Creates the and clause.
	 *
	 * @param queryMap the query map
	 * @param startIndex the start index
	 * @return the string
	 */
	private String createAndClause(LinkedHashMap<String, Object> queryMap,
			int startIndex) {
		return createClauseList(queryMap, startIndex, " and ");
	}

	/**
	 * Creates the or clause.
	 *
	 * @param queryMap the query map
	 * @param startIndex the start index
	 * @return the string
	 */
	private String createOrClause(LinkedHashMap<String, Object> queryMap,
			int startIndex) {
		return createClauseList(queryMap, startIndex, " or ");
	}

	/**
	 * Creates the clause list.
	 *
	 * @param queryMap the query map
	 * @param startIndex the start index
	 * @param joiner the joiner
	 * @return the string
	 */
	private String createClauseList(LinkedHashMap<String, Object> queryMap,
			int startIndex, String joiner) {
		List<StringBuilder> clauses = new ArrayList<StringBuilder>(
				queryMap.size());

		for (String property : queryMap.keySet()) {
			clauses.add(new StringBuilder("obj.").append(property)
					.append(" = ?").append(startIndex++));
		}
		return StringUtils.join(clauses, joiner);
	}

	/**
	 * Creates the order by clause.
	 *
	 * @param orderBy the order by
	 * @return the string
	 */
	private String createOrderByClause(String[] orderBy) {
		if (orderBy == null || orderBy.length == 0) {
			return StringUtils.EMPTY;
		}
		List<StringBuilder> clauses = new ArrayList<StringBuilder>(
				orderBy.length);
		for (String order : orderBy) {
			StringBuilder builder = new StringBuilder("obj.");
			if (order.charAt(0) == '-') {
				builder.append(order.substring(1)).append(" DESC");
			} else {
				builder.append(order);
			}
			clauses.add(builder);
		}
		return " order by " + StringUtils.join(clauses, ',');
	}

	/**
	 * Convert to count query.
	 *
	 * @param query the query
	 * @return the string
	 */
	private String convertToCountQuery(String query) {
		Pattern pattern = Pattern.compile("select(.+)from");
		Matcher matcher = pattern.matcher(query);
		if (matcher.find()) {
			String s = matcher.group(1).trim();
			query = StringUtils.replaceOnce(query, s, "count(" + s + ")");
		}
		return query;
	}

	/**
	 * Gets the count.
	 *
	 * @param queryString the query string
	 * @param orderedMap the ordered map
	 * @return the count
	 */
	private int getCount(String queryString,
			LinkedHashMap<String, Object> orderedMap) {
		Query query = this.entityManager.createQuery(queryString);
		if (orderedMap != null) {
			assignQueryParameters(query,
					flattenParameterList(Collections.singletonList(orderedMap)));
		}
		return ((Long) query.getSingleResult()).intValue();
	}

	/**
	 * Gets the count.
	 *
	 * @param queryString the query string
	 * @param parameterList the parameter list
	 * @return the count
	 */
	private int getCount(String queryString, List<Object> parameterList) {
		Query query = this.entityManager.createQuery(queryString);
		if (parameterList != null) {
			assignQueryParameters(query, parameterList);
		}
		return ((Long) query.getSingleResult()).intValue();
	}
}
