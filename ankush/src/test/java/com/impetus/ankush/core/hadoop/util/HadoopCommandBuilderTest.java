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
package com.impetus.ankush.core.hadoop.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.impetus.ankush.hadoop.HadoopCommandBuilder;

public class HadoopCommandBuilderTest {

	private HadoopCommandBuilder builder = new HadoopCommandBuilder();

	@Test
	public void testCreateArchiveCmd() {
		assertEquals("Incorrect command returned",
				"archive -archiveName abc.har -p def /dir/src /dir/dest",
				builder.createArchiveCmd("abc", "def", "/dir/src", "/dir/dest"));
	}

	@Test
	public void testCreateDistcpCmd() {
		assertEquals("Incorrect command returned",
				"distcp -a -b -c /source/url dest/url",
				builder.createDistcpCmd("/source/url", "dest/url", "-a -b -c"));
	}

	@Test
	public void testCreateBalancerCmd() {
		assertEquals("Incorrect command returned", "balancer -threshold 1234",
				builder.createBalancerCmd("1234"));
	}

	@Test
	public void testCreateFsckCmd() {
		assertEquals("Incorrect command returned",
				"fsck -a -b -c /home/path -x -y -z",
				builder.createFsckCmd("/home/path", "-a -b -c", "-x -y -z"));
	}

}
