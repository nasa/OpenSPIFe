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
package gov.nasa.ensemble.emf.patch;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeFactory;
import org.eclipse.emf.ecore.change.ChangeKind;
import org.eclipse.emf.ecore.change.ChangePackage;
import org.eclipse.emf.ecore.change.ListChange;
import org.eclipse.emf.ecore.change.impl.EObjectToChangesMapEntryImpl;
import org.eclipse.emf.ecore.change.impl.FeatureChangeImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

public class PatchBuilderFeatureChange extends FeatureChangeImpl {
	
	private int aggregatingSize;
	
	public PatchBuilderFeatureChange() {
		super();
	}
	
	public PatchBuilderFeatureChange(EStructuralFeature feature, Object oldValue, boolean isSet) {
		super(feature, oldValue, isSet);
		if (feature.isMany() && oldValue instanceof Collection) {
			aggregatingSize = ((Collection) oldValue).size();
		}
	}
	
	protected void createListChanges(EList<?> oldList, EList<?> newList) {
		int index = 0;
		int newListDelta = 0;
		int oldListDelta = 0;
		for (Object object : newList) {
			if (index < oldList.size()) {
				Object x = oldList.get(index);
				if (ECollections.indexOf(newList, x, 0) == -1) {
					int position = index+newListDelta-oldListDelta;
					createRemoveListChange(position);
					oldListDelta++;
				}
			}
			int indexInOldList = ECollections.indexOf(oldList, object, 0);
			if (indexInOldList == -1) {
				createAddListChange(feature, object, index);
				newListDelta++;
			} else {
				int offsetIndex = indexInOldList+newListDelta-oldListDelta;
				if (index < offsetIndex) {
					createMoveListChange(offsetIndex, index);
					if (offsetIndex - index > 1) {
						newListDelta++;
					}
				} 
			}
			index++;
		}
		for (int i = index; i < oldList.size(); i++) {
			Object element = oldList.get(i);
			if (ECollections.indexOf(newList, element, 0) == -1) {
				createRemoveListChange(i-oldListDelta);
				oldListDelta++;
			}
		}
	}
	
	protected ListChange createAddListChange(EStructuralFeature feature, Object newObject, int index) {
		if (index < 0 || index > aggregatingSize) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		ListChange listChange = createListChange(ChangeKind.ADD_LITERAL, index);
		listChange.setFeature(feature);
		if (feature instanceof EAttribute)  {
			listChange.getValues().add(newObject);
		} else {
			listChange.getReferenceValues().add((EObject) newObject);
		}
		aggregatingSize++;
		getListChanges().add(listChange);
		return listChange;
	}
	
	protected ListChange createRemoveListChange(int index) {
		ListChange listChange = createListChange(ChangeKind.REMOVE_LITERAL, index);
		getListChanges().add(listChange);
		aggregatingSize--;
		return listChange;
	}
	
	protected ListChange createMoveListChange(int index, int toIndex) {
		ListChange listChange = createListChange(ChangeKind.MOVE_LITERAL, index);
		listChange.setMoveToIndex(toIndex);
		getListChanges().add(listChange);
		return listChange;
	}
	
	protected static ListChange createListChange(ChangeKind kind, int index) {
		ListChange listChange = ChangeFactory.eINSTANCE.createListChange();
		listChange.setKind(kind);
		listChange.setIndex(index);
		return listChange;
	}
	
	public int getAggregatingSize() {
		return aggregatingSize;
	}
	
	@Override
	public Object getValue() {
		EStructuralFeature feature = getFeature();
		if (feature.getUpperBound() != 1) {
			if (eContainer() instanceof EObjectToChangesMapEntryImpl) {
				value = getListValue((EList<?>)((EObjectToChangesMapEntryImpl)eContainer()).getTypedKey().eGet(feature));
			}
		}
		else if (feature instanceof EReference) {
			return getReferenceValue();
		}
		else if (value == null) { // feature is instance of EAttribute 
			EDataType type = (EDataType)feature.getEType();
			value = EcoreUtil.createFromString(type, valueString);
		}
		return value;    
	}
	
	@Override
	protected EList<?> getListValue(EList<?> originalList) {
		if (isSet() && getFeature().getUpperBound() != 1) {
			EList<Object> changedList =  new BasicEList<Object>(originalList);
			apply(changedList);
			value = changedList; // cache result
			return changedList;
		}
		return ECollections.EMPTY_ELIST;
	}
	
