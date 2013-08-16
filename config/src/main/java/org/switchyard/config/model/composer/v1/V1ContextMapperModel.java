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

import javax.xml.namespace.QName;

import org.switchyard.common.lang.Strings;
import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composer.ContextMapperModel;
import org.switchyard.config.model.composer.StaticPropertiesModel;
import org.switchyard.config.model.composite.BindingModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * A version 1 ContextMapperModel.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public class V1ContextMapperModel extends BaseModel implements ContextMapperModel {

    private StaticPropertiesModel _staticInMessageProperties;
    private StaticPropertiesModel _staticOutMessageProperties;
    private StaticPropertiesModel _staticExchangeProperties;

    /**
     * Constructs a new V1ContextMapperModel in the default switchyard namespace.
     */
    public V1ContextMapperModel() {
        this(SwitchYardModel.DEFAULT_NAMESPACE);
    }

    /**
     * Constructs a new V1ContextMapperModel in the specified namespace.
     * @param namespace the specified namespace
     */
    public V1ContextMapperModel(String namespace) {
        super(new QName(namespace, CONTEXT_MAPPER));
    }

    /**
     * Constructs a new V1ContextMapperModel with the specified Configuration and Descriptor.
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1ContextMapperModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BindingModel getBindingModel() {
        return (BindingModel)getModelParent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClazz() {
        return Strings.trimToNull(getModelAttribute("class"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContextMapperModel setClazz(String clazz) {
        setModelAttribute("class", clazz);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIncludes() {
        return Strings.trimToNull(getModelAttribute("includes"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContextMapperModel setIncludes(String includes) {
        setModelAttribute("includes", includes);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExcludes() {
        return Strings.trimToNull(getModelAttribute("excludes"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContextMapperModel setExcludes(String excludes) {
        setModelAttribute("excludes", excludes);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIncludeNamespaces() {
        return Strings.trimToNull(getModelAttribute("includeNamespaces"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContextMapperModel setIncludeNamespaces(String includeNamespaces) {
        setModelAttribute("includeNamespaces", includeNamespaces);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExcludeNamespaces() {
        return Strings.trimToNull(getModelAttribute("excludeNamespaces"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContextMapperModel setExcludeNamespaces(String excludeNamespaces) {
        setModelAttribute("excludeNamespaces", excludeNamespaces);
        return this;
    }

    @Override
    public StaticPropertiesModel getStaticInMessageProperties() {
        if (_staticInMessageProperties == null) {
            _staticInMessageProperties = (StaticPropertiesModel)getFirstChildModelStartsWith(StaticPropertiesModel.IN);
        }
        return _staticInMessageProperties;
    }

    @Override
    public ContextMapperModel setStaticInMessageProperties(StaticPropertiesModel inHeaderProperties) {
        setChildModel(inHeaderProperties);
        _staticInMessageProperties = inHeaderProperties;
        return this;
    }

    @Override
    public StaticPropertiesModel getStaticOutMessageProperties() {
        if (_staticOutMessageProperties == null) {
            _staticOutMessageProperties = (StaticPropertiesModel)getFirstChildModelStartsWith(StaticPropertiesModel.OUT);
        }
        return _staticOutMessageProperties;
    }

    @Override
    public ContextMapperModel setStaticOutMessageProperties(StaticPropertiesModel outHeaderProperties) {
        setChildModel(outHeaderProperties);
        _staticOutMessageProperties = outHeaderProperties;
        return this;
    }

    @Override
    public StaticPropertiesModel getStaticExchangeProperties() {
        if (_staticExchangeProperties == null) {
            _staticExchangeProperties = (StaticPropertiesModel)getFirstChildModelStartsWith(StaticPropertiesModel.EXCHANGE);
        }
        return _staticExchangeProperties;
    }

    @Override
    public ContextMapperModel setStaticExchangeProperties(StaticPropertiesModel contextHeaderProperties) {
        setChildModel(contextHeaderProperties);
        _staticExchangeProperties = contextHeaderProperties;
        return this;
    }

}
