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

package com.sangupta.jerry.jersey;

import org.springframework.context.ConfigurableApplicationContext;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * @author sangupta
 * 
 */
public class SpringServletWithCustomApplicationContext extends SpringServlet {
	
	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = -4365856846912383821L;
	
	/**
	 * 
	 */
	private static ConfigurableApplicationContext configurableApplicationContext;
	
	/**
	 * 
	 * @param context
	 */
	public static void setConfigurableApplicationContext(ConfigurableApplicationContext context) {
		SpringServletWithCustomApplicationContext.configurableApplicationContext = context;
	}

	/**
	 * Make this an abstract method so that child classes need to provide their
	 * own {@link ConfigurableApplicationContext}.
	 * 
	 */
	@Override
	protected ConfigurableApplicationContext getContext() {
		return SpringServletWithCustomApplicationContext.configurableApplicationContext;
	}
	
	/**
	 * @see com.sun.jersey.spi.spring.container.servlet.SpringServlet#getDefaultContext()
	 */
	@Override
	protected ConfigurableApplicationContext getDefaultContext() {
		return SpringServletWithCustomApplicationContext.configurableApplicationContext;
	}
	
}