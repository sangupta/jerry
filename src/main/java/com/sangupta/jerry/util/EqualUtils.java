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

/**
 * @author sangupta
 *
 */
public class EqualUtils {

	/**
	 * Tests if two byte arrays are equal in content or not.
	 * 
	 * @param bytes1
	 * @param bytes2
	 * @return
	 */
	public static boolean equals(byte[] bytes1, byte[] bytes2) {
		if(bytes1 == null || bytes2 == null) {
			return false;
		}
		
		if(bytes1 == bytes2) {
			return true;
		}
		
		if(bytes1.length != bytes2.length) {
			return false;
		}
		
		for(int index = 0; index < bytes1.length; index++) {
			if(bytes1[index] != bytes2[index]) {
				return false;
			}
		}
		
		return true;
	}

}
