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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility functions around {@link String} objects.
 * 
 * @author sangupta
 *
 */
public class StringUtils {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);
	
	/**
	 * An empty string object containing nothing.
	 */
	public static final String EMPTY_STRING = "";
	
	/**
	 * A blank string containing one blank white-space character.
	 */
	public static final String BLANK_STRING = " ";

	/**
	 * Defines the platform dependent line encoding
	 */
	public static final String SYSTEM_NEW_LINE = System.getProperty("line.separator");
	
	/**
	 * Function to give a HEX representation of the byte array.
	 * 
	 * @param bytes the source byte array
	 * @return the HEX coded string representing the byte array
	 */
	public static String getHex(byte bytes[]) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; ++i) {
			sb.append(Integer.toHexString((bytes[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}
	
	/**
     * Return true if the string is equal to "true" or "yes"
     * otherwise return false.
     * 
     * @param string string whose content is checked for true or false
     * @return boolean flag indicating whether the input was true or false
     */
	public static boolean getBoolean(String boolString) {
	    return getBoolean(boolString, false); 
 	}
		
	public static boolean getBoolean(String boolString, boolean defaultValue) {
		if (AssertUtils.isNotEmpty(boolString)) {
	        boolString = boolString.toLowerCase();
	        if ("yes".equals(boolString) || "true".equals(boolString)) {
	            return true;
	        }
	        
	        return false;
	    }
		
	    return defaultValue; 
 	}
	
	public static int getIntValue(String string, int defaultValue) {
		try {
			if(AssertUtils.isNotEmpty(string)) {
				return Integer.parseInt(string);
			}
		} catch(NumberFormatException e) {
			LOGGER.debug("error getting integer from string: " + string, e);
		}
		
		return defaultValue;
	}
	
	public String fromList(List<Object> list, String appender) {
		if(AssertUtils.isEmpty(list)) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder();
		for(int index = 0; index < list.size(); index++) {
			if(index > 0) {
				builder.append(appender);
			}
			builder.append(list.get(index));
		}
		
		return builder.toString();
	}
	
	public static boolean contains(String[] list, String value) {
		if(list == null) {
			return false;
		}
		
		if(list.length == 0) {
			return false;
		}
		
		for(String item : list) {
			if(item.equals(value)) {
				return true;
			}
		}
		
		return false;
	}

}
