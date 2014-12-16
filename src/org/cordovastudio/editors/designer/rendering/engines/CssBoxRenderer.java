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
import com.intellij.openapi.project.Project;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import org.cordovastudio.devices.Device;
import org.cordovastudio.editors.designer.rendering.*;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.css.CSSNorm;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.css.DOMAnalyzer;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.layout.BrowserCanvas;

import java.awt.image.BufferedImage;
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
    public RenderSession createSession(RenderParams params, Device device) throws RenderingException {

        URL baseUrl;
        XmlDocument document;
        DOMAnalyzer domAnalyzer;

        if (!(params.getParser() instanceof LayoutPsiPullParser)) {
            if (myLogger != null) {
                myLogger.addMessage(RenderProblem.createPlain(HighlightSeverity.ERROR, "LayoutParser needs to be of type LayoutPsiPullParser!"));
            }
            return null;
        }

        XmlFile file = ((LayoutPsiPullParser) params.getParser()).getXmlFile();

        if (file == null) {
            if (myLogger != null) {
                myLogger.addMessage(RenderProblem.createPlain(HighlightSeverity.ERROR, "LayoutParser carries invalid XML file!"));
            }
            return null;
        }

        try {
            baseUrl = new URL(file.getVirtualFile().getUrl());
            //document    = XmlUtils.parseDocument(file.getText(), false);
            document = file.getDocument();
            domAnalyzer = new DOMAnalyzer(document, baseUrl);
        } catch (MalformedURLException e) {
            throw new RenderingException("Could not create RenderSession, due to malformed URL of input file.", e);
        } catch (IOException e) {
            throw new RenderingException("Could not create RenderSession, due to being unable to read input file.", e);
        }

        /*
            Do not delete or uncommend the adding of these default style sheets, as this will end up with the
            <html> tag being interpreted as an inline element which in turn will crash the hole rendering.

            The CSSBox renderer needs this styles, which represent the W3C defaults.
            - CSSNorm.stdStyleSheet(): corresponds to the CSS2.1 recommendation (Appendix D)
            - CSSNorm.userStyleSheet(): style sheet defining the additional basic style not covered by the CSS recommendation
            - CSSNorm.formsStyleSheet(): A style sheet defining a basic style of form fields. This style sheet may be used
                for a simple rendering of form fields when their functionality is not implemented
                in another way.
         */


        domAnalyzer.attributesToStyles(); //convert the HTML presentation attributes to inline styles
        domAnalyzer.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
        domAnalyzer.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
        domAnalyzer.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css
        domAnalyzer.addStyleSheet(null, CSSNorm.html5StdStyleSheet(), DOMAnalyzer.Origin.AGENT);


        //TODO: getting styles takes quite a long time, can we omit or shorten this?
        // May be by buffering after the first load or something?
        domAnalyzer.getStyleSheets();

        XmlElementFactory factory = XmlElementFactory.getInstance((Project)params.getProjectKey());

        BrowserCanvas canvas = new BrowserCanvas(domAnalyzer.getRoot(), domAnalyzer, baseUrl, factory);

        canvas.getConfig().setLoadImages(true);
        canvas.getConfig().setLoadBackgroundImages(true);
        canvas.createLayout(device.getScreenSize());

        BufferedImage image = canvas.getImage();

        return new StaticRenderSession(SUCCESS.createResult(), canvas.getBodyViewInfo(), image);
    }
}
