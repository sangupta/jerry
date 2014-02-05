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

import com.sangupta.jerry.util.AssertUtils;

/**
 * @author sangupta
 *
 */
public class CheckBoxTag extends SimpleTagSupport {
	
	private boolean test;
	
	private String name;
	
	private String value;
	
	private String id;
	
	/**
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		
		out.write("<input type=\"checkbox\" ");
		
		if(AssertUtils.isNotEmpty(this.id)) {
			out.write("id=\"");
			out.write(this.id);
			out.write("\" ");
		}
		
		out.write("name=\"");
		out.write(this.name);
		out.write("\" ");
		
		if(AssertUtils.isNotEmpty(this.value)) {
			out.write("value=\"");
			out.write(this.value);
			out.write("\" ");
		}
		
		if(this.test) {
			out.write("checked=\"checked\" ");
		}
		
		out.write(">\n");
	}

	// Usual accessors follow

	/**
	 * @return the test
	 */
	public boolean isTest() {
		return test;
	}

	/**
	 * @param test the test to set
	 */
	public void setTest(boolean test) {
		this.test = test;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
