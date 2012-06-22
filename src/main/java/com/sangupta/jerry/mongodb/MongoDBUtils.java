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

import org.springframework.data.mongodb.core.MongoTemplate;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.mongodb.CommandResult;
import com.mongodb.DB;


/**
 * Utility class that lets you perform operations on MongoDB
 * directly.
 * 
 * @author sangupta
 *
 */
public class MongoDBUtils {

	/**
	 * Returns the MongoDB statistics for the given database.
	 * 
	 * @param mongoDatabase
	 * 
	 * @return
	 * 
	 * @throws NullPointerException if database instance provided is <code>null</code>.
	 */
	public static MongoDBStats getDatabaseStatistics(DB mongoDatabase) {
		CommandResult commandResult = mongoDatabase.getStats();
		MongoDBStats stats = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create().fromJson(commandResult.toString(), MongoDBStats.class);
		return stats;
	}
	
	/**
	 * Returns the MongoDB statistics for the database in use by this {@link MongoTemplate}.
	 * 
	 * @param template
	 * 
	 * @return
	 * 
	 * @throws NullPointerException if template instance provided is <code>null</code>.
	 */
	public static MongoDBStats getDatabaseStatistics(MongoTemplate template) {
		return getDatabaseStatistics(template.getDb());
	}
	
}
