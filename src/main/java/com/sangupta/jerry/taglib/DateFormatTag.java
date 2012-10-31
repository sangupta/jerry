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
	
	private Date value;
	
	private String pattern = "EEE, MMM dd yyyy, HH:mm:ss z";

	@Override
	public void doTag() throws JspException, IOException {
		if(value != null) {
			JspWriter out = getJspContext().getOut();
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			String formatted = format.format(value);
			out.write(formatted);
		}
	}
	
	// Usual accessor's follow

	/**
	 * @return the value
	 */
	public Date getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Date value) {
		this.value = value;
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
