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
package com.impetus.ankush.common.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.security.core.GrantedAuthority;

/**
 * The class <code>UserTest</code> contains tests for the class
 * <code>{@link User}</code>.
 */
public class UserTest {
	private static final long DATE = 1372406912786L;
	private User fixture;

	/**
	 * Run the User() constructor test.
	 */
	@Test
	public void testUser_1() {

		User result = new User();

		assertNotNull(result);
		assertEquals(
				"id=<null>,username=<null>,enabled=false,accountExpired=false,credentialsExpired=false,accountLocked=false,Granted Authorities: ,<null>",
				result.toString());
		assertEquals(null, result.getId());
		assertEquals(null, result.getVersion());
		assertEquals(false, result.isEnabled());
		assertEquals(null, result.getPassword());
		assertEquals("null null", result.getFullName());
		assertEquals(null, result.getCreationDate());
		assertEquals(null, result.getUsername());
		assertEquals(null, result.getEmail());
		assertEquals(null, result.getForcePasswordChange());
		assertEquals(null, result.getFirstName());
		assertEquals(null, result.getLastName());
		assertEquals(false, result.isAccountExpired());
		assertEquals(true, result.isAccountNonExpired());
		assertEquals(false, result.isAccountLocked());
		assertEquals(true, result.isAccountNonLocked());
		assertEquals(false, result.isCredentialsExpired());
		assertEquals(true, result.isCredentialsNonExpired());
		assertEquals(null, result.getMobile());
		assertEquals(null, result.getLastLogin());
		assertEquals("None", result.getUserStatus());
	}

