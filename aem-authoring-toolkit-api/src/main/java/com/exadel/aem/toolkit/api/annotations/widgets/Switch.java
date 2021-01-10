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
package com.exadel.aem.toolkit.api.annotations.widgets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.exadel.aem.toolkit.api.annotations.meta.PropertyMapping;
import com.exadel.aem.toolkit.api.annotations.meta.ResourceType;
import com.exadel.aem.toolkit.api.annotations.meta.ResourceTypes;

/**
 * Used to set up
 * <a href="https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/reference-materials/granite-ui/api/jcr_root/libs/granite/ui/components/coral/foundation/form/switch/index.html">
 * Switch element</a> in TouchUI dialog
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ResourceType(ResourceTypes.SWITCH)
@PropertyMapping
@SuppressWarnings("unused")
public @interface Switch {
    /**
     * Maps to the 'value' attribute of this TouchUI dialog component's node.
     * Used to define value for a Switch in 'on' state
     * @return String {@code {Boolean}}-casted value
     */
    String value() default "{Boolean}true";
    /**
     * Maps to the 'uncheckedValue' attribute of this TouchUI dialog component's node.
     * Used to define value for a Switch in 'off' state
     * @return String {@code {Boolean}}-casted value
     */
    String uncheckedValue() default "{Boolean}false";
    /**
     * Maps to the 'checked' attribute of this TouchUI dialog component's node.
     * Used to define state for a Switch
     * @return True or false
     */
    boolean checked() default false;
    /**
     * Maps to the 'ignoreData' attribute of this TouchUI dialog component's node.
     * @return True or false
     */
    boolean ignoreData() default false;
    /**
     * Maps to the 'onText' attribute of this TouchUI dialog component's node.
     * Used to define text for a Switch in 'on' state
     * @return String value, non-blank
     */
    String onText() default "On";
    /**
     * Maps to the 'offText' attribute of this TouchUI dialog component's node.
     * Used to define text for a Switch in 'off' state
     * @return String value, non-blank
     */
    String offText() default "Off";
}
