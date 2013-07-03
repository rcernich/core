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

package org.switchyard.admin;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Component
 * 
 * Represents a SwitchYard component within an application.
 */
public interface Component {
    /**
     * @return the name of this component.
     */
    QName getName();

    /**
     * @return implementation type, e.g. bean, bpm, etc.
     */
    String getType();

    /**
     * @return component properties.
     */
    Map<String,String> getProperties();

    
    /**
     * @return the service provided by this component.
     */
    ComponentService getService();

    /**
     * @return the references contained by this component.
     */
    List<ComponentReference> getReferences();

    /**
     * @param componentReferenceName the name of a reference required by
     *            this component.
     * @return the requested reference, may be null
     */
    ComponentReference getReference(QName componentReferenceName);
}
