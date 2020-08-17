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

package com.exadel.aem.toolkit.test.mvc.component;

import com.exadel.aem.toolkit.api.annotations.main.Component;
import com.exadel.aem.toolkit.core.util.TestConstants;
import com.exadel.aem.toolkit.test.component.ComplexComponent1;
import com.exadel.aem.toolkit.test.mvc.component.views.ChildEditConfigView;
import com.exadel.aem.toolkit.test.mvc.component.views.DesignDialogView;
import com.exadel.aem.toolkit.test.mvc.component.views.HtmlTagView;

@Component(
        path = TestConstants.DEFAULT_COMPONENT_NAME,
        title = ClassicModel.COMPONENT_TITLE,
        description = "test component",
        componentGroup = TestConstants.DEFAULT_COMPONENT_GROUP,
        resourceSuperType = "resource/super/type",
        disableTargeting = true,
        views = {
                DesignDialogView.class,
                ComplexComponent1.class,
                HtmlTagView.class,
                ChildEditConfigView.class
        }
)
public class ClassicModel {
       public static final String COMPONENT_TITLE = "Test Component";
}
