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
 * Holds one record of database statistics for MongoDB.
 * 
 * @author sangupta
 *
 */
public class MongoDBStats {
	
	private String serverUsed;
	
	private String db;
	
	private int collections;
	
	private long objects;
	
	private double avgObjSize;
	
	private long dataSize;
	
	private long storageSize;
	
	private int numExtents;
	
	private int indexes;
	
	private long indexSize;
	
	private long fileSize;
	
	private int nsSizeMB;
	
	private int ok;
	
	// Usual accessors follow

	/**
	 * @return the serverUsed
	 */
	public String getServerUsed() {
		return serverUsed;
	}

	/**
	 * @param serverUsed the serverUsed to set
	 */
	public void setServerUsed(String serverUsed) {
		this.serverUsed = serverUsed;
	}

	/**
	 * @return the db
	 */
	public String getDb() {
		return db;
	}

	/**
	 * @param db the db to set
	 */
	public void setDb(String db) {
		this.db = db;
	}

	/**
	 * @return the collections
	 */
	public int getCollections() {
		return collections;
	}

	/**
	 * @param collections the collections to set
	 */
	public void setCollections(int collections) {
		this.collections = collections;
	}

	/**
	 * @return the objects
	 */
	public long getObjects() {
		return objects;
	}

	/**
	 * @param objects the objects to set
	 */
	public void setObjects(long objects) {
		this.objects = objects;
	}

	/**
	 * @return the avgObjSize
	 */
	public double getAvgObjSize() {
		return avgObjSize;
	}

	/**
	 * @param avgObjSize the avgObjSize to set
	 */
	public void setAvgObjSize(double avgObjSize) {
		this.avgObjSize = avgObjSize;
	}

	/**
	 * @return the dataSize
	 */
	public long getDataSize() {
		return dataSize;
	}

	/**
	 * @param dataSize the dataSize to set
	 */
	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}

	/**
	 * @return the storageSize
	 */
	public long getStorageSize() {
		return storageSize;
	}

	/**
	 * @param storageSize the storageSize to set
	 */
	public void setStorageSize(long storageSize) {
		this.storageSize = storageSize;
	}

	/**
	 * @return the numExtents
	 */
	public int getNumExtents() {
		return numExtents;
	}

	/**
	 * @param numExtents the numExtents to set
	 */
	public void setNumExtents(int numExtents) {
		this.numExtents = numExtents;
	}

	/**
	 * @return the indexes
	 */
	public int getIndexes() {
		return indexes;
	}

	/**
	 * @param indexes the indexes to set
	 */
	public void setIndexes(int indexes) {
		this.indexes = indexes;
	}

	/**
	 * @return the indexSize
	 */
	public long getIndexSize() {
		return indexSize;
	}

	/**
	 * @param indexSize the indexSize to set
	 */
	public void setIndexSize(long indexSize) {
		this.indexSize = indexSize;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the nsSizeMB
	 */
	public int getNsSizeMB() {
		return nsSizeMB;
	}

	/**
	 * @param nsSizeMB the nsSizeMB to set
	 */
	public void setNsSizeMB(int nsSizeMB) {
		this.nsSizeMB = nsSizeMB;
	}

	/**
	 * @return the ok
	 */
	public int getOk() {
		return ok;
	}

	/**
	 * @param ok the ok to set
	 */
	public void setOk(int ok) {
		this.ok = ok;
	}

}
