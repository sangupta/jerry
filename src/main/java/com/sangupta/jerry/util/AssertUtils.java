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

import java.util.Collection;
import java.util.Map;

/**
 * Common assertion functions that are null safe. These may not be
 * performant for common Java data types like {@link String} due
 * to the extra overhead of a method call.
 * 
 * @author sangupta
 *
 */
public class AssertUtils {
	
	/**
	 * Check if a given string is <code>null</code> or zero-length.
	 * Returns <code>false</code> even if the string contains white
	 * spaces.
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		if(string == null || string.length() == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if a given string is <code>non null</code> and non-zero
	 * length. Whitespaces are considered to be non-empty.
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		if(string == null || string.length() == 0) {
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		if(array == null || array.length == 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * @param array
	 * @return
	 */
	public static boolean isNotEmpty(Object[] array) {
		if(array == null || array.length == 0) {
			return false;
		}
		
		return true;
	}

	/**
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Map params) {
		if(params == null || params.isEmpty()) {
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Map params) {
		if(params == null || params.isEmpty()) {
			return false;
		}
		
		return true;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection collection) {
		if(collection == null || collection.isEmpty()) {
			return true;
		}
		
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Collection collection) {
		if(collection == null || collection.isEmpty()) {
			return false;
		}
		
		return true;
	}

	/**
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		if(object == null) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isNotEmpty(Object object) {
		if(object == null) {
			return false;
		}
		
		return true;
	}
}
