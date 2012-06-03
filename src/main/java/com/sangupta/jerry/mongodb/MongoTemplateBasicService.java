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

package com.sangupta.jerry.mongodb;

/**
 * Interface that {@link MongoTemplateBasicOperations} implements and provides
 * an abstraction layer for Spring-based applications.
 * 
 * @author sangupta
 *
 * @param <T>
 * @param <X>
 */
public interface MongoTemplateBasicService<T, X> {
	
	public T get(X primaryID);
	
	public boolean insert(T entity);
	
	public boolean update(T entity);
	
	public boolean addOrUpdate(T object);
	
	public T delete(X primaryID);
	
	public long count();

}
