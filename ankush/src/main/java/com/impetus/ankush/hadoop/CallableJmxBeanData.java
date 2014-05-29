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
package com.impetus.ankush.hadoop;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Akhil
 *
 */
public class CallableJmxBeanData implements Callable<Map<String, Object>> {

	String nodeIp;
	
	int clientPort;
	
	String beanName;
	
	/**
	 * Instantiates a new callable jmx bean data.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @param beanName the bean name
	 */
	public CallableJmxBeanData(String nodeIp, int clientPort, String beanName) {
		this.nodeIp = nodeIp;
		this.clientPort = clientPort;
		this.beanName = beanName;
	}
	
	@Override
	public Map<String, Object> call() throws Exception {
		return HadoopUtils.getJmxBeanData(nodeIp, clientPort, beanName);
	}

}
