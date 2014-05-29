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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.beanutils.BeanUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.impetus.ankush.common.dao.GenericDao;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.impl.GenericManagerImpl;

@ContextConfiguration(locations = { "classpath:/applicationContext-resources.xml",
	"classpath:/applicationContext-dao.xml", "classpath*:/applicationContext.xml",
	"classpath:**/applicationContext*.xml" })
public class GenericManagerImplTest {

    private GenericDao<User, Long> genericDao;
    private GenericManager<User, Long> genericManager;
    private User user;

    @Before
    public void setUp() {
	this.genericDao = EasyMock.createMock(GenericDao.class);

	genericManager = new GenericManagerImpl<User, Long>(genericDao);

	user = new User("testuser");
	user.setPassword("junkPassword");
	user.setFirstName("Test");
	user.setLastName("User");
	user.setEmail("user@test.com");
	user.setCreationDate(new Date());
    }

    @Test
    public void testGetAll() {
	user.setId(1L);

	EasyMock.expect(genericDao.getAll(0, Integer.MAX_VALUE)).andReturn(Collections.singletonList(user));
	EasyMock.replay(genericDao);
	List<User> users = genericManager.getAll();
	assertEquals("Incorrect user list returned", 1, users.size());
	assertSame("Incorrect user object returned", user, users.get(0));
    }
    
    @Test
    public void testGetAllCount() {
	user.setId(1L);

	EasyMock.expect(genericDao.getAllCount()).andReturn(1);
	EasyMock.replay(genericDao);
	assertEquals("Incorrect user list returned", 1, genericManager.getAllCount());
    }

    @Test
    public void testGet() {
	user.setId(1L);

	EasyMock.expect(genericDao.get(user.getId())).andReturn(user);
	EasyMock.replay(genericDao);

	assertSame("Incorrect user object returned", user, genericManager.get(user.getId()));
    }
    
    @Test
    public void testGetReference() {
    user.setId(1L);

    EasyMock.expect(genericDao.getReference(user.getId())).andReturn(user);
    EasyMock.replay(genericDao);

    assertSame("Incorrect user object returned", user, genericManager.getReference(user.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetNegative() {
	user.setId(1L);

	EasyMock.expect(genericDao.get(user.getId())).andThrow(new EntityNotFoundException());
	EasyMock.replay(genericDao);

	genericManager.get(user.getId());
    }

    @Test
    public void testExists() {
	EasyMock.expect(genericDao.exists(-1L)).andReturn(true);
	EasyMock.expect(genericDao.exists(-2L)).andReturn(false);
	EasyMock.replay(genericDao);

	assertTrue("should return true", genericManager.exists(-1L));
	assertFalse("should return false", genericManager.exists(-2L));
    }

    @Test
    public void testSave() throws IllegalAccessException, InvocationTargetException, InstantiationException,
	    NoSuchMethodException {
	user.setId(null);
	User savedUser = (User) BeanUtils.cloneBean(user);
	savedUser.setId(1L);

	EasyMock.expect(genericDao.save(user)).andReturn(savedUser);
	EasyMock.replay(genericDao);

	user = genericManager.save(user);

	assertNotNull("No userobject returned", user);
	assertNotNull("Object id not generated", user.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemove() {
	genericDao.remove(1L);
	EasyMock.expectLastCall();
	EasyMock.expect(genericDao.get(1L)).andThrow(new EntityNotFoundException());
	EasyMock.replay(genericDao);

	genericManager.remove(1L);
	genericManager.get(1L);
    }

    @Test
    public void testGetAllByPropertyValue() {
	String property = "property";
	Object value = "value";

	EasyMock.expect(
		genericDao.getAllByPropertyValue(Collections.singletonMap(property, value), 0, Integer.MAX_VALUE))
		.andReturn(Collections.singletonList(user));
	EasyMock.replay(genericDao);

	assertNotNull("no object returned", genericManager.getAllByPropertyValue(property, value));
    }
    
    @Test
    public void testGetAllByPropertyValueCount() {
	String property = "property";
	Object value = "value";

	EasyMock.expect(
		genericDao.getAllByPropertyValueCount(Collections.singletonMap(property, value)))
		.andReturn(1);
	EasyMock.replay(genericDao);

	assertEquals("incorrect count", 1, genericManager.getAllByPropertyValueCount(property, value));
    }

    @Test
    public void testGetByPropertyValue() {
	String property = "property";
	Object value = "value";

	EasyMock.expect(genericDao.getByPropertyValue(Collections.singletonMap(property, value))).andReturn(user);
	EasyMock.replay(genericDao);

	assertNotNull("no object returned", genericManager.getByPropertyValue(property, value));
    }

    @Test
    public void testGetByPropertyValueMapGuarded() {
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.getAllByPropertyValue(queryMap, 0, Integer.MAX_VALUE)).andReturn(
		Collections.EMPTY_LIST);
	EasyMock.replay(genericDao);

	assertNull("null not returned", genericManager.getByPropertyValueGuarded(queryMap));
    }
    
    @Test
    public void testGetByPropertyValueGuarded() {
    String property = "property";
    Object value = "value";

    EasyMock.expect(genericDao.getAllByPropertyValue(Collections.singletonMap(property, value), 0, Integer.MAX_VALUE))
    .andReturn(Collections.EMPTY_LIST);
    EasyMock.replay(genericDao);

    assertNull("null not returned", genericManager.getByPropertyValueGuarded(property, value));
    }

    @Test
    public void testGetAllByPropertyValueMap() {
    Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

    EasyMock.expect(genericDao.getAllByPropertyValue(queryMap, 0, Integer.MAX_VALUE)).andReturn(
        Collections.singletonList(user));
    EasyMock.replay(genericDao);

    assertNotNull("no object returned", genericManager.getAllByPropertyValue(queryMap));
    }

    @Test
    public void testGetByPropertyValueMap() {
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.getByPropertyValue(queryMap)).andReturn(user);
	EasyMock.replay(genericDao);

	assertNotNull("no object returned", genericManager.getByPropertyValue(queryMap));
    }
    
    @Test
    public void getAllByNamedQuery(){
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.getAllByNamedQuery("queryName", queryMap, 0, Integer.MAX_VALUE))
	.andReturn(Collections.singletonList(user));
	EasyMock.replay(genericDao);

	List<User>  users = genericManager.getAllByNamedQuery("queryName", queryMap);
	assertEquals("incorrect object returned", Collections.singletonList(user), users);
    }
    
    @Test
    public void getAllByNamedQueryPaged(){
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.getAllByNamedQuery("queryName", queryMap, 1, 1))
	.andReturn(Collections.singletonList(user));
	EasyMock.replay(genericDao);

	assertEquals("incorrect object returned", Collections.singletonList(user), genericManager.getAllByNamedQuery("queryName", queryMap, 1, 1));
    }
    
    @Test
    public void getAllOfOrMatch() {
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.getAllOfOrMatch(queryMap, 0, Integer.MAX_VALUE)).andReturn(
		Collections.singletonList(user));
	EasyMock.replay(genericDao);

	assertEquals("incorrect object returned", Collections.singletonList(user), genericManager.getAllOfOrMatch(queryMap));
    }
    
