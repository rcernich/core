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

import java.util.Set;

import org.switchyard.config.model.Model;

/**
 * The "header" configuration model.
 */
public interface StaticPropertyModel extends Model {

    /** The "header" name. */
    public static final String HEADER = "header";
    /** The "labels" name. */
    public static final String LABELS = "labels";

    /**
     * Gets the parent properties model.
     * @return the parent properties model.
     */
    public StaticPropertiesModel getStaticProperties();

    /**
     * Gets the name attribute.
     * @return the name attribute
     */
    public String getName();

    /**
     * Sets the name attribute.
     * @param name the name attribute
     * @return this StaticPropertyModel (useful for chaining)
     */
    public StaticPropertyModel setName(String name);

    /**
     * Gets the labels attribute.
     * @return the labels attribute
     */
    public Set<String> getLabels();

    /**
     * Sets the labels attribute.
     * @param labels the labels attribute
     * @return this StaticPropertyModel (useful for chaining)
     */
    public StaticPropertyModel setLabels(Set<String> labels);

    /**
     * Gets the value.
     * @return the value
     */
    public String getValue();

    /**
     * Sets the value.
     * @param value the value
     * @return this StaticPropertyModel (useful for chaining)
     */
    public StaticPropertyModel setValue(String value);

}
