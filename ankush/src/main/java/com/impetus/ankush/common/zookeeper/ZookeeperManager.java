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
package com.impetus.ankush.common.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class ZookeeperManager implements Watcher {

	/** The Constant connectedSignal. */
	private CountDownLatch connectedSignal = new CountDownLatch(1);
	
	/** The Constant SESSION_TIMEOUT. */
	private static final int SESSION_TIMEOUT = 500000;
	
	/** The Constant zk. */
	private ZooKeeper zk;
	
	/** The Constant lstDefaultACL. */
	private final static List<ACL> lstDefaultACL = Ids.OPEN_ACL_UNSAFE;
	
	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			connectedSignal.countDown();
		}
	}

	public void connect(String hosts) throws Exception {
		try {
			System.out.println("Connecting to Zookeeper Host : " + hosts);
			zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
			System.out.println("Connected to Host " + hosts + " Successfully");
			connectedSignal.await();
		} catch (Exception e) {
			throw (new Exception("Unable to Connect to Zookeeper Host " + hosts
					+ " : " + e.getMessage()));
		}
	}
	
	public void create(String groupName, byte[] data, CreateMode mode) throws Exception {

		String path = "/" + groupName;
		String createdPath = zk.create(path, data, lstDefaultACL,
				mode);
		System.out.println("zk.getState(): ");
		System.out.println("Created " + createdPath);
	}
	
	public byte[] getData(String znodeName , boolean watch) throws Exception {
		String path = "/" + znodeName;
		byte[] objData = null;
		try {
			objData = zk.getData(path, watch, this.getStat(path));
		} catch (Exception e) {
			throw (new Exception("Unable to get Data for path " + path
					+ " : " + e.getMessage()));
		}
		return objData;
	}
	
	public Stat getStat(String zkPath) throws Exception {
		Stat objNodeStat = null;
		try {
			objNodeStat = zk.exists(zkPath, false);
			if (objNodeStat == null)
				throw (new Exception("Path does not exists"));
		} catch (Exception e) {
			throw (new Exception("Unable to get stat for path " + zkPath
					+ " : " + e.getMessage()));
		}
		return objNodeStat;
	}
	
	public void delete(String groupName) throws KeeperException,
	InterruptedException {
		String path = "/" + groupName;
		try {
			List<String> children = zk.getChildren(path, false);
			for (String child : children) {
				zk.delete(path + "/" + child, -1);
			}
			zk.delete(path, -1);
		} catch (KeeperException.NoNodeException e) {
			System.out.printf("Group %s does not exist\n", groupName);
			System.exit(1);
		}
	}
	
	public void close() throws InterruptedException {
		zk.close();
	}
	
}
