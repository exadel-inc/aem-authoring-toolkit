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
package com.exadel.aem.toolkit.core.handlers.editconfig;

import java.util.function.BiConsumer;

import org.w3c.dom.Element;
import com.google.common.collect.ImmutableMap;

import com.exadel.aem.toolkit.api.annotations.editconfig.ChildEditConfig;
import com.exadel.aem.toolkit.api.annotations.editconfig.EditConfig;
import com.exadel.aem.toolkit.core.util.PluginObjectUtility;

/**
 * Contains methods to generate and trigger the chain of handlers to store {@code cq:editConfig}
 * and {@code cq:childEditConfig} XML markup
 */
public class EditConfigHandlingHelper {
    private static final String METHOD_DROP_TARGETS = "dropTargets";
    private static final String METHOD_LISTENERS = "listeners";

    private EditConfigHandlingHelper() {}

    /**
     * Builds in-place editing markup for the current component based on the set of component class fields
     * @param element {@code Element} representing {@code cq:editConfig} XML node
     * @param editConfig {@link EditConfig} instance
     */
    public static void append(Element element, EditConfig editConfig) {
        getEditConfigHandlerChain().accept(element, editConfig);
    }

    /**
     * Builds in-place editing markup for the <i>children</i> of the current component based on the set
     * of component class fields
     * @param element {@code Element} representing {@code cq:editConfig} XML node
     * @param childEditConfig {@link ChildEditConfig} instance
     */
    public static void append(Element element, ChildEditConfig childEditConfig) {
        // herewith create a "proxied" @EditConfig object out of the provided @ChildEditConfig
        // with "dropTargets" and "listeners" methods of @EditConfig populated with  @ChildEditConfig values
        EditConfig derivedEditConfig = PluginObjectUtility.create(EditConfig.class, ImmutableMap.of(
                METHOD_DROP_TARGETS, childEditConfig.dropTargets(),
                METHOD_LISTENERS, childEditConfig.listeners()
        ));
        getChildEditConfigHandlerChain().accept(element, derivedEditConfig);
    }

    /**
     * Generates the chain of handlers to store {@code cq:editConfig} XML markup
     * @return {@code BiConsumer<Element, EditConfig>} instance
     */
    private static BiConsumer<Element, EditConfig> getEditConfigHandlerChain() {
        return new PropertiesHandler()
                .andThen(new DropTargetsHandler())
                .andThen(new FormParametersHandler())
                .andThen(new InplaceEditingHandler())
                .andThen(new ListenersHandler());
    }

    /**
     * Generates the chain of handlers to store {@code cq:editConfig} XML markup
     * @return {@code BiConsumer<Element, EditConfig>} instance
     */
    private static BiConsumer<Element, EditConfig> getChildEditConfigHandlerChain() {
        return new DropTargetsHandler().andThen(new ListenersHandler());
    }
}
