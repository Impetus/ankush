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
package com.impetus.ankush.controller.listener;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.junit.Assert;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.controller.listener.StartupListener;


/**
 * @author anand
 *
 */
public class StartupListenerTest {

    private MockServletContext sc = null;
    private ServletContextListener listener = null;
    private ContextLoaderListener springListener = null;

    /**
     * @throws java.lang.Exception
     */
//    @Before
    public void setUp() throws Exception {
        sc = new MockServletContext("");

        // initialize Spring
        sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, "classpath:/applicationContext-dao.xml, "
                + "classpath:/applicationContext-service.xml");

        springListener = new ContextLoaderListener();
        springListener.contextInitialized(new ServletContextEvent(sc));
        listener = new StartupListener();

    }

    /**
     * @throws java.lang.Exception
     */
//    @After
    public void tearDown() throws Exception {
        springListener = null;
        listener = null;
        sc = null;
    }

    /**
     * Test method for
     * {@link com.impetus.ankush.webapp.listener.StartupListener#contextInitialized(javax.servlet.ServletContextEvent)}.
     */
//    @Test
    public void testContextInitialized() {
        listener.contextInitialized(new ServletContextEvent(sc));

        Assert.assertNotNull(sc.getAttribute(Constant.Server.CONFIG));
        Map config = (Map) sc.getAttribute(Constant.Server.CONFIG);
        Assert.assertNotNull("config should not be null", config);

        Assert.assertNotNull(sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));

    }

}
