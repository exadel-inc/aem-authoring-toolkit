/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
package com.exadel.aem.toolkit.bundle.customlists.datasource;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;
import static javax.jcr.query.Query.JCR_SQL2;

/**
 * Retrieves all components that have acl-item-component group
 */
@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.resourceTypes=aem-custom-lists/datasources/list-items",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET
    }
)
@SuppressWarnings("PackageAccessibility")
public class ItemComponentDatasource extends SlingSafeMethodsServlet {
    private static final String SELECT_STATEMENT = "SELECT * FROM [cq:Component] AS s WHERE ISDESCENDANTNODE(s,'/apps') AND [componentGroup] = 'acl-item-component'";

    private static final String VALUE = "value";
    private static final String TEXT = "text";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        ResourceResolver resolver = request.getResourceResolver();
        Iterator<Resource> resources = resolver.findResources(SELECT_STATEMENT, JCR_SQL2);
        List<Resource> actualList = new ArrayList<>();
        while (resources.hasNext()) {
            Resource item = resources.next();
            ValueMap valueMap = new ValueMapDecorator(new HashMap<>());
            valueMap.put(VALUE, item.getPath());
            valueMap.put(TEXT, item.getValueMap().get(JCR_TITLE, ""));
            actualList.add(new ValueMapResource(resolver, new ResourceMetadata(), NT_UNSTRUCTURED, valueMap));
        }
        DataSource dataSource = new SimpleDataSource(actualList.iterator());
        request.setAttribute(DataSource.class.getName(), dataSource);
    }
}
