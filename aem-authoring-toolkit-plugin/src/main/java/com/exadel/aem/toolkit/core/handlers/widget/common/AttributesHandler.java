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
package com.exadel.aem.toolkit.core.handlers.widget.common;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import com.exadel.aem.toolkit.api.annotations.widgets.attribute.Attribute;
import com.exadel.aem.toolkit.core.maven.PluginRuntime;
import com.exadel.aem.toolkit.core.util.DialogConstants;

/**
 * Handler for storing {@link Attribute} properties to a Granite UI widget XML node
 */
public class AttributesHandler implements BiConsumer<Element, Field> {
    /**
     * Processes the user-defined data and writes it to XML entity
     * @param element XML element
     * @param field Current {@code Field} instance
     */
    @Override
    @SuppressWarnings({"deprecation", "squid:S1874"})
    // "clas" attribute processing remains for compatibility reasons until v.2.0.0
    public void accept(Element element, Field field) {
        if(!field.isAnnotationPresent(Attribute.class)){
            return;
        }
        Attribute attribute = field.getAnnotation(Attribute.class);
        PluginRuntime.context().getXmlUtility().appendDataAttributes(element, attribute.data());
        if (StringUtils.isNotBlank(attribute.clas())) {
            element.setAttribute(DialogConstants.PN_GRANITE_CLASS, attribute.clas());
        }
    }
}
