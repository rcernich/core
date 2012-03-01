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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.Scanner;
import org.codehaus.plexus.util.StringUtils;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.switchyard.ArtifactModel;
import org.switchyard.config.model.switchyard.ArtifactsModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * DependencyInjectorMojo
 * 
 * <p/>
 * Injects artifact dependencies into the project build path.
 * 
 * @author Rob Cernich
 */
@Component(role = ArtifactDependencyInjector.class)
public class ArtifactDependencyInjector {

    @Requirement
    private ArtifactInjectorConfigurationReader _configurationReader;

    @Requirement
    private BuildContext _buildContext;

    @Requirement
    private Logger _logger;

    /**
     * Adds dependencies for any service artifacts used by a SwitchYard project.
     * 
     * @param session the maven session
     * @param project the maven project
     * @throws MavenExecutionException if something goes awry.
     */
    public void inject(MavenSession session, MavenProject project) throws MavenExecutionException {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Entering SwitchYardMavenLifecycleParticpant");
        }
        final ArtifactInjectorConfiguration config = _configurationReader.readConfiguration(session, project);
        if (config == null) {
            if (_logger.isDebugEnabled()) {
                _logger.debug("SwitchYard plugin configuration could not be found.");
            }
            return;
        }

        try {
            List<String> manifestDependencies = new ArrayList<String>();

            SwitchYardModel symodel = getSwitchYardModel(project);
            if (symodel == null) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug("Could not find switchyard.xml file for project: " + project);
                }
                return;
            }

            ArtifactsModel artifacts = symodel.getArtifacts();
            if (artifacts == null) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug("No SwitchYard artifact dependencies for project: " + project);
                }
                return;
            }

            File outputLocation = new File(new File(session.getLocalRepository().getBasedir(), session
                    .getLocalRepository().pathOf(project.getArtifact())).getParentFile(), "service-artifacts");
            for (ArtifactModel artifact : artifacts.getArtifacts()) {
                String name = artifact.getName();
                if (config.isAddJarExtension() && !name.endsWith(".jar")) {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("Adding jar extension to artifact: " + name);
                    }
                    name = name + ".jar";
                }
                File artifactJarFile = new File(outputLocation, name);
                if (!session.isOffline() && (!artifactJarFile.exists() || config.isAlwaysUpdate())) {
                    downloadArtifact(artifact, artifactJarFile);
                } else if (_logger.isDebugEnabled()) {
                    _logger.debug("Skipping download of artifact \"" + artifact.getName() + "\" from \""
                            + artifact.getURL() + "\" to \"" + artifactJarFile + "\".");
                }
                if (config.isAddArtifactsToPomDependencies()) {
                    addSystemDependency(project, artifact, artifactJarFile);
                }
                addManifestDependency(artifactJarFile, config.getManifestDependencyPrefix(), manifestDependencies);
            }
            project.getProperties().setProperty(config.getManifestDependenciesPropertyName(),
                    StringUtils.join(manifestDependencies.iterator(), ","));
        } catch (IOException e) {
            throw new MavenExecutionException("unexpected error occurred", e);
        }
    }

    private SwitchYardModel getSwitchYardModel(MavenProject project) throws IOException {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Scanning \"" + project.getName() + "\" resources for META-INF/switchyard.xml file");
        }
        File syFile = null;
        for (Resource resource : project.getResources()) {
            Scanner scanner = _buildContext.newScanner(new File(resource.getDirectory()), true);
            List<String> includes = resource.getIncludes();
            List<String> excludes = resource.getExcludes();
            if (includes == null || includes.size() == 0) {
                scanner.setIncludes(new String[] {"**/**" });
            } else {
                scanner.setIncludes(includes.toArray(new String[includes.size()]));
            }
            if (excludes != null && excludes.size() > 0) {
                scanner.setExcludes(excludes.toArray(new String[excludes.size()]));
            }
            scanner.addDefaultExcludes();
            scanner.scan();
            String[] files = scanner.getIncludedFiles();
            if (files == null) {
                continue;
            }
            for (String file : files) {
                if (file.endsWith("/META-INF/switchyard.xml") || file.equals("META-INF/switchyard.xml")) {
                    syFile = new File(resource.getDirectory(), file);
                    break;
                }
            }
        }
        if (syFile == null) {
            _logger.warn("META-INF/switchyard.xml could not be found.");
            return null;
        }
        if (_logger.isDebugEnabled()) {
            _logger.debug("found switchyard.xml file: " + syFile);
        }
        return new ModelPuller<SwitchYardModel>().pull(syFile);
    }

    private void downloadArtifact(ArtifactModel artifact, File artifactJarFile) throws IOException,
            MalformedURLException {
        URL url = new URL(artifact.getURL());
        InputStream is = null;
        OutputStream os = null;
        _logger.info("Downloading artifact \"" + artifact.getName() + "\" from \"" + url + "\" to \"" + artifactJarFile
                + "\".");
        try {
            // ensure parent directories have been created
            artifactJarFile.getParentFile().mkdirs();
            is = url.openStream();
            os = new FileOutputStream(artifactJarFile);
            byte[] buffer = new byte[8192];
            while (true) {
                int read = is.read(buffer);
                if (read < 0) {
                    break;
                }
                os.write(buffer, 0, read);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        }
    }

    private void addSystemDependency(MavenProject project, ArtifactModel artifact, File artifactJarFile) {
        Dependency dependency = new Dependency();
        dependency.setGroupId("switchyard.repository.artifact");
        dependency.setArtifactId(artifact.getName());
        dependency.setVersion("1");
        dependency.setScope("system");
        dependency.setSystemPath(artifactJarFile.getAbsolutePath());
        project.getDependencies().add(dependency);
        if (_logger.isDebugEnabled()) {
            _logger.debug("Adding dependency: " + dependency);
        }
    }

    private void addManifestDependency(File artifactJarFile, String manifestDependencyPrefix,
            List<String> manifestDependencies) {
        String dependency = manifestDependencyPrefix + artifactJarFile.getName();
        manifestDependencies.add(dependency);
        if (_logger.isDebugEnabled()) {
            _logger.debug("Adding item to Dependencies property: " + dependency);
        }
    }

}
