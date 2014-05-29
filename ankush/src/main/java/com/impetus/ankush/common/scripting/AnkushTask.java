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
package com.impetus.ankush.common.scripting;

import java.util.Iterator;

import net.neoremind.sshxcute.task.CustomTask;

/**
 * The Class AnkushTask.
 *
 * @author mayur
 */
public abstract class AnkushTask extends CustomTask {

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#checkExitCode(int)
	 */
	@Override
	protected Boolean checkExitCode(int exitCode) {
		return exitCode == 0;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#checkStdOut(java.lang.String)
	 */
	@Override
	protected Boolean checkStdOut(String stdout) {
		Iterator<String> iter = err_sysout_keyword_list.iterator();
		while (iter.hasNext()) {
			if (stdout.contains(iter.next())) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getInfo()
	 */
	@Override
	public String getInfo() {
		return "Ankush Task Info...";
	}
}
