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

package org.switchyard.admin.base;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.ServiceDomain;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.deploy.ServiceDomainManager;
import org.switchyard.deploy.event.ApplicationDeployedEvent;
import org.switchyard.deploy.event.ApplicationUndeployedEvent;
import org.switchyard.deploy.internal.Deployment;

public class SwitchYardBuilderTest extends SwitchYardBuilderTestBase {

    public SwitchYardBuilderTest() throws Exception {
        super();
    }

	@Test
    public void testApplication() {
        Assert.assertEquals(1, _switchYard.getApplications().size());
    }

    @Test
    public void testService() {
        Assert.assertEquals(1, _switchYard.getApplication(TEST_APP).getServices().size());
    }

    @Test
    public void testComponent() {
        Assert.assertEquals(2, _switchYard.getApplication(TEST_APP).getComponentServices().size());
    }

    @Test
    public void testNoComponentService() throws Exception{
        Deployment testDeployment = new MockDeployment(
                new ModelPuller<SwitchYardModel>().pull("switchyard_multiappweb.xml", getClass()), 
                QName.valueOf("{urn:switchyard-quickstart-demo:multiapp:0.1.0}web"), _domainManager);
        _domainManager.getEventManager().publish(new ApplicationDeployedEvent(testDeployment));
        Assert.assertEquals(2, _builder.getSwitchYard().getApplications().size());
        _domainManager.getEventManager().publish(new ApplicationUndeployedEvent(testDeployment));
        Assert.assertEquals(1, _builder.getSwitchYard().getApplications().size());
    }

}

class MockDeployment extends Deployment {
    private ServiceDomain _domain;
    private QName _name;
    
    MockDeployment(SwitchYardModel config, QName name, ServiceDomainManager sdm) {
        super(config);
        _name = name;
        _domain = sdm.createDomain(name, config);
    }
    
    @Override
    public QName getName() {
        return _name;
    }

    @Override
    public ServiceDomain getDomain() {
        return _domain;
    }
    
}
