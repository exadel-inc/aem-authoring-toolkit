package com.exadel.aem.toolkit.api.annotations.widgets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to specify subclasses fields that are ignored in the process of TouchUI XML markup rendering
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreFields {

    /**
     * For the child classes, enumerates the fields to be not rendered
     * @see ClassField
     * @return One or more {@code ClassField} annotations
     */
    ClassField[] ignoreFields() default {};
}