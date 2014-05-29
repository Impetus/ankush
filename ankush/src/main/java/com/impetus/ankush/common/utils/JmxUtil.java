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
package com.impetus.ankush.common.utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.impetus.ankush.common.exception.AnkushException;

//TODO: Auto-generated Javadoc
/**
 * The Class JMXUtil.
 */
public class JmxUtil {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(JmxUtil.class);

	/** The node ip. */
	private String nodeIp = "";

	/** The jmx port. */
	private int jmxPort;

	/** The jmx connector. */
	private JMXConnector jmxConnector = null;

	/** The mbean server connection. */
	private MBeanServerConnection mbeanServerConnection = null;


	/**
	 * Instantiates a new jMX util.
	 *
	 * @param nodeIp the node ip
	 * @param jmxPort the jmx port
	 * @throws Exception 
	 */

	public JmxUtil(String nodeIp,int jmxPort) throws Exception {
		this.nodeIp = nodeIp;
		this.jmxPort = jmxPort;
	}

	public MBeanServerConnection connect() throws AnkushException{
		try {
			jmxConnector = this.jmxConnection(this.nodeIp,this.jmxPort);
			if(jmxConnector != null){
				this.mbeanServerConnection = jmxConnector.getMBeanServerConnection();
			}
		}catch (IOException e) {
			logger.info("Mbean Server Connection is not available at Node..."+nodeIp);
			logger.error(e.getMessage());
		}catch(Exception e){
			logger.info("Mbean Server Connection is not available at Node..."+nodeIp);
			logger.error(e.getMessage());
		}

		return this.mbeanServerConnection;
	}

	public void disconnect(){
		try{
			if(this.jmxConnector != null){
				this.jmxConnector.close();
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}


	}

	/**
	 * Jmx connection.
	 *
	 * @param nodeIp the node ip
	 * @param jmxPort the jmx port
	 * @return the jMX connector
	 * @throws Exception 
	 */
	private JMXConnector jmxConnection(String nodeIp,int jmxPort){
		JMXConnector connector = null;
		try {
			JMXServiceURL address = new JMXServiceURL(
					"service:jmx:rmi:///jndi/rmi://" + nodeIp + ":"
							+ jmxPort + "/jmxrmi");
			connector = JMXConnectorFactory.connect(address);
		} catch (Exception e) {
			if (e.getMessage().contains("java.net.ConnectException")) {
				logger.info("Host unavailable at Node..."+nodeIp);
				logger.error(e.getMessage());
				//				throw new AnkushException("Host unavailable-" + nodeIp);
			}
		}
		return connector;
	}

	public Set<ObjectName> getObjectSetFromPatternString(String patternStr){
		Set<ObjectName> objectSet  = null;
		try {
			objectSet = mbeanServerConnection.queryNames(new ObjectName(patternStr),
					null);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return objectSet;
	}

	public Object getResultObjectFromOperation(ObjectName name, String operationName, Object params[],String[] signature){
		Object result  = null;
		try {
			if(mbeanServerConnection != null){
				result = mbeanServerConnection.invoke(name,
						operationName, params,
						signature);
			}

		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return result;
	}


	/**
	 * Gets the attribute.
	 *
	 * @param objName the obj name
	 * @param attr the attr
	 * @return the attribute
	 */
	public Object getAttribute(ObjectName objName,String attr){
		Object attrVal = null;
		try {
			if(mbeanServerConnection != null){
				attrVal = mbeanServerConnection.getAttribute(objName, attr);
			}
		} catch (AttributeNotFoundException e) {
			logger.info(e.getMessage());
		} catch (InstanceNotFoundException e) {
			logger.info(e.getMessage());
		} catch (MBeanException e) {
			logger.info(e.getMessage());
		} catch (ReflectionException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return attrVal;
	}

	/**
	 * Gets the attribute name list.
	 *
	 * @param objName the obj name
	 * @return the attribute name list
	 */
	public MBeanAttributeInfo[] getAttributeNameList(ObjectName objName){
		MBeanAttributeInfo[] mbeanAttributeInfo = null;
		try {
			MBeanInfo mbeanInfo = mbeanServerConnection.getMBeanInfo(objName);
			mbeanAttributeInfo = mbeanInfo.getAttributes();
		} catch (InstanceNotFoundException e) {
			logger.info(e.getMessage());
		} catch (IntrospectionException e) {
			logger.info(e.getMessage());
		} catch (ReflectionException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return mbeanAttributeInfo;
	}

	/**
	 * Gets the operations.
	 * 
	 * @param objName
	 *            the obj name
	 * @param attr
	 *            the attr
	 * @return the attribute
	 */
	public MBeanOperationInfo[] getOperations(ObjectName objName) {
		MBeanInfo mBeanInfo = null;
		MBeanOperationInfo[] mBeanOperationInfo = null;
		try {
			if (mbeanServerConnection != null) {
				mBeanInfo = mbeanServerConnection.getMBeanInfo(objName);
				mBeanOperationInfo = mBeanInfo.getOperations();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return mBeanOperationInfo;
	}

	/**
	 * Gets the attribute name value map.
	 *
	 * @param objName the obj name
	 * @return the attribute name value map
	 */
	public Map<String, Object> getDoubleAttributes(ObjectName objName){
		Map<String, Object> attrMap = null;
		try {
			MBeanInfo mbeanInfo = mbeanServerConnection.getMBeanInfo(objName);
			MBeanAttributeInfo[] mbeanAttributeInfo =mbeanInfo.getAttributes();
			attrMap = new HashMap<String, Object>();
			DecimalFormat df = new DecimalFormat("###.##");
			for (int i = 0; i < mbeanAttributeInfo.length; i++) {
				String attrName = mbeanAttributeInfo[i].getName(); 
				Object attrValue = getAttribute(objName,mbeanAttributeInfo[i].getName());
				if(mbeanAttributeInfo[i].getType().equals("double")){
					attrValue = df.format((Double)getAttribute(objName,mbeanAttributeInfo[i].getName()));
				}
				attrMap.put(attrName,attrValue);
			}
		} catch (InstanceNotFoundException e) {
			logger.info(e.getMessage());
		} catch (IntrospectionException e) {
			logger.info(e.getMessage());
		} catch (ReflectionException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return attrMap;
	}

	/**
	 * Gets the attribute name value map.
	 *
	 * @param objName the obj name
	 * @return the attribute name value map
	 */
	public Map<String, Object> getAttributes(ObjectName objName){
		Map<String, Object> attrMap = null;
		try {
			MBeanInfo mbeanInfo = mbeanServerConnection.getMBeanInfo(objName);
			MBeanAttributeInfo[] mbeanAttributeInfo =mbeanInfo.getAttributes();
			attrMap = new HashMap<String, Object>();
			for (int i = 0; i < mbeanAttributeInfo.length; i++) {
				String attrName = mbeanAttributeInfo[i].getName(); 
				Object attrValue = getAttribute(objName,mbeanAttributeInfo[i].getName());
				attrValue = getAttribute(objName,mbeanAttributeInfo[i].getName());
				attrMap.put(attrName,attrValue);
			}
		} catch (InstanceNotFoundException e) {
			logger.info(e.getMessage());
		} catch (IntrospectionException e) {
			logger.info(e.getMessage());
		} catch (ReflectionException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(e.getMessage());
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return attrMap;
	}
}
