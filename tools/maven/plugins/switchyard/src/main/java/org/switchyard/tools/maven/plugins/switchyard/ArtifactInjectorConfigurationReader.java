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

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * ArtifactInjectorConfigurationReader
 * 
 * <p/>
 * Reads configuration for SwitchYard artifact injection.
 * 
 * @author Rob Cernich
 */
@Component(role = ArtifactInjectorConfigurationReader.class)
public class ArtifactInjectorConfigurationReader {

    @Requirement
    private Logger _logger;

    /**
     * Reads artifact injector configuration from a project.
     * 
     * @param session the maven session.
     * @param project the maven project.
     * @return the configuration; null if the SwitchYard plugin is not
     *         configured on the project.
     */
    public ArtifactInjectorConfiguration readConfiguration(MavenSession session, MavenProject project) {
        Plugin plugin = project.getPlugin("org.switchyard:switchyard-plugin");
        if (plugin == null) {
            return null;
        }

        ArtifactInjectorConfiguration config = new ArtifactInjectorConfiguration();

        initializeDefaults(config, project);

        Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
        if (configuration != null) {
            if (_logger.isDebugEnabled()) {
                _logger.debug("switchyard-plugin configuration for " + project.toString() + ":\n"
                        + configuration.toString());
            }
            for (Xpp3Dom node : configuration.getChildren()) {
                if (node.getName().equals("outputFolder")) {
                    if (node.getValue() != null) {
                        config.setOutputFolder(new File(project.getBasedir(), node.getValue()).getAbsolutePath());
                    }
                } else if (node.getName().equals("addArtifactsToPomDependencies")) {
                    if (node.getValue() != null) {
                        config.setAddArtifactsToPomDependencies(Boolean.parseBoolean(node.getValue()));
                    }
                } else if (node.getName().equals("manifestDependenciesPropertyName")) {
                    if (node.getValue() != null) {
                        config.setManifestDependenciesPropertyName(node.getValue());
                    }
                } else if (node.getName().equals("addJarExtension")) {
                    if (node.getValue() != null) {
                        config.setAddJarExtension(Boolean.parseBoolean(node.getValue()));
                    }
                } else if (node.getName().equals("manifestDependencyPrefix")) {
                    if (node.getValue() != null) {
                        config.setManifestDependencyPrefix(node.getValue());
                    }
                } else if (node.getName().equals("alwaysUpdate")) {
                    if (node.getValue() != null) {
                        config.setAlwaysUpdate(Boolean.parseBoolean(node.getValue()));
                    }
                }
            }
        }

        return config;
    }

    private void initializeDefaults(ArtifactInjectorConfiguration config, MavenProject project) {
        String property = project.getProperties().getProperty("switchyard.artifacts.addArtifactsToPomDependencies",
                Boolean.TRUE.toString());
        config.setAddArtifactsToPomDependencies(Boolean.parseBoolean(property));

        property = project.getProperties().getProperty("switchyard.artifacts.addJarExtension", Boolean.TRUE.toString());
        config.setAddJarExtension(Boolean.parseBoolean(property));

        property = project.getProperties().getProperty("switchyard.artifacts.alwaysUpdate", Boolean.FALSE.toString());
        config.setAlwaysUpdate(Boolean.parseBoolean(property));

        property = project.getProperties().getProperty("switchyard.artifacts.manifestDependenciesPropertyName",
                "switchyard.manifest.dependencies");
        config.setManifestDependenciesPropertyName(property);

        property = project.getProperties().getProperty("switchyard.artifacts.manifestDependencyPrefix", "deployment.");
        config.setManifestDependencyPrefix(property);

        property = project.getProperties().getProperty("switchyard.artifacts.outputFolder",
                "dependency/service-artifacts");
        config.setOutputFolder(new File(project.getBuild().getDirectory(), property).getAbsolutePath());
    }

}
