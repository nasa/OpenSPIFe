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
package gov.nasa.arc.spife.core.plan.advisor.resources;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.AmountConstraint;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.jscience.physics.amount.Amount;

public class AmountConstraintResource extends ResourceImpl {

	private static final int INDEX_KEY 		= 0;
	private static final int INDEX_MIN 		= 1;
	private static final int INDEX_MAX 		= 2;
	private static final int INDEX_WAIVED 	= 3;
	
	public AmountConstraintResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		List<AmountConstraint> constraints = new ArrayList<AmountConstraint>();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String line = in.readLine();
		while (line != null) {
			String[] tokens = CommonUtils.COMMA_PATTERN.split(line);
			
			// Key
			//
			String key = (String) getValue(tokens, INDEX_KEY);
			
			// Min
			//
			Amount min = (Amount) getValue(tokens, INDEX_MIN);
			
			// Max
			//
			Amount max = (Amount) getValue(tokens, INDEX_MAX);
			
			// Waived
			//
			Boolean waived = (Boolean) getValue(tokens, INDEX_WAIVED);
			
			AmountConstraint constraint = JScienceFactory.eINSTANCE.createAmountConstraint();
			constraint.setKey(key);
			constraint.setExtent(new AmountExtent(min, max));
			constraint.setWaived(waived);
			constraints.add(constraint);
			
			line = in.readLine();
		}
		getContents().addAll(constraints);
	}

	/**
	 * @throws IOException  
	 */
	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		PrintWriter writer = new PrintWriter(outputStream);
		for (EObject o : getContents()) {
			AmountConstraint c = (AmountConstraint) o;
			String key = c.getKey();
			AmountExtent<?> extent = c.getExtent();
			Amount<?> min = extent.getMin();
			Amount<?> max = extent.getMax();
			boolean waived = c.isWaived();
			StringBuffer buffer = new StringBuffer(key)
				.append(",").append(JScienceFactory.eINSTANCE.convertToString(JSciencePackage.Literals.EAMOUNT, min))
				.append(",").append(JScienceFactory.eINSTANCE.convertToString(JSciencePackage.Literals.EAMOUNT, max))
				.append(",").append(waived);
			writer.println(buffer.toString());	
		}
		writer.close();
	}
	
	private Object getValue(String[] tokens, int index) {
		String stringValue = null;
		if (tokens.length > index) {
			stringValue = tokens[index].trim();
		}
		switch (index) {
			case INDEX_KEY:
				return stringValue;
			case INDEX_MIN:
			case INDEX_MAX:
				try {
					return EnsembleAmountFormat.INSTANCE.parseAmount(stringValue, Unit.ONE);
				} catch (Exception e) {
					LogUtil.error(e);
				}
				return null;
			case INDEX_WAIVED:
				return stringValue == null ? Boolean.FALSE : Boolean.parseBoolean(stringValue);
		}
		return null;
	}
	
}
