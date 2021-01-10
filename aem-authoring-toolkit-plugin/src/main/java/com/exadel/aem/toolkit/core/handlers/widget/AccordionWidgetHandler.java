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
package com.exadel.aem.toolkit.core.handlers.widget;

import com.exadel.aem.toolkit.api.annotations.widgets.AccordionWidget;
import com.exadel.aem.toolkit.api.handlers.Source;
import com.exadel.aem.toolkit.api.handlers.Target;
import com.exadel.aem.toolkit.core.handlers.container.common.WidgetContainerHandler;

/**
 * {@link Handler} implementation used to create markup responsible for Granite {@code Accordion} widget functionality
 * within the {@code cq:dialog} XML node
 */
class AccordionWidgetHandler extends WidgetContainerHandler {

    /**
     * Processes the user-defined data and writes it to XML entity
     * @param element Current XML element
     * @param field   Current {@code Field} instance
     */
    @Override
    public void accept(Source source, Target target) {
        acceptParent(source, AccordionWidget.class, target);
    }
}
