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

package com.sangupta.jerry.quartz.domain;

import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.Trigger.TriggerState;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Contains information on the state of various triggers configured with the Quartz {@link Scheduler}.
 *  
 * @author sangupta
 */
@XStreamAlias("trigger")
public class QuartzJobTriggerInfo {
	
	@XStreamAlias("name")
	private String triggerName;
	
	@XStreamAlias("group")
	private String triggerGroup;
	
	private Date lastFireTime;
	
	private Date nextFireTime;
	
	private TriggerState status;
	
	public String getStatusAsString() {
		return this.status.toString();
	}
	
	// Usual accessor's follow

	/**
	 * @return the triggerName
	 */
	public String getTriggerName() {
		return triggerName;
	}

	/**
	 * @param triggerName the triggerName to set
	 */
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	/**
	 * @return the triggerGroup
	 */
	public String getTriggerGroup() {
		return triggerGroup;
	}

	/**
	 * @param triggerGroup the triggerGroup to set
	 */
	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	/**
	 * @return the lastFireTime
	 */
	public Date getLastFireTime() {
		return lastFireTime;
	}

	/**
	 * @param lastFireTime the lastFireTime to set
	 */
	public void setLastFireTime(Date lastFireTime) {
		this.lastFireTime = lastFireTime;
	}

	/**
	 * @return the nextFireTime
	 */
	public Date getNextFireTime() {
		return nextFireTime;
	}

	/**
	 * @param nextFireTime the nextFireTime to set
	 */
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	/**
	 * @return the status
	 */
	public TriggerState getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TriggerState status) {
		this.status = status;
	}
	
}
