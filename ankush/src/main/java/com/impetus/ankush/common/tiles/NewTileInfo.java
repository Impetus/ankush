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
package com.impetus.ankush.common.tiles;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class NewTileInfo.
 * @author monika
 */
public class NewTileInfo {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The type. */
	private String tileType;
	
	/** The size. */
	private String size;
	
	/** The status. */
	private String status;
	
	/** The data. */
	private Map<String, Object> data;
	
	/**
	 * Instantiates a new new tile info.
	 */
	public NewTileInfo(){
		
	}

	/**
	 * Instantiates a new new tile info.
	 *
	 * @param tileType the type
	 * @param size the size
	 * @param status the status
	 * @param data the data
	 */
	public NewTileInfo(String tileType, String size, String status,
			Map<String, Object> data) {
		super();
		this.tileType = tileType;
		this.size = size;
		this.status = status;
		this.data = data;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getTileType() {
		return tileType;
	}

	/**
	 * Sets the type.
	 *
	 * @param tileType the type to set
	 */
	public void setTileType(String tileType) {
		this.tileType = tileType;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the data to set
	 */
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
