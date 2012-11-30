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

package com.sangupta.jerry.encoder;

/**
 * Simple class to convert numbers to Base62 and back. The class can encode/decode and compare
 * around 100 million long numbers in around 14 seconds on an i7 quad-core machine with 8 gigs
 * of RAM
 *  
 * @author sangupta
 *
 */
public class Base62Encoder {
	
	/**
	 * List of elements in order to be shown
	 */
	private static final char[] elements = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z'
	};
	
	/**
	 * Function to encode a given number to Base62
	 * 
	 * @param number the number to encode
	 * 
	 * @throws IllegalArgumentException if the number is less than zero
	 * 
	 * @return string representation of number
	 */
	public static String encode(long number) {
		if(number < 0) {
			throw new IllegalArgumentException("Number cannot be negative");
		}
		
		StringBuilder builder = new StringBuilder(10);
		
		int remainder;
		do {
			remainder = (int) number % 62;
			builder.append(elements[remainder]);
			number = (int) number / 62;
		} while(number > 0);
		
		return builder.toString();
	}
	
	/**
	 * Function to decode a given Base62 string back to its original form
	 * 
	 * @param string the Base62 representation
	 * 
	 * @throws IllegalArgumentException if the base62 encoded string contains foreign characters
	 *  
	 * @return the number that it represents
	 */
	public static long decode(String string) {
		char[] array = string.toCharArray();
		long num = 0;
		int index = array.length - 1;
		for( ; index >= 0; index--) {
			num = num * 62;
			char c = array[index];
			if('A' <= c && c <= 'Z') {
				num += (c - 'A');
				continue;
			}
			
			if('0' <= c && c <= '9') {
				num += (c - '0' + 26);
				continue;
			}
			
			if('a' <= c && c <= 'z') {
				num += (c - 'a' + 36);
				continue;
			}
			
			throw new IllegalArgumentException("String is not Base62 encoded");
		}
		
		return num;
	}
	
	/**
	 * Test function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		System.out.println(decode("A"));
		
		final long MAX = 100 * 1000 * 1000; // 100 million
		for(long index = 0; index < 100; index++) {
			String enc = encode(index);
			long dec = decode(enc);
			
			System.out.println("We build for " + index + ", enc: " + enc + ", dec: " + dec);
			if(index != dec) {
				break;
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Done in " + (end - start) + "ms.");
	}

}
