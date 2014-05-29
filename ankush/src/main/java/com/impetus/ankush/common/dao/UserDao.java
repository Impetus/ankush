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
package com.impetus.ankush.common.dao;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;

/**
 * User Data Access Object (GenericDao) interface.
 *
 */
public interface UserDao extends GenericDao<User, Long> {

    /**
     * Gets users information based on login name.
     *
     * @param username the user's username
     * @return userDetails populated userDetails object
     */
    @Transactional
    UserDetails loadUserByUsername(String username);

    /**
     * Gets a list of users ordered by the uppercase version of their username.
     *
     * @return List populated list of users
     */
    List<User> getUsers();

    /**
     * Saves a user's information.
     *
     * @param user
     *            the object to be saved
     * @return the persisted User object
     */
    User saveUser(User user);

    /**
     * Retrieves the password in DB for a user.
     *
     * @param username the user's username
     * @return the password in DB, if the user is already persisted
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getUserPassword(String username);
    
	/**
	 * Get all the users with the given role.
	 *
	 * @param role Role to find by.
	 * @return List of users with given role.
	 */
    List<User> getUsersByRole(Role role);
    
    /**
     * Get the user count from the database for those users whose account is enabled.
     * @return returns users count.
     */
    int createdUserCount();
}
