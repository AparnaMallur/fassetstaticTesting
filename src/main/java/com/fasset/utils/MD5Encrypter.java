package com.fasset.utils;

import java.security.MessageDigest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MD5Encrypter use MD5 encryption to encrypt user passwords.
 */
public class MD5Encrypter {

	static Logger log = LogManager.getLogger(MD5Encrypter.class.getName());

	/**
	 * Encrypts the given string using MD5 encryption.
	 * 
	 * @param password
	 *            string to be encrypted
	 * @return encrypted string
	 */
	public static String encrypt(String password) {
		String hash = "";
		try {
			MessageDigest algorithm = java.security.MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(password.getBytes());
			byte[] md5 = algorithm.digest();
			String tmp = "";
			for (int i = 0; i < md5.length; i++) {
				tmp = (Integer.toHexString(0xFF & md5[i]));
				if (tmp.length() == 1) {
					hash += "0" + tmp;
				} else {
					hash += tmp;
				}
			}
		} catch (Exception e) {
			log.error("Problem encrypting password: " + password);
		}
		return hash;
	}

}
