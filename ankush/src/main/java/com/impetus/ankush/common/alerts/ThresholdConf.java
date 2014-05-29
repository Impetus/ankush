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
package com.impetus.ankush.common.alerts;

import java.io.Serializable;

/**
 * It is used for defining the threshold values for the CPU and Memory.
 * 
 * @author hokam
 * 
 */
public class ThresholdConf implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The metric name. */
	private String metricName;
	
	/** The warning level. */
	private Double warningLevel;
	
	/** The alert level. */
	private Double alertLevel;

	/**
	 * Gets the metric name.
	 *
	 * @return the metricName
	 */
	public String getMetricName() {
		return metricName;
	}

	/**
	 * Sets the metric name.
	 *
	 * @param metricName the metricName to set
	 */
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	/**
	 * Gets the warning level.
	 *
	 * @return the warningLevel
	 */
	public Double getWarningLevel() {
		return warningLevel;
	}

	/**
	 * Sets the warning level.
	 *
	 * @param warningLevel the warningLevel to set
	 */
	public void setWarningLevel(Double warningLevel) {
		this.warningLevel = warningLevel;
	}

	/**
	 * Gets the alert level.
	 *
	 * @return the alertLevel
	 */
	public Double getAlertLevel() {
		return alertLevel;
	}

	/**
	 * Sets the alert level.
	 *
	 * @param alertLevel the alertLevel to set
	 */
	public void setAlertLevel(Double alertLevel) {
		this.alertLevel = alertLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ThresholdConf [metricName=" + metricName + ", warningLevel="
				+ warningLevel + ", alertLevel=" + alertLevel
				+ ", getMetricName()=" + getMetricName()
				+ ", getWarningLevel()=" + getWarningLevel()
				+ ", getAlertLevel()=" + getAlertLevel() + "]";
	}

}
