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
package com.impetus.ankush.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.impetus.ankush.common.domain.AppConf;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.service.impl.AnkushApplicationConf;
import com.impetus.ankush.common.service.impl.AppConfServiceImpl;

/**
 * The Class AppConfServiceImplTest.
 */
@ContextConfiguration(locations = {
	"classpath:/applicationContext-resources.xml",
	"classpath:/applicationContext-dao.xml",
	"classpath*:/applicationContext.xml",
	"classpath:**/applicationContext*.xml" })
public class AppConfServiceImplTest {

	/** The app conf service impl. */
	AppConfServiceImpl appConfServiceImpl;
	
	/** The common conf. */
	AnkushApplicationConf commonConf;
	
	/** The user manager. */
	UserManager userManager;
	
	/** The app conf manager. */
	GenericManager<AppConf, Long> appConfManager;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		appConfServiceImpl = EasyMock.createMock(AppConfServiceImpl.class);
		appConfManager = EasyMock.createMock(GenericManager.class);
		commonConf = EasyMock.createMock(AnkushApplicationConf.class);
		userManager = EasyMock.createMock(UserManager.class);
	}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	@Test
	public void getState() {
		EasyMock.expect(appConfServiceImpl.getState()).andReturn(null);
	}

	/**
	 * Test get state.
	 */
	@Test
	public void testGetState() {
		AppConf appConf = new AppConf();
		EasyMock.expect(appConfManager.getByPropertyValueGuarded("confKey",
				"email")).andReturn(appConf);
	}

	/**
	 * Manage common configuration.
	 */
	@Test
	public void manageCommonConfiguration() {
		Map<String, Object> response = new HashMap<String, Object>();
		EasyMock.expect(appConfServiceImpl.manageCommonConfiguration(commonConf)).andReturn(response);
	}
	
	/**
	 * Gets the common configuration.
	 *
	 * @return the common configuration
	 */
	@Test
	public void getCommonConfiguration() {
		Map<String, Object> response = new HashMap<String, Object>();
		EasyMock.expect(appConfServiceImpl.getCommonConfiguration()).andReturn(response);
		
		List<User> ankushUsers = new ArrayList<User>(); 
		EasyMock.expect(userManager.getUsers()).andReturn(ankushUsers);
		
		Map<String, Object> serverIpInfo = new HashMap<String, Object>();
		EasyMock.expect(appConfServiceImpl.getAppConf("serverIP")).andReturn(serverIpInfo);

		Map<String, String> emailMap = new HashMap<String, String>();
		EasyMock.expect(appConfServiceImpl.getAppConf("email")).andReturn(emailMap);
	}

	/**
	 * Gets the app conf.
	 *
	 * @return the app conf
	 */
	@Test
	public void getAppConf() {
		String key = "test";
		Map<String, Object> response = new HashMap<String, Object>();
		EasyMock.expect(appConfServiceImpl.getAppConf(key)).andReturn(response);
		
		AppConf appConf = new AppConf(); 
		EasyMock.expect(appConfManager.getByPropertyValueGuarded("confKey", key)).andReturn(appConf);
	}

	/**
	 * Sets the app conf.
	 */
	@Test
	public void setAppConf() {
		String key = "test";
		EasyMock.expect(appConfServiceImpl.setAppConf(key, "test")).andReturn(true);

		AppConf appConf = new AppConf();
		EasyMock.expect(appConfManager.getByPropertyValueGuarded("confKey", key)).andReturn(appConf);

		appConf.setConfKey(key);
		appConf.setObject("test");
		EasyMock.expect(appConfManager.save(appConf)).andReturn(appConf);
	}

	/**
	 * Gets the app access public url.
	 *
	 * @return the app access public url
	 */
	@Test
	public void getAppAccessPublicURL() {
		String url = "appAccessURL";
		EasyMock.expect(appConfServiceImpl.getAppAccessPublicURL()).andReturn(url);
	}

}
