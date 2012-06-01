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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Query;

import com.sangupta.jerry.util.AssertUtils;

/**
 * Abstract MongoDB data provider of a given domain object. Providers type-safe, basic CRUD
 * operations for the object.
 * 
 * Creating a provider is as easy as,
 * <pre>
 * public class Student {
 * 	
 * 	private String id;
 * 
 * 	private String name;
 * 
 * }
 * 
 * public class MongoDBStudentServiceImpl extends MongoTemplateBasicOperations&lt;Student, String&gt; {
 * 
 * 	public boolean allowEmptyOrZeroID() {
 * 		return false;
 * 	}
 * }
 * </pre>
 * 
 * @author sangupta
 *
 */
public abstract class MongoTemplateBasicOperations<T, X> {
	
	private MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = null;
	
	private ConversionService conversionService = null;

	private MongoTemplate mongoTemplate = null;
	
	private Class<T> entityClass = null;
	
	private Class<X> primaryIDClass = null;
	
	public MongoTemplateBasicOperations() {
		inferEntityClassViaGenerics();
	}
	
	/**
	 * Return the object as defined by this primary key.
	 * 
	 * @param primaryID
	 * @return
	 */
	public T get(X primaryID) {
		if(primaryID == null) {
			return null;
		}
		
		T dbObject = this.mongoTemplate.findById(primaryID, this.entityClass);
		return dbObject;
	}
	
	/**
	 * Insert the object into the data store. 
	 * 
	 * @param entity
	 * @return
	 */
	public boolean insert(T entity) {
		if(entity == null) {
			return false;
		}
		
		X primaryID = getPrimaryID(entity);
		if(primaryID != null) {
			if(!allowEmptyOrZeroID() && AssertUtils.isNotEmpty(primaryID)) {
				return false;
			}
		}
		
		this.mongoTemplate.insert(entity);
		return true;
	}

	/**
	 * Update the entity in the data store.
	 * 
	 * @param entity
	 * @return
	 */
	public boolean update(T entity) {
		if(entity == null) {
			return false;
		}
		
		X primaryID = getPrimaryID(entity);
		if(primaryID == null) {
			return false;
		}
		
		if(!allowEmptyOrZeroID() && AssertUtils.isEmpty(primaryID)) {
			return false;
		}
		
		this.mongoTemplate.save(entity);
		return true;
	}

	/**
	 * Add or update an existing object in the data store.
	 * 
	 * @param object
	 * @return
	 */
	public boolean addOrUpdate(T object) {
		if(object == null) {
			return false;
		}
		
		X primaryID = getPrimaryID(object);
		if(primaryID == null) {
			return false;
		}
		
		if(!allowEmptyOrZeroID() && AssertUtils.isEmpty(primaryID)) {
			return false;
		}
		
		this.mongoTemplate.save(object);
		return true;
	}
	
	/**
	 * Delete 
	 * @param primaryID
	 * @return
	 */
	public T delete(X primaryID) {
		if(primaryID == null) {
			return null;
		}
		
		T entity = get(primaryID);
		if(entity == null) {
			return null;
		}
		
		this.mongoTemplate.remove(entity);
		return entity;
	}
	
	/**
	 * Count the total number of objects in the collection
	 * 
	 * @return
	 */
	public long count() {
		long items = this.mongoTemplate.count(new Query(), this.entityClass);
		return items;
	}
	
	/**
	 * Defines if we need to allow empty or zero value in primary ID
	 * of the entity object.
	 * 
	 * @return
	 */
	public abstract boolean allowEmptyOrZeroID();
	
	/**
	 * Extract the value of the primary ID of the entity object
	 * 
	 * @param entity
	 * @return
	 */
	private X getPrimaryID(T entity) {
		if(mappingContext == null || conversionService == null) {
			fetchMappingContextAndConversionService();
		}

		MongoPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(entity.getClass());
		MongoPersistentProperty idProperty = persistentEntity.getIdProperty();
		if(idProperty == null) {
			return null;
		}
		
		X idValue = BeanWrapper.create(entity, conversionService).getProperty(idProperty, this.primaryIDClass, true);
		return idValue;
	}
	
	/**
	 * Get the basic services from mongo template
	 */
	private synchronized void fetchMappingContextAndConversionService() {
		if(mappingContext == null) {
			MongoConverter mongoConverter = this.mongoTemplate.getConverter();
			mappingContext = mongoConverter.getMappingContext();
			conversionService = mongoConverter.getConversionService();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void inferEntityClassViaGenerics() {
		// fetch the entity class over which we will work
		Type t = getClass().getGenericSuperclass();
		if(t instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) t).getActualTypeArguments();
			this.entityClass = (Class<T>) actualTypeArguments[0];
			this.primaryIDClass = (Class<X>) actualTypeArguments[1];
		}
	}
	
}