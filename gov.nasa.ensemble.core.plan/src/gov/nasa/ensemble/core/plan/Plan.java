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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;

/** 
 * Represents an activity plan.
*/
public class Plan implements Externalizable, IAdaptable, HPlanElement {

	public static final String ATTRIBUTE_NEEDS_SAVE_AS = "ensemble_needs_save_as";
	// name of the transient property that indicates what shared mode this plan is in 
	public static final String SHARED_MODE = "__SHARED_MODE";
	
    private final Map<Class, Object> adapterRegistry = new HashMap<Class, Object>();

	private Map<Class, int[]> locks = new LinkedHashMap<Class, int[]>();
	private Map<Class, IMember> members = new LinkedHashMap<Class, IMember>();

	// Fields    

	private transient HashMap<String, Object> transientProperties = null;

	private EPlan ePlan;

    /** 
     * Constructor called from readExternal
     */
    public Plan() {
    	super();
    }

    public Plan(EPlan plan) {
    	init(plan);
    }
    
	protected void init(EPlan ePlan) {
		this.ePlan = ePlan;
		WrapperUtils.initHibernateMember(this, ePlan);
	}
	
	@Override
	public Object getAdapter(Class type) {
		Object eAdapter = ePlan.getAdapter(type);
		if (eAdapter != null) {
			return eAdapter;
		}
		if (EObject.class.isAssignableFrom(type)) {
			return ePlan;
		}
		if (adapterRegistry.containsKey(type)) {
			return adapterRegistry.get(type);
		}
		Object adapter = Platform.getAdapterManager().getAdapter(this, type);
		adapterRegistry.put(type, adapter);
		return adapter;
	}
	
	public void addTransientProperty(String s, Object o) {
		if (transientProperties == null) {
			transientProperties = new HashMap<String, Object>();
		}
		transientProperties.put(s,o);
	}
	
	public Object getTransientProperty(String s) {
		if (transientProperties == null) {
			return null;
		}
		return transientProperties.get(s);
	}
	
	public void removeTransientProperty(String s) {
		if (transientProperties != null) {
			transientProperties.remove(s);
		}
	}
	
    /**
	 * EMF: Clear the EMF undo stack?
	 */
    public void dispose() {
    	if (getTransientProperty(SHARED_MODE) != null)
    		return;
    	adapterRegistry.clear();
		List<IMember> disposableMembers = new ArrayList<IMember>();
		synchronized (members) {
			disposableMembers.addAll(members.values());
			members.clear();
		}
		for (IMember member : disposableMembers) {
			try {
				if (member != null) {
					member.dispose();
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				Logger logger = Logger.getLogger(Plan.class);
				logger.error("IMember.dispose()", t);
			}
		}
    }
	
	/**
	 * Return all members of the type supplied
	 * @param <T>
	 * @param klass
	 * @return a set of all the members of the type supplied
	 */
    @SuppressWarnings("unchecked")
	public <T extends IMember> T getMember(Class<T> klass) {
		T member = (T)members.get(klass);
		if (member == null) {
			Set<Class> dependencyClasses = MemberRegistry.getInstance().getDependenyClasses(klass);
			if (dependencyClasses != null) {
		    	for (Class dependentClass : dependencyClasses) {
		    		try {
		    			getMember(dependentClass); // initialize
		    		} catch (Exception e) {
		    			LogUtil.error("creating "+dependentClass, e);
		    		}
		    	}
			}
			int[] lock = locks.get(klass);
			if (lock == null) {
				synchronized (locks) {
					lock = locks.get(klass);
					if (lock == null) {
						locks.put(klass, lock = new int[0]);
					}
				}
			}
			synchronized (lock) {
				member = (T)members.get(klass);
				if (member == null) {
					IMemberFactory<T> factory = MemberRegistry.getInstance().getFactory(klass);
					try {
						member = factory.getMember(ePlan);
					} catch (Exception e) {
						Logger logger = Logger.getLogger(Plan.class);
						logger.error("getMember", e);
						return null;
					}
					if (members.put(klass, member) != null) {
						LogUtil.error("doubly created member for class: " + klass);
					}
				}
			}
		}
		return member;
	}

	/**
	 * Writes this to an externalized object.
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		WrapperUtils.writeExternal(out, ePlan);
	}
	
	/**
	 * @throws ClassNotFoundException  
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		EObject eObject = WrapperUtils.readExternal(in);
		if (eObject instanceof EPlan) {
			init((EPlan)eObject);
		}
	}

	@Override
    public String toString() {
    	return ePlan.getName();
    }

}
