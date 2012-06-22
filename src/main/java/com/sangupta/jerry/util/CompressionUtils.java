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

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utility function to compress given data into a byte-array and vice-versa.
 * Useful for situation where data needs to be stored in database etc.
 * 
 * @author sangupta
 *
 */
public class CompressionUtils {
	
	/**
	 * Compress the given string data into byte-array.
	 * 
	 * @param string
	 * @return
	 */
	public static byte[] compress(String string) {
		if(string == null) {
			return null;
		}
		
		return compress(string.getBytes());
	}
	
	/**
	 * Compress the given input byte-array into a byte-array.
	 * 
	 * @param inputBytes
	 * @return
	 */
	public static byte[] compress(byte[] inputBytes) {
		if(AssertUtils.isEmpty(inputBytes)) {
			return null;
		}
	
		byte[] output = new byte[inputBytes.length];
		
		Deflater deflater = new Deflater(Deflater.DEFLATED);
		deflater.setInput(inputBytes);
		deflater.finish();
		
		int compressedDataLength = deflater.deflate(output);
		
		deflater.end();

		if(compressedDataLength != 0) {
			byte[] finalOutput = new byte[compressedDataLength];
			System.arraycopy(output, 0, finalOutput, 0, compressedDataLength);
			return finalOutput;
		}
		
		return null;
	}
	
	/**
	 * Uncompress the given input byte-array assuming that the max uncompressed
	 * size is not known. The method will try and allocate a buffer of 3 times
	 * the size of input array. If the uncompressed size is known, use the method
	 * {@link CompressionUtils#uncompress(byte[], int)}.
	 * 
	 * @param inputBytes
	 * @return
	 */
	public static byte[] uncompress(byte[] inputBytes) {
		if(inputBytes == null) {
			return null;
		}
		
		return uncompress(inputBytes, inputBytes.length * 3);
	}
	
	/**
	 * Uncompress the byte-array when the uncompressed size is known.
	 * 
	 * @param inputBytes
	 * @param uncompressedSize
	 * @return
	 */
	public static byte[] uncompress(byte[] inputBytes, int uncompressedSize) {
		if(AssertUtils.isEmpty(inputBytes)) {
			return null;
		}
		
		Inflater inflater = new Inflater();
		inflater.setInput(inputBytes);

		byte[] output = new byte[uncompressedSize + 1];
		try {
			int total = inflater.inflate(output);
			inflater.end();
			
			return Arrays.copyOf(output, total);
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Uncompress the given byte-array and convert it into a string.
	 * 
	 * @param inputBytes
	 * @return
	 */
	public static String uncompressToString(byte[] inputBytes) {
		return new String(uncompress(inputBytes));
	}

	/**
	 * Uncompress the given byte-array and convert it into a string where uncompressed
	 * size is known.
	 * 
	 * @param inputBytes
	 * @param uncompressedSize
	 * @return
	 */
	public static String uncompressToString(byte[] inputBytes, int uncompressedSize) {
		return new String(uncompress(inputBytes, uncompressedSize));
	}

}
