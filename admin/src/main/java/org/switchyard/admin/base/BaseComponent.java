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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.switchyard.admin.Component;
import org.switchyard.admin.ComponentReference;
import org.switchyard.admin.ComponentService;

/**
 * BaseComponent
 * 
 * Basic implementation for Component.
 */
public class BaseComponent implements Component {

    private final QName _name;
    private final String _type;
    private final ComponentService _service;
    private final Map<QName, ComponentReference> _references;
    private final Map<String, String> _properties;

    /**
     * Create a new BaseComponent.
     * 
     * @param name the name of the component.
     * @param type the type of the component.
     * @param service the service provided by this component (may be null).
     * @param references the references required by this component.
     * @param properties the configuration properties of this component.
     */
    public BaseComponent(QName name, String type, ComponentService service, Map<QName, ComponentReference> references, Map<String, String> properties) {
        _name = name;
        _type = type;
        _service = service;
        _references = references;
        _properties = properties;
    }

    @Override
    public Map<String, String> getProperties() {
        return _properties;
    }

    @Override
    public QName getName() {
        return _name;
    }

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public ComponentService getService() {
        return _service;
    }

    @Override
    public List<ComponentReference> getReferences() {
        if (_references == null) {
            return Collections.emptyList();
        }
        return new ArrayList<ComponentReference>(_references.values());
    }

    @Override
    public ComponentReference getReference(QName componentReferenceName) {
        if (_references == null) {
            return null;
        }
        return _references.get(componentReferenceName);
    }
}
