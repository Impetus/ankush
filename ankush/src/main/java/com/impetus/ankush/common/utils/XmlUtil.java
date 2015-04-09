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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush2.common.scripting.impl.AddConfProperty;
import com.impetus.ankush2.hadoop.utils.Parameter;

/**
 * The Class XmlUtil.
 * 
 * @author mayur
 */
public class XmlUtil {

	/** The log. */
	private static final AnkushLogger LOG = new AnkushLogger(XmlUtil.class);

	/**
	 * Adds the configuration properties.
	 * 
	 * @param nameValuePair
	 *            the name value pair
	 * @param filePath
	 *            the file path
	 * @param connection
	 *            the connection
	 * @return true, if successful
	 */
	public static boolean addConfigurationProperties(
			Map<String, Object> nameValuePair, String filePath,
			SSHExec connection) {

		AnkushTask configureXml = null;
		Result result = null;

		// iterating over map
		for (Map.Entry<String, Object> entry : nameValuePair.entrySet()) {
			LOG.debug("Key = " + entry.getKey() + ", Value = "
					+ entry.getValue());

			// configuring xml
			configureXml = new AddConfProperty(entry.getKey(),
					(String) entry.getValue(), filePath,
					Constant.File_Extension.XML);
			try {
				result = connection.exec(configureXml);
				if (!result.isSuccess) {
					return false;
				}
			} catch (TaskExecFailException e) {
				LOG.error(e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates the configuraton xml.
	 * 
	 * @param filePath
	 *            the file path
	 * @param connection
	 *            the connection
	 * @return true, if successful
	 */
	public static boolean createConfiguratonXml(String filePath,
			SSHExec connection) {

		StringBuilder fileContents = new StringBuilder();
		fileContents.append("<configuration>").append("\n")
				.append("</configuration>");

		AnkushTask createConfFile = new AppendFileUsingEcho(fileContents.toString(),
				filePath);

		try {
			return connection.exec(createConfFile).isSuccess;
		} catch (TaskExecFailException e) {
			LOG.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static List<Parameter> loadXMLParameters(String str) {
		// parameters list.
		List<Parameter> parameters = new ArrayList<Parameter>();
		try {
			// creating sax builder obj.
			SAXBuilder builder = new SAXBuilder();
			// jdom document object.
			org.jdom.Document doc = (org.jdom.Document) builder
					.build(new InputSource(new ByteArrayInputStream((str)
							.getBytes("utf-8"))));
			// getting root element.
			Element elements = ((org.jdom.Document) doc).getRootElement();
			// getting child elements.
			List child = elements.getChildren("property");
			// iterating over the childs.
			for (int index = 0; index < child.size(); index++) {
				// getting element.
				Element e = (Element) child.get(index);
				// getting name property value.
				String name = getTagContent(e, "name");
				// getting value property value.
				String value = getTagContent(e, "value");
				// getting description property value.
				String description = getTagContent(e, "description");
				// getting final property value.
				String finalVal = getTagContent(e, "final");
				Boolean isfinal = null;
				// if finalVal is not empty.
				if (!finalVal.isEmpty()) {
					isfinal = Boolean.parseBoolean(finalVal);
				}
				// adding parameter.
				parameters.add(new Parameter(name, value, "", description,
						isfinal));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// returning parameters.
		return parameters;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static Map loadConfigXMLParameters(String filePath, String root,
			List<String> subItems) {
		// map of item.
		Map items = new HashMap();
		try {
			// creating sax builder obj.
			SAXBuilder builder = new SAXBuilder();
			// getting file object.
			File xml = new File(filePath);
			// input file stream.
			InputStream inputStream = new FileInputStream(xml);
			// jdom document object.
			org.jdom.Document doc = builder.build(inputStream);
			// getting root element.
			Element elements = doc.getRootElement();
			// getting child elements.
			List child = elements.getChildren(root);
			// iterating over the childs.
			for (int index = 0; index < child.size(); index++) {
				// getting element.
				Element e = (Element) child.get(index);
				// creating empty map.
				Map map = new HashMap();
				// iterating over the element properties.
				for (String subItem : subItems) {
					// getting element values.
					String value = getTagContent(e, subItem);
					// putting element value.
					map.put(subItem, value);
				}
				// putting map against the attribute value.
				items.put(e.getAttribute("id").getValue(), map);
			}
			// closing input stream.
			inputStream.close();
		} catch (Exception e) {
			// printing stack trace.
			e.printStackTrace();
		}
		// returning items.
		return items;
	}
	
	/**
	 * Gets the tag content.
	 * 
	 * @param element
	 *            the element
	 * @param tagName
	 *            the tag name
	 * @return the tag content
	 */
	public static String getTagContent(Element element, String tagName) {
		String content = "";
		Element e = element.getChild(tagName);
		if (e != null) {
			content = e.getValue();
		}
		return content;
	}

}
