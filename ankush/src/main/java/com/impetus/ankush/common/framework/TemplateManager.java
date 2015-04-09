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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Template;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush2.constant.Constant.Component;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;

/**
 * Class to Manage the cluster templates.
 * 
 * @author hokam
 * 
 */
public class TemplateManager {

	// template manager which deals with the database operations.
	private GenericManager<Template, Long> templateManager = AppStoreWrapper
			.getManager(Constant.Manager.TEMPLATE, Template.class);

	/**
	 * Saving template.
	 * 
	 * @param template
	 * @throws Exception
	 */
	public String save(Template template, boolean update) throws Exception {
		// getting all tempates by name.
		List<Template> templates = templateManager.getAllByPropertyValue(
				"name", template.getName());
		// if update is true then update the existing template otherwise return
		// template exists message
		if (templates != null && !templates.isEmpty()) {
			if (update) {
				template.setId(templates.get(0).getId());
			} else {
				throw new Exception("Already exists");
			}
		}
		// set current date as update date.
		template.setUpdateDate(new Date());
		// saving template
		templateManager.save(template);
		// returning template save message.
		return "Template saved successfully.";
	}

	/**
	 * Method to save template and return map of status and error message if
	 * any.
	 * 
	 * @param template
	 * @param update
	 * @return
	 */
	public Map saveTemplate(Template template, boolean update) {
		// hashmap object.
		Map map = new HashMap();
		try {
			// save and get template save message
			String message = this.save(template, update);
			// putting status
			map.put("status", true);
			// putting message
			map.put("message", message);
		} catch (Exception e) {
			// putting false status
			map.put("status", false);
			// putting error as list of exception message.
			map.put("data",
					Collections.singletonMap("error",
							Collections.singletonList(e.getMessage())));
		}
		return map;
	}

	/**
	 * Method to get template in map with status and error if there is any
	 * 
	 * @param name
	 * @return
	 */
	public Map getTemplateMap(String name) {
		// hashmap object.
		Map map = new HashMap();
		try {
			// getting template.
			Template template = this.get(name);
			// converting template object into map.
			map = JsonMapperUtil.mapFromObject(template);
			// getting data from template.
			Map data = (HashMap) map.get("data");
			
			// Special Handling is added for Hadoop 2 Templates
			// Node.Type field in Hadoop 2 Nodes is missing in POST JSON for
			// Template and is required while loading the template
			// This can be deleted only if Node.Type field is sent from the UI
//			ClusterConf clusterConf = template.getData();
//			if (clusterConf.getClusterComponents().containsKey(
//					Component.Name.HADOOP2)) {
//				Hadoop2Conf h2Conf = (Hadoop2Conf) template.getData()
//						.getClusterComponents()
//						.get(Component.Name.HADOOP2);
//				if (h2Conf.isHaEnabled()) {
//					h2Conf.setHaNameNodes(new ArrayList<NodeConf>());
//					if (h2Conf.getNamenode() != null) {
//						h2Conf.getHaNameNodes().add(h2Conf.getNamenode());
//					}
//					if (h2Conf.getStandByNamenode() != null) {
//						h2Conf.getHaNameNodes()
//								.add(h2Conf.getStandByNamenode());
//					}
//				}
//				HadoopUtils.setNodeTypesForTemplate(h2Conf);
//				clusterConf.getClusterComponents().put(
//						Component.Name.HADOOP2, h2Conf);
//				template.setData(clusterConf);
//			}

			// getting cluster nodes.
//			List<NodeConf> nodes = template.getData().getNodeConfs();

			// creating copy of cluster nodes.
//			List<NodeConf> finalNodes = new ArrayList<NodeConf>(nodes);

			// iterating over the nodes to remove nodes which dont have values
			// in public and private ip.
//			for (NodeConf node : nodes) {
//				if ((node.getPublicIp() == null || node.getPublicIp().isEmpty())
//						&& (node.getPrivateIp() == null || node.getPrivateIp()
//								.isEmpty())) {
//					finalNodes.remove(node);
//				}
//			}
//			data.put("nodes", finalNodes);
			// putting false status
			map.put("status", true);
		} catch (Exception e) {
			e.printStackTrace();
			// putting false status
			map.put("status", false);
			// putting error as list of exception message.
			map.put("data",
					Collections.singletonMap("error",
							Collections.singletonList(e.getMessage())));
		}
		return map;

	}

	/**
	 * Getting the template by template name.
	 * 
	 * @param templateName
	 * @return
	 * @throws Exception
	 */
	public Template get(String templateName) throws Exception {
		try {
			// Getting the template by name.
			Template template = templateManager.getByPropertyValueGuarded(
					"name", templateName);

			// if null throwing exception of no template exists.
			if (template == null) {
				throw new Exception("No template exists with \"" + templateName
						+ "\" template name.");
			}
			// returning template.
			return template;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Method to get the templates based on user and technology.
	 * 
	 * @param user
	 * @param technology
	 * @return
	 */
	public List<Template> getAll(String technology) {
		// Property value map.
		Map<String, Object> propsValMap = new HashMap<String, Object>();
		// Putting technology name.
		propsValMap.put("technology", technology);
		// Getting the templates which are mapping with user and technology
		// name.
		List<Template> templates = templateManager
				.getAllByPropertyValue(propsValMap);

		return setDataAsNull(templates);
	}

	/**
	 * Method to set the data as null.
	 * 
	 * @param templates
	 * @return
	 */
	private List<Template> setDataAsNull(List<Template> templates) {
		// Setting template data as null.
		for (Template template : templates) {
			template.setDataBytes(null);
		}
		return templates;
	}

	/**
	 * Method to get the templates based on user and technology.
	 * 
	 * @param user
	 * @param technology
	 * @return
	 */
	public List<Template> getAll() {
		// Getting the templates which are mapping with user and technology
		// name.
		List<Template> templates = templateManager.getAll();

		return setDataAsNull(templates);
	}

	/**
	 * Method to delete the template by name.
	 * 
	 * @param name
	 */
	public void delete(String name) {
		try {
			// get template.
			Template template = get(name);
			// remove template using id.
			templateManager.remove(template.getId());
		} catch (Exception e) {
			// return if any exception comes.
			return;
		}
	}
}
