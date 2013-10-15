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
package org.switchyard.config.model.switchyard;

import org.switchyard.config.model.NamedModel;
import org.switchyard.config.model.composite.CompositeModel;
import org.switchyard.config.model.domain.DomainModel;
import org.switchyard.config.model.transform.TransformsModel;
import org.switchyard.config.model.validate.ValidatesModel;

/**
 * The root "switchyard" configuration model.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public interface SwitchYardModel extends NamedModel {

    /** The default switchyard namespace. */
    public static final String DEFAULT_NAMESPACE = "urn:switchyard-config:switchyard:1.0";

    /** The "switchyard" name. */
    public static final String SWITCHYARD = "switchyard";

    /** The "version" attribute. */
    public static final String VERSION = "version";

    /**
     * Represents a specific version of SwitchYard application configuration
     * model.  Aligns with versionEnum in switchyard-v1.xsd.
     * 
     * @since 1.1
     */
    public enum ConfigurationVersion {
        /** An unknown version (i.e. one newer than supported by this model). */
        UNKNOWN(""),
        /** The configuration version used with SwitchYard 1.0.x projects. */
        v1_0("1.0"),
        /** The configuration version used with SwitchYard 1.1.x projects. */
        v1_1("1.1");

        private final String _version;

        private ConfigurationVersion(final String version) {
            _version = version;
        }

        /**
         * @return the version string, e.g. "1.0"
         */
        public String getString() {
            return _version;
        }

        /**
         * @return the default configuration version
         */
        public static ConfigurationVersion getDefault() {
            return v1_0;
        }

        /**
         * @return the default configuration version
         */
        public static ConfigurationVersion getLatest() {
            return ConfigurationVersion.values()[ConfigurationVersion.values().length - 1];
        }

        /**
         * @param version the version string, e.g. "1.0"
         * @return the corresponding ConfigurationVersion.
         */
        public static ConfigurationVersion fromString(final String version) {
            if (version == null) {
                return getDefault();
            }
            for (ConfigurationVersion configurationVersion : ConfigurationVersion.values()) {
                if (configurationVersion._version.equals(version)) {
                    return configurationVersion;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Gets the child composite model.
     * @return the child composite model
     */
    public CompositeModel getComposite();

    /**
     * Sets the child composite model.
     * @param composite the child composite model
     * @return this SwitchYardModel (useful for chaining)
     */
    public SwitchYardModel setComposite(CompositeModel composite);

    /**
     * Gets the child transforms model.
     * @return the child transforms model
     */
    public TransformsModel getTransforms();

    /**
     * Sets the child transforms model.
     * @param transforms the child transforms model.
     * @return this SwitchYardModel (useful for chaining)
     */
    public SwitchYardModel setTransforms(TransformsModel transforms);

    /**
     * Gets the child validates model.
     * @return the child validates model
     */
    public ValidatesModel getValidates();
    
    /**
     * Sets the child artifacts model.
     * @param artifacts the child artifacts model.
     * @return this SwitchYardModel (useful for chaining)
     */
    public SwitchYardModel setArtifacts(ArtifactsModel artifacts);

    /**
     * Gets the child artifacts model.
     * @return the child artifacts model
     */
    public ArtifactsModel getArtifacts();
    
    /**
     * Sets the child validates model.
     * @param validatesModel the child validates model.
     * @return this SwitchYardModel (useful for chaining)
     */
    public SwitchYardModel setValidates(ValidatesModel validatesModel);

    /**
     * Gets the child domain model.
     * @return the child domain model
     */
    public DomainModel getDomain();
    
    /**
     * Sets the child domain model.
     * @param domain the child domain model.
     * @return this SwitchyardModel (useful for chaining)
     */
    public SwitchYardModel setDomain(DomainModel domain);

    /**
     * Sets the domain property resolver based on the current state of the model.
     */
    public void setDomainPropertyResolver();

    /**
     * XXX: consider moving this up into Model.
     * 
     * @return the specified configuration version, or the default version.
     * @since 1.1
     */
    public ConfigurationVersion getConfigurationVersion();
    
    /**
     * XXX: consider moving this up into Model.
     * 
     * @param version the minimum runtime version for which this configuration
     *            is applicable.
     * @since 1.1
     */
    public void setRawConfigurationVersion(String version);
    
    /**
     * @return the raw text for the configuration version.
     */
    public String getRawConfigurationVersion();
}
