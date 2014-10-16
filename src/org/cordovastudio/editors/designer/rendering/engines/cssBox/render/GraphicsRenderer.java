/*
 * GraphicsRenderer.java
 * Copyright (c) 2005-2013 Radek Burget
 *
 * CSSBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * CSSBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with CSSBox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created on 8.3.2013, 11:41:50 by burgetr
 */
package org.cordovastudio.editors.designer.rendering.engines.cssBox.render;

import org.cordovastudio.editors.designer.rendering.engines.cssBox.layout.ElementBox;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.layout.ReplacedBox;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.layout.TextBox;

import java.awt.*;

/**
 * This renderers displays the boxes graphically using the given Graphics2D context.
 * 
 * @author burgetr
 */
public class GraphicsRenderer implements BoxRenderer
{
    /** the used graphic context */
    protected Graphics2D g;

    /**
     * Constructs a renderer using the given graphics contexts.
     * @param g The graphics context used for painting the boxes.
     */
    public GraphicsRenderer(Graphics2D g)
    {
        this.g = g;
    }
    
    //====================================================================================================
    
    public void startElementContents(ElementBox elem)
    {
    }

    public void finishElementContents(ElementBox elem)
    {
    }
    
    public void renderElementBackground(ElementBox elem)
    {
        elem.drawBackground(g);
    }

    public void renderTextContent(TextBox text)
    {
        text.drawContent(g);
    }

    public void renderReplacedContent(ReplacedBox box)
    {
        box.drawContent(g);
    }

    public void close()
    {
    }    
    
}
