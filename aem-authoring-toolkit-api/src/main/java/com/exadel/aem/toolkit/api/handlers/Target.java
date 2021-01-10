/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
package com.exadel.aem.toolkit.api.handlers;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.w3c.dom.Document;


public interface Target {

    Target create(String s);

    Target create(Supplier<String> s);

    Target getOrCreate(String s);

    Target get(String s);

    default Target mapProperties(Annotation annotation) {
        return mapProperties(annotation, Collections.emptyList());
    }

    Target mapProperties(Annotation annotation, List<String> skipped);

    Target attribute(String name, String value);

    Target attribute(String name, boolean value);

    Target attribute(String name, long value);

    Target attribute(String name, double value);

    Target attribute(String name, Date value);

    Target attributes(Map<String, Object> map);

    Target prefix(String prefix);

    Target postfix(String postfix);

    String getPrefix();

    String getPostfix();

    void delete();

    void deleteAttribute(String name);

    boolean hasAttribute(String name);

    List<Target> listChildren();

    String getName();

    Target parent();

    <T> T getAttribute(String name, Class<T> tClass);

    boolean hasChild(String relPath);

    Map<String, String> getValueMap();

    void setSource(Source source);

    Source getSource();

    Document buildXml(Document document);
}
