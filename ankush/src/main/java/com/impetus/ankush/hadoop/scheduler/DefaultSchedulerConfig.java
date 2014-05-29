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
package com.impetus.ankush.hadoop.scheduler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.impetus.ankush.hadoop.config.Parameter;

/**
 * The Class DefaultSchedulerConfig.
 *
 * @author mayur
 */
@XmlRootElement(name = "configuration")
public class DefaultSchedulerConfig extends SchedulerConfig {

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#toXML()
	 */
	@Override
	public String toXML() throws Exception {
		return "";
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#toParameters()
	 */
	@Override
	public List<Parameter> toParameters() {
		return new ArrayList<Parameter>();
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#allParameters()
	 */
	@Override
	public List<Parameter> allParameters() {
		return new ArrayList<Parameter>();
	}
}
