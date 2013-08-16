/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.config.model.composer.v1;

import static org.switchyard.config.model.switchyard.SwitchYardModel.DEFAULT_NAMESPACE;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composer.StaticPropertiesModel;
import org.switchyard.config.model.composer.StaticPropertyModel;

/**
 * Implementation of StaticPropertyModel : v1.
 */
public class V1StaticPropertyModel extends BaseModel implements StaticPropertyModel {

    /**
     * Creates a new PropertyModel in the default namespace.
     */
    public V1StaticPropertyModel() {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Creates a new StaticPropertyModel in the specified namespace.
     * @param namespace the specified namespace
     */
    public V1StaticPropertyModel(String namespace) {
        super(new QName(namespace, HEADER));
    }

    /**
     * Creates a new StaticPropertyModel with the specified configuration and descriptor.
     * @param config the configuration
     * @param desc the descriptor
     */
    public V1StaticPropertyModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    @Override
    public StaticPropertiesModel getStaticProperties() {
        return (StaticPropertiesModel)getModelParent();
    }

    @Override
    public String getName() {
        return getModelAttribute("name");
    }

    @Override
    public StaticPropertyModel setName(String name) {
        setModelAttribute("name", name);
        return this;
    }

    @Override
    public Set<String> getLabels() {
        Set<String> labelSet = new HashSet<String>();
        String labels = getModelConfiguration().getAttribute(LABELS);
        if (labels != null) {
            for (String label : labels.split(" ")) {
                labelSet.add(label);
            }
        }
        return labelSet;
    }

    @Override
    public StaticPropertyModel setLabels(Set<String> labels) {
        if (labels == null || labels.isEmpty()) {
            return this;
        }
        Iterator<String> it = labels.iterator();
        String labelString = it.next();
        while (it.hasNext()) {
            labelString += " " + it.next();
        }
        
        getModelConfiguration().setAttribute(LABELS, labelString);
        return this;
    }

    @Override
    public String getValue() {
        return getModelValue();
    }

    @Override
    public StaticPropertyModel setValue(String value) {
        setModelValue(value);
        return this;
    }

}
