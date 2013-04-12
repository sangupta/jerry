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

package com.sangupta.jerry.util;

import java.util.Date;

/**
 * @author sangupta
 *
 */
public class DateUtils {
	
	/**
	 * One second expressed as millis
	 */
	public static final long ONE_SECOND = 1000l;
	
	/**
	 * Fifteen seconds expressed as millis
	 */
	public static final long FIFTEEN_SECONDS = 15l * ONE_SECOND;
	
	/**
	 * One minute expressed as millis
	 */
	public static final long ONE_MINUTE = 60l * ONE_SECOND;
	
	/**
	 * Five minutes expressed as millis
	 */
	public static final long FIVE_MINUTES = 15l * ONE_MINUTE;

	/**
	 * Fifteen minutes expressed in millis
	 */
	public static final long FIFTEEN_MINUTES = 15l * ONE_MINUTE;
	
	/**
	 * One hour expressed as millis
	 */
	public static final long ONE_HOUR = 60l * ONE_MINUTE;
	
	/**
	 * One day (24-hours) expressed as millis
	 */
	public static final long ONE_DAY = 24l * ONE_HOUR;
	
	/**
	 * One week (7 days) expressed as millis
	 */
	public static final long ONE_WEEK = 7l * ONE_DAY;
	
	/**
	 * One month (30-days) expressed as millis
	 */
	public static final long ONE_MONTH = 30l * ONE_DAY; 

    /**
     * Convert the given time (in millis) represented as a {@link Long} object into the {@link Date} object.
     * 
     * @param millis
     * @return
     */
    public static final Date getDate(Long millis) {
        if(millis == null) {
            return null;
        }
        return new Date(millis);
    }

}
