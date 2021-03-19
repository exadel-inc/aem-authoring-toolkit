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
package com.exadel.aem.toolkit.api.annotations.meta;

import org.apache.commons.lang.StringUtils;

/**
 * Contains possible values of {@link com.exadel.aem.toolkit.api.annotations.meta.Scope} annotation
 */
public enum Scope {
    DEFAULT,
    COMPONENT(".content.xml"),
    CQ_DIALOG("_cq_dialog.xml"),
    CQ_DESIGN_DIALOG("_cq_design_dialog.xml"),
    CQ_EDIT_CONFIG("_cq_editConfig.xml"),
    CQ_CHILD_EDIT_CONFIG("_cq_childEditConfig.xml"),
    CQ_HTML_TAG("_cq_htmlTag.xml");

    private final String value;

    /**
     * Default constructor
     */
    Scope() {
        this(null);
    }

    /**
     * Value-specializing constructor
     * @param value The return value of {@link Scope#toString()} method
     */
    Scope(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringUtils.defaultString(value);
    }
}
