/*
 * Copyright (C) 2014 Christoffer T. Timm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * StreamDocumentSource.java
 *
 * Created on 19.12.2012, 15:07:56 by burgetr
 */
package org.cordovastudio.editors.designer.rendering.engines.cssBox.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A dummy implementation of the document source for encapsulating an already created input stream
 * and the additional parametres.
 * 
 * @author burgetr
 */
public class StreamDocumentSource extends DocumentSource
{
    private URL url;
    private String ctype;
    private InputStream is;
    
    /**
     * Creates the document source from the input stream.
     * @param is the input stream
     * @param url the base URL to be used with the data source
     * @param contentType stream content type
     * @throws java.io.IOException
     */
    public StreamDocumentSource(InputStream is, URL url, String contentType) throws IOException
    {
        super(url);
        this.url = url;
        this.ctype = contentType;
        this.is = is;
    }

    @Override
    public URL getURL()
    {
        return url;
    }

    @Override
    public String getContentType()
    {
        return ctype;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return is;
    }

    @Override
    public void close() throws IOException
    {
        is.close();
    }

}
