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
package com.exadel.aem.toolkit.api.annotations.widgets.anchorbutton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.exadel.aem.toolkit.api.annotations.meta.EnumValue;
import com.exadel.aem.toolkit.api.annotations.meta.PropertyMapping;
import com.exadel.aem.toolkit.api.annotations.meta.PropertyRendering;
import com.exadel.aem.toolkit.api.annotations.meta.ResourceType;
import com.exadel.aem.toolkit.api.annotations.meta.ResourceTypes;
import com.exadel.aem.toolkit.api.annotations.widgets.common.ElementSizeConstants;
import com.exadel.aem.toolkit.api.annotations.widgets.common.ElementVariantConstants;
import com.exadel.aem.toolkit.api.annotations.widgets.common.Linkchecker;

/**
 * Used to set up
 * <a href="https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/reference-materials/granite-ui/api/jcr_root/libs/granite/ui/components/coral/foundation/anchorbutton/index.html">
 * AnchorButton is a component to represent a standard HTML hyperlink (<a>), but to look like a button in TouchUI dialog
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ResourceType(ResourceTypes.ANCHOR_BUTTON)
@PropertyMapping
public @interface AnchorButton {
    /**
     * Maps to the href attribute of this TouchUI dialog component's node
     * @return String value
     */
    String href();

    /**
     * Maps to the href attribute of this TouchUI dialog component's node.
     * This is usually used to produce different value based on locale
     * @return String value
     */
    String hrefI18n() default "";

    /**
     * Maps to the body text of the element of this TouchUI dialog component's node
     * @return String value
     */
    String text();

    /**
     * Maps to the target attribute of this TouchUI dialog component's node
     * @return String value
     */
    String target() default "";

    /**
     * Visually hide the text. It is RECOMMENDED that every button has a text for a11y purpose.
     * Use this property to hide it visually, while still making it available for a11y
     * @return True or false
     */
    @PropertyRendering(ignoreValues = "false")
    boolean hideText() default false;

    /**
     * Maps to the x-cq-linkchecker attribute of this TouchUI dialog component's node
     * @return One of {@code HyperlinkLinkchecker} values
     * @see Linkchecker
     */
    @PropertyRendering(ignoreValues = "NONE")
    Linkchecker xCqLinkchecker() default Linkchecker.NONE;

    /**
     * Maps to the icon name. e.g. “search” of this TouchUI dialog component's node
     * @return String value
     */
    String icon() default "";

    /**
     * Maps to the size of the icon of this TouchUI dialog component's node
     * @return String value, non-blank
     * @see ElementSizeConstants
     */
    String iconSize() default ElementSizeConstants.SMALL;

    /**
     * Maps to the size of the button of this TouchUI dialog component's node
     * @return One of {@code Size} values
     * @see Size
     */
    @EnumValue
    Size size() default Size.M;

    /**
     * Maps to the 'block' attribute of this TouchUI dialog component's node.
     * Used to ensure the button is rendered as a block element
     * @return True or false
     */
    boolean block() default false;

    /**
     * Maps to the 'variant' attribute of this TouchUI dialog component's node.
     * Used to define button variant
     * @return One of {@code ButtonVariant} values
     * @see com.exadel.aem.toolkit.api.annotations.widgets.common.ElementVariantConstants
     */
    String variant() default ElementVariantConstants.PRIMARY;

    /**
     * When set to a non-blank string, maps to the 'command' attribute of this TouchUI dialog component's node.
     * Used to define keyboard shortcut for the action. Overrides 'actionConfigName' value
     * @return String value, non-blank
     */
    String command() default "";

    /**
     * When set to a non-blank string, maps to the 'actionConfigName' attribute of this TouchUI dialog component's node.
     * Used to define standard definitions of command, icon and text
     * @return String value, non-blank
     */
    String actionConfigName() default "";
}
