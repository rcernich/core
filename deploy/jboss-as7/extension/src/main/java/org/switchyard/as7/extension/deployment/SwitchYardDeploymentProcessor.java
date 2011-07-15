/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.as7.extension.deployment;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.jboss.as.controller.PathElement;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.weld.WeldDeploymentMarker;
import org.jboss.as.weld.services.BeanManagerService;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.ImmediateValue;
import org.switchyard.admin.Component;
import org.switchyard.admin.Service;
import org.switchyard.admin.base.BaseApplication;
import org.switchyard.admin.base.BaseService;
import org.switchyard.admin.base.BaseSwitchYard;
import org.switchyard.as7.extension.SwitchYardDeploymentMarker;
import org.switchyard.as7.extension.SwitchYardExtension;
import org.switchyard.as7.extension.admin.SwitchYardAdminService;
import org.switchyard.as7.extension.services.SwitchYardService;
import org.switchyard.config.model.composite.BindingModel;
import org.switchyard.config.model.composite.CompositeModel;
import org.switchyard.config.model.composite.CompositeServiceModel;
import org.switchyard.config.model.composite.InterfaceModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * Deployment processor that installs the SwitchYard service and all other dependent services.
 * 
 * @author Magesh Kumar B <mageshbk@jboss.com> (C) 2011 Red Hat Inc.
 */
public class SwitchYardDeploymentProcessor implements DeploymentUnitProcessor {

    private static final Logger LOG = Logger.getLogger("org.switchyard");

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (!SwitchYardDeploymentMarker.isSwitchYardDeployment(deploymentUnit)) {
            return;
        }
        LOG.info("Deploying SwitchYard application '" + deploymentUnit.getName() + "'");

        SwitchYardMetaData metaData = deploymentUnit.getAttachment(SwitchYardMetaData.ATTACHMENT_KEY);
        SwitchYardDeployment deployment = new SwitchYardDeployment(deploymentUnit, metaData.geSwitchYardModel());
        SwitchYardService container = new SwitchYardService(deployment);
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();
        final ServiceName switchyardServiceName = deploymentUnit.getServiceName().append(SwitchYardService.SERVICE_NAME);
        final ServiceBuilder<SwitchYardDeployment> switchyardServiceBuilder = serviceTarget.addService(switchyardServiceName, container);

        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        if (moduleDescription != null) {
            container.getNamespaceSelector().setValue(new ImmediateValue<NamespaceContextSelector>(moduleDescription.getNamespaceContextSelector()));
        }

        // Only add a dependency on the Weld BeanManager if the deployment has beans (i.e. Weld Metadata)...
        if (WeldDeploymentMarker.isPartOfWeldDeployment(deploymentUnit)) {
            final ServiceName beanManagerServiceName = deploymentUnit.getServiceName().append(BeanManagerService.NAME);
            switchyardServiceBuilder.addDependency(beanManagerServiceName);
        }

        switchyardServiceBuilder.setInitialMode(Mode.ACTIVE);
        switchyardServiceBuilder.install();

        // we need the module's class loader in order to properly parse the
        // configuration
        final Module module = deploymentUnit.getAttachment(Attachments.MODULE);
        ClassLoader origCL = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(module.getClassLoader());
            registerContents(deploymentUnit, phaseContext.getServiceRegistry(), metaData);
        } catch (Exception e) {
            LOG.error("Error populating admin model.", e);
        } finally {
            Thread.currentThread().setContextClassLoader(origCL);
        }

        processManagement(deploymentUnit, switchyardServiceName);
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {
        // TODO: unregister the services
    }

    private void processManagement(DeploymentUnit deploymentUnit, ServiceName switchyardServiceName) {
        deploymentUnit.createDeploymentSubModel(SwitchYardExtension.SUBSYSTEM_NAME,
                PathElement.pathElement("service-name", switchyardServiceName.getCanonicalName()));
    }

    private void registerContents(DeploymentUnit deploymentUnit, ServiceRegistry serviceRegistry,
            SwitchYardMetaData metaData) {
        final ServiceController<?> controller = serviceRegistry.getRequiredService(SwitchYardAdminService.SERVICE_NAME);
        BaseSwitchYard switchYard = BaseSwitchYard.class.cast(controller.getService().getValue());

        SwitchYardModel switchYardModel = metaData.geSwitchYardModel();

        QName applicationName = switchYardModel.getQName();
        if (applicationName == null) {
            applicationName = new QName(deploymentUnit.getName());
        }
        BaseApplication application = new BaseApplication(applicationName);

        CompositeModel composite = switchYardModel.getComposite();
        List<CompositeServiceModel> compositeServices = composite.getServices();
        List<Service> services = new ArrayList<Service>(compositeServices.size());
        for (CompositeServiceModel compositeService : compositeServices) {
            final QName name = compositeService.getQName();
            final String interfaceName = getServiceInterface(compositeService);
            final Component implementation = switchYard.findComponent(compositeService.getComponent()
                    .getImplementation().getType());
            final List<Component> gateways = new ArrayList<Component>();
            for (BindingModel binding : compositeService.getBindings()) {
                gateways.add(switchYard.findComponent(binding.getType()));
            }
            services.add(new BaseService(name, interfaceName, application, implementation, gateways));
        }
        application.setServices(services);
        switchYard.addApplication(application);
    }
    
    private String getServiceInterface(CompositeServiceModel compositeService) {
        InterfaceModel interfaceModel = compositeService.getInterface();
        if (interfaceModel != null) {
            return interfaceModel.getInterface();
        }
        return compositeService.getComponentService().getInterface().getInterface();
    }

}
