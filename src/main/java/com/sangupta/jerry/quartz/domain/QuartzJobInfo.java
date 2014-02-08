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

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Value object that contains information on a given Quartz {@link Job} scheduled with the scheduler.
 * 
 * @author sangupta
 */
@XStreamAlias("job")
public class QuartzJobInfo implements Comparable<QuartzJobInfo> {

	@XStreamAlias("name")
    private String jobName;
    
	@XStreamAlias("groupName")
    private String jobGroupName;
    
	private String description;
    
    private List<QuartzJobTriggerInfo> triggers = new ArrayList<QuartzJobTriggerInfo>();
    
	public void addTriggerInfo(QuartzJobTriggerInfo triggerInfo) {
		triggers.add(triggerInfo);
	}
	
	public int compareTo(QuartzJobInfo job) {
		if(job != null) {
			return this.jobName.compareTo(job.jobName);
		}
		return 0;
	}

	// Usual getter/setter's follow
	
	public List<QuartzJobTriggerInfo> getTriggers() {
		return this.triggers;
	}

    /** 
     * Returns the jobName.
     *
     * @return the jobName.
     */
    public String getJobName() {
        return this.jobName;
    }

    /** 
     * Sets the jobName to the specified value.
     *
     * @param jobName jobName to set.
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /** 
     * Returns the jobGroupName.
     *
     * @return the jobGroupName.
     */
    public String getJobGroupName() {
        return this.jobGroupName;
    }

    /** 
     * Sets the jobGroupName to the specified value.
     *
     * @param jobGroupName jobGroupName to set.
     */
    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    /** 
     * Returns the description.
     *
     * @return the description.
     */
    public String getDescription() {
        return this.description;
    }

    /** 
     * Sets the description to the specified value.
     *
     * @param description description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
