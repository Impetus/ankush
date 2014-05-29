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
package com.impetus.ankush.storm;

/**
 * 
 * @author mayur
 *
 */
public class Spout extends BaseComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double completeLatency;

	/**
	 * @return the completeLatency
	 */
	public double getCompleteLatency() {
		return completeLatency;
	}

	/**
	 * @param completeLatency the completeLatency to set
	 */
	public void setCompleteLatency(double completeLatency) {
		this.completeLatency = completeLatency;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Spout [completeLatency=" + completeLatency
				+ ", getCompleteLatency()=" + getCompleteLatency()
				+ ", getId()=" + getId() + ", getNumberOfExecutors()="
				+ getNumberOfExecutors() + ", getNumberOfTasks()="
				+ getNumberOfTasks() + ", getEmitted()=" + getEmitted()
				+ ", getTransferred()=" + getTransferred() + ", getAcked()="
				+ getAcked() + ", getFailed()=" + getFailed()
				+ ", getLastError()=" + getLastError() + ", getParallelism()="
				+ getParallelism() + ", getType()=" + getType()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
