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
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author sangupta
 *
 */
public class UserNameTag extends SimpleTagSupport {
	
	private String onAnonymous = "anonymous";
	
	/**
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		Principal principal = ((HttpServletRequest) pageContext.getRequest()).getUserPrincipal();
		JspWriter out = getJspContext().getOut();
		
		if(principal == null) {
			out.write(onAnonymous);
		} else {
			principal.getName();
		}
		
	}
	
	// Usual accessors follow

	/**
	 * @return the onAnonymous
	 */
	public String getOnAnonymous() {
		return onAnonymous;
	}

	/**
	 * @param onAnonymous the onAnonymous to set
	 */
	public void setOnAnonymous(String onAnonymous) {
		this.onAnonymous = onAnonymous;
	}

}
