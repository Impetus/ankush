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
package com.impetus.ankush.common.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The Class Migration.
 *
 * @author mayur
 */
@Entity(name = "migration")
public class Migration extends BaseObject {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private Long id;
	
	/** The script name. */
	private String scriptName;
	
	/** The execution time. */
	private Date executionTime;

	/**
	 * Gets the id.
	 *
	 * @return Id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the script name.
	 *
	 * @return the sciptName
	 */
	public String getScriptName() {
		return scriptName;
	}

	/**
	 * Sets the script name.
	 *
	 * @param scriptName the sciptName to set
	 */
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	/**
	 * Gets the execution time.
	 *
	 * @return the executionTime
	 */
	public Date getExecutionTime() {
		return executionTime;
	}

	/**
	 * Sets the execution time.
	 *
	 * @param executionTime the executionTime to set
	 */
	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Migration)) {
			return false;
		}
		Migration other = (Migration) obj;
		if (scriptName == null) {
			if (other.scriptName != null) {
				return false;
			}
		} else if (!scriptName.equals(other.scriptName)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((scriptName == null) ? 0 : scriptName.hashCode());
		return result;
	}

}
