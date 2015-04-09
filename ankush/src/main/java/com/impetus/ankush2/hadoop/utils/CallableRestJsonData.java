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

import java.util.concurrent.Callable;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.AnkushRestClient;

/**
 * @author Akhil
 *
 */
public class CallableRestJsonData implements Callable<JSONObject> {

	String url;
	
	public CallableRestJsonData(String url) {
		this.url = url;
	}
	
	@Override
	public JSONObject call() throws Exception {
		JSONObject json = null;
		AnkushRestClient restClient = new AnkushRestClient();
		String data = restClient.getRequest(this.url);
		if (data == null) {
			throw new AnkushException("Could not fetch data from " + url);
		} else {
			json = (JSONObject) new JSONParser().parse(data);
		}
		return json;
	}
}
