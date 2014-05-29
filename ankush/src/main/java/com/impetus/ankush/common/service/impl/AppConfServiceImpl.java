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

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.AppConf;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.exception.UserExistsException;
import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.PasswordUtil;

/**
 * The Class AppConfServiceImpl.
 */
public class AppConfServiceImpl implements AppConfService {

	private static final String DEFAULT_SERVER_PORT = "8080";

	/** The Constant KEY_EMAIL. */
	private static final String KEY_EMAIL = "email";

	/** The Constant KEY_PASSWORD. */
	private static final String KEY_PASSWORD = "password";

	/** The Constant KEY_SERVERIP. */
	private static final String KEY_SERVERIP = "serverIP";

	/** The Constant KEY_USER. */
	private static final String KEY_USER = "users";

	/** The Constant KEY_CONFKEY. */
	private static final String KEY_CONFKEY = "confKey";

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			AppConfServiceImpl.class);

	/** The app conf manager. */
	private GenericManager<AppConf, Long> appConfManager;

	/** The user manager. */
	private UserManager userManager;

	/* Error list */
	/** The errors. */
	private List<String> errors = new ArrayList<String>();

	/* Store result */
	/** The result. */
	private Map<String, Object> result = new HashMap<String, Object>();

	/**
	 * Sets the user manager.
	 * 
	 * @param userManager
	 *            the new user manager
	 */
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * Sets the ankush configurator manager.
	 * 
	 * @param appConfManager
	 *            the app conf manager
	 */
	@Autowired
	public void setAnkushConfiguratorManager(
			@Qualifier("appConfManager") GenericManager<AppConf, Long> appConfManager) {
		this.appConfManager = appConfManager;
	}

	/**
	 * Adds the error.
	 * 
	 * @param error
	 *            the error
	 */
	protected void addError(String error) {
		this.errors.add(error);
	}

	/**
	 * Clear result.
	 */
	protected void clearResult() {
		this.result.clear();
		this.errors.clear();
	}

	/**
	 * Return result.
	 * 
	 * @return the map
	 */
	protected Map returnResult() {
		if (this.errors.isEmpty()) {
			this.result.put("status", true);
		} else {
			this.result.put("status", false);
			this.result.put("error", this.errors);
		}
		return this.result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.service.AppConfService#getState()
	 */
	public String getState() {
		if (appConfManager.getByPropertyValueGuarded(KEY_CONFKEY, KEY_EMAIL) == null
				|| appConfManager.getByPropertyValueGuarded(KEY_CONFKEY,
						KEY_SERVERIP) == null
				|| userManager.getUsers().size() == 0) {
			return Constant.App.State.CONFIGURE;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.AppConfService#manageCommonConfiguration
	 * (com.impetus.ankush.common.service.impl.AnkushApplicationConf)
	 */
	@Override
	public Map manageCommonConfiguration(AnkushApplicationConf commonConf) {
		clearResult();

		Map map = new HashMap();
		try {

			// Managing Email and Server IP Configuration
			manageAppConf(commonConf);

			// Managing Users
			map.put("user",
					manageAnkushUsers(commonConf.getUsers(),
							commonConf.getLoggedUser()));

		} catch (Exception e) {
			addError("Error in managing Common configuration, Reson : "
					+ e.getMessage());
		}
		result.putAll(map);
		return returnResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.AppConfService#getCommonConfiguration()
	 */
	@Override
	public Map getCommonConfiguration() {
		Map<String, Object> ankushAppConf = new HashMap<String, Object>();
		try {
			List<User> ankushUsers = userManager.getUsers();
			Map serverIpInfo = getAppConf(KEY_SERVERIP);

			Object emailConf = getAppConf(KEY_EMAIL);
			Map<String, String> emailMap = new HashMap<String, String>();
			if (emailConf != null
					&& (getAppConf(KEY_EMAIL).get(KEY_EMAIL) != null)) {
				emailConf = getAppConf(KEY_EMAIL).get(KEY_EMAIL);
				emailMap = (Map) emailConf;
				String encryptedPassword = emailMap.get(KEY_PASSWORD);
				String decryptedPassword = new PasswordUtil()
						.decrypt(encryptedPassword);
				emailMap.put(KEY_PASSWORD, decryptedPassword);
			}
			Map serverIpMap = new HashMap();
			if ((serverIpInfo != null)
					&& serverIpInfo.get(KEY_SERVERIP) != null) {
				serverIpMap = (Map) serverIpInfo.get(KEY_SERVERIP);
			}
			ankushAppConf.put(KEY_EMAIL, emailMap);
			ankushAppConf.put(KEY_SERVERIP, serverIpMap);
			ankushAppConf.put(KEY_USER, ankushUsers);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError("Could not get Ankush common configuration information..");
		}

		result.putAll(ankushAppConf);
		return returnResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.AppConfService#getAppConf(java.util
	 * .Enumeration)
	 */
	public Map getAppConf(Enumeration keys) {
		Map res = new HashMap();

		// Return empty result
		if (keys == null) {
			return res;
		}

		// Iterate keys
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();

			// Fetch values from database
			try {
				AppConf appConf = appConfManager.getByPropertyValueGuarded(
						KEY_CONFKEY, key);
				if (appConf != null) {
					res.put(key, appConf.getObject());
				} else {
					res.put("error", "Unable to fetch " + key);
				}

			} catch (Exception e) {
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.AppConfService#getAppConf(java.lang
	 * .String)
	 */
	@Override
	public Map getAppConf(String key) {
		Map res = new HashMap();
		try {
			AppConf appConf = appConfManager.getByPropertyValueGuarded(
					KEY_CONFKEY, key);
			if (appConf != null) {
				res.put(key, appConf.getObject());
			} else {
				res.put(Constant.Keys.ERROR, "Unable to fetch " + key);
			}

		} catch (Exception e) {
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.AppConfService#setAppConf(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public boolean setAppConf(String key, Object value) {
		AppConf appConf = appConfManager.getByPropertyValueGuarded(KEY_CONFKEY,
				key);
		if (appConf == null) {
			appConf = new AppConf();
		}
		appConf.setConfKey(key);
		appConf.setObject(value);
		try {
			appConfManager.save(appConf);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.AppConfService#getAppAccessPublicURL()
	 */
	public String getAppAccessPublicURL() {
		Map m = (Map) getAppConf(AppConfServiceImpl.KEY_SERVERIP).get(
				AppConfServiceImpl.KEY_SERVERIP);
		String serverIP = "";
		String port = "";
		if ((m != null) && m.size() > 0 && (m.get("error") == null)) {
			serverIP = (String) m.get("publicIp");
			port = (String) m.get("port");
		}

		String appURL = "http://" + serverIP + ":" + port + "/ankush/";
		String accessURL = null;
		if ((serverIP != null) && !serverIP.equals("")) {
			accessURL = appURL;
		}
		return accessURL;
	}

	/**
	 * Manage app conf.
	 * 
	 * @param appConf
	 *            the app conf
	 * @return the map
	 */
	private Map manageAppConf(AnkushApplicationConf appConf) {
		Map<String, String> responseMap = new HashMap<String, String>();
		try {
			Object email = appConf.getEmail();
			String emailStr = JsonMapperUtil.jsonFromObject(email);
			JsonMapperUtil.objectFromString(emailStr, Object.class);
			Map appMail = (Map) email;
			String originalPassword = (String) appMail.get(KEY_PASSWORD);
			String password = new PasswordUtil().encrypt(originalPassword);
			appMail.put(KEY_PASSWORD, password);

			setAppConf(KEY_EMAIL, appMail);
			AppStoreWrapper.setupMail(appMail);

			setAppConf(KEY_SERVERIP, appConf.getServerIP());
		} catch (Exception e) {
			logger.error(e.getMessage());
			addError("Error in saving App conf information");
		}
		return responseMap;
	}

	// Managing Users
	/** The Constant USER_STATUS_NONE. */
	public static final String USER_STATUS_NONE = "None";

	/** The Constant USER_STATUS_ADD. */
	public static final String USER_STATUS_ADD = "Add";

	/** The Constant USER_STATUS_DELETE. */
	public static final String USER_STATUS_DELETE = "Delete";

	/** The Constant USER_STATUS_UPDATE. */
	public static final String USER_STATUS_UPDATE = "Update";

	/**
	 * Manage ankush users.
	 * 
	 * @param ankushUsers
	 *            the ankush users
	 * @param loggedUser
	 *            the logged user
	 * @return the map
	 */
	private Map manageAnkushUsers(List<User> ankushUsers, User loggedUser) {
		class UserComparator implements Comparator<User> {
			public int compare(User prm1, User prm2) {
				return prm1.getUserStatus().toUpperCase()
						.compareTo(prm2.getUserStatus().toUpperCase());
			}
		}
		Collections.sort(ankushUsers,
				Collections.reverseOrder(new UserComparator()));

		Map<Long, Object> status = new HashMap<Long, Object>();
		for (User au : ankushUsers) {
			User databaseUser = au;
			Long assignedId = au.getId();

			Long userId = au.getId();
			try {
				String userStatus = au.getUserStatus();
				if ((userStatus == null)
						|| (userStatus.equals(USER_STATUS_NONE))) {
					continue;
				}
				if (userStatus.equals(USER_STATUS_ADD)) {
					if (au.getPassword() == null) {
						au.setPassword("");
					}
					User addedUser = null;
					synchronized (this) {
						try {
							if (!userManager.checkUserExistance(au)) {
								// addedUser = userManager.createUser(au, true);
								addedUser = userManager.createUser(au,
										au.isEnabled());
								userId = addedUser.getId();
								databaseUser = addedUser;
							} else {
								addError("Duplicate UserId / Email for user - "
										+ au.getUsername());
							}
						} catch (UserExistsException e) {
							addError("Error occurred. Unable to Save Information & "
									+ e.getMessage());
						}
					}
				}

				if (userStatus.equals(USER_STATUS_UPDATE)) {
					try {
						userId = au.getId();
						User existingUser = userManager.get(userId);
						existingUser.merge(au);
						databaseUser = userManager.saveUser(existingUser);
					} catch (Exception e) {
						addError("Could not update user '" + au.getUsername()
								+ "' information..");
					}
				}

				if (userStatus.equals(USER_STATUS_DELETE)) {

					userId = au.getId();
					User requestedUser = userManager.get(userId);

					// User cannot delete himself
					if ((loggedUser == null)
							|| loggedUser.equals(requestedUser)) {
						addError("User cannot be deleted !");
					} else {
						userManager.removeUser(userId);
					}
				}

				status.put(assignedId, databaseUser);
			} catch (Exception e) {
				addError("Error in updating users..");
				logger.error(e.getMessage());
			}
		}
		return status;
	}

	/**
	 * Method to set default Host IP address and port.
	 */
	public void setDefaultHostAddress() {
		try {
			// fetching existing appconf object if exists.
			AppConf appConf = appConfManager.getByPropertyValueGuarded(
					KEY_CONFKEY, KEY_SERVERIP);
			// if not exists.
			if (appConf == null) {
				// Fetching the host ip address.
				InetAddress addr = InetAddress.getLocalHost();

				// creating a hash map object.
				HashMap map = new HashMap();
				map.put(Constant.Keys.PUBLICIP, addr.getHostAddress());
				map.put(Constant.Keys.PORT, DEFAULT_SERVER_PORT);

				// setting key and value in app conf.
				appConf = new AppConf();
				appConf.setConfKey(KEY_SERVERIP);
				appConf.setObject(map);

				// saving app conf object.
				appConfManager.save(appConf);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Map getMetadata(String file) {
		Map<String, Object> result = new HashMap<String, Object>();
		String fileName = AppStoreWrapper.getResourcePath() + "metadata/"
				+ file + ".json";
		try {
			result.put(file, new ObjectMapper().readValue(new File(fileName),
					new TypeReference<Object>() {
					}));
		} catch (Exception e) {
			result.put("error", "Failed to get " + file);
			logger.error(e.getMessage(), e);
		}

		return result;
	}
}
