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

package com.sangupta.jerry.domain;

import java.util.Date;

/**
 * A simple contract for POJO classes that act as data-store entities
 * may implement to be automatically user-time-stamped during save-updates.
 * This relieves the developer of the headache to do the same on every DB
 * call.
 * 
 * A Hibernate-based interceptor <b>UserTimeStampedModelInterceptor</b> is
 * provided that works with this model and can save the values for you. See
 * the interceptor on usage examples.
 * 
 * @author sangupta
 *
 */
public interface UserTimeStampedModel {
	
	/**
	 * Set the last updated user value to the given one.
	 * 
	 * @param username
	 */
	public void setLastUpdatedUser(String username);
	
	/**
	 * Set the last updated timestamp on this model object.
	 * 
	 * @param lastUpdatedTime
	 */
	public void setLastUpdatedTime(Date lastUpdatedTime);

}
