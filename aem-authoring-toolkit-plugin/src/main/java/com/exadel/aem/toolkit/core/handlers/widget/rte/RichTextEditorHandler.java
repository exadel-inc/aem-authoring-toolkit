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
package com.exadel.aem.toolkit.core.handlers.widget.rte;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.exadel.aem.toolkit.api.annotations.widgets.rte.*;
import com.exadel.aem.toolkit.api.handlers.Source;
import com.exadel.aem.toolkit.api.handlers.Target;
import com.exadel.aem.toolkit.core.exceptions.ValidationException;
import com.exadel.aem.toolkit.core.maven.PluginRuntime;
import com.exadel.aem.toolkit.core.util.DialogConstants;
import com.exadel.aem.toolkit.core.util.PluginReflectionUtility;
import com.exadel.aem.toolkit.core.util.PluginXmlUtility;
import com.exadel.aem.toolkit.core.util.validation.CharactersObjectValidator;
import com.exadel.aem.toolkit.core.util.validation.Validation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * {@code BiConsumer<Source, Target>} implementation used to create markup responsible for Granite UI {@code RichTextEditor} widget functionality
 * within the {@code cq:dialog} and {@code cq:editConfig} XML nodes
 */
public class RichTextEditorHandler implements BiConsumer<Source, Target> {
    private static final String KEYWORD_AUTO = "auto";

    private static final String FEATURE_ALL = "*";
    static final String PLUGIN_FEATURE_SEPARATOR = "#";

    private static final Pattern FEATURE_TOKEN_PATTERN = Pattern.compile("^(?:[\\w-]+#[\\w-:]+|-)$");
    private static final Pattern FEATURE_TOKEN_ARRAY_PATTERN = Pattern.compile("^\\[\\s*(?:(?:[\\w-]+#[\\w-:]+|-)(?:\\s*,\\s*)?)+\\s*]$");
    private static final Pattern HTML_PASTE_RULES_ALLOW_PATTERN = Pattern.compile("^allow([A-Z].+?)s?$");

    private static final String MALFORMED_TOKEN_EXCEPTION_MESSAGE = "Malformed feature token in @RichTextEditor";

    private RichTextEditor rteAnnotation;
    private final boolean renderDialogFullScreenNode;

    public RichTextEditorHandler() {
        this(true);
    }
    public RichTextEditorHandler(boolean renderDialogFullScreenNode) {
        this.renderDialogFullScreenNode = renderDialogFullScreenNode;
    }

    /**
     * Processes the user-defined data and writes it to XML entity
     * @param source Current {@code SourceFacade} instance
     * @param target Current XML targetFacade
     */
    @Override
    public void accept(Source source, Target target) {
        accept(source.adaptTo(RichTextEditor.class), target);
    }

/**
     * Processes the user-defined data and writes it to XML entity
     * @param target Current {@code TargetFacade} instance
     * @param rteAnnotation Current {@link RichTextEditor} instance
     */

