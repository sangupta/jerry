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

package com.sangupta.jerry.unsafe;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

/**
 * Provides capability to write to a byte buffer using Java {@link Unsafe} class.
 * 
 * @author sangupta
 *
 */
@SuppressWarnings("restriction")
public class UnsafeMemory {
	
	private static final Unsafe unsafe;
	
	static {
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (Unsafe) field.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * array offset for bytes on this machine
	 */
	private static final long byteArrayOffset = unsafe.arrayBaseOffset(byte[].class);
	
	/**
	 * array offset for longs on this machine
	 */
	private static final long longArrayOffset = unsafe.arrayBaseOffset(long[].class);
	
	/**
	 * array offset for double's on this machine
	 */
	private static final long doubleArrayOffset = unsafe.arrayBaseOffset(double[].class);

	/**
	 * Size of a boolean value
	 */
	private static final int SIZE_OF_BOOLEAN = 1;
	
	/**
	 * Size of an integer
	 */
	private static final int SIZE_OF_INT = 4;
	
	/**
	 * Size of a double
	 */
	private static final int SIZE_OF_LONG = 8;

	/**
	 * The current position in the buffer
	 */
	private int pos = 0;
	
	/**
	 * The current length of the byte buffer
	 */
	private final int bufferLength;
	
	/**
	 * The buffer of bytes over which we operate
	 */
	private final byte[] buffer;

	/**
	 * Create a new instance of {@link UnsafeMemory} that we work upon.
	 * 
	 */
	public UnsafeMemory(final byte[] buffer) {
		if (null == buffer) {
			throw new NullPointerException("buffer cannot be null");
		}

		this.buffer = buffer;
		this.bufferLength = buffer.length;
	}

	/**
	 * Reset the current position of the seek in the internal
	 * byte buffer.
	 * 
	 */
	public void reset() {
		this.pos = 0;
	}
	
	/**
	 * Return the current position of the seek in the internal
	 * byte buffer.
	 * 
	 * @return
	 */
	public int getPosition() {
		return this.pos;
	}
	
	/**
	 * Write a string to the current location in the buffer. The length of the string
	 * is written first as an {@link Integer}.
	 * 
	 * @param value
	 */
	public void putString(final String value) {
		if(value == null) {
			putInt(0);
			return;
		}

		putInt(value.length());
		char[] chars = value.toCharArray();
		for(char c : chars) {
			unsafe.putChar(buffer, byteArrayOffset + pos, c);
			pos++;
		}
	}
	
	/**
	 * A method that checks for the position pointer inside the buffer
	 * to be in the range before making a read call.
	 * 
	 */
	private final void positionCheck() {
		if(this.pos > this.bufferLength) {
			throw new IndexOutOfBoundsException("Trying to read from memory position out of buffer area");
		}
	}
	
	/**
	 * Read a string from the current location in buffer. The first {@link Integer}
	 * in the string would be its length.
	 *  
	 * @return
	 */
	public String getString() {
		positionCheck();
		
		int length = getInt();
		if(length == 0) {
			return null;
		}
		
		return getString(length);
	}
	
	/**
	 * Read a string from the current location in buffer. The length of the string
	 * has been provided.
	 * 
	 * @param length
	 * @return
	 */
	public String getString(int length) {
		positionCheck();
		
		char[] values = new char[length];
		
		for(int index = 0; index < length; index++) {
			values[index] = (char) unsafe.getByte(buffer, byteArrayOffset + pos);
			pos ++;
		}

		return String.valueOf(values);
	}

	/**
	 * Write a boolean value to the memory buffer.
	 * 
	 * @param value
	 */
	public void putBoolean(final boolean value) {
		positionCheck();
		
		unsafe.putBoolean(buffer, byteArrayOffset + pos, value);
		pos += SIZE_OF_BOOLEAN;
	}

	/**
	 * Read a boolean value from the memory buffer.
	 * 
	 * @return
	 */
	public boolean getBoolean() {
		positionCheck();
		
		boolean value = unsafe.getBoolean(buffer, byteArrayOffset + pos);
		pos += SIZE_OF_BOOLEAN;

		return value;
	}

	/**
	 * Write an integer value to the memory buffer.
	 * 
	 * @param value
	 */
	public void putInt(final int value) {
		unsafe.putInt(buffer, byteArrayOffset + pos, value);
		pos += SIZE_OF_INT;
	}

	/**
	 * Read an integer value from the memory buffer.
	 * 
	 * @return
	 */
	public int getInt() {
		positionCheck();
		
		int value = unsafe.getInt(buffer, byteArrayOffset + pos);
		pos += SIZE_OF_INT;

		return value;
	}

	/**
	 * Write a long value to the memory buffer.
	 * 
	 * @param value
	 */
	public void putLong(final long value) {
		unsafe.putLong(buffer, byteArrayOffset + pos, value);
		pos += SIZE_OF_LONG;
	}

	/**
	 * Read a long value from the memory buffer.
	 * 
	 * @return
	 */
	public long getLong() {
		positionCheck();
		
		long value = unsafe.getLong(buffer, byteArrayOffset + pos);
		pos += SIZE_OF_LONG;

		return value;
	}

	/**
	 * Write an array of long values to the memory buffer.
	 * 
	 * @param values
	 */
	public void putLongArray(final long[] values) {
		putInt(values.length);

		long bytesToCopy = values.length << 3;
		unsafe.copyMemory(values, longArrayOffset, buffer, byteArrayOffset + pos, bytesToCopy);
		pos += bytesToCopy;
	}

	/**
	 * Read an array of long values from the memory buffer.
	 * 
	 * @return
	 */
	public long[] getLongArray() {
		positionCheck();
		
		int arraySize = getInt();
		long[] values = new long[arraySize];

		long bytesToCopy = values.length << 3;
		unsafe.copyMemory(buffer, byteArrayOffset + pos, values, longArrayOffset, bytesToCopy);
		pos += bytesToCopy;

		return values;
	}

	/**
	 * Write an array of double values into the memory buffer.
	 * 
	 * @param values
	 */
	public void putDoubleArray(final double[] values) {
		putInt(values.length);

		long bytesToCopy = values.length << 3;
		unsafe.copyMemory(values, doubleArrayOffset, buffer, byteArrayOffset + pos, bytesToCopy);
		pos += bytesToCopy;
	}

	/**
	 * Read an array of double values from the memory buffer.
	 * 
	 * @return
	 */
	public double[] getDoubleArray() {
		positionCheck();
		
		int arraySize = getInt();
		double[] values = new double[arraySize];

		long bytesToCopy = values.length << 3;
		unsafe.copyMemory(buffer, byteArrayOffset + pos, values, doubleArrayOffset, bytesToCopy);
		pos += bytesToCopy;

		return values;
	}

}
