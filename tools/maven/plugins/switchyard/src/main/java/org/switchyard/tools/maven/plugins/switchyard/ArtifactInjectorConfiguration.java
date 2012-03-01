/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.tools.maven.plugins.switchyard;

/**
 * ArtifactInjectorConfiguration
 * 
 * <p/>
 * Configuration used for processing artifact dependency injection.
 * 
 * @author Rob Cernich
 */
public class ArtifactInjectorConfiguration {

    private String _outputFolder;
    private boolean _addArtifactsToPomDependencies;
    private String _manifestDependenciesPropertyName;
    private boolean _addJarExtension;
    private String _manifestDependencyPrefix;

    // TODO: think about adding version markers, e.g. similar to dependency
    // plugin.
    private boolean _alwaysUpdate;

    /**
     * Defaults to ${project.build.directory}/dependency/service-artifacts.
     * 
     * @return Folder where dependencies will be copied.
     */
    public String getOutputFolder() {
        return _outputFolder;
    }

    /**
     * @param outputFolder Folder where dependencies will be copied.
     */
    public void setOutputFolder(String outputFolder) {
        _outputFolder = outputFolder;
    }

    /**
     * Defaults to true.
     * 
     * @return true if a system dependency should be added for each artifact.
     */
    public boolean isAddArtifactsToPomDependencies() {
        return _addArtifactsToPomDependencies;
    }

    /**
     * @param addArtifactsToPomDependencies true if a system dependency should
     *            be added for each artifact.
     */
    public void setAddArtifactsToPomDependencies(boolean addArtifactsToPomDependencies) {
        _addArtifactsToPomDependencies = addArtifactsToPomDependencies;
    }

    /**
     * The name of the property used to store manifest dependencies. This
     * property can be used when configuring manifest entries for the project's
     * archive. For example: <code>
     * <pre>
     * <manifestEntries>
     *   <Dependencies>${switchyard.manifest.dependencies}</Dependencies>
     * </manifestEntries>
     * </pre>
     * </code>
     * 
     * Default is "switchyard.manifest.dependencies"
     * 
     * @return the property name
     */
    public String getManifestDependenciesPropertyName() {
        return _manifestDependenciesPropertyName;
    }

    /**
     * @param manifestDependenciesPropertyName the property name.
     */
    public void setManifestDependenciesPropertyName(String manifestDependenciesPropertyName) {
        _manifestDependenciesPropertyName = manifestDependenciesPropertyName;
    }

    /**
     * Defaults to true.
     * 
     * @return true to add ".jar" to the artifact name if the file name does not
     *         already end with ".jar".
     */
    public boolean isAddJarExtension() {
        return _addJarExtension;
    }

    /**
     * @param addJarExtension true to add ".jar" to the artifact name if the
     *            file name does not already end with ".jar".
     */
    public void setAddJarExtension(boolean addJarExtension) {
        _addJarExtension = addJarExtension;
    }

    /**
     * Defaults to "deployment." For example, "deployment.some-artifact.jar".
     * 
     * @return Prefix to be applied to manifest "Dependencies" entries.
     */
    public String getManifestDependencyPrefix() {
        return _manifestDependencyPrefix;
    }

    /**
     * @param manifestDependencyPrefix Prefix to be applied to manifest
     *            "Dependencies" entries.
     */
    public void setManifestDependencyPrefix(String manifestDependencyPrefix) {
        _manifestDependencyPrefix = manifestDependencyPrefix;
    }

    /**
     * Defaults to false.
     * 
     * @return true to always download the resource from the repository, even if
     *         the resource exists in the workspace.
     */
    public boolean isAlwaysUpdate() {
        return _alwaysUpdate;
    }

    /**
     * @param alwaysUpdate true to always download the resource from the
     *            repository, even if the resource exists in the workspace.
     */
    public void setAlwaysUpdate(boolean alwaysUpdate) {
        _alwaysUpdate = alwaysUpdate;
    }
}
