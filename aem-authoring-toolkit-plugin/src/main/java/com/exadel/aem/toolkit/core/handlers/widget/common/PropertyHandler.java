package com.exadel.aem.toolkit.core.handlers.widget.common;

import com.exadel.aem.toolkit.api.annotations.widgets.property.Property;
import com.exadel.aem.toolkit.api.handlers.Source;
import com.exadel.aem.toolkit.api.handlers.Target;
import com.exadel.aem.toolkit.core.util.NamingUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PropertyHandler implements BiConsumer<Source, Target> {

    @Override
    public void accept(Source source, Target target) {
        Arrays.stream(source.adaptTo(Property[].class))
            .forEach(p -> acceptProperty(p, target));
    }

    private void acceptProperty(Property property, Target target) {
        List<String> list = Pattern.compile("/").splitAsStream(property.name()).collect(Collectors.toList());
        String propertyName = list.remove(list.size() - 1);
        target.getOrCreate(String.join(StringUtils.EMPTY, list)).attribute(NamingUtil.getValidFieldName(propertyName), property.value());
    }
}
