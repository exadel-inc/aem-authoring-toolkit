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

package com.exadel.aem.toolkit.core.exceptions;

import com.exadel.aem.toolkit.core.util.DialogConstants;

/**
 * Represents the plugin-specific exception thrown when there is no possibility to operate a particular tab
 * (the tab with such name does not exists, or there are no tabs in the current setup)
 */
public class InvalidContainerException extends RuntimeException {
    private static final String TABS_NOT_DEFINED_MESSAGE = "No tabs defined for the current component";
    private static final String TAB_NOT_DEFINED_MESSAGE_TEMPLATE = "Tab \"%s\" is not defined";
    private static final String ACCORDION_PANEL_NOT_DEFINED_MESSAGE_TEMPLATE = "Accordion Panel \"%s\" is not defined";

    public InvalidContainerException() {
        super(TABS_NOT_DEFINED_MESSAGE);
    }

    public InvalidContainerException(String tabTitle, String containerName) {
        super(containerName.equals(DialogConstants.NN_TABS) ?
            String.format(TAB_NOT_DEFINED_MESSAGE_TEMPLATE, tabTitle) :
            String.format(ACCORDION_PANEL_NOT_DEFINED_MESSAGE_TEMPLATE, tabTitle));

    }
}
