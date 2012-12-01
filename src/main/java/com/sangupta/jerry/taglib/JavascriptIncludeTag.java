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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author sangupta
 * 
 */
public class JavascriptIncludeTag extends BodyTagSupport {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = 2077562949591289721L;

	/**
	 * Name of the page context attribute that is used to include all tags
	 */
	public static final String JAVASCRIPT_INCLUDE_TAG = "javascript.include.tag";

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int doAfterBody() throws JspException {
		BodyContent bodycontent = getBodyContent();
		String body = bodycontent.getString();
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Object object = request.getAttribute(JavascriptIncludeTag.JAVASCRIPT_INCLUDE_TAG);
		
		List<String> scripts;
		if(object == null) {
			scripts = new ArrayList<String>();
			request.setAttribute(JavascriptIncludeTag.JAVASCRIPT_INCLUDE_TAG, scripts);
		} else {
			scripts = (List<String>) object;
		}
		
		scripts.add(body);
		
		return SKIP_BODY;
	}
}
