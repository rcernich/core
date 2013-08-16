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

package org.switchyard.config.model.composer;

import java.util.List;

import org.switchyard.config.model.Model;

/**
 * The "HeaderPropertiesType" configuration model.
 */
public interface StaticPropertiesModel extends Model {

    /** "in" properties. */
    public static final String IN = "in";
    /** "out" properties. */
    public static final String OUT = "out";
    /** "context" properties. */
    public static final String EXCHANGE = "exchange";

    /**
     * Gets the parent context mapper model.
     * @return the parent context mapper model
     */
    public ContextMapperModel getContextMapperModel();

    /**
     * Gets the child property models.
     * @return the child property models
     */
    public List<StaticPropertyModel> getStaticProperties();

    /**
     * Adds a child property model.
     * @param property the child property model to add
     * @return this StaticPropertiesModel (useful for chaining)
     */
    public StaticPropertiesModel addStaticProperty(StaticPropertyModel property);
    
    /**
     * Fetch a property model by name.
     * @param name name of the property
     * @return property with the specified name, or null if no such property exists
     */
    public StaticPropertyModel getStaticProperty(String name);
    
    /**
     * Removes a child property model.
     * @param propertyName the name of the property
     * @return the removed property or null if the named property was not present
     */
    public StaticPropertyModel removeStaticProperty(String propertyName);

}
