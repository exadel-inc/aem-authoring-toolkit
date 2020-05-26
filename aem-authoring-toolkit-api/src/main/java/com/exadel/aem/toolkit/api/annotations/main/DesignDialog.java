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

package com.exadel.aem.toolkit.api.annotations.main;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.exadel.aem.toolkit.api.annotations.container.Tab;
import com.exadel.aem.toolkit.api.annotations.meta.*;

/**
 * Used to store generic properties of TouchUI Design Dialog and most common properties of AEM Component according to the
 * <a href="https://docs.adobe.com/content/help/en/experience-manager-65/developing/components/components-basics.html#design-dialogs"> Adobe specification</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PropertyMapping
@SuppressWarnings("unused")
public @interface DesignDialog {

    /**
     * Maps to the 'jcr:title' attributes of both the component root node node and its {@code cq:dialog} node
     *
     * @return String value, non-blank
     */
    @PropertyRendering(name = JcrConstants.PN_TITLE)
    @ValueRestriction(ValueRestrictions.NOT_BLANK)
    String title();

    /**
     * Used to specify TouchUI dialog layout
     *
     * @return One of the FIXED_COLUMNS and TABS values
     * @see DialogLayout
     */
    @IgnorePropertyMapping
    DialogLayout layout() default DialogLayout.FIXED_COLUMNS;

    /**
     * Renders as the `height` attribute of component's {@code cq:dialog} node. If no value, or a value less or equal to zero provided, default 480 is used
     *
     * @return Double
     */
    @ValueRestriction(ValueRestrictions.POSITIVE)
    double height() default 480;

    /**
     * Renders as the `width` attribute of component's {@code cq:dialog} node. If no value, or a value less or equal to zero provided, default 560 is used
     *
     * @return Double
     */
    @ValueRestriction(ValueRestrictions.POSITIVE)
    double width() default 560;

    /**
     * For the tabbed TouchUI dialog layout, enumerates the tabs to be rendered
     *
     * @return One or more {@code Tab} annotations
     * @see Tab
     */
    Tab[] tabs() default {};
}