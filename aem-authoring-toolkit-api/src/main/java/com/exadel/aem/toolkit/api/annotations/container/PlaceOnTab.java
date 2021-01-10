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
package com.exadel.aem.toolkit.api.annotations.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In multi-tab TouchUI Dialog setup, used to specify on which tab a dialog field is placed
 * @deprecated Use {@link PlaceOn} instead
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
@SuppressWarnings("unused")
public @interface PlaceOnTab {
    /**
     * String equal to appropriate {@link Tab#title()} value
     * @return String value
     */
    String value();
}
