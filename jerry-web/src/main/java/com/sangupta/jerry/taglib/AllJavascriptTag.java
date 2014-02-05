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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sangupta.jerry.util.AssertUtils;

/**
 * Tag that adds all javascript in the {@link ThreadLocal} variable at the given
 * location in the page.
 * 
 * @author sangupta
 *
 */
public class AllJavascriptTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		final PageContext pageContext = (PageContext) getJspContext();
		final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		final JspWriter out = getJspContext().getOut();
		
		
		// let's emit all source files first
		Object object = request.getAttribute(JavascriptIncludeTag.JAVASCRIPT_INCLUDE_URL_TAG);
		if(object != null) {
			@SuppressWarnings("unchecked")
			List<String> urls = (List<String>) object;
			if(AssertUtils.isNotEmpty(urls)) {
				for(String url : urls) {
					out.write("<script type=\"text/javascript\" src=\"" + url + "\">");
					out.write("</script>\n");
				}
			}
		}
		
		// now turn of individual script bodies
		object = request.getAttribute(JavascriptIncludeTag.JAVASCRIPT_INCLUDE_TAG);
		if(object == null) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) object;
		if(list.size() == 0) {
			return;
		}
		
		out.write("<script type=\"text/javascript\">");
		
		for(String script : list) {
			out.write(script);
			out.write("\n\n");
		}
		
		pageContext.removeAttribute(JavascriptIncludeTag.JAVASCRIPT_INCLUDE_TAG);
		
		out.write("</script>");
	}
	
}
