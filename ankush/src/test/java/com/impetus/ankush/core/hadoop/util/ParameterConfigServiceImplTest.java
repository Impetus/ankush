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
package com.impetus.ankush.core.hadoop.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.easymock.EasyMock;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.InputSource;

import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.config.Parameter;
import com.impetus.ankush.hadoop.service.impl.ParameterConfigServiceImpl;

/**
 * The Class ParameterConfigServiceImplTest.
 *
 * @author bgunjan
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SSHUtils.class)
public class ParameterConfigServiceImplTest {
	
	/** The parameter config service impl. */
	ParameterConfigServiceImpl parameterConfigServiceImpl;
	
	/** The cluster. */
	Cluster cluster;
	
	/** The parameter map. */
	Map<String, String> parameterMap;
	
	/** The hadoop cluster conf. */
	HadoopClusterConf hadoopClusterConf;
	
	/** The component conf path. */
	String componentConfPath;
	
	/** The hostname. */
	String hostname;
	
	/** The username. */
	String username;
	
	/** The password. */
	String password;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		parameterConfigServiceImpl = EasyMock.createMock(ParameterConfigServiceImpl.class);
		cluster = EasyMock.createMock(Cluster.class);
		parameterMap = new HashMap<String, String>();
		parameterMap.put("compName", "hadoop");
		hadoopClusterConf = (HadoopClusterConf) cluster.getClusterConf(); 
		componentConfPath = "/home/impetus/hes/hadoop/hadoop-1.0.4/conf/";
		hostname = "192.168.41.109";
		username = "impetus";
		password = "impetus";
	}
	
	/**
	 * Gets the component config files.
	 *
	 * @return the component config files
	 */
	@Test
	public void getComponentConfigFiles() {
		Map<String, Object> response = new HashMap<String, Object>();
		EasyMock.expect(
				parameterConfigServiceImpl.getComponentConfigFiles(cluster,
						parameterMap)).andReturn(response);

	}
	
	/**
	 * Test component config files.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testComponentConfigFiles() throws Exception {
		boolean authUsingPassword = true;
		List<String> confFiles = new ArrayList<String>();
		String fileName = "core-site.xml";
		String filePath = FileUtils
				.getSeparatorTerminatedPathEntry(componentConfPath) + fileName;
		
		// This is the way to tell PowerMock to mock all static methods of a given class
        PowerMock.mockStatic(SSHUtils.class);
        /*
         * The static method call to SSHUtils.listDirectory expectation. 
         */
        EasyMock.expect(SSHUtils.listDirectory(hostname, username, password,
				authUsingPassword, componentConfPath)).andReturn(confFiles);

        // replay the class
        PowerMock.replay(SSHUtils.class);

        JSON json = null;
		String output = null;
		PowerMock.mockStatic(SSHUtils.class);
		EasyMock.expect(SSHUtils.getFileContents(filePath, hostname, username,
						password, authUsingPassword)).andReturn(output);
        // Note how we replay the class, not the instance!
        PowerMock.replay(SSHUtils.class);

        XMLSerializer xmlSerializer = EasyMock.createMock(XMLSerializer.class);
		EasyMock.expect(xmlSerializer.read(output)).andReturn(json);
	}
	
	/**
	 * Gets the comp conf file params.
	 *
	 * @return the comp conf file params
	 */
	@Test
	public void getCompConfFileParams() {
		Map<String, List<Parameter>> response = new HashMap<String, List<Parameter>>();
		EasyMock.expect(
				parameterConfigServiceImpl.getCompConfFileParams(cluster,
						parameterMap)).andReturn(response);
	}

	/**
	 * Test get comp conf file params.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCompConfFileParams() throws Exception {

		String fileName = "core-site.xml";
		String filePath = componentConfPath+fileName;
		boolean authUsingPassword= true;
        String content = null;
		List child = new ArrayList();
		
		// This is the way to tell PowerMock to mock all static methods of a given class
        PowerMock.mockStatic(SSHUtils.class);
        /*
         * The static method call to SSHUtils.listDirectory expectation. 
         */
        EasyMock.expect(SSHUtils.getFileContents(filePath, hostname,
				username, password, authUsingPassword)).andReturn(content);

        // replay the class
        PowerMock.replay(SSHUtils.class);
        
		Document doc = EasyMock.createMock(Document.class);
		SAXBuilder builder = EasyMock.createMock(SAXBuilder.class);//new SAXBuilder();
		EasyMock.expect(builder.build(new InputSource(new ByteArrayInputStream(("<" + content).getBytes("utf-8"))))).andReturn(doc);
		Element elements = EasyMock.createMock(Element.class);
		EasyMock.expect(doc.getRootElement()).andReturn(elements);
		EasyMock.expect(elements.getChildren("property")).andReturn(child);
	}
	
	/**
	 * Update config file param.
	 */
	@Test
	public void updateConfigFileParam() {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterConfigServiceImpl.updateConfigFileParam(cluster, parameterMap);
		EasyMock.replay(parameterConfigServiceImpl);
	}
	
	/**
	 * Test add conf param.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAddConfParam() throws Exception {
		Result res = null;
		SSHExec connection = EasyMock.createMock(SSHExec.class);
		// This is the way to tell PowerMock to mock all static methods of a
		// given class
		PowerMock.mockStatic(SSHUtils.class);
		/*
		 * The static method call to SSHUtils.connectToNode expectation.
		 */
		EasyMock.expect(
				SSHUtils.connectToNode(hostname, username, password, password))
				.andReturn(connection);

		// replay the class
		PowerMock.replay(SSHUtils.class);

		AnkushTask add = EasyMock.createMock(AddConfProperty.class);
		EasyMock.expect(connection.exec(add)).andReturn(res);
	}
	
	/**
	 * Test delete conf param.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testDeleteConfParam() throws Exception {

		Result res = null;
		SSHExec connection = EasyMock.createMock(SSHExec.class);
		// This is the way to tell PowerMock to mock all static methods of a
		// given class
		PowerMock.mockStatic(SSHUtils.class);
		/*
		 * The static method call to SSHUtils.connectToNode expectation.
		 */
		EasyMock.expect(
				SSHUtils.connectToNode(hostname, username, password, password))
				.andReturn(connection);

		// replay the class
		PowerMock.replay(SSHUtils.class);

		AnkushTask delete = EasyMock.createMock(DeleteConfProperty.class);
		EasyMock.expect(connection.exec(delete)).andReturn(res);
	}
	
	/**
	 * Test edit conf param.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEditConfParam() throws Exception {

		Result res = null;
		SSHExec connection = EasyMock.createMock(SSHExec.class);
		// This is the way to tell PowerMock to mock all static methods of a
		// given class
		PowerMock.mockStatic(SSHUtils.class);
		/*
		 * The static method call to SSHUtils.connectToNode expectation.
		 */
		EasyMock.expect(
				SSHUtils.connectToNode(hostname, username, password, password))
				.andReturn(connection);

		// replay the class
		PowerMock.replay(SSHUtils.class);

		AnkushTask update = EasyMock.createMock(EditConfProperty.class);
		EasyMock.expect(connection.exec(update)).andReturn(res);
	}
}