	@Override
	public EStructuralFeature getFeature() {
		if (feature == null) {
			if (featureName != null && eContainer() instanceof EObjectToChangesMapEntryImpl) {
				EObject key = ((EObjectToChangesMapEntryImpl)eContainer()).getTypedKey();
				if (key != null) {
					feature = key.eClass().getEStructuralFeature(featureName);
				}
			}
		} else if ((eFlags & EPROXY_FEATURECHANGE) !=0) {
			EStructuralFeature oldFeature = feature;
			feature = (EStructuralFeature)EcoreUtil.resolve(feature, this);
			if (feature != oldFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChangePackage.FEATURE_CHANGE__FEATURE, oldFeature, feature));
			}
			eFlags &= ~ EPROXY_FEATURECHANGE;
		}
		return feature;
	}
	
	 @Override
	protected void apply(EObject originalObject, boolean reverse)
	  {
	    EStructuralFeature.Internal internalFeature = (EStructuralFeature.Internal)getFeature();
	    if (internalFeature != null && internalFeature.isChangeable() && !internalFeature.isContainer())
	    {
	      if (!isSet())
	      {
	        if (reverse && internalFeature.isMany())
	        {
	          ListChange listChange = createListChange(getListChanges(), ChangeKind.ADD_LITERAL, 0);
	          if (internalFeature.getEOpposite() != null || internalFeature.isContainment())
	          {
	            listChange.getValues().addAll((EList<?>)value);
	          }
	          else
	          {
	            listChange.getValues().addAll((EList<?>)originalObject.eGet(internalFeature));
	          }
	        }
	        originalObject.eUnset(internalFeature);
	      }
	      else if (internalFeature.isMany())
	      {
	        if (listChanges != null)
	        {
	          if (internalFeature.isFeatureMap())
	          {
	            FeatureMap.Internal result = (FeatureMap.Internal)originalObject.eGet(internalFeature);
	            @SuppressWarnings("unchecked") EList<FeatureMap.Entry.Internal> prototype = (EList<FeatureMap.Entry.Internal>)getValue();
	            EList<FeatureMap.Entry> featureMapEntryList = new BasicEList<FeatureMap.Entry>(prototype.size());
	            for (FeatureMap.Entry.Internal entry : prototype)
	            {
	              Object entryValue = entry.getValue();
	              // Create a proper feature map entry.
	              //
	              entry = entry.createEntry(entryValue);
	              featureMapEntryList.add(entry);
	              EStructuralFeature entryFeature = entry.getEStructuralFeature();
	              if (!FeatureMapUtil.isMany(originalObject, entry.getEStructuralFeature()))
	              {
	                for (int i = 0, size = result.size(); i < size; ++i)
	                {
	                  if (result.getEStructuralFeature(i) == entryFeature && !(entryValue == null ? result.getValue(i) == null : entryValue.equals(result.getValue(i))))
	                  {
	                    result.set(i, entry);
	                  }
	                }
	              }
	            }
	            ECollections.setEList(result, featureMapEntryList);
	          }
	          else if (internalFeature.isFeatureMap() || internalFeature.getEOpposite() != null || internalFeature.isContainment())
	          {
	            // Bidirectional references need to use this less efficient approach because some
	            //  or all of the changes may already have been made from the other end.
	            //
	            @SuppressWarnings("unchecked") EList<Object> result = (EList<Object>)originalObject.eGet(internalFeature);
	            @SuppressWarnings("unchecked") EList<Object> prototype = (EList<Object>)value;
	            ECollections.setEList(result, prototype);
	          }
	          else
	          {
	            @SuppressWarnings("unchecked") EList<Object> applyToList = (EList<Object>)originalObject.eGet(internalFeature);
	            if (reverse)
	            {
	              applyAndReverse(applyToList);
	            }
	            else
	            {
	              apply(applyToList);
	            }
	          }
	          
	          if (reverse)
	          {
	            ECollections.reverse(getListChanges());
	          }
	        }
	      }
	      else 
	      {
	        originalObject.eSet(internalFeature, getValue());
	      }
	      
	      if (reverse)
	      {
	        setSet(newIsSet);
	        setValue(newValue);
	        
	        if (!isSet())
	        {
	          getListChanges().clear();
	        }        
	      }
	    }    
	  }
	 
	 @Override
	 public String toString() {
		 StringBuffer buffer = new StringBuffer();
		 buffer.append("PatchBuilderFeatureChange - ");
		 buffer.append("feature: " + feature.getName() + " ");
		 buffer.append("value: " + value);
		 return buffer.toString();
	 }

}
