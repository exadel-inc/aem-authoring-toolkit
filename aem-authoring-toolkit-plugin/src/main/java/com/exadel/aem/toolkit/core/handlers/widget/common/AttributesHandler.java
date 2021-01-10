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

import java.util.function.BiConsumer;

import com.exadel.aem.toolkit.api.handlers.Source;
import com.exadel.aem.toolkit.api.handlers.Target;

import com.exadel.aem.toolkit.api.annotations.widgets.attribute.Attribute;
import com.exadel.aem.toolkit.core.util.PluginXmlUtility;

/**
 * Handler for storing {@link Attribute} properties to a Granite UI widget node
 */
public class AttributesHandler implements BiConsumer<Source, Target> {
    /**
     * Processes the user-defined data and writes it to {@link Target}
     * @param source Current {@link Source} instance
     * @param target Current {@link Target} instance
     */
    @Override
    @SuppressWarnings({"deprecation", "squid:S1874"})
    // "clas" attribute processing remains for compatibility reasons until v.2.0.0
    public void accept(Source source, Target target) {
        Attribute attribute = source.adaptTo(Attribute.class);
        if (attribute == null) {
            return;
        }
        PluginXmlUtility.appendDataAttributes(target, attribute.data());
    }
}
