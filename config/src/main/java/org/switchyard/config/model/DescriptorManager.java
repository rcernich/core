/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.switchyard.config.model;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * DescriptorManager
 * <p/>
 * Manages descriptors for various model versions.
 */
public class DescriptorManager {

    /**
     * Represents a specific version of SwitchYard application configuration
     * model.
     */
    public enum ConfigurationVersion {
        /** The configuration version used with SwitchYard 1.0.x projects. */
        v1_0("1.0"),
        /** The configuration version used with SwitchYard 1.1.x projects. */
        v1_1("1.1");

        private static final String PROPERTIES_PATH = "/org/switchyard/config/model/%/descriptor.properties";
        private final String _version;

        private ConfigurationVersion(final String version) {
            _version = version;
        }

        /**
         * @return descriptor.properties path for this version.
         */
        public String getDescriptorPath() {
            return String.format(PROPERTIES_PATH, _version);
        }

        /**
         * @return the default configuration version
         */
        public static ConfigurationVersion getDefault() {
            return v1_1;
        }

        /**
         * @param version the version string, e.g. "1.0"
         * @return the corresponding ConfigurationVersion.
         */
        public static ConfigurationVersion fromString(final String version) {
            for (ConfigurationVersion configurationVersion : ConfigurationVersion.values()) {
                if (configurationVersion._version.equals(version)) {
                    return configurationVersion;
                }
            }
            return null;
        }
    }

    private final Map<ConfigurationVersion, Descriptor> _descriptorsByVersion = new TreeMap<ConfigurationVersion, Descriptor>(
            new Comparator<ConfigurationVersion>() {
                @Override
                public int compare(ConfigurationVersion o1, ConfigurationVersion o2) {
                    // we want newest first
                    return o2.compareTo(o1);
                }
            });

    /**
     * Create a new DescriptorManager, loading default Descriptor objects for
     * each supported version.
     */
    public DescriptorManager() {
        for (ConfigurationVersion version : ConfigurationVersion.values()) {
            try {
                _descriptorsByVersion.put(version, new Descriptor(version));
            } catch (Exception e) {
                // TODO: log an error for the version
                e.fillInStackTrace();
            }
        }
    }

    /**
     * @param version the specific configuration version.
     * @return the model descriptor for a specific version of a SwitchYard
     *         application configuration.
     */
    public Descriptor getDescriptor(final ConfigurationVersion version) {
        return _descriptorsByVersion.get(version);
    }

    /**
     * XXX: if we update all namespaces for each version, the
     * "latest descriptor" bit is irrelevant.
     * 
     * @param namespace the namespace of a configuration element.
     * @return the latest descriptor which supports the specified configuration.
     */
    public Descriptor getDescriptor(final String namespace) {
        if (namespace == null) {
            return null;
        }
        for (Descriptor descriptor : _descriptorsByVersion.values()) {
            if (descriptor.getMarshaller(namespace) != null) {
                return descriptor;
            }
        }
        return null;
    }
}
