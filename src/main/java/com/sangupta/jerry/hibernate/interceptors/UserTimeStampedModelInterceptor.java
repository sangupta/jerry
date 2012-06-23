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

package com.sangupta.jerry.hibernate.interceptors;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.sangupta.jerry.domain.UserTimeStampedModel;
import com.sangupta.jerry.security.SecurityContext;

/**
 * A hibernate-interceptor that intercepts calls to the Hibernate framework
 * and populate the last updated user fields as defined in the 
 * @author sangupta
 *
 */
public class UserTimeStampedModelInterceptor extends EmptyInterceptor {
	
	/**
	 * Interceptor method called on every save operation to set the last udpated
	 * user and timestamp on entities that implement the {@link UserTimeStampedModel} contract.
	 * 
	 * @param entity
	 * @param id
	 * @param state
	 * @param propertyNames
	 * @param types
	 * @return
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if(entity instanceof UserTimeStampedModel) {
			UserTimeStampedModel pmo = (UserTimeStampedModel) entity;
			pmo.setLastUpdatedTime(new Date());
			pmo.setLastUpdatedUser(SecurityContext.getPrincipal().getName());
		}
		
		return super.onSave(entity, id, state, propertyNames, types);
	}
	
	/**
	 * Interceptor method called on every update operation to set the last udpated
	 * user and timestamp on entities that implement the {@link UserTimeStampedModel} contract.
	 * 
	 * @param entity
	 * @param id
	 * @param currentState
	 * @param previousState
	 * @param propertyNames
	 * @param types
	 * @return
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		if(entity instanceof UserTimeStampedModel) {
			UserTimeStampedModel pmo = (UserTimeStampedModel) entity;
			pmo.setLastUpdatedTime(new Date());
			pmo.setLastUpdatedUser(SecurityContext.getPrincipal().getName());
		}
		
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}
	
}