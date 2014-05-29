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
package com.impetus.ankush.agent.action.impl;

import java.util.List;

import com.impetus.ankush.agent.action.Actionable;
import com.impetus.ankush.agent.action.Manipulatable;

public class ConfigHandler implements Actionable {

	/**
	 * The Enum Operation.
	 */
	private enum Operation {

		/** The add. */
		add,
		/** The delete. */
		delete,
		/** The edit. */
		edit,
		/** The invalid. */
		invalid,
		/** The read. */
		read;

		/**
		 * From string.
		 * 
		 * @param string
		 *            the string
		 * @return the operation
		 */
		public static Operation fromString(String string) {
			try {
				return Operation.valueOf(string);
			} catch (final Exception ex) {
				return invalid;
			}
		}
	};

	private Manipulatable manipulatable;

	/**
	 * Adds the node.
	 * 
	 * @param args
	 *            the args
	 * @return true, if successful
	 */
	private boolean addNode(List<String> args) {
		if (args.size() != 3) {
			System.err
					.println("Usage - <xml/properties/yaml> <add> propertyName propertyValue filePath");
			return false;
		}
		final String propertyName = args.get(0);
		final String propertyValue = args.get(1);
		final String xmlPath = args.get(2);
		return manipulatable.writeConfValue(xmlPath, propertyName,
				propertyValue);
	}

	/**
	 * Delete node.
	 * 
	 * @param args
	 *            the args
	 * @return true, if successful
	 */
	private boolean deleteNode(List<String> args) {
		if (args.size() != 2) {
			System.err
					.println("Usage - <xml/properties/yaml> <delete> propertyName filePath");
			return false;
		}
		final String propertyName = args.get(0);
		final String xmlPath = args.get(1);
		return manipulatable.deleteConfValue(xmlPath, propertyName);
	}

	/**
	 * Edits the node.
	 * 
	 * @param args
	 *            the args
	 * @return true, if successful
	 */
	private boolean editNode(List<String> args) {
		if (args.size() != 3) {
			System.err
					.println("Usage - <xml/properties/yaml> <edit> propertyName propertyValue filePath");
			return false;
		}
		final String propertyName = args.get(0);
		final String propertyValue = args.get(1);
		final String xmlPath = args.get(2);

		return manipulatable
				.editConfValue(xmlPath, propertyName, propertyValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.agent.action.Actionable#execute(java.util.List)
	 */
	@Override
	public boolean execute(List<String> argsList) {

		if (argsList.size() == 0) {
			System.err
					.println("Usage - <xml/properties/yaml> <add/edit/read> [argsList]");
			System.exit(1);
		}

		// identify type of Manipulator
		final String type = argsList.get(0);

		argsList.remove(0);

		// identify operation to be performed
		final String operation = argsList.get(0);

		argsList.remove(0);

		boolean status = false;
		manipulatable = ManipulatorFactory.getInstanceById(type);

		// perform operation
		switch (Operation.fromString(operation)) {

		case edit:
			status = editNode(argsList);
			break;
		case add:
			status = addNode(argsList);
			break;
		case read:
			status = readNode(argsList);
			break;
		case delete:
			status = deleteNode(argsList);
			break;
		default:
			System.err
					.println("Usage - <xml/properties/yaml> <add/edit/read/delete> [argsList]");
			break;
		}

		if (status) {
			System.exit(0);
		} else {
			System.exit(1);
		}
		return status;
	}

	/**
	 * Read node.
	 * 
	 * @param args
	 *            the args
	 * @return true, if successful
	 */
	private boolean readNode(List<String> args) {
		if (args.size() != 2) {
			System.err
					.println("Usage - <xml/properties/yaml> <read> propertyName filePath");
			return false;
		}
		final String propertyName = args.get(0);
		final String xmlPath = args.get(1);
		final String propertyValue = manipulatable.readConfValue(xmlPath,
				propertyName);

		if (propertyValue != null) {
			System.out.println(propertyValue);
		} else {
			System.err.println("Unable to get property value.");
			return false;
		}
		return true;
	}

}
