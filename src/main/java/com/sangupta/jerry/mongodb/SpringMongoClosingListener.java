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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * {@link ApplicationListener} of Spring framework that allows for closing of MongoDB
 * instance so that all thread local's are released and it does not create a memory leak.
 * 
 * @author sangupta
 *
 */
public class SpringMongoClosingListener implements ApplicationListener<ContextClosedEvent> {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private Mongo mongo;
	
	/**
	 * Initialize method that gets the instance of MongoDB that needs
	 * to be closed.
	 * 
	 */
	@PostConstruct
	public void init() {
		DB mongoDB = this.mongoTemplate.getDb();
		this.mongo = mongoDB.getMongo();
		
		// add a JVM shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				if(mongo != null) {
					mongo.close();
					mongo = null;
				}
			}
			
		});
	}

	/**
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		if(this.mongo != null) {
			this.mongo.close();
			this.mongo = null;
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
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
