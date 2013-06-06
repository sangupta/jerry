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

package com.sangupta.jerry.batch;


/**
 * @author sangupta
 *
 */
public class WorkerGroup {
	
	private final int numWorkers;
	
	private final String name;
	
	private final Runnable[] workers;
	
	private final Thread[] workerThreads;
	
	private final ThreadGroup myThreads;
	
	private WorkerGroup(int numWorkers, String groupName) {
		this.numWorkers = numWorkers;
		this.name = groupName;
		
		this.workers = new Runnable[this.numWorkers];
		this.workerThreads = new Thread[this.numWorkers];
		
		this.myThreads = new ThreadGroup(groupName);
	}
	
	public WorkerGroup(int numWorkers, String groupName, Class<Runnable> workerClass) throws InstantiationException, IllegalAccessException {
		this(numWorkers, groupName);
		
		for(int index = 0; index < this.numWorkers; index++) {
			this.workers[index] = workerClass.newInstance();
		}
	}
	
	public WorkerGroup(int numWorkers, String groupName, Runnable worker) {
		this(numWorkers, groupName);
		
		for(int index = 0; index < this.numWorkers; index++) {
			this.workers[index] = worker;
		}
	}
	
	public void setDeamon(boolean daemon) {
		this.myThreads.setDaemon(daemon);
	}
	
	/**
	 * Instantiate and start all workers.
	 * 
	 */
	public void startWorkers() {
		
		for(int index = 0; index < this.numWorkers; index++) {
			Thread thread = new Thread(this.myThreads, this.workers[index], this.name + "-" + index);
			this.workerThreads[index] = thread;
			
			thread.start();
		}
	}
	
	/**
	 * Interrupt all worker threads.
	 * 
	 */
	public void interrupt() {
		this.myThreads.interrupt();
	}
	
	/**
	 * Method that waits for closure of the worker
	 * threads.
	 */
	public void waitForClosure() {
		for(int index = 0; index < this.numWorkers; index++) {
			if(this.workerThreads[index].isAlive()) {
				// we need to wait for this thread to die out
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				index--;
				continue;
			}
		}
	}
	
	/**
	 * Stop all workers now and wait for them to be
	 * closed.
	 * 
	 */
	public void stopWorkers() {
		this.myThreads.interrupt();
		
		this.waitForClosure();
	}

}
