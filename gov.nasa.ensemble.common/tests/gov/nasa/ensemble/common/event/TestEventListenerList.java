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
/*
 * Created on Aug 5, 2004
 */
package gov.nasa.ensemble.common.event;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import junit.framework.TestCase;

/**
 * @author mpowell
 */
public class TestEventListenerList extends TestCase {
	EventListenerList ell;
	PropertyChangeListener foo;
	PropertyChangeListener bar;
	PropertyChangeListener bing;
	PropertyChangeListener dah;
	
	public TestEventListenerList (String name)
	{
		super(name);
	}
	
	@Override
	public void setUp()
	{
		ell = new EventListenerList();
		foo = new PropertyChangeListener () {
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
				// stub
			}
		};
		
		bar = new PropertyChangeListener () {
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
				// stub
			}
		};
		
		bing = new PropertyChangeListener () {
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
				// stub
			}
		};
		
		dah = new PropertyChangeListener () {
			@Override
			public void propertyChange(PropertyChangeEvent e)
			{
				// stub
			}
		};
	}
	
	public void testCount()
	{
		assertTrue(ell.getListenerCount() == 0);
	}
	
	public void testAdd()
	{
		ell.add(foo);
		assertTrue(ell.getListenerCount() == 1);
		ell.add(bar);
		assertTrue(ell.getListenerCount() == 2);
		EventListener[] listeners = ell.getListenerList();
		assertNotNull(listeners);
		assertTrue(listeners.length == 2);
		assertTrue(listeners[0] == foo);
		assertTrue(listeners[1] == bar);
	}
	
	public void testRemove()
	{
		ell.add(foo);
		ell.add(bar);
		assertTrue(ell.remove(foo));
		assertTrue(ell.getListenerCount() == 1);
		assertTrue(ell.remove(bar));
		assertTrue(ell.getListenerCount() == 0);
		assertTrue(!ell.remove(bar));
		EventListener[] listeners = ell.getListenerList();
		assertNotNull(listeners);
		assertTrue(listeners.length == 0);
		
		// make sure we can add stuff again
		testAdd();
	}
	
	public void testAddIndex()
	{
		ell.add(foo);
		
		// to the front
		ell.add(bar, 0);
		assertTrue(ell.getListenerCount() == 2);
		EventListener[] listeners = ell.getListenerList();
		assertNotNull(listeners);
		assertTrue(listeners.length == 2);
		assertTrue(listeners[0] == bar);
		assertTrue(listeners[1] == foo);
		
		// to the end
		ell.add(bing, 2);
		assertTrue(ell.getListenerCount() == 3);
		listeners = ell.getListenerList();
		assertNotNull(listeners);
		assertTrue(listeners.length == 3);
		assertTrue(listeners[0] == bar);
		assertTrue(listeners[1] == foo);
		assertTrue(listeners[2] == bing);
		
		// to the middle
		ell.add(dah, 1);
		assertTrue(ell.getListenerCount() == 4);
		listeners = ell.getListenerList();
		assertNotNull(listeners);
		assertTrue(listeners.length == 4);
		assertTrue(listeners[0] == bar);
		assertTrue(listeners[1] == dah);
		assertTrue(listeners[2] == foo);
		assertTrue(listeners[3] == bing);
	}
	
}
