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
package com.impetus.ankush.common.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.dao.UserDao;
import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.exception.UserExistsException;
import com.impetus.ankush.common.mail.MailManager;
import com.impetus.ankush.common.mail.MailMsg;
import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.PasswordUtil;

/**
 * Implementation of UserManager interface.
 * 
 */
public class UserManagerImpl extends GenericManagerImpl<User, Long> implements
		UserManager {

	/** The password encoder. */
	private PasswordEncoder passwordEncoder;

	/** The user dao. */
	private UserDao userDao;

	/** The role manager. */
	private GenericManager<Role, Long> roleManager;

	/** The Constant GENERATED_PASSWORD_LENGTH. */
	private static final int GENERATED_PASSWORD_LENGTH = 8;

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(UserManagerImpl.class);

	/** The app conf service. */
	private AppConfService appConfService;

	/**
	 * Sets the app conf service.
	 * 
	 * @param appConfService
	 *            the new app conf service
	 */
	@Autowired
	public void setAppConfService(AppConfService appConfService) {
		this.appConfService = appConfService;
	}

	/**
	 * Sets the password encoder.
	 * 
	 * @param passwordEncoder
	 *            the new password encoder
	 */
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Sets the role manager.
	 * 
	 * @param roleManager
	 *            the role manager
	 */
	@Autowired
	public void setRoleManager(
			@Qualifier("roleManager") GenericManager<Role, Long> roleManager) {
		this.roleManager = roleManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.UserManager#setUserDao(com.impetus.
	 * ankush.common.dao.UserDao)
	 */
	@Override
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.dao = userDao;
		this.userDao = userDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUser(String userId) {
		return userDao.get(Long.valueOf(userId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getUsers() {
		return userDao.getAllDistinct();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User saveUser(User user) throws UserExistsException {

		// lowercase userId
		user.setUsername(user.getUsername().toLowerCase());

		// Get and prepare password management-related artifacts
		boolean passwordChanged = false;
		if (passwordEncoder != null) {
			// Check whether we have to encrypt (or re-encrypt) the password
			if (user.getVersion() == null) {
				// New user, always encrypt
				passwordChanged = true;
			} else {
				// Existing user, check password in DB
				String currentPassword = null;
				try {
					currentPassword = userDao.getUserPassword(user
							.getUsername());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				if (currentPassword == null) {
					passwordChanged = true;
				} else if (!currentPassword.equals(user.getPassword())) {
					passwordChanged = true;
				}
			}

			if (passwordChanged) {
				// password was changed (or new user), encrypt it
				user.setPassword(passwordEncoder.encodePassword(
						user.getPassword(), null));
			}
		} else {
			log.warn("PasswordEncoder not set, skipping password encryption...");
		}

		try {
			return userDao.saveUser(user);
		} catch (DataIntegrityViolationException e) {
			log.warn(e.getMessage());
			throw new UserExistsException("User '" + user.getUsername()
					+ "' already exists!");
		} catch (JpaSystemException e) {
			// needed for JPA
			logger.error(e.getMessage());
			log.warn(e.getMessage());
			throw new UserExistsException("User '" + user.getUsername()
					+ "' already exists!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.UserManager#createUser(com.impetus.
	 * ankush.common.domain.User, boolean)
	 */
	@Override
	public User createUser(User user, boolean enabled)
			throws UserExistsException {
		user.setEnabled(enabled);
		user.setId(null);
		Set<Role> userRole = new HashSet<Role>();
		userRole.add(roleManager.get(1L));
		user.setRoles(userRole);
		user.setForcePasswordChange(true);

		String generatedPassword = "";
		if ((user.getPassword() == null)
				|| (user.getPassword().trim().length() == 0)) {
			generatedPassword = PasswordUtil
					.getRandomPassword(GENERATED_PASSWORD_LENGTH);
			user.setPassword(generatedPassword);
		}

		user = this.saveUser(user);
		MailMsg message = new MailMsg();
		message.setTo(user.getEmail());
		message.setSubject("Your Ankush Account has been created");

		MailManager mm = AppStoreWrapper.getMailManager();
		String accessURL = appConfService.getAppAccessPublicURL();
		if (accessURL != null) {
			accessURL = "at " + accessURL;
		}
		StringBuilder msgBuff = new StringBuilder();
		msgBuff.append(getUserSalutation(user));
		msgBuff.append(
				"You can now access Ankush " + accessURL
						+ ". Your Account Details are as below:").append("\n");
		msgBuff.append("User Id: ").append(user.getUsername()).append("\n");
		msgBuff.append("Password: ").append(generatedPassword).append("\n");
		msgBuff.append(
				"\t - The above password is a system generated password. It is recommended that you change it after login. \n");
		String msg = msgBuff.toString();

		message.setMessage(msg);

		try {
			message.setContentType("text/plain");
			mm.sendSystemMail(message);
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
		}

		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUser(String userId) {
		log.debug("removing user: " + userId);
		userDao.remove(Long.valueOf(userId));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param username
	 *            the login name of the human
	 * @return User the populated user object
	 * @throws UsernameNotFoundException
	 *             thrown when username not found
	 */
	@Override
	public User getUserByUsername(String username)
			throws UsernameNotFoundException {
		return (User) userDao.loadUserByUsername(username);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getUsersByRole(Role role) {
		return userDao.getUsersByRole(role);
	}

	/** The Constant PRODUCT_NAME. */
	private static final String PRODUCT_NAME = "Ankush";

	/**
	 * Gets the user salutation.
	 * 
	 * @param user
	 *            the user
	 * @return the user salutation
	 */
	private String getUserSalutation(User user) {
		StringBuilder content = new StringBuilder();
		content.append("Dear ").append(user.getFirstName()).append(" ")
				.append(user.getLastName()).append(",\n\n");
		return content.toString();
	}

	/**
	 * generates new password for user & send it over mail on users mail id
	 * registered with profile.
	 * 
	 * @param user
	 *            user for which password needs to be reset
	 * @return true, if successful
	 */

	public boolean forgotPassword(User user) {
		String newPassword = PasswordUtil
				.getRandomPassword(GENERATED_PASSWORD_LENGTH);
		user.setPassword(newPassword);
		user.setForcePasswordChange(true);
		try {
			this.saveUser(user);
		} catch (UserExistsException e) {
			// will not happen
			log.error("Should not occur", e);
			throw new RuntimeException(e);
		}

		MailMsg message = new MailMsg();
		message.setTo(user.getEmail());
		message.setSubject("Ankush Password Reset Request");
		StringBuilder content = new StringBuilder();
		content.append(getUserSalutation(user));
		content.append("A password reset request was received for this Used ID.  The new password is as below: \n");
		content.append("Password : ").append(newPassword).append("\n");
		content.append("\t - The above password is a system generated password. It is recommended that you change it after login. \n");
		message.setMessage(content.toString());
		message.setContentType("text/plain");

		boolean mailStatus = AppStoreWrapper.getMailManager().sendSystemMail(
				message);

		return mailStatus;
	}

	/**
	 * sends mail to user by specifying requested user id for the login account.
	 * 
	 * @param user
	 *            user to which user ID needs to be notified
	 * @return true, if successful
	 */

	public boolean forgotUserId(User user) {
		MailMsg message = new MailMsg();
		message.setTo(user.getEmail());
		message.setSubject("Ankush User ID Details");
		StringBuilder content = new StringBuilder();
		content.append(getUserSalutation(user));

		content.append("Your User ID for ").append(PRODUCT_NAME)
				.append(" is '").append(user.getUsername()).append("'")
				.append("\n");
		content.append(
				"If you have forgotten your User ID or Password, you can request for account details/new password at ")
				.append(appConfService.getAppAccessPublicURL());
		message.setMessage(content.toString());
		message.setContentType("text/plain");

		boolean mailStatus = AppStoreWrapper.getMailManager().sendSystemMail(
				message);

		return mailStatus;
	}

	/**
	 * changes password for user if user with given user name found with user's
	 * existing password matching with given current password else throws
	 * Exception. *
	 * 
	 * @param userName
	 *            user name of user
	 * @param currentPassword
	 *            existing password of user
	 * @param newPassword
	 *            new password
	 * @throws Exception
	 *             the exception
	 */
	public void changePassword(String userName, String currentPassword,
			String newPassword) throws Exception {
		if ((userName == null) || (userName.length() == 0)) {
			return;
		}

		User user = null;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", userName);
		try {
			user = userDao.getByPropertyValue(queryMap);
			if (user != null) {
				currentPassword = passwordEncoder.encodePassword(
						currentPassword, null);
				if (user.getPassword().equals(currentPassword)) {
					user.setPassword(newPassword);
					user.setForcePasswordChange(false);
					saveUser(user);
				} else {
					throw new Exception(
							"Existing password is wrong. Please specify the correct password.");
				}
			}
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			throw new Exception("User does not exists.");
		}
	}

	/**
	 * removes user with give user id.
	 * 
	 * @param id
	 *            id for user that needs to be removed
	 */

	@Override
	public void removeUser(Long id) {
		User user = this.get(id);
		String messageStr;
		String subject;
		StringBuffer msgBuff = new StringBuffer();
		if (user.getRoles().isEmpty()) {
			messageStr = "Your ankush account creation request with user id "
					+ user.getUsername() + " has been disapproved.";
			subject = "Ankush: Account request disapproved";
		} else {
			msgBuff.append(getUserSalutation(user));
			String accessURL = appConfService.getAppAccessPublicURL();
			msgBuff.append(
					"You can no longer access Ankush at "
							+ accessURL
							+ " with the User Id: '"
							+ user.getUsername()
							+ "'. This Used ID has been deleted by the Administrator.")
					.append("\n");
			messageStr = msgBuff.toString();
			subject = "Your Ankush Account has been deleted";
		}

		MailMsg message = new MailMsg();
		message.setTo(user.getEmail());
		message.setSubject(subject);
		message.setMessage(messageStr);
		message.setContentType("text/plain");
		boolean userDeleted = true;
		try {
			this.remove(id);
		} catch (Exception e) {
			userDeleted = false;
			logger.error(e.getMessage());
		}

		try {
			if (userDeleted) {
				AppStoreWrapper.getMailManager().sendSystemMail(message);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.service.UserManager#createdUserCount()
	 */
	@Override
	public int createdUserCount() {
		return userDao.createdUserCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.UserManager#checkUserExistance(com.
	 * impetus.ankush.common.domain.User)
	 */
	@Override
	public Boolean checkUserExistance(User user) {
		boolean userExist = false;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("email", user.getEmail());
		User loadUser = null;
		try {
			loadUser = userDao.getByPropertyValue(param);
		} catch (Exception e) {
		}
		if (loadUser != null) {
			userExist = true;
		} else {
			param.clear();
			param.put("username", user.getUsername());
			try {
				loadUser = userDao.getByPropertyValue(param);
				if (loadUser != null) {
					userExist = true;
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return userExist;
	}

	@Override
	public void addAdminUser() {
		List<User> users = getUsers();
		if (users == null || users.isEmpty()) {
			try {
				Role role = new Role();
				role.setName("ROLE_SUPER_USER");
				role = roleManager.save(role);

				Set<Role> roles = new HashSet<Role>();
				roles.add(role);

				User user = new User();
				user.setEnabled(true);
				user.setId(null);
				user.setRoles(roles);
				user.setPassword("admin");
				user.setUsername("admin");
				user.setVersion(1);
				user.setAccountExpired(false);
				user.setAccountLocked(false);
				user.setCreationDate(new Date());
				user.setCredentialsExpired(false);
				user.setEmail("admin@company.com");
				user.setFirstName("Admin");
				user.setLastName("User");
				user.setForcePasswordChange(true);

				user = this.saveUser(user);
			} catch (Exception e) {
				logger.error("Unable to create 'admin' user.", e);
			}
		}
	}
	
	@Override
	public boolean doesPasswordMatch(String userName, String password) throws Exception {
		boolean status = false;
		if ((userName == null) || (userName.length() == 0)) {
			return status;
		}

		User user = null;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", userName);
		try {
			user = userDao.getByPropertyValue(queryMap);
			if (user != null) {
				password = passwordEncoder.encodePassword(
						password, null);
				if (user.getPassword().equals(password)) {
					status = true;
				} else {
					throw new Exception(
							"Given password is wrong. Please specify the correct password.");
				}
			}
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage(),e);
			throw new Exception("User does not exists.");
		}
		return status;
	}

}
