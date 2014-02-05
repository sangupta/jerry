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

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.UriUtils;

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
	 * Name of the page context attribute that is used to include all tags
	 */
	public static final String JAVASCRIPT_INCLUDE_URL_TAG = "javascript.include.url.tag";
	
	/**
	 * The url of the file to include. If specified, the URL is included at the end
	 */
	private String url;
	
	/**
	 * Specifies if application context needs to be appended to the URL or not
	 */
	private boolean appendContext = false;
	
	/** (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		super.doStartTag();

		if(AssertUtils.isNotEmpty(this.url)) {
			final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			Object object = request.getAttribute(JavascriptIncludeTag.JAVASCRIPT_INCLUDE_URL_TAG);
			List<String> urls;
			
			if(object == null) {
				urls = new ArrayList<String>();
				request.setAttribute(JavascriptIncludeTag.JAVASCRIPT_INCLUDE_URL_TAG, urls);
			} else {
				urls = (List<String>) object;
			}
			
			if(this.appendContext) {
				url = UriUtils.addWebPaths(request.getContextPath(), url); 
			}
			
			// do not add a duplicate url here
			if(!urls.contains(url)) {
				urls.add(url);
			}
			
			return SKIP_BODY;
		}
		
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doAfterBody()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int doAfterBody() throws JspException {
		if(AssertUtils.isNotEmpty(this.url)) {
			return SKIP_BODY;
		}
		
		final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		BodyContent bodycontent = getBodyContent();
		if(bodycontent == null) {
			// nothing to do
			return SKIP_BODY;
		}
		
		String body = bodycontent.getString();
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

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the appendContext
	 */
	public boolean isAppendContext() {
		return appendContext;
	}

	/**
	 * @param appendContext the appendContext to set
	 */
	public void setAppendContext(boolean appendContext) {
		this.appendContext = appendContext;
	}
}
