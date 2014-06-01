/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage;
import gov.nasa.ensemble.core.plan.resources.dependency.Graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Graph</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.impl.GraphImpl#getDependencies <em>Dependencies</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GraphImpl extends MinimalEObjectImpl.Container implements Graph {
	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<Dependency> dependencies;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GraphImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DependencyPackage.Literals.GRAPH;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<Dependency> getDependencies() {
		if (dependencies == null) {
			dependencies = new EObjectContainmentEList<Dependency>(Dependency.class, this, DependencyPackage.GRAPH__DEPENDENCIES);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DependencyPackage.GRAPH__DEPENDENCIES:
				return ((InternalEList<?>)getDependencies()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DependencyPackage.GRAPH__DEPENDENCIES:
				return getDependencies();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DependencyPackage.GRAPH__DEPENDENCIES:
				getDependencies().clear();
				getDependencies().addAll((Collection<? extends Dependency>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DependencyPackage.GRAPH__DEPENDENCIES:
				getDependencies().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DependencyPackage.GRAPH__DEPENDENCIES:
				return dependencies != null && !dependencies.isEmpty();
		}
		return super.eIsSet(featureID);
	}
	
	public static String toShortXML(Dependency d, String tab)
	{
		StringBuffer buf = new StringBuffer();

		buf.append(tab).append("<Dependency ")
		   .append("type=\"").append(d.getClass().getSimpleName()).append("\" ")
		   .append("name=\"").append(d.getName()).append("\"/>\n");
		
		return buf.toString();				
	}
	
	public static String toXML(Dependency d, String tab, Set<Dependency> visited)
	{
		visited.add(d);
		
		StringBuffer buf = new StringBuffer();
		String childTab=tab+"    ";
		buf.append(tab).append("<Dependency ")
		   .append("type=\"").append(d.getClass().getSimpleName()).append("\" ")
		   .append("name=\"").append(d.getName()).append("\">\n");
		buf.append(childTab).append("<Value>").append(d.getValue()).append("</Value>\n");
		
		buf.append(childTab).append("<Previous>\n");
		for (Dependency prev : d.getPrevious())
			buf.append(toShortXML(prev,childTab+"    "));
		buf.append(childTab).append("</Previous>\n");

		buf.append(childTab).append("<Next>\n");
		for (Dependency next : d.getNext())
			if (visited.contains(next)) {
				buf.append(childTab+"    ").append("<!-- CIRCULAR DEPENDENCY -->\n");
				buf.append(toShortXML(next,childTab+"    "));
			}
			else
				buf.append(toXML(next,childTab+"    ", visited));
		buf.append(childTab).append("</Next>\n");

		buf.append(tab).append("</Dependency>\n");
		return buf.toString();		
	}
	
	@Override
	public String toXML()
	{
		StringBuffer buf = new StringBuffer();
		
		Set<Dependency> visited = new HashSet<Dependency>();
		
		buf.append("\n<Graph>\n");
		for (Dependency d : getDependencies())
			if (d.getPrevious().isEmpty()) // Only print root nodes
				buf.append(toXML(d,"    ",visited));
		buf.append("</Graph>\n");
		
		return buf.toString();
	}

} //GraphImpl
