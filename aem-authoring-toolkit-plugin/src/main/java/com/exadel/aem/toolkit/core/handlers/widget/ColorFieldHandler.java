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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.exadel.aem.toolkit.api.handlers.Source;
import com.exadel.aem.toolkit.api.handlers.Target;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.exadel.aem.toolkit.api.annotations.widgets.color.ColorField;
import com.exadel.aem.toolkit.core.util.DialogConstants;

/**
 * {@code BiConsumer<Source, Target>} implementation used to create markup responsible for Granite UI {@code ColorField} widget functionality
 * within the {@code cq:dialog} node
 */
class ColorFieldHandler implements BiConsumer<Source, Target> {
    private static final String NODE_NAME_COLOR = "color";
    private static final String SKIPPED_COLOR_NODE_NAME_SYMBOLS = "^\\w+";

    /**
     * Processes the user-defined data and writes it to {@link Target}
     * @param source Current {@link Source} instance
     * @param target Current {@link Target} instance
     */
    @Override
    public void accept(Source source, Target target) {
        ColorField colorField = source.adaptTo(ColorField.class);
        List<String> validCustomColors = ArrayUtils.isNotEmpty(colorField.customColors())
                ? Arrays.stream(colorField.customColors()).filter(StringUtils::isNotBlank).collect(Collectors.toList())
                : Collections.emptyList();
        if (validCustomColors.isEmpty()) {
            return;
        }
        Target itemsNode = target.getOrCreate(DialogConstants.NN_ITEMS);
        for (String customColor: validCustomColors) {
            itemsNode.getOrCreate(NODE_NAME_COLOR + customColor.toLowerCase().replace(SKIPPED_COLOR_NODE_NAME_SYMBOLS, StringUtils.EMPTY))
                    .attribute(DialogConstants.PN_PRIMARY_TYPE, DialogConstants.NT_UNSTRUCTURED)
                    .attributes(Collections.singletonMap(DialogConstants.PN_VALUE, customColor));
        }
    }
}
