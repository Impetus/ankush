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

import com.impetus.ankush.common.constant.Constant;

import static org.junit.Assert.*;

/**
 * The Class ReadConfPropertyTest.
 */
public class ReadConfPropertyTest {
    
    /**
     * Test read conf property_1.
     *
     * @throws Exception the exception
     */
    @Test
    public void testReadConfProperty_1()
        throws Exception {
        String propertyName = "propertyName";
        String xmlPath = "core-site.xml";

        ReadConfProperty result = new ReadConfProperty(propertyName, xmlPath,Constant.File_Extension.XML);

        assertNotNull(result);
        assertEquals("java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar com.impetus.ankush.agent.action.ActionHandler config xml read propertyName core-site.xml", result.getCommand());
        assertEquals("Ankush Task Info...", result.getInfo());
    }

    /**
     * Test get command_1.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetCommand_1()
        throws Exception {
        String propertyName = "propertyName";
        String xmlPath = "core-site.xml";
        ReadConfProperty fixture = new ReadConfProperty(propertyName, xmlPath,Constant.File_Extension.XML);

        String result = fixture.getCommand();
        assertEquals("java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar com.impetus.ankush.agent.action.ActionHandler config xml read propertyName core-site.xml", result);
    }
}