    public void accept(RichTextEditor rteAnnotation, Target target) {
        this.rteAnnotation = rteAnnotation;
        // create the four basic builders: for ./uiSettings/cui/inline, ./uiSettings/cui/dialogFullScreen,
        // ./uiSettings/cui/tableEditOptions, and ./rtePlugins
        XmlNodeWithListBuilder inlineBuilder = new XmlNodeWithListBuilder(DialogConstants.NN_INLINE, DialogConstants.PN_TOOLBAR);
        XmlNodeWithListBuilder fullScreenBuilder = new XmlNodeWithListBuilder(DialogConstants.NN_FULLSCREEN, DialogConstants.PN_TOOLBAR);

        XmlTreeWithListsBuilder popoversBuilder = new XmlTreeWithListsBuilder(DialogConstants.NN_POPOVERS, DialogConstants.NN_ITEMS, true);
        popoversBuilder.setPostprocessing(popoverNode -> popoverNode.attribute(DialogConstants.PN_REF, popoverNode.getName()));

        inlineBuilder.setChildBuilder(popoversBuilder);
        fullScreenBuilder.setChildBuilder(new XmlTreeWithListsBuilder(popoversBuilder)); // 'cloned' popovers builder

        XmlNodeWithListBuilder tableEditBuilder = new XmlNodeWithListBuilder(DialogConstants.NN_TABLE_EDIT_OPTIONS,DialogConstants.PN_TOOLBAR);
        tableEditBuilder.setFilter((pluginId, feature) -> DialogConstants.NN_TABLE.equals(pluginId) && !RteFeatures.TABLE_TABLE.equals(feature));
        // we do not feed non-'table#...' features to ./uiSettings/cui/tableEditOptions

        XmlTreeWithListsBuilder pluginsBuilder = new XmlTreeWithListsBuilder(DialogConstants.NN_RTE_PLUGINS, DialogConstants.PN_FEATURES);
        pluginsBuilder.setFilter((pluginId, feature) -> !DialogConstants.NN_TABLE.equals(pluginId) || FEATURE_ALL.equals(feature));
        // we do not feed table features to ./rtePlugins, except for 'table#*'

        // concat values of .features() and .fullscreenFeatures() to feed them to all four builders in single run
        Stream.concat(
                Arrays.stream(rteAnnotation.features()).map(feature -> new ImmutablePair<>(inlineBuilder, feature)),
                Arrays.stream(rteAnnotation.fullscreenFeatures()).map(feature -> new ImmutablePair<>(fullScreenBuilder, feature))
        ).forEach(featureItem -> processFeatureItem(featureItem, tableEditBuilder, pluginsBuilder));

        // build uiSettings node with subnodes, append conditionally if not empty
        Target uiSettings = target.getOrCreate(DialogConstants.NN_UI_SETTINGS);
        Target cui = uiSettings.getOrCreate(DialogConstants.NN_CUI);
        inlineBuilder.build(cui);
        // if .features() are set, but .fullscreenFeatures() are not
        // build either node './inline', './fullscreen' and './dialogFullScreen' (if latter is needed)  from .features()
        // (that allows user to specify only .features() and avoid copy-pasting)
        if (fullScreenBuilder.isEmpty() && !inlineBuilder.isEmpty()) {
            inlineBuilder.setName(DialogConstants.NN_FULLSCREEN);
            inlineBuilder.build(cui);
            if (renderDialogFullScreenNode) {
                inlineBuilder.setName(DialogConstants.NN_DIALOG_FULL_SCREEN);
                inlineBuilder.build(cui);
            }
        }
        // if .fullscreenFeatures() are set, build nodes './fullscreen' and './dialogFullScreen' (latter if needed) from .fullscreenFeatures()
        fullScreenBuilder.build(cui);
        fullScreenBuilder.setName(DialogConstants.NN_DIALOG_FULL_SCREEN);
        if (renderDialogFullScreenNode) fullScreenBuilder.build(cui);
        tableEditBuilder.build(cui);
        getIconsNode(cui);
        // if ./cui node has been added any children, append it to ./uiSettings and then append ./uiSettings to root target
        if (rteAnnotation.externalStyleSheets().length != 0)
            target.attribute(DialogConstants.PN_EXTERNAL_STYLESHEETS, Arrays.asList(rteAnnotation.externalStyleSheets()).toString().replace(" ", ""));
        // build rtePlugins node, merge it to existing target structure (to pick up child nodes that may have already been populated)
        // then populate rtePlugins node with the context rteAnnotation fields, then merge again
        Target rtePlugins = pluginsBuilder.build(target);
        getFormatNode(rtePlugins.getOrCreate(DialogConstants.NN_PARAFORMAT));
        getSpecialCharactersNode(rtePlugins.getOrCreate(DialogConstants.NN_MISCTOOLS));
        populatePasteRulesNode(rtePlugins);
        populateStylesNode(rtePlugins.getOrCreate(DialogConstants.NN_STYLES).attribute(DialogConstants.PN_PRIMARY_TYPE, DialogConstants.NT_WIDGET_COLLECTION));
        if (rtePlugins.hasChild(DialogConstants.NN_UNDO))
            rtePlugins.getOrCreate(DialogConstants.NN_UNDO).attribute(DialogConstants.PN_MAX_UNDO_STEPS, rteAnnotation.maxUndoSteps());
        if (rteAnnotation.tabSize() != 4)
            rtePlugins.getOrCreate(DialogConstants.NN_KEYS).attribute(DialogConstants.PN_TAB_SIZE, rteAnnotation.tabSize());
        if (rtePlugins.hasChild(DialogConstants.NN_LISTS))
            rtePlugins.getOrCreate(DialogConstants.NN_LISTS).attribute(DialogConstants.PN_INDENT_SIZE, rteAnnotation.indentSize());

        // build htmlLinkRules node and append to root target, if needed
        populateHtmlLinkRules(target);
        clearEmpty(target);
        if (!isEmpty(rtePlugins)) {
            rtePlugins.getOrCreate(DialogConstants.NN_STYLES);
        }
    }

