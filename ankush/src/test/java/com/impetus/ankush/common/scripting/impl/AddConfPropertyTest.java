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
package com.impetus.ankush.common.scripting.impl;

import org.junit.*;

import com.impetus.ankush2.common.scripting.impl.AddConfProperty;

import static org.junit.Assert.*;

/**
 * The Class AddConfPropertyTest.
 */
public class AddConfPropertyTest {

    /**
     * Test add conf property_1.
     *
     * @throws Exception
     *             the exception
     */
/*	
    @Test
    public void testAddConfProperty_1() throws Exception {
        String name = "replicationFactor";
        String value = "3";
        String xmlPath = "/home/hes/hadoop-1.0.4/conf/core-site.xml";
        String typeOfFile = "xml";

        AddConfProperty result = new AddConfProperty(name, value, xmlPath,typeOfFile);

        assertNotNull(result);
        assertEquals(
                "java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar com.impetus.ankush.agent.action.ActionHandler config xml add \""
                        + name + "\" \"" + value + "\" " + xmlPath,
                result.getCommand());
        assertEquals("Ankush Task Info...", result.getInfo());
    }
*/
    /**
     * Test get command_1.
     *
     * @throws Exception
     *             the exception
     */
/*	
    @Test
    public void testGetCommand_1() throws Exception {
        String name = "replicationFactor";
        String value = "3";
        String xmlPath = "/home/hes/hadoop-1.0.4/conf/core-site.xml";
        String typeOfFile = "xml";

        AddConfProperty fixture = new AddConfProperty(name, value, xmlPath,typeOfFile);

        String result = fixture.getCommand();

        assertEquals(
                "java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar com.impetus.ankush.agent.action.ActionHandler config xml add \""
                        + name + "\" \"" + value + "\" " + xmlPath, result);
    }
*/    
}
