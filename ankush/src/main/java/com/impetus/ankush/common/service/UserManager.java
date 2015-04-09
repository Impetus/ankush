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
package com.impetus.ankush.common.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.impetus.ankush.common.dao.UserDao;
import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.exception.UserExistsException;

/**
 * Business Service Interface to handle communication between web and persistence layer.
 *
 */
public interface UserManager extends GenericManager<User, Long> {
    /**
     * Convenience method for testing - allows you to mock the DAO and set it on an interface.
     *
     * @param userDao
     *            the UserDao implementation to use
     */
    void setUserDao(UserDao userDao);

    /**
     * Retrieves a user by userId. An exception is thrown if user not found
     *
     * @param userId
     *            the identifier for the user
     * @return User
     */
    User getUser(String userId);

    /**
     * Finds a user by their username.
     *
     * @param username the user's username used to login
     * @return User a populated user object
     * @throws UsernameNotFoundException the username not found exception
     */
    User getUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Retrieves a list of all users.
     *
     * @return List
     */
    List<User> getUsers();

    /**
     * Saves a user's information.
     *
     * @param user the user's information
     * @return user the updated user object
     * @throws UserExistsException thrown when user already exists
     */
    User saveUser(User user) throws UserExistsException;
    
    /**
     * Creates the user.
     *
     * @param user the user
     * @param enabled the enabled
     * @return the user
     * @throws UserExistsException the user exists exception
     */
    User createUser(User user, boolean enabled) throws UserExistsException;
    
    /**
     * Removes a user from the database by their userId.
     *
     * @param userId the user's id
     */
    void removeUser(String userId);
    
	/**
	 * Get all the users with the given role.
	 *
	 * @param role Role to find by.
	 * @return List of users with given role.
	 */
    List<User> getUsersByRole(Role role);
    
    /**
     * Forgot password.
     *
     * @param user the user
     * @return true, if successful
     */
    boolean forgotPassword(User user);
    
    /**
     * Forgot user id.
     *
     * @param user the user
     * @return true, if successful
     */
    boolean forgotUserId(User user);
    
    /**
     * Change password.
     *
     * @param userName the user name
     * @param currentPassword the current password
     * @param newPassword the new password
     * @throws Exception the exception
     */
    void changePassword(String userName, String currentPassword, String newPassword) throws Exception;

	/**
	 * Removes the user.
	 *
	 * @param id the id
	 */
	void removeUser(Long id);
	
	/**
	 * Created user count.
	 *
	 * @return the int
	 */
	int createdUserCount();
	
	/**
	 * Check user existance.
	 *
	 * @param user the user
	 * @return the boolean
	 */
	Boolean checkUserExistance(User user);
	
	void addAdminUser();

	boolean doesPasswordMatch(String userName, String password)
			throws Exception;
}
