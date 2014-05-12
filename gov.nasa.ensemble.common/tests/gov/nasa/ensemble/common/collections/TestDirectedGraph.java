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
package gov.nasa.ensemble.common.collections;

import junit.framework.TestCase;

import org.junit.Test;


public class TestDirectedGraph extends TestCase {
	
	@Test
	public void testNonCyclical () {
		DirectedGraph<String> graph = new DirectedGraph<String>();
		graph.addDependency("a1", "A");
		graph.addDependency("a2", "A");
		graph.addDependency("a3", "A");
		graph.addDependency("a4", "A");
		graph.addDependency("b1", "B");
		graph.addDependency("b2", "B");
		assertEquals("[A]", graph.getTransitiveDependencies("a1").toString());
		assertEquals("[A]", graph.getTransitiveDependencies("a2").toString());
		assertEquals("[A]", graph.getTransitiveDependencies("a3").toString());
		assertEquals("[A]", graph.getTransitiveDependencies("a4").toString());
		assertEquals("[B]", graph.getTransitiveDependencies("b1").toString());
		assertEquals("[B]", graph.getTransitiveDependencies("b2").toString());
		graph.addDependency("A", "B");
		assertEquals("[A, B]", graph.getTransitiveDependencies("a1").toString());
		assertEquals("[A, B]", graph.getTransitiveDependencies("a2").toString());
		assertEquals("[A, B]", graph.getTransitiveDependencies("a3").toString());
		assertEquals("[A, B]", graph.getTransitiveDependencies("a4").toString());
		assertEquals("[B]", graph.getTransitiveDependencies("b1").toString());
		assertEquals("[B]", graph.getTransitiveDependencies("b2").toString());
		assertFalse(graph.hasCyclicDependencies());
	}
	
	@Test
	public void testNonCyclical2 () {
		DirectedGraph<String> graph = new DirectedGraph<String>();
		graph.addDependency("X", "x1");
		graph.addDependency("X", "x2");
		graph.addDependency("X", "x3");
		graph.addDependency("X", "x4");
		graph.addDependency("Y", "y1");
		graph.addDependency("Y", "y2");
		assertEquals("[x1, x2, x3, x4]", graph.getTransitiveDependencies("X").toString());
		assertEquals("[y1, y2]", graph.getTransitiveDependencies("Y").toString());
		graph.addDependency("y1", "x4");
		assertEquals("[x1, x2, x3, x4]", graph.getTransitiveDependencies("X").toString());
		assertEquals("[x4, y1, y2]", graph.getTransitiveDependencies("Y").toString());
		graph.addDependency("x4", "x3");
		graph.addDependency("x3", "x2");
		graph.addDependency("x2", "x1");
		assertEquals("[x1, x2, x3, x4, y1, y2]", graph.getTransitiveDependencies("Y").toString());
		assertFalse(graph.hasCyclicDependencies());
	}
	
	@Test
	public void testCyclicalChicken () {
		DirectedGraph<String> graph = new DirectedGraph<String>();
		graph.addDependency("chicken", "egg");
		graph.addDependency("egg", "chicken");
		assertTrue(graph.hasCyclicDependencies());
	}

	@Test
	public void testCyclicalDearLiza () {
		DirectedGraph<String> graph = new DirectedGraph<String>();
		graph.addDependency("wash dishes", "fill bucket");
		graph.addDependency("fill bucket", "fix bucket");
		graph.addDependency("fix bucket", "short straw available");
		graph.addDependency("short straw available", "cut straw");
		graph.addDependency("cut straw", "sharpen ax");
		graph.addDependency("sharpen ax", "wet stone");
		assertFalse(graph.hasCyclicDependencies());
		assertFalse(graph.hasCyclicDependencies("wash dishes"));
		assertFalse(graph.hasCyclicDependencies("fill bucket"));
		graph.addDependency("wet stone", "fill bucket");
		assertTrue(graph.hasCyclicDependencies());
		assertFalse(graph.hasCyclicDependencies("wash dishes"));
		assertTrue(graph.hasCyclicDependencies("fill bucket"));
	}


}
