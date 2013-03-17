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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.UriUtils;

/**
 * Encodes the given value as uri-component and either writes it out
 * or saves as a request attribute.
 * 
 * @author sangupta
 *
 */
public class EncodeUriComponentTag extends SimpleTagSupport {
	
	private String value;
	
	private String var;
	
	@Override
	public void doTag() throws JspException, IOException {
		if(AssertUtils.isEmpty(value)) {
			return;
		}
		
		String encValue = UriUtils.encodeURIComponent(this.value);
		
		if(AssertUtils.isEmpty(this.var)) {
			JspWriter out = getJspContext().getOut();
			out.write(encValue);
			return;
		}
		
		final HttpServletRequest request = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
		request.setAttribute(this.var, encValue);
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

	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}

	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}
	
}
