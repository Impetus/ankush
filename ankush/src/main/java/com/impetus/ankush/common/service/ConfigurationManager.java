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
package com.impetus.ankush.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Configuration;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.HibernateUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;

/**
 * The Class ConfigurationManager.
 * 
 * @author hokam
 */
public class ConfigurationManager {
	// configuration manager.
	/** The configuration manager. */
	private GenericManager<Configuration, Long> configurationManager = AppStoreWrapper
			.getManager(Constant.Manager.CONFIGURATION, Configuration.class);

	// Ankush logger.
	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(ConfigurationManager.class);

	/**
	 * Save configuration.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param username
	 *            the username
	 * @param fileName
	 *            the file name
	 * @param host
	 *            the host
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 * @return the configuration
	 */
	public Configuration saveConfiguration(Long clusterId, String username,
			String fileName, String host, String propertyName,
			String propertyValue) {

		// Get old configuration value if null return the empty object.
		Configuration conf = getOldConfiguration(clusterId, host, fileName,
				propertyName);

		conf.setClusterId(clusterId);
		conf.setUsername(username);
		conf.setSource(fileName);
		conf.setPropertyName(propertyName);
		conf.setPropertyValue(propertyValue);
		conf.setHost(host);

		LOG.debug("Saving Configuration " + conf);
		return configurationManager.save(conf);
	}

	/**
	 * Save configuration.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param username
	 *            the username
	 * @param fileName
	 *            the file name
	 * @param host
	 *            the host
	 * @param params
	 *            the params
	 */
	public void saveConfiguration(Long clusterId, String username,
			String fileName, String host, Map params) {

		for (Object key : params.keySet()) {

			// Get old configuration value if null return the empty object.
			Configuration conf = getOldConfiguration(clusterId, host, fileName,
					key.toString());

			conf.setClusterId(clusterId);
			conf.setUsername(username);
			conf.setSource(fileName);
			conf.setPropertyName(key.toString());
			conf.setPropertyValue(params.get(key).toString());
			conf.setHost(host);
			configurationManager.save(conf);
		}
	}

	/**
	 * Save configuration.
	 * 
	 * @param conf
	 *            the conf
	 * @return the configuration
	 */
	public Configuration saveConfiguration(Configuration conf) {
		// Saving configuration object.
		return saveConfiguration(conf.getClusterId(), conf.getUsername(),
				conf.getSource(), conf.getHost(), conf.getPropertyName(),
				conf.getPropertyValue());
	}

	/**
	 * Method to remove the audit trail of the cluster.
	 * 
	 * @param clusterId
	 */
	public void removeAuditTrail(Long clusterId) {
		try {
			// Get entity manager.
			EntityManager em = HibernateUtils.getEntityManager();
			// Get the transaction
			EntityTransaction tc = em.getTransaction();
			// begin the transaction
			tc.begin();
			// build the query.
			Query query = em
					.createQuery("delete from com.impetus.ankush.common.domain.Configuration_AUD where clusterId=:clusterId");
			// setting the cluster id parameter in query.
			query.setParameter(Constant.Keys.CLUSTERID, clusterId);
			// execute the query
			query.executeUpdate();
			// commit the transaction
			tc.commit();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Gets the configuration.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the configuration
	 */
	public List getConfiguration(Long clusterId) {
		try {
			AuditReader reader = AuditReaderFactory.get(HibernateUtils
					.getEntityManager());
			AuditQuery query = reader.createQuery().forRevisionsOfEntity(
					Configuration.class, false, true);

			// filter results besed on cluster id.
			query.add(AuditEntity.property("clusterId").eq(clusterId));
			query.addOrder(AuditEntity.revisionProperty("timestamp").desc());

			// getting result list.
			List list = query.getResultList();

			// creating list object.
			List result = new ArrayList();
			for (Object object : list) {
				Object[] obj = (Object[]) object;
				Map map = new HashMap();

				DefaultRevisionEntity ri = (DefaultRevisionEntity) obj[1];
				map.putAll(JsonMapperUtil.mapFromObject(obj[0]));
				map.put("date", ri.getRevisionDate());
				map.put("revisionId", ri.getId());
				map.put("type", obj[2]);
				result.add(map);
			}
			return result;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;

	}

	/**
	 * Method to get old configuration.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param host
	 *            the host
	 * @param fileName
	 *            the file name
	 * @param propertyName
	 *            the property name
	 * @return the old configuration
	 */
	public Configuration getOldConfiguration(Long clusterId, String host,
			String fileName, String propertyName) {
		Map<String, Object> propsMap = new HashMap<String, Object>();
		propsMap.put("clusterId", clusterId);
		propsMap.put("host", host);
		propsMap.put("source", fileName);
		propsMap.put("propertyName", propertyName);

		Configuration configuration = configurationManager
				.getByPropertyValueGuarded(propsMap);
		if (configuration == null) {
			configuration = new Configuration();
		}
		return configuration;
	}

	/**
	 * Method to get old configuration.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param host
	 *            the host
	 * @param fileName
	 *            the file name
	 * @param propertyName
	 *            the property name
	 * @return the old configuration
	 */
	public void removeOldConfiguration(Long clusterId, String host,
			String fileName, String propertyName) {
		Map<String, Object> propsMap = new HashMap<String, Object>();
		propsMap.put("clusterId", clusterId);
		propsMap.put("host", host);
		propsMap.put("source", fileName);
		propsMap.put("propertyName", propertyName);

		configurationManager.deleteAllByPropertyValue(propsMap);
	}
}
