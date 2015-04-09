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
package com.impetus.ankush2.framework.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.impetus.ankush2.constant.Constant;

public class ProgressConfig implements Configuration {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Constant.Operation.Status state = Constant.Operation.Status.WAITING;
	private Integer progress = 0;
	Map<String, ProgressConfig> breakup = new HashMap<String, ProgressConfig>();

	public ProgressConfig() {
	}

	public ProgressConfig(Collection<String> components) {
		for (String component : components) {
			breakup.put(component, new ProgressConfig());
		}
	}

	public void setProgress(String component, int percentage) {
		setProgress(component, percentage, false);
	}

	public void setProgress(String component, int percentage, boolean isError) {
		if (!breakup.containsKey(component)) {
			return;
		}
		breakup.get(component).setProgress(percentage);
		if (isError) {
			breakup.get(component).setState(Constant.Operation.Status.ERROR);
		}

		progress = 0;
		for (ProgressConfig comp : breakup.values()) {
			progress += comp.getProgress();
		}
		setProgress(progress / breakup.size());
	}

	/**
	 * @return the state
	 */
	public Constant.Operation.Status getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(Constant.Operation.Status state) {
		this.state = state;
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(int progress) {

		if (progress <= 0) {
			progress = 0;
			setState(Constant.Operation.Status.WAITING);
		} else if (progress >= 100) {
			progress = 100;
			setState(Constant.Operation.Status.COMPLETED);
		} else {
			setState(Constant.Operation.Status.INPROGRESS);
		}
		this.progress = progress;
	}

	/**
	 * @return the breakup
	 */
	public Map<String, ProgressConfig> getBreakup() {
		return breakup;
	}

	/**
	 * @param breakup
	 *            the breakup to set
	 */
	public void setBreakup(Map<String, ProgressConfig> breakup) {
		this.breakup = breakup;
	}

}
