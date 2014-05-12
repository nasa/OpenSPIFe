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
package gov.nasa.ensemble.tests.core.plan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gov.nasa.ensemble.common.ui.type.editor.SoftOutOfBoundsException;
import gov.nasa.ensemble.core.activityDictionary.ParameterDef;
import gov.nasa.ensemble.core.plan.ChoicesStringifier;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EChoice;
import gov.nasa.ensemble.dictionary.impl.DictionaryFactoryImpl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestChoicesStringifier {

	DictionaryFactory factory = DictionaryFactoryImpl.init();
	
	private ParameterDef createListOfOptionsParameterDef() {
		ParameterDef param = new ParameterDef();
		param.setName("testparam");
		param.setType("integer");
		List<EChoice> choices = new ArrayList<EChoice>();
		EChoice choice = factory.createEChoice();
		choice.setValue("1");
		choices.add(choice);
		choice = factory.createEChoice();
		choice.setValue("2");
		choices.add(choice);
		param.getChoices().addAll(choices);
		return param;
	}
	
	private ParameterDef createRangeParameterDef() {
		ParameterDef param = new ParameterDef();
		param.setName("testparam");
		param.setType("integer");
		List<EChoice> choices = new ArrayList<EChoice>();
		EChoice choice = factory.createEChoice();
		choice.setMinimum("1");
		choice.setMaximum("10");
		choices.add(choice);
		choice = factory.createEChoice();
		choice.setMinimum("20");
		choice.setMaximum("30");
		choices.add(choice);
		choice = factory.createEChoice();
		choice.setValue("42");
		choices.add(choice);
		param.getChoices().addAll(choices);
		return param;
	}
	
	private ParameterDef createMultipleOfParameterDef() {
		ParameterDef param = new ParameterDef();
		param.setName("testparam");
		param.setType("integer");
		List<EChoice> choices = new ArrayList<EChoice>();
		EChoice choice = factory.createEChoice();
		choice.setMultipleOf("10");
		choices.add(choice);
		choice = factory.createEChoice();
		param.getChoices().addAll(choices);
		return param;
	}

	@Test
	public void testGetDisplayString() {
		ParameterDef param = createListOfOptionsParameterDef();
		ChoicesStringifier<Integer> stringifier = new ChoicesStringifier<Integer>(param);
		assertEquals("42", stringifier.getDisplayString(42));
		assertEquals("-1", stringifier.getDisplayString(-1));
	}


	@Test
	public void testGetJavaObjectForListOfOptions() throws ParseException {
		ParameterDef param = createListOfOptionsParameterDef();
		ChoicesStringifier<Integer> stringifier = new ChoicesStringifier<Integer>(param);
		Integer one = stringifier.getJavaObject("1", null);
		assertEquals(one.intValue(), 1);
		
		try {		
			stringifier.getJavaObject("9", null);
		} catch (SoftOutOfBoundsException e) {
			return; // pass
		}
		fail("Did not catch out-of-bounds number.");
	}

	@Test
	public void testGetJavaObjectForRange() throws ParseException {
		ParameterDef param = createRangeParameterDef();
		ChoicesStringifier<Integer> stringifier = new ChoicesStringifier<Integer>(param);
		assertEquals(1, stringifier.getJavaObject("1", null).intValue());
		assertEquals(5, stringifier.getJavaObject("5", null).intValue());
		assertEquals(10, stringifier.getJavaObject("10", null).intValue());
		assertEquals(22, stringifier.getJavaObject("22", null).intValue());
		assertEquals(42, stringifier.getJavaObject("42", null).intValue());

		try {		
			stringifier.getJavaObject("11", null);
		} catch (SoftOutOfBoundsException e) {
			return; // pass
		}
		fail("Did not catch out-of-bounds number.");

	}
	
	@Test
	public void testGetJavaObjectForMultipleOf() throws ParseException {
		ParameterDef param = createMultipleOfParameterDef();
		ChoicesStringifier<Integer> stringifier = new ChoicesStringifier<Integer>(param);
		assertEquals(0, stringifier.getJavaObject("0", null).intValue());
		assertEquals(10, stringifier.getJavaObject("10", null).intValue());
		try {		
			stringifier.getJavaObject("11", null);
		} catch (IllegalArgumentException e) {
			return; // pass
		}
		fail("Did not catch out-of-bounds number.");
			
	}
}
