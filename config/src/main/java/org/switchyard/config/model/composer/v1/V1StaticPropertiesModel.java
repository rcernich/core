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

import static org.switchyard.config.model.property.PropertyModel.PROPERTY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composer.ContextMapperModel;
import org.switchyard.config.model.composer.StaticPropertiesModel;
import org.switchyard.config.model.composer.StaticPropertyModel;

/**
 * A version 1 StaticPropertiesModel.
 */
public class V1StaticPropertiesModel extends BaseModel implements StaticPropertiesModel {

    private List<StaticPropertyModel> _properties = new ArrayList<StaticPropertyModel>();

    /**
     * Creates a new StaticPropertiesModel in the specified namespace.
     * @param name the specified name
     * @param namespace the specified namespace
     */
    public V1StaticPropertiesModel(String name, String namespace) {
        super(name, namespace);
    }

    /**
     * Creates a new PropertiesModel with the specified configuration and descriptor.
     * @param config the configuration
     * @param desc the descriptor
     */
    public V1StaticPropertiesModel(Configuration config, Descriptor desc) {
        super(config, desc);
        for (Configuration property_config : config.getChildren(StaticPropertyModel.HEADER)) {
            StaticPropertyModel property = (StaticPropertyModel)readModel(property_config);
            if (property != null) {
                _properties.add(property);
            }
        }
        setModelChildrenOrder(PROPERTY);
    }

    @Override
    public ContextMapperModel getContextMapperModel() {
        return (ContextMapperModel) getModelParent();
    }

    @Override
    public synchronized List<StaticPropertyModel> getStaticProperties() {
        return Collections.unmodifiableList(_properties);
    }

    @Override
    public synchronized StaticPropertiesModel addStaticProperty(StaticPropertyModel property) {
        addChildModel(property);
        _properties.add(property);
        return this;
    }

    @Override
    public StaticPropertyModel getStaticProperty(String name) {
        StaticPropertyModel property = null;
        for (StaticPropertyModel p : _properties) {
            if (p.getName().equals(name)) {
                property = p;
                break;
            }
        }
        return property;
    }

    @Override
    public StaticPropertyModel removeStaticProperty(String propertyName) {
        StaticPropertyModel removed = null;
        
        for (StaticPropertyModel property : _properties) {
            if (property.getName().equals(propertyName)) {
                removed = property;
                _properties.remove(property);
            }
        }
        return removed;
    }

}
