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
package com.impetus.ankush2.hadoop.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class ParameterConfigServiceImpl.
 * 
 * @author bgunjan
 */
public class ConfigParameterUtils {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			ConfigParameterUtils.class);

	/**
	 * Load xml parameters.
	 * 
	 * @param str
	 *            the str
	 * @return the list
	 */
	public static List<Parameter> loadXMLParameters(String str) {
		List<Parameter> parameters = new ArrayList<Parameter>();
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new InputSource(
					new ByteArrayInputStream(str.getBytes("utf-8"))));

			Element elements = doc.getRootElement();
			List child = elements.getChildren("property");
			for (int index = 0; index < child.size(); index++) {
				Element e = (Element) child.get(index);
				String name = getTagContent(e, "name");
				String value = getTagContent(e, "value");
				parameters.add(new Parameter(name, value, "", "", false));
			}
		} catch (Exception e) {
			logger.error("ConfigLoader#loadXMLParameters " + e);
		}
		logger.debug("Loading XML Config file Parameters Done..!");
		return parameters;
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
	private static String getTagContent(Element element, String tagName) {
		String content = "";
		Element e = element.getChild(tagName);
		if (e != null) {
			content = e.getValue();
		}
		return content;
	}
}
