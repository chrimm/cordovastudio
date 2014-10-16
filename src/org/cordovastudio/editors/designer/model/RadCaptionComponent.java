/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cordovastudio.editors.designer.model;

import com.intellij.ui.JBColor;
import com.intellij.util.containers.hash.HashSet;
import org.cordovastudio.editors.designer.designSurface.decorators.CaptionStaticDecorator;
import org.cordovastudio.editors.designer.designSurface.decorators.StaticDecorator;
import org.cordovastudio.editors.designer.designSurface.editableArea.IEditableArea;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Lobas
 */
public abstract class RadCaptionComponent<T extends RadViewComponent> extends RadVisualComponent {
    private final StaticDecorator myDecorator;
    private final IEditableArea myMainArea;
    protected final T myContainer;
    protected final int myIndex;
    protected final int myOffset;
    protected final int myWidth;

    public RadCaptionComponent(IEditableArea mainArea, T container, int index, int offset, int width, boolean empty) {
        myMainArea = mainArea;
        myContainer = container;
        myIndex = index;
        myOffset = offset;
        myWidth = width;

        if (empty) {
            myDecorator = new CaptionStaticDecorator(this, JBColor.PINK);
        } else {
            myDecorator = new CaptionStaticDecorator(this);
        }

        setNativeComponent(container.getNativeComponent());
    }

    public int getIndex() {
        return myIndex;
    }

    @Override
    public void addStaticDecorators(List<StaticDecorator> decorators, List<RadComponent> selection) {
        decorators.add(myDecorator);
    }

    protected final void deselect(List<RadComponent> components) {
        final Set<RadComponent> allComponents = new HashSet<RadComponent>();
        for (RadComponent component : components) {
            component.accept(new RadComponentVisitor() {
                @Override
                public void endVisit(RadComponent component) {
                    allComponents.add(component);
                }
            }, true);
        }
        myMainArea.deselect(allComponents);
    }
}