    @Test
    public void getAllOfOrMatchPaged() {
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.getAllOfOrMatch(queryMap, 1, 1, "username")).andReturn(
		Collections.singletonList(user));
	EasyMock.replay(genericDao);

	assertEquals("incorrect object returned", Collections.singletonList(user), genericManager.getAllOfOrMatch(queryMap, 1, 1, "username"));
    }
    
    @Test
    public void getAllOfOrMatchCount() {
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.getAllOfOrMatchCount(queryMap)).andReturn(1);
	EasyMock.replay(genericDao);

	assertEquals("incorrect count returned", 1, genericManager.getAllOfOrMatchCount(queryMap));
    }
    
    @Test
    public void getAllByDisjunctionveNormalQuery(){
	List<Map<String, Object>> queryMaps = Collections.singletonList(Collections.singletonMap("property", (Object) "value"));

	EasyMock.expect(genericDao.getAllByDisjunctionveNormalQuery(queryMaps, 0, Integer.MAX_VALUE, "username")).andReturn(
		Collections.singletonList(user));
	EasyMock.replay(genericDao);

	assertEquals("incorrect object returned", Collections.singletonList(user), genericManager.getAllByDisjunctionveNormalQuery(queryMaps, "username"));
    }
    
    @Test
    public void getAllByDisjunctionveNormalQueryPaged(){
	List<Map<String, Object>> queryMaps = Collections.singletonList(Collections.singletonMap("property", (Object) "value"));

	EasyMock.expect(genericDao.getAllByDisjunctionveNormalQuery(queryMaps, 100, 500)).andReturn(
		Collections.singletonList(user));
	EasyMock.replay(genericDao);

	assertEquals("incorrect object returned", Collections.singletonList(user), 
		genericManager.getAllByDisjunctionveNormalQuery(queryMaps, 100, 500));
    }
    
    @Test
    public void getAllByDisjunctionveNormalQueryCount(){
	List<Map<String, Object>> queryMaps = Collections.singletonList(Collections.singletonMap("property", (Object) "value"));

	EasyMock.expect(genericDao.getAllByDisjunctionveNormalQueryCount(queryMaps)).andReturn(1);
	EasyMock.replay(genericDao);

	assertEquals("incorrect object returned", 1, genericManager.getAllByDisjunctionveNormalQueryCount(queryMaps));
    }
    
    @Test
    public void deleteAllByPropertyValue() {
	String property = "property";
	Object value = "value";

	EasyMock.expect(
		genericDao.deleteAllByPropertyValue(Collections.singletonMap(property, value)))
		.andReturn(1);
	EasyMock.replay(genericDao);

	assertEquals("incorrect no of objects returned", 1, genericManager.deleteAllByPropertyValue(property, value));
    }
    
    @Test
    public void deleteAllByPropertyValueMap() {
	Map<String, Object> queryMap = Collections.singletonMap("property", (Object) "value");

	EasyMock.expect(genericDao.deleteAllByPropertyValue(queryMap)).andReturn(1);
	EasyMock.replay(genericDao);

	assertEquals("incorrect no of objects returned", 1, genericManager.deleteAllByPropertyValue(queryMap));
    }

}
