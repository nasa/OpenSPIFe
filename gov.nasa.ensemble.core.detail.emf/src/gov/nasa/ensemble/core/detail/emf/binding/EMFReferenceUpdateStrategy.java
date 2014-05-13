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
package gov.nasa.ensemble.core.detail.emf.binding;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * PROVISIONAL
 * This API is subject to arbitrary change, including renaming or removal.
 */
@SuppressWarnings("restriction")
public class EMFReferenceUpdateStrategy extends UpdateValueStrategy
{
  private final List<?> values;
  private final ILabelProvider labelProvider;
  
  public EMFReferenceUpdateStrategy(int updatePolicy, List<?> values, ILabelProvider labelProvider)
  {
    super(true, updatePolicy);
    this.values = values;
    this.labelProvider = labelProvider;
  }
  
  @Override
  protected IConverter createConverter(Object fromType, Object toType)
  {
    if (fromType == String.class)
    {
      if (toType instanceof EAttribute)
      {
        final EAttribute eAttribute = (EAttribute)toType;
        final EDataType eDataType = eAttribute.getEAttributeType();
        final EFactory eFactory = eDataType.getEPackage().getEFactoryInstance();
        return
          new Converter(fromType, toType)
          {
            @Override
			public Object convert(Object fromObject)
            {
              String value = fromObject == null ? null : fromObject.toString();
              if (eAttribute.isMany())
              {
                List<Object> result = new ArrayList<Object>();
                if (value != null)
                {
                  for (String element : value.split(" "))
                  {
                    result.add(eFactory.createFromString(eDataType, element));
                  }
                }
                return result;
              }
              else
              {
                return eFactory.createFromString(eDataType, value);
              }
            }
          };
      } else if (toType instanceof EReference) {
    	  return new Converter(fromType, toType)
          {
            @Override
			public Object convert(Object fromObject)
            {
              String value = fromObject == null ? null : fromObject.toString();
              for (Object o : values) {
            	  String valueString = labelProvider.getText(o);
            	  if (valueString.equals(value)) {
            		  return o;
            	  }
              }
              return null;
            }
          };
      }
    }
    else if (toType == String.class)
    {
      if (fromType instanceof EAttribute)
      {
        final EAttribute eAttribute = (EAttribute)fromType;
        final EDataType eDataType = eAttribute.getEAttributeType();
        final EFactory eFactory = eDataType.getEPackage().getEFactoryInstance();
        return
          new Converter(fromType, toType)
          {
            @Override
			public Object convert(Object fromObject)
            {
              if (eAttribute.isMany())
              {
                StringBuilder result = new StringBuilder();
                for (Object value : (List<?>)fromObject)
                {
                  if (result.length() == 0)
                  {
                    result.append(' ');
                  }
                  result.append(eFactory.convertToString(eDataType, value));
                }
                return result.toString();
              }
              else
              {
            	  if (!values.contains(fromObject)) // See HACK below
            		  return null;
            	  return eFactory.convertToString(eDataType, fromObject);
              }
            }
          };
      } else if (fromType instanceof EReference) {
    	  return
          new Converter(fromType, toType)
          {
            @Override
			public Object convert(Object fromObject)
            {
              return labelProvider.getText(fromObject);
            }
          };
      }
    }
    return super.createConverter(fromType, toType);
  }
}

