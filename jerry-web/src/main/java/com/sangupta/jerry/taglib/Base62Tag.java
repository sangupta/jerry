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

package com.sangupta.jerry.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sangupta.jerry.encoder.Base62Encoder;

/**
 * @author sangupta
 *
 */
public class Base62Tag extends SimpleTagSupport {
	
	private Long encode;
	
	private String decode;
	
	/**
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		
		if(this.encode != null) {
			out.write(Base62Encoder.encode(this.encode));
			return;
		}
		
		if(this.decode != null) {
			out.write(String.valueOf(Base62Encoder.decode(this.decode)));
			return;
		}
		
		throw new IllegalArgumentException("One of encode/decode params must be specified.");
	}
	
	// Usual accessors follow

	/**
	 * @return the encode
	 */
	public Long getEncode() {
		return encode;
	}

	/**
	 * @param encode the encode to set
	 */
	public void setEncode(Long encode) {
		this.encode = encode;
	}

	/**
	 * @return the decode
	 */
	public String getDecode() {
		return decode;
	}

	/**
	 * @param decode the decode to set
	 */
	public void setDecode(String decode) {
		this.decode = decode;
	}

}
