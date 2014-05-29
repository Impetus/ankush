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

import org.codehaus.jackson.map.ObjectMapper;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import com.impetus.ankush.hadoop.controller.ClusterCommandsController;
import com.impetus.ankush.hadoop.service.ClusterCommandExecutionService;

@ContextConfiguration(locations = {
        "classpath:/applicationContext-resources.xml",
        "classpath:/applicationContext-dao.xml",
        "classpath*:/applicationContext.xml",
        "classpath:**/applicationContext*.xml" })
public class ClusterCommandsControllerTest {
    
    private MockMvc mockMvc;
    private ClusterCommandExecutionService commandExecutionService;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        ClusterCommandsController controller = new ClusterCommandsController();
        
        commandExecutionService = EasyMock.createMock(ClusterCommandExecutionService.class);
        controller.setCommandExecutionService(commandExecutionService);
        
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testExecuteHandoopCommand() throws Exception {/*
        Map<String, String> commandParams = new HashMap<String, String>();
        commandParams.put("archiveName", "abc");
        commandParams.put("sourceDirectory", "/dir/src");
        commandParams.put("destinationDirectory", "/dir/dest");
        
        EasyMock.expect(commandExecutionService.executeHadoopCommand(1L, "archive", commandParams)).andReturn(1234);
        EasyMock.replay(commandExecutionService);
        
        mockMvc.perform(post("/cluster/1/commands/hadoop/archive")
                .body(mapper.writeValueAsBytes(commandParams))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().mimeType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    */}
    
    @Test
    public void testExecuteHandoopCommandMissingParam() throws Exception {/*
        Map<String, String> commandParams = new HashMap<String, String>();
        commandParams.put("archiveName", "abc");
        commandParams.put("sourceDirectory", "/dir/src");

        mockMvc.perform(post("/cluster/1/commands/hadoop/archive")
                .body(mapper.writeValueAsBytes(commandParams))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().mimeType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    */}
    
    @Test
    public void testExecuteHandoopCommandInvalidCommand() throws Exception {/*
        Map<String, String> commandParams = new HashMap<String, String>();
        commandParams.put("archiveName", "abc");
        commandParams.put("sourceDirectory", "/dir/src");

        mockMvc.perform(post("/cluster/1/commands/hadoop/unknown")
                .body(mapper.writeValueAsBytes(commandParams))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().mimeType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    */}

}
