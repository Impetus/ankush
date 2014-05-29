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
package com.impetus.ankush.common.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.Table;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.impetus.ankush.common.dao.UserDao;
import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 */
@Repository("userDao")
public class UserDaoJpa extends GenericDaoJpa<User, Long> implements UserDao,
		UserDetailsService {
	
	/** The data source. */
	@Autowired
	private DataSource dataSource;

	/**
	 * Constructor that sets the entity to User.class.
	 */
	public UserDaoJpa() {
		super(User.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		Query q = getEntityManager().createQuery(
				"select u from User u order by upper(u.username)");
		return q.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	public int createdUserCount() {
		Query q = getEntityManager().createQuery(
				"select u from User u where account_enabled=?");
		q.setParameter(1, 1);
		return q.getResultList().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		Query q = getEntityManager().createQuery(
				"select u from User u where username=?");
		q.setParameter(1, username);
		List<User> users = q.getResultList();
		if (users == null || users.isEmpty()) {
			throw new UsernameNotFoundException("user '" + username
					+ "' not found...");
		}

		return users.get(0);
	}

	/**
	 * Save user and flush entityManager.
	 *
	 * @param user the user to save
	 * @return the updated user
	 */
	public User saveUser(User user) {
		User u = super.save(user);
		getEntityManager().flush();
		return u;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsersByRole(Role role) {
		Query query = getEntityManager().createNamedQuery("getUserByRole");
		query.setParameter("role", role);
		return query.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUserPassword(String username) {
		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(dataSource);
		Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
		return jdbcTemplate.queryForObject(
				"select password from " + table.name() + " where username=?",
				String.class, username);
	}
}
