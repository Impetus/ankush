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
package com.impetus.ankush.hadoop;

import java.util.ArrayList;

import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringListener;
import com.impetus.ankush.hadoop.job.HadoopJob;

/**
 * @author hokam
 * 
 */
public class HadoopMonitoringListener implements MonitoringListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.MonitoringListener#preSave(com.impetus
	 * .ankush.common.framework.TechnologyData,
	 * com.impetus.ankush.common.framework.TechnologyData)
	 */
	@Override
	public void preSave(TechnologyData oldTechnologyData,
			TechnologyData newTechnologyData) {

		if (oldTechnologyData == null || newTechnologyData == null) {
			return;
		}

		HadoopMonitoringData oldMonitoringData = (HadoopMonitoringData) oldTechnologyData;
		HadoopMonitoringData newMonitoringData = (HadoopMonitoringData) newTechnologyData;

		ArrayList<HadoopJob> retiredJobs = oldMonitoringData.getRetiredJobs();
		ArrayList<HadoopJob> oldJobs = oldMonitoringData.getJobInfos();
		ArrayList<HadoopJob> currentJobs = new ArrayList<HadoopJob>();

		for (HadoopJob hadoopJob : newMonitoringData.getJobInfos()) {
			int indexOfJob = oldJobs.indexOf(hadoopJob);

			if (oldJobs.contains(hadoopJob)) {
				if (hadoopJob.isJobComplete()
						&& hadoopJob.getMapCompleted() == 0) {
					if (!retiredJobs.contains(hadoopJob)) {
						retiredJobs.add(oldJobs.get(indexOfJob));
					}
					continue;
				}
				oldJobs.remove(indexOfJob);
			}
			currentJobs.add(hadoopJob);
		}
		currentJobs.addAll(oldJobs);
		newMonitoringData.setJobInfos(currentJobs);
		newMonitoringData.setRetiredJobs(retiredJobs);
	}
}
