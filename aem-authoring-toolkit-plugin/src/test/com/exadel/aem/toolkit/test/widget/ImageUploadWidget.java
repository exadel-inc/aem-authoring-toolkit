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

package com.exadel.aem.toolkit.test.widget;

import com.exadel.aem.toolkit.api.annotations.main.Dialog;
import com.exadel.aem.toolkit.api.annotations.main.DialogLayout;
import com.exadel.aem.toolkit.api.annotations.widgets.DialogField;
import com.exadel.aem.toolkit.api.annotations.widgets.imageupload.ButtonVariant;
import com.exadel.aem.toolkit.api.annotations.widgets.imageupload.ImageUpload;

import static com.exadel.aem.toolkit.core.util.TestConstants.DEFAULT_COMPONENT_NAME;

@Dialog(
        name = DEFAULT_COMPONENT_NAME,
        title = "ImageUpload Widget Dialog",
        layout = DialogLayout.FIXED_COLUMNS
)
@SuppressWarnings("unused")
public class ImageUploadWidget {
    @DialogField
    @ImageUpload(
            title="Valid Image Upload",
            mimeTypes=".png",
            sizeLimit = 100000,
            buttonText = "Upload Image",
            buttonVariant = ButtonVariant.QUIET,
            fileNameParameter = "./image/fileName",
            multiple = true,
            className = "css-class-name"
    )
    String file1;
}