    /**
     * Called by {@link RichTextEditorHandler#accept(Source, Target)} to facilitate single feature token from the
     * {@link RichTextEditor#features()} or {@link RichTextEditor#fullscreenFeatures()} collection to one or more appropriate
     * {@code XmlNodeBuilder}-s
     * @param featureItem A mutually linked pair consisting of a {@code XmlNodeBuilder} for either {@code features()}
     *                    or {@code fullscreenFeatures()} or the current RTE config, and a particular feature token
     * @param tableEditBuilder Additional {@code XmlNodeBuilder} for the tables node
     * @param pluginsBuilder Additional {@code XmlNodeBuilder} for the plugins node
     */

    private static void processFeatureItem(
            ImmutablePair<XmlNodeWithListBuilder,String> featureItem,
            XmlNodeWithListBuilder tableEditBuilder,
            XmlTreeWithListsBuilder pluginsBuilder
    ) {
        XmlNodeWithListBuilder nodeBuilder = featureItem.left;
        String feature = featureItem.right;
        if (FEATURE_TOKEN_PATTERN.matcher(feature).matches()) {
            // single#feature
            nodeBuilder.store(null, feature);
            pluginsBuilder.store(null, feature);

        } else if (FEATURE_TOKEN_ARRAY_PATTERN.matcher(feature).matches()) {
            // [popover#features...], [table#features...], [some:specific:feature]
            String[] nestedTokens = getNestedTokens(feature);
            String leadingPluginToken = StringUtils.substringBefore(nestedTokens[0], PLUGIN_FEATURE_SEPARATOR);
            if (leadingPluginToken.equals(DialogConstants.NN_TABLE)) {
                nodeBuilder.store(null, RteFeatures.TABLE_TABLE);
                tableEditBuilder.storeMany(leadingPluginToken, nestedTokens);
            } else {
                nodeBuilder.store(null, PLUGIN_FEATURE_SEPARATOR + leadingPluginToken);
                nodeBuilder.getChildBuilder().storeMany(leadingPluginToken, nestedTokens);
            }
            if (leadingPluginToken.equals(DialogConstants.NN_TABLE)
                    || leadingPluginToken.equals(DialogConstants.NN_PARAFORMAT)
                    || leadingPluginToken.equals(DialogConstants.NN_STYLES)) {
                pluginsBuilder.store(leadingPluginToken, FEATURE_ALL);
            } else {
                pluginsBuilder.storeMany(null, nestedTokens);
            }

        } else {
            PluginRuntime.context().getExceptionHandler().handle(new ValidationException(MALFORMED_TOKEN_EXCEPTION_MESSAGE));
        }
    }

/**
     * Called by {@link RichTextEditorHandler#accept(Source, Target)} to create if necessary and then retrieve
     * the {@code icons} node for the RichTextEditor XML markup
     */

    private void getIconsNode(Target parent) {
        Target icons = parent.getOrCreate(DialogConstants.NN_ICONS);
        Arrays.stream(rteAnnotation.icons()).forEach(iconMapping -> icons.getOrCreate(iconMapping.command()).mapProperties(iconMapping));
    }

/**
     * Called by {@link RichTextEditorHandler#accept(Source, Target)} to create if necessary and then retrieve
     * the {@code formats} node for the RichTextEditor XML markup
     */

