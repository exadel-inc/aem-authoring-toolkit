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
package com.exadel.aem.toolkit.api.annotations.widgets.fileupload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.exadel.aem.toolkit.api.annotations.meta.EnumValue;
import com.exadel.aem.toolkit.api.annotations.meta.PropertyMapping;
import com.exadel.aem.toolkit.api.annotations.meta.PropertyRendering;
import com.exadel.aem.toolkit.api.annotations.meta.ResourceType;
import com.exadel.aem.toolkit.api.annotations.meta.ResourceTypes;
import com.exadel.aem.toolkit.api.annotations.meta.StringTransformation;
import com.exadel.aem.toolkit.api.annotations.meta.ValueRestriction;
import com.exadel.aem.toolkit.api.annotations.meta.ValueRestrictions;

/**
 * Used to set up
 * <a href="https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/reference-materials/granite-ui/api/jcr_root/libs/granite/ui/components/coral/foundation/form/fileupload/index.html">
 * FileUpload element</a> in TouchUI dialog
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ResourceType(ResourceTypes.CORAL_FILEUPLOAD)
@PropertyMapping
@SuppressWarnings("unused")
public @interface FileUpload {
    /**
     * When set to a non-blank string, maps to the 'emptyText' attribute of this TouchUI dialog component's node.
     * Used to define text hint for an empty FileUpload
     * @return String value
     */
    String emptyText() default "";
    /**
     * Maps to the 'async' attribute of this TouchUI dialog component's node.
     * Used to set possibility asynchronous upload
     * @return True or false
     */
    boolean async() default true;
    /**
     * Maps to the 'multiple' attribute of this TouchUI dialog component's node.
     * Used to set possibility for multiple files upload
     * @return True or false
     */
    boolean multiple() default false;
    /**
     * Maps to the 'autoStart' attribute of this TouchUI dialog component's node.
     * If true, the upload starts automatically once the file is selected
     * @return True or false
     */
    boolean autoStart() default true;
    /**
     * Maps to the 'uploadUrl' attribute of this TouchUI dialog component's node.
     * Used to determine the URL where to upload the file
     * @return String value representing valid JCR path
     */
    @ValueRestriction(ValueRestrictions.JCR_PATH)
    String uploadUrl();
    /**
     * When set to a positive number, maps to the 'sizeLimit' attribute of this TouchUI dialog component's node.
     * Used to determine the file size limit
     * @return Long value
     */
    @ValueRestriction(ValueRestrictions.NON_NEGATIVE)
    @PropertyRendering(ignoreValues = "0")
    long sizeLimit() default 0;
    /**
     * Maps to the 'mimeTypes' attribute of this TouchUI dialog component's node.
     * Defines the browse and selection filter for file selection
     * @return String value, or an array of strings
     */
    String[] mimeTypes() default {};
    /**
     * Maps to the 'variant' attribute of this TouchUI dialog component's node.
     * Used to define button variant
     * @see ButtonVariant
     * @return One of {@code ButtonVariant} values
     */
    @PropertyRendering(name = "variant")
    @EnumValue(transformation = StringTransformation.CAMELCASE)
    ButtonVariant buttonVariant() default ButtonVariant.PRIMARY;
    /**
     * Maps to the 'text' attribute of this TouchUI dialog component's node.
     * Used to define the text of the button
     * @return String value
     */
    @PropertyRendering(name = "text")
    String buttonText() default "Upload";
    /**
     * Maps to the 'hideText' attribute of this TouchUI dialog component's node.
     * Used to define whether text is hidden
     * @return True or false
     */
    @PropertyRendering(name = "hideText")
    boolean hideButtonText() default false;
    /**
     * Maps to the 'buttonSize' attribute of this TouchUI dialog component's node.
     * Used to define button size
     * @see ButtonSize
     * @return One of {@code ButtonSize} values
     */
    @PropertyRendering(name = "size")
    @EnumValue()
    ButtonSize buttonSize() default ButtonSize.MEDIUM;
    /**
     * When set to a non-blank string, maps to the 'text' attribute of this TouchUI dialog component's node.
     * Used to define component's icon
     * @return String value
     */
    String icon() default "";
    /**
     * Maps to the 'iconSize' attribute of this TouchUI dialog component's node.
     * Used to define icon size
     * @see IconSize
     * @return One of {@code IconSize} values
     */
    @EnumValue()
    IconSize iconSize() default IconSize.SMALL;
}

