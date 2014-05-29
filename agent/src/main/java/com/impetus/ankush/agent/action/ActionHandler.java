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
package com.impetus.ankush.agent.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class ActionHandler.
 *
 * @author mayur
 */
public class ActionHandler {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String... args) {
		// Get action id 
		String actionId = args[0];
		// Create action by id 
		Actionable obj = ActionFactory.getInstanceById(actionId);
		// remove first element(action id) from array
		List<String> argsList = new ArrayList<String>(Arrays.asList(args));
		argsList.remove(0);
		// Call execute on Actionable object
		obj.execute(argsList);
	}
	
}
