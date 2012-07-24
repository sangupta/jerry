/**
 *
 * jerry - Common Java Functionality
 * Copyright (c) 2012, Sandeep Gupta
 * 
 * http://www.sangupta/projects/jerry
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.jerry.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides easier access to cryptographic functions.
 * 
 * @author sangupta
 *
 */
public class CryptoUtil {
	
	/**
	 * Computes the MD5 hash of the given data.
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getMD5(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(data);
		return digest;
	}
	
	/**
	 * Computes the MD5 hash of the given data and returns 
	 * the representation in Hex format.
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5Hex(byte[] data) throws NoSuchAlgorithmException {
		byte[] digest = getMD5(data);
		return StringUtils.getHex(digest);
	}
	
	/**
	 * Computes the SHA-1 hash of the given data.
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getSHA1(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] digest = md.digest(data);
		return digest;
	}

	/**
	 * Computes the SHA-1 hash of the given data and returns the
	 * representation in Hex format.
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSHA1Hex(byte[] data) throws NoSuchAlgorithmException {
		byte[] digest = getSHA1(data);
		return StringUtils.getHex(digest);
	}
	
	/**
	 * Computes the SHA-256 hash of the given data.
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getSHA256(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(data);
		return digest;
	}
	
	/**
	 * Computes the SHA-256 hash of the given data and returns the
	 * representation in Hex format.
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSHA256Hex(byte[] data) throws NoSuchAlgorithmException {
		byte[] digest = getSHA256(data);
		return StringUtils.getHex(digest);
	}
}
