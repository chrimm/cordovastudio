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

package org.cordovastudio.editors.designer.rendering.engines;

import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.cordovastudio.editors.designer.model.ViewInfo;
import org.cordovastudio.editors.designer.rendering.*;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.css.CSSNorm;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.css.DOMAnalyzer;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.layout.BrowserCanvas;
import org.cordovastudio.utils.XmlUtils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.cordovastudio.editors.designer.rendering.Result.Status.SUCCESS;

/**
 * Created by cti on 09.09.14.
 */
public class CssBoxRenderer extends RenderingEngine {

    private RenderLogger myLogger;

    /**
     * Initializes the Bridge object.
     *
     * @param log a {@link org.cordovastudio.editors.designer.rendering.RenderLogger} object. Can be null.
     * @return true if success.
     */
    public boolean init(RenderLogger log) {
        myLogger = log;
        return true;
    }

    /**
     * Starts a layout session by inflating and rendering it. The method returns a
     * {@link org.cordovastudio.editors.designer.rendering.RenderSession} on which further actions can be taken.
     *
     * @param params
     * @return a new {@link org.cordovastudio.editors.designer.rendering.RenderSession} object that contains the result of the scene creation and
     * first rendering.
     */
    @Override
    public RenderSession createSession(RenderParams params) throws RenderingException {

        //ILayoutPullParser parser = params.getParser();
        //XmlFile file = (parser instanceof LayoutPsiPullParser) ? ((LayoutPsiPullParser)parser).getXmlFile() : null;

        URL baseUrl                 = null;
        Document document           = null;
        DOMAnalyzer domAnalyzer     = null;

        if(!(params.getParser() instanceof LayoutPsiPullParser)) {
            if(myLogger != null) {
                myLogger.addMessage(RenderProblem.createPlain(HighlightSeverity.ERROR, "LayoutParser needs to be of type LayoutPsiPullParser!"));
            }
            return null;
        }

        XmlFile file                = ((LayoutPsiPullParser)params.getParser()).getXmlFile();

        if(file == null) {
            if(myLogger != null) {
                myLogger.addMessage(RenderProblem.createPlain(HighlightSeverity.ERROR, "LayoutParser carries invalid XML file!"));
            }
            return null;
        }

        try {
            baseUrl     = new URL(file.getVirtualFile().getUrl());
            //docSource   = new DefaultDocumentSource(baseUrl);
            //parser      = new DefaultDOMSource(docSource);
            //document    = parser.parse();
            document    = XmlUtils.parseDocument(file.getText(), false);

            domAnalyzer = new DOMAnalyzer(document, baseUrl);
        } catch (MalformedURLException e) {
            throw new RenderingException("Could not create RenderSession, due to malformed URL of input file.", e);
        } catch (IOException e) {
            throw new RenderingException("Could not create RenderSession, due to being unable to read input file.", e);
        } catch (SAXException e) {
            throw new RenderingException("Could not create RenderSession, due to being unable to parse input file.", e);
        } catch (ParserConfigurationException e) {
            throw new RenderingException("Could not create RenderSession, due to ill configured XML parser.", e);
        }

        if(domAnalyzer != null) {
            domAnalyzer.attributesToStyles(); //convert the HTML presentation attributes to inline styles
            domAnalyzer.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
            domAnalyzer.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
            domAnalyzer.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css

            //TODO: getting styles takes quite a long time, can we omit this?
            domAnalyzer.getStyleSheets();

            BrowserCanvas canvas = new BrowserCanvas(domAnalyzer.getRoot(), domAnalyzer, baseUrl);

            canvas.getConfig().setLoadImages(true);
            canvas.getConfig().setLoadBackgroundImages(true);
            canvas.createLayout(new Dimension(720, 1280));

            BufferedImage image = canvas.getImage();

            //XmlTag bodyTag = document.getElementsByTagName("body").item(0);

            XmlTag rootTag = file.getRootTag().findFirstSubTag("body");

            ViewInfo rootViewInfo = new ViewInfo("body", rootTag, 0, 0, 720, 1280);

            return new StaticRenderSession(SUCCESS.createResult(), rootViewInfo, image);
        } else {
            throw new RenderingException("Parser must be of type LayoutPsiPullParser");
        }
    }
}
