/*
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exadel.aem.toolkit.core.util.writer;

import javax.xml.transform.Transformer;

import com.exadel.aem.toolkit.api.annotations.meta.PropertyScope;
import com.exadel.aem.toolkit.api.handlers.Target;
import com.exadel.aem.toolkit.api.annotations.main.Component;
import org.w3c.dom.Document;

import com.exadel.aem.toolkit.api.annotations.main.Dialog;
import com.exadel.aem.toolkit.api.annotations.widgets.common.XmlScope;
import com.exadel.aem.toolkit.core.util.DialogConstants;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The {@link PackageEntryWriter} implementation for storing component-wide attributes (writes data to the
 * {@code .content.xml} file within the current component folder before package is uploaded
 */
class ContentXmlWriter extends PackageEntryWriter {
    /**
     * Basic constructor
     * @param transformer {@code Transformer} instance used to serialize XML DOM document to an output stream
     */
    ContentXmlWriter(Transformer transformer) {
        super(transformer);
    }

    /**
     * Gets {@code XmlScope} value of current {@code PackageEntryWriter} implementation
     * @return {@link XmlScope} value
     */
    @Override
    XmlScope getXmlScope() {
        return XmlScope.COMPONENT;
    }

    /**
     * Gets whether current {@code Class} is eligible for populating {@code .content.xml} structure
     * @param componentClass The {@code Class} under consideration
     * @return True if current {@code Class} is annotated with {@link Dialog} or {@link Component}; otherwise, false
     */
    @Override
    boolean isProcessed(Class<?> componentClass) {
        return componentClass.isAnnotationPresent(Dialog.class) ||
                componentClass.isAnnotationPresent(Component.class);
    }

    /**
     * Overrides {@link PackageEntryWriter#populateDomDocument(Class, Target)} abstract method to write down contents
     * of {@code .content.xml} file
     * @param componentClass The {@code Class} being processed
     * @param root The root element of DOM {@link Document} to feed data to
     */
    @Override
    void populateDomDocument(Class<?> componentClass, Target root) {
        Dialog dialog = componentClass.getDeclaredAnnotation(Dialog.class);
        root.attribute(DialogConstants.PN_PRIMARY_TYPE, DialogConstants.NT_COMPONENT);
        root.mapProperties(dialog, Arrays.stream(Dialog.class.getDeclaredMethods())
                .filter(m -> !fitsInScope(m, getXmlScope())).map(Method::getName).collect(Collectors.toList()));
        if(dialog.isContainer()) root.attribute(DialogConstants.PN_IS_CONTAINER, String.valueOf(true));
    }

    private static boolean fitsInScope(Method method, XmlScope scope) {
        if (!method.isAnnotationPresent(PropertyScope.class)) {
            return true;
        }
        return Arrays.asList(method.getAnnotation(PropertyScope.class).value()).contains(scope);
    }
}
