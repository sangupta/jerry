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

package com.sangupta.jerry.jersey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.sangupta.jerry.util.XStreamUtils;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;

/**
 * XML serialization mechanism for Jersey. We don't want to use the default XML
 * serialization as customization of XML fields is already being done via XStream
 * annotations all through out the code.
 * 
 * @author sangupta
 *
 */
@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Consumes({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
@Provider
public class XStreamXmlProvider extends AbstractMessageReaderWriterProvider<Object> {

    private static final String DEFAULT_ENCODING = "utf-8";
    
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType arg3) {
        return true;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType arg3) {
    	return true;
    }

    /**
     * Returns the charset as a string depending on the encoding specified, or
     * the usual default of <code>UTF-8</code>.
     * 
     * @param m
     * @return
     */
    protected static String getCharsetAsString(MediaType m) {
        if (m == null) {
            return DEFAULT_ENCODING;
        }
        String result = m.getParameters().get("charset");
        return (result == null) ? DEFAULT_ENCODING : result;
    }

    public Object readFrom(Class<Object> aClass, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> map, InputStream stream) throws IOException, WebApplicationException  {
        String encoding = getCharsetAsString(mediaType);
        XStream xStream = XStreamUtils.getXStream(aClass);
        return xStream.fromXML(new InputStreamReader(stream, encoding));
    }

    public void writeTo(Object o, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> map, OutputStream stream) throws IOException, WebApplicationException {
        String encoding = getCharsetAsString(mediaType);
        XStream xStream = XStreamUtils.getXStream(o.getClass());
        xStream.marshal(o, new CompactWriter(new OutputStreamWriter(stream, encoding)));
    }

}
