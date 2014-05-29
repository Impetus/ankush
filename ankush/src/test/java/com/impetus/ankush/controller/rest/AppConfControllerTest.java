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
package com.impetus.ankush.controller.rest;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.impetus.ankush.common.controller.rest.AppConfController;
import com.impetus.ankush.common.service.AppConfService;

@ContextConfiguration(locations = {
		"classpath:/applicationContext-resources.xml",
		"classpath:/applicationContext-dao.xml",
		"classpath*:/applicationContext.xml",
		"classpath:**/applicationContext*.xml" })
public class AppConfControllerTest {
	private MockMvc mockMvc;

	private AppConfController appConfController;
	private AppConfService appConfService;

	@Before
	public void setUp() throws Exception {
		appConfController = new AppConfController();
		appConfService = EasyMock.createMock(AppConfService.class);
		appConfController.setConfigService(appConfService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(appConfController)
				.build();
	}

	@Test
	public void testGetDefaultConf() throws Exception {
		String key = "technology";
		Map result = new HashMap();
		Map tech = new HashMap();
		tech.put("Hadoop", "Hadoop");
		tech.put("Storm", "Storm");
		result.put(key, tech);

		EasyMock.expect(appConfService.getAppConf(key)).andReturn(result);
		EasyMock.replay(appConfService);

		mockMvc.perform(
				get("/app/conf/technology").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(
						jsonPath("$.output.technology.Hadoop").value("Hadoop"))
				.andExpect(jsonPath("$.output.technology.Storm").value("Storm"));

	}
}
