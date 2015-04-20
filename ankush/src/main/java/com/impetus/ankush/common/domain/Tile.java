///*******************************************************************************
// * ===========================================================
// * Ankush : Big Data Cluster Management Solution
// * ===========================================================
// * 
// * (C) Copyright 2014, by Impetus Technologies
// * 
// * This is free software; you can redistribute it and/or modify it under
// * the terms of the GNU Lesser General Public License (LGPL v3) as
// * published by the Free Software Foundation;
// * 
// * This software is distributed in the hope that it will be useful, but
// * WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// * See the GNU Lesser General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public License 
// * along with this software; if not, write to the Free Software Foundation, 
// * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
// ******************************************************************************/
///**
// * 
// */
//package com.impetus.ankush.common.domain;
//
//import java.io.Serializable;
//import java.util.Arrays;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Lob;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import org.apache.commons.lang.SerializationUtils;
//
//import com.impetus.ankush.common.tiles.TileInfo;
//
///**
// * The Class Tile.
// * 
// * @author nikunj
// */
//@Entity
//@Table(name = "tile")
//public class Tile extends BaseObject {
//
//	/** The Constant serialVersionUID. */
//	private static final long serialVersionUID = 1L;
//
//	/** The id. */
//	private Long id;
//
//	/** The cluster id. */
//	private Long clusterId;
//
//	/** The minor key. */
//	private String minorKey;
//
//	/** The tile info. */
//	private byte[] tileInfo;
//
//	/** The data. */
//	private byte[] data;
//
//	/** The destroy. */
//	private Boolean destroy = Boolean.FALSE;
//
//	/**
//	 * Gets the id.
//	 * 
//	 * @return the id
//	 */
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	public Long getId() {
//		return id;
//	}
//
//	/**
//	 * Sets the id.
//	 * 
//	 * @param id
//	 *            the id to set
//	 */
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	/**
//	 * Gets the cluster id.
//	 * 
//	 * @return the clusterId
//	 */
//	public Long getClusterId() {
//		return clusterId;
//	}
//
//	/**
//	 * Sets the cluster id.
//	 * 
//	 * @param clusterId
//	 *            the clusterId to set
//	 */
//	public void setClusterId(Long clusterId) {
//		this.clusterId = clusterId;
//	}
//
//	/**
//	 * Gets the tile info obj.
//	 * 
//	 * @return the tile info obj
//	 */
//	@Transient
//	public TileInfo getTileInfoObj() {
//		return (TileInfo) SerializationUtils.deserialize(getTileInfo());
//	}
//
//	/**
//	 * Sets the tile info obj.
//	 * 
//	 * @param tileInfo
//	 *            the new tile info obj
//	 */
//	public void setTileInfoObj(TileInfo tileInfo) {
//		setTileInfo(SerializationUtils.serialize(tileInfo));
//	}
//
//	/**
//	 * Gets the data obj.
//	 * 
//	 * @return the data obj
//	 */
//	@Transient
//	public Object getDataObj() {
//		return SerializationUtils.deserialize(getData());
//	}
//
//	/**
//	 * Sets the data obj.
//	 * 
//	 * @param data
//	 *            the new data obj
//	 */
//	public void setDataObj(Serializable data) {
//		setData(SerializationUtils.serialize(data));
//	}
//
//	/**
//	 * Gets the tile info.
//	 * 
//	 * @return the tileInfo
//	 */
//	@Lob
//	@Column(length = Integer.MAX_VALUE - 1)
//	private byte[] getTileInfo() {
//		return tileInfo;
//	}
//
//	/**
//	 * Sets the tile info.
//	 * 
//	 * @param tileInfo
//	 *            the tileInfo to set
//	 */
//	private void setTileInfo(byte[] tileInfo) {
//		this.tileInfo = tileInfo;
//	}
//
//	/**
//	 * Gets the data.
//	 * 
//	 * @return the data
//	 */
//	@Lob
//	@Column(length = Integer.MAX_VALUE - 1)
//	private byte[] getData() {
//		return data;
//	}
//
//	/**
//	 * Sets the data.
//	 * 
//	 * @param data
//	 *            the data to set
//	 */
//	private void setData(byte[] data) {
//		this.data = data;
//	}
//
//	/**
//	 * Gets the minor key.
//	 * 
//	 * @return the minorKey
//	 */
//	public String getMinorKey() {
//		return minorKey;
//	}
//
//	/**
//	 * Sets the minor key.
//	 * 
//	 * @param minorKey
//	 *            the minorKey to set
//	 */
//	public void setMinorKey(String minorKey) {
//		this.minorKey = minorKey;
//	}
//
//	/**
//	 * Gets the destroy.
//	 * 
//	 * @return the destroy
//	 */
//	public Boolean getDestroy() {
//		return destroy;
//	}
//
//	/**
//	 * Sets the destroy.
//	 * 
//	 * @param destroy
//	 *            the destroy to set
//	 */
//	public void setDestroy(Boolean destroy) {
//		this.destroy = destroy;
//	}
//
//	/* (non-Javadoc)
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj == null) {
//			return false;
//		}
//		if (!(obj instanceof Tile)) {
//			return false;
//		}
//		Tile other = (Tile) obj;
//		if (clusterId == null) {
//			if (other.clusterId != null) {
//				return false;
//			}
//		} else if (!clusterId.equals(other.clusterId)) {
//			return false;
//		}
//		if (!Arrays.equals(data, other.data)) {
//			return false;
//		}
//		if (destroy == null) {
//			if (other.destroy != null) {
//				return false;
//			}
//		} else if (!destroy.equals(other.destroy)) {
//			return false;
//		}
//		if (minorKey == null) {
//			if (other.minorKey != null) {
//				return false;
//			}
//		} else if (!minorKey.equals(other.minorKey)) {
//			return false;
//		}
//		if (!Arrays.equals(tileInfo, other.tileInfo)) {
//			return false;
//		}
//		return true;
//	}
//
//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((clusterId == null) ? 0 : clusterId.hashCode());
//		result = prime * result + Arrays.hashCode(data);
//		result = prime * result + ((destroy == null) ? 0 : destroy.hashCode());
//		result = prime * result
//				+ ((minorKey == null) ? 0 : minorKey.hashCode());
//		result = prime * result + Arrays.hashCode(tileInfo);
//		return result;
//	}
//
//}
