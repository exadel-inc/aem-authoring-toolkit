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
package com.exadel.aem.toolkit.plugin.handlers.layouts;

import java.util.List;
import java.util.function.BiConsumer;

import com.exadel.aem.toolkit.api.annotations.meta.ResourceTypes;
import com.exadel.aem.toolkit.api.handlers.Source;
import com.exadel.aem.toolkit.api.handlers.Target;
import com.exadel.aem.toolkit.plugin.handlers.layouts.common.PlacementHelper;
import com.exadel.aem.toolkit.plugin.util.ClassUtil;
import com.exadel.aem.toolkit.plugin.util.DialogConstants;

/**
 * The {@code BiConsumer<Source, Target>} implementation for a fixed-columns TouchUI dialog.
 */
public class FixedColumnsHandler implements BiConsumer<Source, Target> {
    /**
     * Implements {@code BiConsumer<Class<?>, Element>} pattern
     * to process component-backing Java class and append the results to the {@link Target} root node
     * @param source {@code Source} object used for data retrieval
     * @param target Resulting {@code Target} object
     */
    @Override
    public void accept(Source source, Target target) {
        Target contentItemsColumn = target.getOrCreateTarget(DialogConstants.NN_CONTENT)
                .attribute(DialogConstants.PN_SLING_RESOURCE_TYPE, ResourceTypes.CONTAINER)
                .getOrCreateTarget(DialogConstants.NN_LAYOUT)
                .attribute(DialogConstants.PN_SLING_RESOURCE_TYPE, ResourceTypes.FIXED_COLUMNS)
                .getParent()
                .getOrCreateTarget(DialogConstants.NN_ITEMS)
                .getOrCreateTarget(DialogConstants.NN_COLUMN)
                .attribute(DialogConstants.PN_SLING_RESOURCE_TYPE, ResourceTypes.CONTAINER);

        List<Source> members = ClassUtil.getSources(source.adaptTo(Class.class));
        PlacementHelper.builder()
            .container(contentItemsColumn)
            .members(members)
            .build()
            .doPlacement();
    }
}