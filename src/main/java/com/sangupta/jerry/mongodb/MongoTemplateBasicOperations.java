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
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sangupta.jerry.db.DatabaseBasicOperationsService;
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
public abstract class MongoTemplateBasicOperations<T, X> implements DatabaseBasicOperationsService<T, X> {
	
	protected MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = null;
	
	protected ConversionService conversionService = null;

	@Autowired
	protected MongoTemplate mongoTemplate = null;
	
	protected Class<T> entityClass = null;
	
	protected Class<X> primaryIDClass = null;
	
	/**
	 * The identifier key that has been used as the primary key to store in the datastore.
	 * 
	 */
	protected String idKey = null;
	
	/**
	 * Default constructor that goes ahead and infers the entity class via
	 * generics - it will be needed with {@link MongoTemplate} while working.
	 */
	public MongoTemplateBasicOperations() {
		inferEntityClassViaGenerics();
	}
	
	/**
	 * Return the object as defined by this primary key.
	 * 
	 * @param primaryID
	 * @return
	 */
	@Override
	public T get(X primaryID) {
		if(primaryID == null) {
			return null;
		}
		
		T dbObject = this.mongoTemplate.findById(primaryID, this.entityClass);
		return dbObject;
	}

	/**
	 * 
	 */
	@Override
	public List<T> getForIdentifiers(Collection<X> ids) {
		if(this.idKey == null) {
			fetchMappingContextAndConversionService();
		}
		
		Query query = new Query(Criteria.where(this.idKey).in(ids));
		return this.mongoTemplate.find(query, this.entityClass);
	}
	
	/**
	 * 
	 */
	@Override
	public List<T> getForIdentifiers(X... ids) {
		if(this.idKey == null) {
			fetchMappingContextAndConversionService();
		}
		
		Query query = new Query(Criteria.where(this.idKey).in(ids));
		return this.mongoTemplate.find(query, this.entityClass);
	}
	
	/**
	 * 
	 */
	@Override
	public List<T> getAllEntities() {
		return this.mongoTemplate.findAll(this.entityClass);
	}
	
	/**
	 * 
	 */
	@Override
	public List<T> getEntities(int page, int pageSize) {
		Query query = new Query();
		query.limit(pageSize);
		if(page > 1) {
			query.skip((page - 1) * pageSize);
		}
		return this.mongoTemplate.find(query, this.entityClass);
	}
	
	/**
	 * Insert the object into the data store. 
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public boolean insert(T entity) {
		if(entity == null) {
			return false;
		}
		
		X primaryID = getPrimaryID(entity);
		if(primaryID != null) {
			if(!allowEmptyOrZeroID() && AssertUtils.isEmpty(primaryID)) {
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
	@Override
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
	@Override
	public boolean addOrUpdate(T object) {
		if(object == null) {
			return false;
		}
		
		X primaryID = getPrimaryID(object);
		if(primaryID == null) {
			this.mongoTemplate.save(object);
			return true;
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
	@Override
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
	@Override
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
	public boolean allowEmptyOrZeroID() {
		return false;
	}
	
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

			MongoPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(entityClass);
			MongoPersistentProperty idProperty = persistentEntity.getIdProperty();
			this.idKey = idProperty == null ? "_id" : idProperty.getName();
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
	
	// Usual accessors follow

	/**
	 * @return the mongoTemplate
	 */
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	/**
	 * @param mongoTemplate the mongoTemplate to set
	 */
	@Required
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
}
