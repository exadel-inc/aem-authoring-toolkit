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

public class InvalidTabException extends InvalidSettingException {

    private static final String TAB_NOT_DEFINED_MESSAGE_TEMPLATE = "Tab \"%s\" is not defined";

    public InvalidTabException(String tabTitle) {
        super(String.format(TAB_NOT_DEFINED_MESSAGE_TEMPLATE, tabTitle));
    }
}
