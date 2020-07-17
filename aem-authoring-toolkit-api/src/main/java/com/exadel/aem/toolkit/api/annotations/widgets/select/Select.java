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
package com.exadel.aem.toolkit.api.annotations.widgets.select;

import com.exadel.aem.toolkit.api.annotations.meta.*;
import com.exadel.aem.toolkit.api.annotations.widgets.DataSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to set up
 * <a href="https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/reference-materials/granite-ui/api/jcr_root/libs/granite/ui/components/coral/foundation/form/select/index.html">
 * Select element</a> in TouchUI dialog
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ResourceType(ResourceTypes.SELECT)
@PropertyMapping
@SuppressWarnings("unused")
public @interface Select {
    /**
     * Used to specify collection of {@link Option}s within this Select
     *
     * @return Single {@code Option} annotation, or an array of Options
     */
    @IgnorePropertyMapping
    Option[] options() default {};

    /**
     * When set to a non-blank string, maps to the 'emptyText' attribute of this TouchUI dialog component's node.
     *
     * @return String value
     */
    String emptyText() default "";

    /**
     * When set, the {@code datasource} node is appended to the JCR buildup of this component
     * and populated with values of provided {@link DataSource} annotation
     *
     * @return {@code @DataSource} instance
     */
    @IgnorePropertyMapping
    DataSource datasource() default @DataSource;

    /**
     * @return String value
     * @deprecated Use {@code datasource:resourceType} instead
     * When set to a non-blank string, allows to override {@code sling:resourceType} attribute of a {@code datasource node}
     * pointing to a ACS Commons list
     */
    @Deprecated
    @IgnorePropertyMapping
    String acsListResourceType() default "";

    /**
     * @return Valid JCR path, or an empty string
     * @deprecated Use {@code datasource:path} instead
     * When set to a non-blank string, a {@code datasource} node is appended to the JCR buildup of this component
     * pointing to a ACS Commons list
     */
    @Deprecated
    @IgnorePropertyMapping
    String acsListPath() default "";

    /**
     * Indicates if the user is able to select multiple options.
     *
     * @return True or false
     */
    boolean multiple() default false;

    /**
     * If set to true, the options' labels are translated by AEM.
     *
     * @return True or false
     */
    boolean translateOptions() default true;

    /**
     * If set to true, the options are sorted according to their labels' alphabetical order".
     *
     * It is assumed that the options don’t contain option group.
     *
     * @return True or false
     */
    boolean ordered() default false;

    /**
     * Returns true to also add an empty option; false otherwise.
     * <p>
     * Empty option is an option having both value and text equal to empty string.
     *
     * @return True or false
     */
    boolean emptyOption() default false;

    /**
     * Maps to the 'variant' attribute of this TouchUI dialog component's node.
     * Used to define select variant
     * @see SelectVariant
     * @return One of {@code SelectVariant} values
     *
     * @return String value
     */
    @EnumValue(transformation = StringTransformation.LOWERCASE)
    SelectVariant variant() default SelectVariant.DEFAULT;

    /**
     * Return true to generate the SlingPostServlet @Delete hidden input based on the
     * field name.
     *
     * @return True or false
     */
    boolean deleteHint() default true;

    /**
     * Return true to force to be ignore-freshness specifically just for this field.
     *
     * This property is useful when you have a newly introduced field in the form, and there is a need to specifically
     * set the default selected item. To set the default selected item, set the selected property of the item as usual.
     *
     * @return True or false
     */
    boolean forceIgnoreFreshness() default false;
}

