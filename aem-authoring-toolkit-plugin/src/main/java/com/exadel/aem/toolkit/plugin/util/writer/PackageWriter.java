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

package com.exadel.aem.toolkit.plugin.util.writer;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.project.MavenProject;

import com.exadel.aem.toolkit.api.annotations.main.Component;
import com.exadel.aem.toolkit.api.annotations.main.Dialog;
import com.exadel.aem.toolkit.api.annotations.widgets.common.XmlScope;
import com.exadel.aem.toolkit.plugin.exceptions.PluginException;
import com.exadel.aem.toolkit.plugin.exceptions.UnknownComponentException;
import com.exadel.aem.toolkit.plugin.exceptions.ValidationException;
import com.exadel.aem.toolkit.plugin.maven.PluginRuntime;
import com.exadel.aem.toolkit.plugin.util.XmlDocumentFactory;

/**
 * Implements actions needed to store collected/processed data into AEM package, optimal for use in "try-with-resources" block
 */
public class PackageWriter implements AutoCloseable {
    private static final String PACKAGE_EXTENSION = ".zip";
    private static final String FILESYSTEM_PREFIX = "jar:";
    private static final Map<String, String> FILESYSTEM_OPTIONS = Collections.singletonMap("create", "true");

    private static final String INVALID_PROJECT_EXCEPTION_MESSAGE = "Invalid project";
    private static final String COMPONENT_PATH_MISSING_EXCEPTION_MESSAGE = "Component path missing for project ";
    private static final String COMPONENT_NAME_MISSING_EXCEPTION_MESSAGE = "Component name missing in @Dialog annotation for class ";
    private static final String CANNOT_WRITE_TO_PACKAGE_EXCEPTION_MESSAGE = "Cannot write to package ";

    private final String componentsBasePath;
    private final FileSystem fileSystem;
    private final List<PackageEntryWriter> writers;

    private PackageWriter(FileSystem fileSystem, String componentsBasePath, List<PackageEntryWriter> writers) {
        this.fileSystem = fileSystem;
        this.componentsBasePath = componentsBasePath;
        this.writers = writers;
    }

    @Override
    public void close() {
        try {
            fileSystem.close();
        } catch (IOException e) {
            throw new PluginException(CANNOT_WRITE_TO_PACKAGE_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Encapsulates steps taken to store authoring features on an AEM component into package. For this, several particular
     * package entry writers, vid. for populating {@code .content.xml}, {@code _cq_dialog.xml}, and {@code _cq_editConfig.xml},
     * are invoked in sequence
     * @param componentClass Current {@code Class} instance
     */
    public void write(Class<?> componentClass) {
        String path = getComponentPath(componentClass);
        if (StringUtils.isBlank(path)) {
            ValidationException validationException = new ValidationException(COMPONENT_NAME_MISSING_EXCEPTION_MESSAGE + componentClass.getSimpleName());
            PluginRuntime.context().getExceptionHandler().handle(validationException);
            return;
        }
        Path componentPath = fileSystem.getPath(componentsBasePath, path);
        if (!Files.isWritable(componentPath)) {
            PluginRuntime.context().getExceptionHandler().handle(new UnknownComponentException(componentPath));
            return;
        }
        new ComponentWriter(componentClass, writers).write(componentPath);
    }

    /**
     * Initializes an instance of {@link PackageWriter} profiled for the current {@link MavenProject} and the tree of
     * folders storing AEM components' data
     * @param project {@code MavenProject instance}
     * @param componentsBasePath Path to the sub-folder within package under which AEM component folders are situated
     * @return {@code PackageWriter} instance
     */
    public static PackageWriter forMavenProject(MavenProject project, String componentsBasePath) {
        if (StringUtils.isBlank(componentsBasePath)) {
            throw new PluginException(COMPONENT_PATH_MISSING_EXCEPTION_MESSAGE + project.getBuild().getFinalName());
        }
        if (project == null) {
            throw new PluginException(INVALID_PROJECT_EXCEPTION_MESSAGE);
        }

        String packageFileName = project.getBuild().getFinalName() + PACKAGE_EXTENSION;
        Path path = Paths.get(project.getBuild().getDirectory()).resolve(packageFileName);
        URI uri = URI.create(FILESYSTEM_PREFIX + path.toUri());
        try {
            FileSystem fs = FileSystems.newFileSystem(uri, FILESYSTEM_OPTIONS);
            return forFileSystem(project.getBuild().getFinalName(), fs, componentsBasePath);
        } catch (IOException e) {
            // exception caught here are critical for the execution, so no further handling
            throw new PluginException(CANNOT_WRITE_TO_PACKAGE_EXCEPTION_MESSAGE + project.getBuild().getFinalName(), e);
        }
    }

    /**
     * Initializes an instance of {@link PackageWriter} profiled for the particular {@link FileSystem} representing
     * the structure of the package
     * @param projectName Name of the project this file system contains information for
     * @param fileSystem Current {@link FileSystem} instance
     * @param componentsBasePath Path to the sub-folder within package under which AEM component folders are situated
     * @return {@code PackageWriter} instance
     */
    private static PackageWriter forFileSystem(String projectName, FileSystem fileSystem, String componentsBasePath) {
        List<PackageEntryWriter> writers;
        try {
            Transformer transformer = XmlDocumentFactory.newDocumentTransformer();
            writers = Arrays.asList(
                    new ContentXmlWriter(transformer),
                    new CqDialogWriter(transformer, XmlScope.CQ_DIALOG),
                    new CqDialogWriter(transformer, XmlScope.CQ_DESIGN_DIALOG),
                    new CqEditConfigWriter(transformer),
                    new CqChildEditConfigWriter(transformer),
                    new CqHtmlTagWriter(transformer)
            );
        } catch (TransformerConfigurationException e) {
            // Exceptions caught here are due to possible XXE security vulnerabilities, so no further handling
            throw new PluginException(CANNOT_WRITE_TO_PACKAGE_EXCEPTION_MESSAGE + projectName, e);
        }
        return new PackageWriter(fileSystem, componentsBasePath, writers);
    }

    private static String getComponentPath(Class<?> componentClass) {
        String pathByComponent = Optional.ofNullable(componentClass.getAnnotation(Component.class))
            .map(Component::path)
            .orElse(null);
        String pathByDialog = Optional.ofNullable(componentClass.getAnnotation(Dialog.class))
            .map(Dialog::name)
            .orElse(null);
        return StringUtils.firstNonBlank(pathByComponent, pathByDialog);
    }
}