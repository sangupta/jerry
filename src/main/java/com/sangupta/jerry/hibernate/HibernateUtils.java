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

package com.sangupta.jerry.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Utility functions around the Hibernate framework.
 * 
 * @author sangupta
 *
 */
public class HibernateUtils {
	
	/**
	 * Method to fetch the next value in the given sequence name.
	 * 
	 * @param hibernateTemplate the {@link HibernateTemplate} to access Hibernate with
	 * 
	 * @param sequenceName the name of the sequence
	 * 
	 * @return the Long value as returned by the sequence, <code>null</code> if no value is obtained
	 * from the database.
	 * 
	 */
    @Transactional
    public static Long getNextSequenceValue(HibernateTemplate hibernateTemplate, final String sequenceName) {
    	// TODO: following query may be susceptible to SQL injection
        final String query = "select " + sequenceName + ".nextval as VALUE from DUAL";
        
		Long result = hibernateTemplate.executeWithNewSession(new HibernateCallback<Long>() {
			
            public Long doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery q = session.createSQLQuery(query);
                Object object = q.uniqueResult();
                
                if(object instanceof BigDecimal) {
                    return ((BigDecimal) object).longValue();
                }
                
                if(object instanceof Long) {
                    return (Long) object;
                }
                
                return null;
            }
            
        });
        
        return result;
    }

}
