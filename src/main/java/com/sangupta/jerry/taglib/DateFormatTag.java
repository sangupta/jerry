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

package com.sangupta.jerry.taglib;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Formats a given date in a standard default pattern, or used supplied pattern.
 * 
 * @author sangupta
 *
 */
public class DateFormatTag extends SimpleTagSupport {
	
	private Object value;
	
	private String pattern = "EEE, MMM dd yyyy, HH:mm:ss z";

	@Override
	public void doTag() throws JspException, IOException {
		if(this.value == null) {
			return;
		}

		Date date  = null;
		if(this.value instanceof Date) {
			date = (Date) this.value;
		}
		
		if(this.value instanceof Long) {
			date = new Date((Long) this.value);
		}
		if(this.value instanceof File) {
			date = new Date(((File) this.value).lastModified());
		}
		
		if(date == null) {
			throw new IllegalArgumentException("Value should be either a valid date object or a long timestamp");
		}
		
		JspWriter out = getJspContext().getOut();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String formatted = format.format(date);
		out.write(formatted);
	}
	
	// Usual accessor's follow

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void setValue(long value) {
		this.value = value;
	}
	
	public void setValue(Date date) {
		this.value = date;
	}
	
	public void setValue(File file) {
		this.value = file;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
