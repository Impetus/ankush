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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiLineTileInfo.
 */
public class MultiLineTileInfo {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The data. */
	private String data;
	
	/** The status. */
	private String status;
	
	/** The type. */
	private String tileType;
	
	/** The url. */
	private String url;
	
	private String header;
	
	
	/** The lines. */
	private List<TileLine> tileLines = new ArrayList<TileLine>();
	
	private Map map = new HashMap();
	
	public MultiLineTileInfo(String header,String data,String status,String tileType,String url,List<TileLine> tileLines){
		this.header = header;
		this.data = data;
		this.status = status;
		this.tileType = tileType;
		this.url = url;
		this.tileLines = tileLines;
		/*for(int i =0;i<tileLines.length;i++){
//			this.map.put("line"+i,lines[i]);
			this.lines.add(tileLines[i]);
		}*/
		
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the type
	 */
	public String getTileType() {
		return tileType;
	}

	/**
	 * @param tileType the type to set
	 */
	public void setTileType(String tileType) {
		this.tileType = tileType;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the tileLines
	 */
	public List<TileLine> getTileLines() {
		return tileLines;
	}

	/**
	 * @param tileLines the tileLines to set
	 */
	public void setTileLines(List<TileLine> tileLines) {
		this.tileLines = tileLines;
	}

	

//	/**
//	 * @return the map
//	 */
//	public Map getMap() {
//		return map;
//	}

		
	
}
