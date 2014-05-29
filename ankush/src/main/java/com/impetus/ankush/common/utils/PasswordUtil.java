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
package com.impetus.ankush.common.utils;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class PasswordUtil.
 */
public class PasswordUtil {
	
	/** The Constant DEFAULT_DIGITS. */
	private static final char[] DEFAULT_DIGITS = { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };
	
	/** The Constant DEFAULT_SMALL_CASE_CHARS. */
	private static final char[] DEFAULT_SMALL_CASE_CHARS = { 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	
	/** The Constant DEFAULT_CAPITAL_CASE_CHARS. */
	private static final char[] DEFAULT_CAPITAL_CASE_CHARS = { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	/** The Constant DEFAULT_SPECIAL_CHARS. */
	private static final char[] DEFAULT_SPECIAL_CHARS = { '~', '!', '@', '#',
			'$', '%', '^', '&', '_', '-', '+' };

	// ,. :; '"` *? \/ ()[]{}<> //@&$

	/**
	 * Gets the random password.
	 *
	 * @param length the length
	 * @return the random password
	 */
	public static String getRandomPassword(int length) {
		return getRandomPassword(length, DEFAULT_DIGITS,
				DEFAULT_SMALL_CASE_CHARS, DEFAULT_CAPITAL_CASE_CHARS, null);
	}

	/**
	 * Gets the random password.
	 *
	 * @param length the length
	 * @param smallChars the small chars
	 * @param capsChars the caps chars
	 * @param digits the digits
	 * @param specialChars the special chars
	 * @return the random password
	 */
	private static String getRandomPassword(int length, char[] smallChars,
			char[] capsChars, char[] digits, char[] specialChars) {
		char[][] charsArrArr = { smallChars, capsChars, digits, specialChars };
		String password = null;
		final int charSetCategories = 4;

		StringBuilder passwordGenBuff = new StringBuilder();
		Random r = new Random();

		int arrIdx[] = new int[charSetCategories];
		for (int pos = 0; pos < charSetCategories; ++pos) {
			arrIdx[pos] = 0;
		}

		while (passwordGenBuff.length() < length) {
			int setIdx = r.nextInt(charSetCategories);
			char[] chArr = charsArrArr[setIdx];
			if ((chArr == null) || (chArr.length == 0)) {
				continue;
			} else {
				int generatedPasswordLength = passwordGenBuff.length();
				int untouchedSetCount = 0;

				if (generatedPasswordLength > 0) {
					for (int idx = 0; idx < charSetCategories; idx++) {
						char[] currArr = charsArrArr[idx];
						if ((currArr != null) && (currArr.length > 0)) {
							if (arrIdx[idx] == 0) {
								++untouchedSetCount;
							}
						}
					}

					if ((untouchedSetCount >= (length - generatedPasswordLength))
							&& (arrIdx[setIdx] > 0)) {
						continue;
					}
				}

				passwordGenBuff.append(RandomStringUtils.random(1, chArr));

				++arrIdx[setIdx];
			}
		}

		password = passwordGenBuff.toString();

		if (r.nextInt() % 2 == 0) {
			password = StringUtils.reverse(password);
		}

		return passwordGenBuff.toString();
	}

	/** The Constant ENCODED_PASSWORD_KEY. */
	private static final String ENCODED_PASSWORD_KEY = "QU1OQUtJVUxTQ0hPTkZJRw==";
	/**
	 * Class that provides encryption/decryption services.
	 */

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(PasswordUtil.class);

	/** Base64 encoded. */

	private static final byte[] SECRET_KEY = Base64
			.decodeBase64(ENCODED_PASSWORD_KEY);
	
	/** Cipher for encryption/decryption. */
	private Cipher cipher;

	/**
	 * Initialize the cipher object.
	 */
	public PasswordUtil() {
		try {
			cipher = Cipher.getInstance("AES");
		} catch (Exception e) {
			LOG.debug("Exception :", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Encrypt the given string.
	 *
	 * @param rawstring string to encrypt
	 * @return encrypted string.
	 */
	public String encrypt(final String rawstring) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY,
					"AES"));
			byte[] encrypted = cipher.doFinal(rawstring.getBytes());
			LOG.debug("Encrypting Password.. ");
			return Base64.encodeBase64String(encrypted).trim();
		} catch (Exception e) {
			LOG.debug("Exception : ", e);
			return null;
		}
	}

	/**
	 * Decrypt the given string.
	 *
	 * @param encryptedString string to decrypt
	 * @return decrypted string.
	 */
	public String decrypt(final String encryptedString) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY,
					"AES"));
			byte[] originalPasswordBytes = cipher.doFinal(Base64
					.decodeBase64(encryptedString));
			LOG.debug("Decrypting Password.. ");
			return new String(originalPasswordBytes);
		} catch (Exception e) {
			LOG.debug("Exception :", e);
			return null;
		}
	}
}
