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

package com.sangupta.jerry.db;

import java.util.Collection;
import java.util.List;

/**
 * Interface that and database-based service implements for common
 * CRUD operations in a database and provides an abstraction layer
 * for Spring-based applications.
 * 
 * @author sangupta
 *
 * @param <T> The entity object type which is persisted in the datastore
 * @param <X> The primary ID key for this entity object
 */
public interface DatabaseBasicOperationsService<T, X> {
	
	/**
	 * Retrieve the entity object with the given primary key.
	 * 
	 * @param primaryID
	 * @return
	 */
	public T get(X primaryID);
	
	/**
	 * Insert a new entity object into the data store
	 * 
	 * @param entity
	 * @return
	 */
	public boolean insert(T entity);
	
	/**
	 * Update the entity object into the data store
	 * 
	 * @param entity
	 * @return
	 */
	public boolean update(T entity);
	
	/**
	 * Add or update the entity object into the data store
	 * 
	 * @param entity
	 * @return
	 */
	public boolean addOrUpdate(T entity);
	
	/**
	 * Delete the data store entity with the given primary key
	 *  
	 * @param primaryID
	 * @return
	 */
	public T delete(X primaryID);
	
	/**
	 * Return the count of total objects in the data store
	 * 
	 * @return
	 */
	public long count();
	
	/**
	 * Retrieves a list of all entities in the datastore that match the list
	 * of given primary identifiers.
	 * 
	 * <b>Note:</b> If there are too many entity identifiers supplied, the code
	 * may go out of memory or may take too long to complete. This method should
	 * be used only by developer at discretion.
	 * 
	 * @param ids
	 * @return
	 */
	public List<T> getForIdentifiers(Collection<X> ids);
	
	public List<T> getForIdentifiers(X... ids);

	/**
	 * Retrieves a list of all entities in the datastore.
	 * 
	 * <b>Note:</b> If there are too many entities in the data store, the code
	 * may go out of memory or may take too long to complete. This method should
	 * be used only by developers at discretion.
	 * 
	 * It is recommended not to use this method in production instances. Rather, use
	 * the method {@link #getEntities(int, int)}.
	 * 
	 * @return
	 */
	public List<T> getAllEntities();

	/**
	 * Retrieves a list of entities for the given page number with the give page
	 * size. The page numbering starts from 1.
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<T> getEntities(int page, int pageSize);
	
	/**
	 * Clean the database of all entities in this collection.
	 * 
	 */
	public void deleteAllEntities();
}
