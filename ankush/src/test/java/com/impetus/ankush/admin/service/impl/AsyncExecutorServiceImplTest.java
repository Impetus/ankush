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

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.TaskScheduler;

import com.impetus.ankush.common.service.impl.AsyncExecutorServiceImpl;

public class AsyncExecutorServiceImplTest {
    
    private AsyncExecutorServiceImpl executorService;
    private TaskScheduler taskScheduler;
    

    @Before
    public void setUp() throws Exception {
        executorService = new AsyncExecutorServiceImpl();
        
        taskScheduler = EasyMock.createMock(TaskScheduler.class);
        executorService.setTaskScheduler(taskScheduler);
        
    }

    @Test
    public void testschedule(){
        Runnable runnable = new Thread();
        Date date = new Date();
        
        EasyMock.expect(taskScheduler.schedule(runnable, date)).andReturn(EasyMock.createMock(ScheduledFuture.class));
        EasyMock.replay(taskScheduler);
        
        executorService.schedule(runnable, date);
    }

}
