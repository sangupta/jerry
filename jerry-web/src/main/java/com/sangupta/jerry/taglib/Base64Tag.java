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

import com.sangupta.jerry.encoder.Base64Encoder;
import com.sangupta.jerry.util.AssertUtils;

/**
 * @author sangupta
 *
 */
public class Base64Tag extends SimpleTagSupport {
	
	private String encode;
	
	private String decode;
	
	private String var;
	
	/**
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {
		final JspWriter out = getJspContext().getOut();
		final HttpServletRequest request = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
		
		if(this.encode != null) {
			String encoded = Base64Encoder.encodeToString(this.encode.getBytes(), false);
			if(AssertUtils.isEmpty(this.var)) {
				out.write(encoded);
			} else {
				request.setAttribute(this.var, encoded);
			}
			
			return;
		}
		
		if(this.decode != null) {
			String decoded = new String(Base64Encoder.decode(this.decode));
			if(AssertUtils.isEmpty(this.var)) {
				out.write(decoded);
			} else {
				request.setAttribute(this.var, decoded);
			}
			return;
		}
		
		throw new IllegalArgumentException("One of encode/decode params must be specified.");
	}
	
	// Usual accessors follow

	/**
	 * @return the encode
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * @param encode the encode to set
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * @return the decode
	 */
	public String getDecode() {
		return decode;
	}

	/**
	 * @param decode the decode to set
	 */
	public void setDecode(String decode) {
		this.decode = decode;
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