    private void getFormatNode(Target parent) {
        Target formats = parent.getOrCreate(DialogConstants.NN_FORMATS).attribute(DialogConstants.PN_PRIMARY_TYPE, DialogConstants.NT_WIDGET_COLLECTION);
        Arrays.stream(rteAnnotation.formats()).forEach(paragraphFormat -> Validation.forType(paragraphFormat.annotationType()).test(paragraphFormat));
        Arrays.stream(rteAnnotation.formats()).forEach(paragraphFormat ->
                formats.getOrCreate(paragraphFormat.tag())
                        .mapProperties(paragraphFormat));
    }


/**
     * Called by {@link RichTextEditorHandler#accept(Source, Target)} to create if necessary and then retrieve
     * the {@code specialCharsConfig} node for the RichTextEditor XML markup
     */

    private void getSpecialCharactersNode(Target parent) {
        Function<Annotation, String> childNodeNameProvider = c -> {
            Characters chars = (Characters) c;
            return chars.rangeStart() > 0 ? String.valueOf(chars.rangeStart()) : chars.entity();
        };
        Target charsConfigNode = parent.getOrCreate(DialogConstants.NN_SPECIAL_CHARS_CONFIG).getOrCreate(DialogConstants.NN_CHARS);
        CharactersObjectValidator validator = new CharactersObjectValidator();
        Annotation[] validCharactersAnnotations = Arrays.stream(rteAnnotation.specialCharacters())
                .map(validator::getFilteredInstance)
                .toArray(Annotation[]::new);
        Arrays.stream(validCharactersAnnotations).forEach(annotation -> Validation.forType(annotation.annotationType()).test(annotation));
        Arrays.stream(validCharactersAnnotations).forEach(annotation ->
                charsConfigNode.getOrCreate(childNodeNameProvider.apply(annotation)).mapProperties(annotation));
    }


    /**
     * Populates with attributes the {@code styles} node
     * @param parent The routine to generate {@code styles} node and append it to the overall RTE markup
     */
    private void populateStylesNode(Target parent) {
        Target styles = parent.getOrCreate(DialogConstants.NN_STYLES).attribute(DialogConstants.PN_PRIMARY_TYPE, DialogConstants.NT_WIDGET_COLLECTION);
        if (!featureExists(RteFeatures.Popovers.STYLES::equals)) {
            return;
        }
        Arrays.stream(rteAnnotation.styles()).forEach(style ->
                styles.getOrCreate(style.cssName()).mapProperties(style));
    }

