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
package com.impetus.ankush.admin.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Collections;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import com.impetus.ankush.common.dao.UserDao;
import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.exception.UserExistsException;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.service.impl.UserManagerImpl;


@ContextConfiguration(locations = {
		"classpath:/applicationContext-resources.xml",
		"classpath:/applicationContext-dao.xml",
		"classpath*:/applicationContext.xml",
		"classpath:**/applicationContext*.xml" })
public class UserManagerImplTest {

	private UserManager userManager;
	private UserDao userDao;
	private User user;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.userDao = EasyMock.createMock(UserDao.class);

		this.userManager = new UserManagerImpl();
		this.userManager.setUserDao(userDao);

		user = new User("testUser");
		user.setPassword("junkPassword");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("user@test.com");
	}

	/**
	 * Test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#getUser(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetUser() {
		user.setId(1L);

		EasyMock.expect(userDao.get(user.getId())).andReturn(user);
		EasyMock.replay(userDao);

		assertSame("Incorrect user object returned", user,
				userManager.getUser(user.getId().toString()));
	}

	/**
	 * Test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#getUsers()}.
	 */
	@Test
	public void testGetUsers() {
		EasyMock.expect(userDao.getAllDistinct()).andReturn(
				Collections.singletonList(user));
		EasyMock.replay(userDao);

		assertEquals("Incorrect no of users returned", 1, userManager
				.getUsers().size());
	}

	/**
	 * Test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#saveUser(com.impetus.ankush.common.domain.model.User)}
	 * .
	 * @throws UserExistsException 
	 */
	@Test
	public void testSaveUser() throws UserExistsException {
		String originalUsername = user.getUsername();
		((UserManagerImpl)userManager).setPasswordEncoder(null);
		
		EasyMock.expect(userDao.saveUser(user)).andReturn(user);
		EasyMock.replay(userDao);
		
		User savedUser = userManager.saveUser(user);
		assertEquals("username not lowercased", originalUsername.toLowerCase(), savedUser.getUsername());
	}
	
	/**
	 * Duplicate username test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#saveUser(com.impetus.ankush.common.domain.model.User)}
	 * .
	 * @throws UserExistsException 
	 */
	@Test(expected=UserExistsException.class)
	public void testSaveUserDuplicateUsername() throws UserExistsException {
		((UserManagerImpl)userManager).setPasswordEncoder(null);
		
		EasyMock.expect(userDao.saveUser(user)).andThrow(new DataIntegrityViolationException(("duplicate username")));
		EasyMock.replay(userDao);
		
		userManager.saveUser(user);
		fail("should throw an exception");
	}
	
	/**
	 * Duplicate username test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#saveUser(com.impetus.ankush.common.domain.model.User)}
	 * .
	 * @throws UserExistsException 
	 */
	@Test(expected=UserExistsException.class)
	public void testSaveUserJPAException() throws UserExistsException {
		((UserManagerImpl)userManager).setPasswordEncoder(null);
		
		EasyMock.expect(userDao.saveUser(user)).andThrow(new JpaSystemException(new PersistenceException()));
		EasyMock.replay(userDao);
		
		userManager.saveUser(user);
		fail("should throw an exception");
	}
	
	/**
	 * Password encryption test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#saveUser(com.impetus.ankush.common.domain.model.User)}
	 * .
	 * @throws UserExistsException 
	 */
	@Test
	public void testSaveUserPasswordEncryptionChanged() throws UserExistsException {
		user.setVersion(1);
		user.setUsername(user.getUsername().toLowerCase());
		String encryptedPassword = "ENCRYPTED"+user.getPassword();
		
		PasswordEncoder passwordEncoder = EasyMock.createMock(PasswordEncoder.class);
		((UserManagerImpl)userManager).setPasswordEncoder(passwordEncoder);
		
		EasyMock.expect(passwordEncoder.encodePassword(user.getPassword(), null)).andReturn(encryptedPassword);
		
		EasyMock.expect(userDao.getUserPassword(user.getUsername())).andReturn(user.getPassword()+"Old");
		EasyMock.expect(userDao.saveUser(user)).andReturn(user);
		EasyMock.replay(userDao, passwordEncoder);
		
		userManager.saveUser(user);
		assertEquals("password not encrypted", encryptedPassword, user.getPassword());
	}
	
	/**
	 * Password encryption test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#saveUser(com.impetus.ankush.common.domain.model.User)}
	 * .
	 * @throws UserExistsException 
	 */
	@Test
	public void testSaveUserPasswordEncryptionNew() throws UserExistsException {
		user.setVersion(1);
		user.setUsername(user.getUsername().toLowerCase());
		String encryptedPassword = "ENCRYPTED"+user.getPassword();
		
		PasswordEncoder passwordEncoder = EasyMock.createMock(PasswordEncoder.class);
		((UserManagerImpl)userManager).setPasswordEncoder(passwordEncoder);
		
		EasyMock.expect(passwordEncoder.encodePassword(user.getPassword(), null)).andReturn(encryptedPassword);
		
		EasyMock.expect(userDao.getUserPassword(user.getUsername())).andReturn(null);
		EasyMock.expect(userDao.saveUser(user)).andReturn(user);
		EasyMock.replay(userDao, passwordEncoder);
		
		userManager.saveUser(user);
		assertEquals("password not encrypted", encryptedPassword, user.getPassword());
	}
	
	/**
	 * Password encryption test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#saveUser(com.impetus.ankush.common.domain.model.User)}
	 * .
	 * @throws UserExistsException 
	 */
	@Test
	public void testSaveUserPasswordEncryptionUnchanged() throws UserExistsException {
		user.setVersion(1);
		user.setUsername(user.getUsername().toLowerCase());
		
		String encryptedPassword = "ENCRYPTED"+user.getPassword();
		user.setPassword(encryptedPassword);
		
		PasswordEncoder passwordEncoder = EasyMock.createStrictMock(PasswordEncoder.class);
		((UserManagerImpl)userManager).setPasswordEncoder(passwordEncoder);
		
		EasyMock.expect(userDao.getUserPassword(user.getUsername())).andReturn(encryptedPassword);
		EasyMock.expect(userDao.saveUser(user)).andReturn(user);
		EasyMock.replay(userDao, passwordEncoder);
		
		userManager.saveUser(user);
		assertEquals("password not encrypted", encryptedPassword, user.getPassword());
	}



	/**
	 * Test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#removeUser(java.lang.String)}
	 * .
	 */
	@Test(expected = EntityNotFoundException.class)
	public void testRemoveUser() {
		user.setId(1L);

		userDao.remove(user.getId());
		EasyMock.expectLastCall();
		EasyMock.expect(userDao.get(user.getId())).andThrow(
				new EntityNotFoundException());
		EasyMock.replay(userDao);

		userManager.removeUser(user.getId().toString());
		userManager.getUser(user.getId().toString());
	}

	/**
	 * Test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#getUserByUsername(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetUserByUsername() {
		user.setId(1L);

		EasyMock.expect(userDao.loadUserByUsername(user.getUsername()))
				.andReturn(user);
		EasyMock.replay(userDao);

		assertSame("incorrect user returned", user,
				userManager.getUserByUsername(user.getUsername()));
	}

	/**
	 * Negative test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#getUserByUsername(java.lang.String)}
	 * .
	 */
	@Test(expected = UsernameNotFoundException.class)
	public void testGetUserByUsernameNegative() {
		user.setId(1L);

		EasyMock.expect(userDao.loadUserByUsername(user.getUsername()))
				.andThrow(
						new UsernameNotFoundException(user.getUsername()
								+ " not found"));
		EasyMock.replay(userDao);

		userManager.getUserByUsername(user.getUsername());
	}
	
	/**
	 * test method for
	 * {@link com.impetus.ankush.service.impl.UserManagerImpl#getUsersByRole(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetUsersByRole() {
		Role role = new Role("testRole");
		
		user.setId(1L);
		user.getRoles().add(role);

		EasyMock.expect(userDao.getUsersByRole(role)).andReturn(Collections.singletonList(user));
		EasyMock.replay(userDao);
		
		assertSame("incorrect user returned", user, userManager.getUsersByRole(role).get(0));
	}
}