	/**
	 * Run the User(String) constructor test.
	 */
	@Test
	public void testUser_2() {
		String username = "";

		User result = new User(username);

		assertNotNull(result);
		assertEquals(
				"id=<null>,username=,enabled=false,accountExpired=false,credentialsExpired=false,accountLocked=false,Granted Authorities: ,<null>",
				result.toString());
		assertEquals(null, result.getId());
		assertEquals(null, result.getVersion());
		assertEquals(false, result.isEnabled());
		assertEquals(null, result.getPassword());
		assertEquals("null null", result.getFullName());
		assertEquals(null, result.getCreationDate());
		assertEquals("", result.getUsername());
		assertEquals(null, result.getEmail());
		assertEquals(null, result.getForcePasswordChange());
		assertEquals(null, result.getFirstName());
		assertEquals(null, result.getLastName());
		assertEquals(false, result.isAccountExpired());
		assertEquals(true, result.isAccountNonExpired());
		assertEquals(false, result.isAccountLocked());
		assertEquals(true, result.isAccountNonLocked());
		assertEquals(false, result.isCredentialsExpired());
		assertEquals(true, result.isCredentialsNonExpired());
		assertEquals(null, result.getMobile());
		assertEquals(null, result.getLastLogin());
		assertEquals("None", result.getUserStatus());
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		User o = new User("user");
		o.setCreationTime();
		boolean result = fixture.equals(o);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_2() {
		Object o = new Object();

		boolean result = fixture.equals(o);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_3() {
		Object o = new User();

		boolean result = fixture.equals(o);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_4() {
		Object o = new User("user");

		boolean result = fixture.equals(o);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		Object o = new User("test");

		boolean result = fixture.equals(o);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		boolean result = fixture.equals(fixture);

		assertEquals(true, result);
	}

	/**
	 * Run the Set<GrantedAuthority> getAuthorities() method test.
	 */
	@Test
	public void testGetAuthorities_1() {
		Set<GrantedAuthority> result = fixture.getAuthorities();

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Run the Date getCreationDate() method test.
	 */
	@Test
	public void testGetCreationDate_1() {
		Date result = fixture.getCreationDate();

		assertNotNull(result);
		assertEquals(DATE, result.getTime());
	}

	/**
	 * Run the String getEmail() method test.
	 */
	@Test
	public void testGetEmail_1() {
		String result = fixture.getEmail();

		assertEquals("email", result);
	}

	/**
	 * Run the String getFirstName() method test.
	 */
	@Test
	public void testGetFirstName_1() {
		String result = fixture.getFirstName();

		assertEquals("first", result);
	}

	/**
	 * Run the Boolean getForcePasswordChange() method test.
	 */
	@Test
	public void testGetForcePasswordChange_1() {
		Boolean result = fixture.getForcePasswordChange();

		assertNotNull(result);
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the String getFullName() method test.
	 */
	@Test
	public void testGetFullName_1() {
		String result = fixture.getFullName();

		assertEquals("first last", result);
	}

	/**
	 * Run the Long getId() method test.
	 */
	@Test
	public void testGetId_1() {
		Long result = fixture.getId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the Date getLastLogin() method test.
	 */
	@Test
	public void testGetLastLogin_1() {
		Date result = fixture.getLastLogin();

		assertNotNull(result);
		assertEquals(DATE, result.getTime());
	}

	/**
	 * Run the String getLastName() method test.
	 */
	@Test
	public void testGetLastName_1() {
		String result = fixture.getLastName();

		assertEquals("last", result);
	}

	/**
	 * Run the String getMobile() method test.
	 */
	@Test
	public void testGetMobile_1() {
		String result = fixture.getMobile();

		assertEquals("mobile", result);
	}

	/**
	 * Run the String getPassword() method test.
	 */
	@Test
	public void testGetPassword_1() {
		String result = fixture.getPassword();

		assertEquals("password", result);
	}

	/**
	 * Run the Set<Role> getRoles() method test.
	 */
	@Test
	public void testGetRoles_1() {
		Set<Role> result = fixture.getRoles();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("description", result.iterator().next().getDescription());
	}

	/**
	 * Run the String getUserStatus() method test.
	 */
	@Test
	public void testGetUserStatus_1() {
		String result = fixture.getUserStatus();

		assertEquals("status", result);
	}

	/**
	 * Run the String getUsername() method test.
	 */
	@Test
	public void testGetUsername_1() {
		String result = fixture.getUsername();

		assertEquals("user", result);
	}

	/**
	 * Run the Integer getVersion() method test.
	 */
	@Test
	public void testGetVersion_1() {
		Integer result = fixture.getVersion();

		assertNotNull(result);
		assertEquals(1, result.intValue());
	}

	/**
	 * Run the boolean isAccountExpired() method test.
	 */
	@Test
	public void testIsAccountExpired_1() {
		boolean result = fixture.isAccountExpired();

		assertEquals(true, result);
	}

	/**
	 * Run the boolean isAccountLocked() method test.
	 */
	@Test
	public void testIsAccountLocked_1() {
		boolean result = fixture.isAccountLocked();

		assertEquals(true, result);
	}

	/**
	 * Run the boolean isAccountNonExpired() method test.
	 */
	@Test
	public void testIsAccountNonExpired_1() {
		boolean result = fixture.isAccountNonExpired();

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAccountNonLocked() method test.
	 */
	@Test
	public void testIsAccountNonLocked_1() {
		boolean result = fixture.isAccountNonLocked();

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isCredentialsExpired() method test.
	 */
	@Test
	public void testIsCredentialsExpired_1() {
		boolean result = fixture.isCredentialsExpired();

		assertEquals(true, result);
	}

	/**
	 * Run the boolean isCredentialsNonExpired() method test.
	 */
	@Test
	public void testIsCredentialsNonExpired_1() {
		boolean result = fixture.isCredentialsNonExpired();

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEnabled() method test.
	 */
	@Test
	public void testIsEnabled_1() {
		boolean result = fixture.isEnabled();

		assertEquals(true, result);
	}

	/**
	 * Run the void merge(BaseObject) method test.
	 * 
	 * @ *
	 * 
	 * @generatedBy CodePro at 28/6/13 1:38 PM
	 */
	@Test
	public void testMerge_1() {
		BaseObject baseObject = new User();

		fixture.merge(baseObject);

	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new User("user");
		fixture.setCredentialsExpired(true);
		fixture.setLastName("last");
		fixture.setLastLogin(new Date(DATE));
		fixture.setAccountExpired(true);
		fixture.setEnabled(true);
		fixture.setUserStatus("status");
		fixture.setVersion(new Integer(1));
		fixture.setRoles(new ManagedSet());
		fixture.setMobile("mobile");
		fixture.setForcePasswordChange(new Boolean(true));
		fixture.setEmail("email");
		fixture.setFirstName("first");
		fixture.setCreationDate(new Date(DATE));
		fixture.setAccountLocked(true);
		fixture.setId(new Long(1L));
		fixture.setPassword("password");
		Role role = new Role();
		role.setDescription("description");
		role.setId(1L);
		role.setName("name");
		fixture.addRole(role);
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