    /**
     * Populates with attributes the {@code htmlPasteRules} node
     * @param parent The routine to generate {@code htmlPasteRules} node and append it to the overall RTE markup
     */
    private void populatePasteRulesNode(Target parent){
        HtmlPasteRules rules = this.rteAnnotation.htmlPasteRules();
        Target edit = parent.getOrCreate(DialogConstants.NN_EDIT);
        Target htmlPasteRulesNode = edit.getOrCreate(DialogConstants.NN_HTML_PASTE_RULES);
        List<String> nonDefaultAllowPropsNames = PluginReflectionUtility.getAnnotationNonDefaultProperties(rules).stream()
                .filter(field -> HTML_PASTE_RULES_ALLOW_PATTERN.matcher(field.getName()).matches())
                .map(field -> HTML_PASTE_RULES_ALLOW_PATTERN.matcher(field.getName()).replaceAll("$1").toLowerCase())
                .filter(propName -> {
                    if (StringUtils.equalsAny(propName, DialogConstants.NN_TABLE, DialogConstants.NN_LIST)) {
                        htmlPasteRulesNode.getOrCreate(propName).attribute(DialogConstants.PN_ALLOW, false)
                                .attribute(DialogConstants.PN_IGNORE_MODE, DialogConstants.NN_TABLE.equals(propName)
                                ? rules.allowTables().toString()
                                : rules.allowLists().toString());
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        if (!nonDefaultAllowPropsNames.isEmpty()) {
            Target allowBasicsNode = htmlPasteRulesNode.getOrCreate(DialogConstants.NN_ALLOW_BASICS);
            // default values are all 'true' so non-defaults are 'false'
            nonDefaultAllowPropsNames.forEach(fieldName -> allowBasicsNode.attribute(fieldName, false));
        }
        htmlPasteRulesNode.attribute(DialogConstants.PN_ALLOW_BLOCK_TAGS,  rules.allowedBlockTags().length == 0 ? null : Arrays.asList(rules.allowedBlockTags()).toString().replace(" ", ""))
                .attribute(DialogConstants.PN_FALLBACK_BLOCK_TAG, rules.fallbackBlockTag().isEmpty() ? null : rules.fallbackBlockTag());
        if (!isEmpty(htmlPasteRulesNode)) {
            edit.attribute(DialogConstants.PN_DEFAULT_PASTE_MODE, rteAnnotation.defaultPasteMode().toString().toLowerCase());
        }
    }

    /**
     * Called by {@link RichTextEditorHandler#accept(Source, Target)} to create and append an node representing
     * {@code htmlRules} to the RichTextEditor XML markup
     * @param parent {@code Target} instance representing the RichTextEditor node
     */

    private void populateHtmlLinkRules(Target parent) {
        HtmlLinkRules rules = this.rteAnnotation.htmlLinkRules();
        if (!PluginReflectionUtility.annotationIsNotDefault(rules)) {
            return;
        }
        parent.getOrCreate(DialogConstants.NN_HTML_RULES)
                .getOrCreate(DialogConstants.NN_LINKS)
                .attribute(DialogConstants.PN_CSS_EXTERNAL, rules.cssExternal().isEmpty() ? null : rules.cssExternal())
                .attribute(DialogConstants.PN_CSS_INTERNAL, rules.cssInternal().isEmpty() ? null : rules.cssInternal())
                .attribute(DialogConstants.PN_DEFAULT_PROTOCOL, rules.defaultProtocol())
                .attribute(DialogConstants.PN_PROTOCOLS, Arrays.asList(rules.protocols()).toString().replace(" ", ""))
                .getOrCreate(DialogConstants.NN_TARGET_CONFIG)
                .attribute(DialogConstants.PN_MODE, KEYWORD_AUTO)
                .attribute(DialogConstants.PN_TARGET_EXTERNAL, rules.targetExternal().toString())
                .attribute(DialogConstants.PN_TARGET_INTERNAL, rules.targetInternal().toString());
    }

    /**
     * Extracts tokens within a {@code [feature#token, feature#token2]} unit
     * @param array String representing a multitude of feature tokens
     * @return {@code String[]} array with the extracted feature tokens
     */
    private static String[] getNestedTokens(String array) {
        return StringUtils.strip(array, PluginXmlUtility.ATTRIBUTE_LIST_SURROUND).split(PluginXmlUtility.ATTRIBUTE_LIST_SPLIT_PATTERN);
    }

    /**
     * Gets whether a certain feature exists in the feature set
     * @param matcher Feature token predicate
     * @return True or false
     */
    private boolean featureExists(Predicate<String> matcher) {
        return Stream.concat(Arrays.stream(rteAnnotation.features()), Arrays.stream(rteAnnotation.fullscreenFeatures()))
                .flatMap(s -> Arrays.stream(s.split(PluginXmlUtility.ATTRIBUTE_LIST_SPLIT_PATTERN)))
                .anyMatch(matcher);
    }

    private boolean isEmpty(Target target) {
        return target.getValueMap().size() == 1 &&
            target.getValueMap().containsKey(DialogConstants.PN_PRIMARY_TYPE) &&
            target.listChildren().isEmpty();
    }

    private void clearEmpty(Target target) {
        target.listChildren().forEach(this::clearEmpty);
        target.listChildren().removeIf(this::isEmpty);
    }
}
