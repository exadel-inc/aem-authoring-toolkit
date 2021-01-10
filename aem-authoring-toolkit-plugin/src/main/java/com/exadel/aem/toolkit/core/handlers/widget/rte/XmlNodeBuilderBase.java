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
package com.exadel.aem.toolkit.core.handlers.widget.rte;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.w3c.dom.Element;

/**
 * Represents a generic builder for a {@code RichTextEditor} configuration XML markup.
 * Required by {@link RichTextEditorHandler#accept(Element, Field)}
 */
abstract class XmlNodeBuilderBase {
    private String name;
    private String attributeName;
    private BiPredicate<String, String> filter;
    private Consumer<Element> postprocessing;

    XmlNodeBuilderBase(String tagName, String attributeName) {
        this.name = tagName;
        this.attributeName = attributeName;
    }

    /**
     * Gets the node name for the XML element this {@code Builder} will create
     * @return String value, non-blank
     */
    String getName() {
        return name;
    }

    /**
     * Sets the node name for the XML element this {@code Builder} will create
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the key attribute of the XML element this {@code Builder} will create
     * @return String value, non-blank
     */
    String getAttributeName() {
        return attributeName;
    }

    /**
     * Gets the postprocessing routine for this {@code Builder}
     * @return {@code Consumer<Element>} instance
     */
    Consumer<Element> getPostprocessing() {
        return postprocessing;
    }

    /**
     * Sets the postprocessing routine for this {@code Builder}
     */
    void setPostprocessing(Consumer<Element> postprocessing) {
        this.postprocessing = postprocessing;
    }

    /**
     * Gets the filtering routine to sort in acceptable feature tokens for this {@code Builder}
     * @return {@code BiPredicate<String, String>} instance
     */
    BiPredicate<String, String> getFilter() {
        return filter;
    }

    /**
     * Sets the filtering routine to sort in acceptable feature tokens for this {@code Builder}
     */
    void setFilter(BiPredicate<String, String> filter) {
        this.filter = filter;
    }

    /**
     * Stores RichTextEditor feature to configuration XML markup
     * @param pluginId RTE Plugin name
     * @param feature Feature identifier
     */
    abstract void store(String pluginId, String feature);

    /**
     * Stores many RichTextEditor features of the same RTE plugin to configuration XML markup
     * @param pluginId RTE Plugin name
     * @param features Feature identifiers
     */
    void storeMany(String pluginId, String[] features) {
        Arrays.stream(features).forEach(feature -> store(pluginId, feature));
    }

    /**
     * Creates and returns the configuration XML node
     * @return {@code Element} instance representing the config node
     */
    abstract Element build();

    /**
     * Gets whether this instance has no features set
     * @return True or false
     */
    abstract boolean isEmpty();
}
