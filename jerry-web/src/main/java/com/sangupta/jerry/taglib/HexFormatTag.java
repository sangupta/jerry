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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Formats a given number in hex format. If number cannot be parsed as {@link Long}
 * the string is written back as is.
 * 
 * @author sangupta
 *
 */
public class HexFormatTag extends SimpleTagSupport {
	
	private String value;
	
	@Override
	public void doTag() throws JspException, IOException {
		if(value != null) {
			JspWriter out = getJspContext().getOut();
			
			// try parsing number
			Long num = null;
			try {
				num = Long.parseLong(value);
			} catch(Exception e) {
				out.write(value);
				return;
			}
			
			if(num != null) {
				out.write(Long.toHexString(num));
			}
		}
	}

	// Usual accessor's follow

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
