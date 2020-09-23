package com.exadel.aem.toolkit.samples.models;

import com.exadel.aem.toolkit.api.annotations.container.Accordion;
import com.exadel.aem.toolkit.api.annotations.container.PlaceOn;
import com.exadel.aem.toolkit.api.annotations.main.Dialog;
import com.exadel.aem.toolkit.api.annotations.widgets.DialogField;
import com.exadel.aem.toolkit.api.annotations.widgets.Extends;
import com.exadel.aem.toolkit.api.annotations.widgets.property.Properties;
import com.exadel.aem.toolkit.api.annotations.widgets.property.Property;
import com.exadel.aem.toolkit.api.annotations.widgets.rte.RichTextEditor;
import com.exadel.aem.toolkit.api.annotations.widgets.rte.RteFeatures;
import com.exadel.aem.toolkit.api.annotations.widgets.select.Option;
import com.exadel.aem.toolkit.api.annotations.widgets.select.Select;
import com.exadel.aem.toolkit.samples.constants.GroupConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Dialog(
        name = "content/dungeons-component",
        title = "Dungeons Component",
        description = "Choose a dungeon for your warrior",
        resourceSuperType = "authoring-toolkit/samples/components/content/father-select-component",
        componentGroup = GroupConstants.COMPONENT_GROUP,
        accordionTabs = {
                @Accordion(title = "Main")
        }
)
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DungeonsComponent extends ParentSelectComponent {

    private static final String DEFAULT_ROTTEN_SWAMPS_TEXT = "rotten swamps, where your nose will suffer from terrible smells,";
    private static final String DEFAULT_ICE_VALLEY_TEXT = "ice valley, where you can lose your arm from strong frost,";
    private static final String DEFAULT_RULES = "no rules!";

    @Extends(value = WarriorDescriptionComponent.class, field = "description")
    @RichTextEditor(
            features = {
                    RteFeatures.SEPARATOR,
                    RteFeatures.LISTS_ORDERED,
                    RteFeatures.LISTS_UNORDERED
            })
    @DialogField(label = "Make your own dungeons rules")
    @ValueMapValue
    @PlaceOn("Main")
    private String dungeonRules;

    @Select(options = {
            @Option(text = "Rotten swamps", value = "1"),
            @Option(text = "Ice valley", value = "2")
    })
    @DialogField(label = "Dungeons select")
    @Default(values = "1")
    @Properties(value = {@Property(name = "sling:hideChildren", value = "*")})
    @ValueMapValue
    @PlaceOn("Main")
    private String dungeonsSelect;

    public String getDungeonRules() {
        return StringUtils.defaultIfBlank(dungeonRules, DEFAULT_RULES);
    }

    public String getDungeonDescription() {
        if (dungeonsSelect.equals("1")) {
            return DEFAULT_ROTTEN_SWAMPS_TEXT;
        }
        return DEFAULT_ICE_VALLEY_TEXT;
    }
}
