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

/**
 * Represents the plugin-specific exception due to one of exceptions produces by {@code java.lang.reflect} routines
 */
public class ReflectionException extends RuntimeException {
    public ReflectionException(String message, Exception inner) {
        super(message, inner);
    }

    public ReflectionException(Class<?> clazz, String fieldName) {
        super(String.format("Field '%s' not present in %s", fieldName, clazz));
    }

}
