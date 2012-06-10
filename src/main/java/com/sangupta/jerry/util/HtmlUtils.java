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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

public class HtmlUtils {
	
	/**
	 * Return all tags that start with the given name.
	 * 
	 * @param htmlBody the HTML of the page
	 * @param tagName the name of the tag being looked for
	 * @return
	 */
	public static List<StartTag> getAllTags(String htmlBody, String tagName) {
		if(AssertUtils.isEmpty(htmlBody)) {
			return null;
		}
		
		Source source = null;
		try {
			source = new Source(htmlBody);
			
			List<StartTag> tags = source.getAllStartTags(tagName);
			return tags;
		} catch(Exception e) {
			// we are unable to parse the page body
			// and extract the links
		}
		
		return null;
	}
	
	/**
	 * Extract the values of the specified attribute 'attributeToExtract' of all tags that start with the given 'tagName' in the HTML body.
	 * Also, if matching attributes are specified then they must match the given values. Also, 'ignoreMissingAttributes' this defines
	 * what to do if the matching attribute is not present in the given attributes of the tag.
	 * 
	 * @param htmlBody
	 * @param tagName
	 * @param attributeToExtract
	 * @param matchingAttributes
	 * @param ignoreMissingAttributes
	 * @return
	 */
	public static List<String> getAttributeForAllTags(final String htmlBody, final String tagName, final String attributeToExtract, final Map<String, String> matchingAttributes, final boolean ignoreMissingAttributes) {
		List<StartTag> tags = getAllTags(htmlBody, tagName);
		if(AssertUtils.isEmpty(tags)) {
			return null;
		}
		
		final List<String> values = new ArrayList<String>();
		final boolean checkMatching = AssertUtils.isNotEmpty(matchingAttributes);
		
		for(StartTag tag : tags) {
			Attributes attributes = tag.getAttributes();
			if(attributes == null || attributes.isEmpty()) {
				continue;
			}
			
			// check if we have the matching attributes
			boolean readyToExtract = true;
			if(checkMatching) {
				for(Entry<String, String> entry : matchingAttributes.entrySet()) {
					final String attributeName = entry.getKey();
					final String attributeValue = entry.getValue();
					
					Attribute attribute = attributes.get(attributeName);
					if(attribute != null) {
						// not a matching value
						if(!(attribute.getValue().equalsIgnoreCase(attributeValue))) {
							// attribute value does not match
							readyToExtract = false;
							break;
						}
					} else {
						if(!ignoreMissingAttributes) {
							// we cannot ignore missing attributes
							// check for next tag
							readyToExtract = false;
							break;
						}
					}
				}
			}
			
			// extract the value of the tag that we need
			if(readyToExtract) {
				Attribute attribute = attributes.get(attributeToExtract);
				if(attribute != null) {
					values.add(attribute.getValue());
				}
			}
		}
		
		// if the set is still empty - return null
		if(values.isEmpty()) {
			return null;
		}
		
		return values;
	}

}
