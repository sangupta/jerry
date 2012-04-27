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

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sangupta
 *
 */
public class UriUtils {

	/**
	 * Characters that are allowed in a URI.
	 */
	private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";
	
	private static final Logger logger = LoggerFactory.getLogger(UriUtils.class);

	/**
	 * Function to convert a given string into URI encoded format.
	 * 
	 * @param input the source string
	 * @return the encoded string
	 */
	public static String encodeURIComponent(String input) {
		if (AssertUtils.isEmpty(input)) {
			return input;
		}

		int l = input.length();
		StringBuilder output = new StringBuilder(l * 3);
		try {
			for (int i = 0; i < l; i++) {
				String e = input.substring(i, i + 1);
				if (ALLOWED_CHARS.indexOf(e) == -1) {
					byte[] b = e.getBytes("utf-8");
					output.append(StringUtils.getHex(b));
					continue;
				}
				output.append(e);
			}
			return output.toString();
		} catch (UnsupportedEncodingException e) {
			logger.error("Unable to encode bytes to UTF-8", e);
		}
		
		return input;
	}
	
	/**
	 * Function to decode a given string from URI encoded format.
	 * 
	 * @param encodedURI the encoded string component
	 * @return the decoded string
	 */
	public static String decodeURIComponent(String encodedURI) {
		if(AssertUtils.isEmpty(encodedURI)) {
			return encodedURI;
		}
		
		char actualChar;
		StringBuffer buffer = new StringBuffer();

		int bytePattern, sumb = 0;

		for (int index = 0, more = -1; index < encodedURI.length(); index++) {
			actualChar = encodedURI.charAt(index);

			switch (actualChar) {
				case '%': {
					actualChar = encodedURI.charAt(++index);
					int hb = (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
					actualChar = encodedURI.charAt(++index);
					int lb = (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
					bytePattern = (hb << 4) | lb;
					break;
				}

				case '+': {
					bytePattern = ' ';
					break;
				}

				default: {
					bytePattern = actualChar;
				}
			}

			if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
				sumb = (sumb << 6) | (bytePattern & 0x3f);
				if (--more == 0)
					buffer.append((char) sumb);
			} else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
				buffer.append((char) bytePattern);
			} else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
				sumb = bytePattern & 0x1f;
				more = 1;
			} else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
				sumb = bytePattern & 0x0f;
				more = 2;
			} else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
				sumb = bytePattern & 0x07;
				more = 3;
			} else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
				sumb = bytePattern & 0x03;
				more = 4;
			} else { // 1111110x
				sumb = bytePattern & 0x01;
				more = 5;
			}
		}
		
		return buffer.toString();
	}

	/**
	 * @param url
	 * @return
	 */
	public static String extractExtension(String url) {
		int index = url.lastIndexOf('.');
		
		// check if extension present
		if(index == -1) {
			return null;
		}
		
		return url.substring(index + 1);
	}
}
