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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;

/**
 * It is used as a configuration class for the alerts configuration.
 * 
 * @author hokam
 * 
 */
public class AlertsConf implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The refresh interval. */
	private Integer refreshInterval = Constant.Alerts.DEFAULT_REFRESH_INTERVAL;

	/** The inform all admins. */
	private boolean informAllAdmins = true;

	/** The mailing list. */
	private String mailingList = null;

	/** The thresholds. */
	private List<ThresholdConf> thresholds = new ArrayList<ThresholdConf>();

	/**
	 * Gets the refresh interval.
	 * 
	 * @return the refreshInterval
	 */
	public Integer getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * Sets the refresh interval.
	 * 
	 * @param refreshInterval
	 *            the refreshInterval to set
	 */
	public void setRefreshInterval(Integer refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	/**
	 * Sets the inform all admins.
	 * 
	 * @param informAllAdmins
	 *            the informAllAdmins to set
	 */
	public void setInformAllAdmins(boolean informAllAdmins) {
		this.informAllAdmins = informAllAdmins;
	}

	/**
	 * Checks if is inform all admins.
	 * 
	 * @return the informAllAdmins
	 */
	public boolean isInformAllAdmins() {
		return informAllAdmins;
	}

	/**
	 * Gets the mailing list.
	 * 
	 * @return the mailingList
	 */
	public String getMailingList() {
		if (mailingList != null) {
			return mailingList.replaceAll(",", ";");
		}
		return mailingList;
	}

	/**
	 * Sets the mailing list.
	 * 
	 * @param mailingList
	 *            the mailingList to set
	 */
	public void setMailingList(String mailingList) {
		this.mailingList = mailingList;
	}

	/**
	 * Gets the thresholds.
	 * 
	 * @return the thresholds
	 */
	public List<ThresholdConf> getThresholds() {
		if (thresholds == null || thresholds.isEmpty()) {

			// Setting default threshold values. It will be replaced with some
			// other code.
			thresholds = new ArrayList<ThresholdConf>();
			// Cpu Threshold values.
			ThresholdConf threshold1 = new ThresholdConf();
			threshold1.setMetricName(Constant.Alerts.Metric.CPU);
			threshold1.setAlertLevel(Constant.Alerts.DEFAULT_ALERT_LEVEL);
			threshold1.setWarningLevel(Constant.Alerts.DEFAULT_WARNING_LEVEL);
			// Memory Threshold values.
			ThresholdConf threshold2 = new ThresholdConf();
			threshold2.setMetricName(Constant.Alerts.Metric.MEMORY);
			threshold2.setAlertLevel(Constant.Alerts.DEFAULT_ALERT_LEVEL);
			threshold2.setWarningLevel(Constant.Alerts.DEFAULT_WARNING_LEVEL);
			// Adding cpu and memory threshold values.
			thresholds.add(threshold1);
			thresholds.add(threshold2);
		}
		return thresholds;
	}

	/**
	 * Sets the thresholds.
	 * 
	 * @param thresholds
	 *            the thresholds to set
	 */
	public void setThresholds(List<ThresholdConf> thresholds) {
		this.thresholds = thresholds;
	}

	@JsonIgnore
	public Map<String, ThresholdConf> getThresholdsMap() {
		Map<String, ThresholdConf> result = new HashMap<String, ThresholdConf>();
		for (ThresholdConf tConf : getThresholds()) {
			result.put(tConf.getMetricName(), tConf);
		}
		return result;
	}
}
