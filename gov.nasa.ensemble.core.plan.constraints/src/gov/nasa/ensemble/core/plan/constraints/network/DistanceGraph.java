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
package gov.nasa.ensemble.core.plan.constraints.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;

/**
 * @class  DistanceGraph
 * @author Paul H. Morris (with mods by Conor McGann)
 * @date   Mon Dec 27 2004

 * @brief  A distance graph is a directed graph that has a
 *         weight associated with each edge. A directed graph
 *         is a graph whose edges are ordered pairs of nodes. That is
 *         each edge can be followed from one node to another node.
 *
 *  Class defines a primitive distance graph mechanism that implements
 *  standard algorithms such as Bellman-Ford, Dijstra etc. for answering
 *  queries such as finding the shortest path between nodes. 
 *
 *  The single-source shortest-path problem is the problem of finding the
 *  shortest paths from a specifc source nodes to every over in a weighted
 *  directed graph. Dijkstra's algorithum solves this if all weights are nonnegative.
 *  The Bellman-Ford algorithum handles any weights.
 *
 * @ingroup TemporalNetwork
 */
@SuppressWarnings("all")
public class DistanceGraph<Time extends Long> {

	@SuppressWarnings("unchecked")
	private final Time getTime(Long i) {
		return (Time)i;
	}

	// We require the magnitude bounds to be symmetric under negation.
	// This is so that the legal values will be closed under negation.
	// The following achieves that without provoking over or under flow.

	public static final long TIME_MAX = 
		(Long.MAX_VALUE + Long.MIN_VALUE <= 0 ?
				Long.MAX_VALUE : -Long.MIN_VALUE);

	public static final long TIME_MIN = -TIME_MAX; // Underflow has been protected against.

	// Following gives granularity (min separation) of Time type.
	// Note that x <= y is equivalent to x < (y + TIME_TICK).
	// This is used in isDistanceLessThanOrEqual.
	public static final int TIME_TICK = 1;

	// Following used to prevent overflow and underflow of temporal
	// distance.  Note that adding an acceptable edge length to an
	// acceptable distance is guaranteed not to produce
	// overflow/underflow.  This allows us to only check the distance
	// values that get stored in nodes rather than all distance values
	// that arise in propagation (most of which are discarded).

	public static final long MAX_LENGTH = (TIME_MAX/8);       // Largest length allowed for edge
	public static final long MIN_LENGTH = (TIME_MIN/8);       // Smallest length allowed for edge

	public static final long POS_INFINITY = MAX_LENGTH + 1;
	public static final long NEG_INFINITY = MIN_LENGTH - 1;
	public static final long MAX_DISTANCE = POS_INFINITY - 1;   // Largest propagated distance for node
	public static final long MIN_DISTANCE = NEG_INFINITY + 1;   // Smallest propagated distance for node

	private static final boolean EUROPA_FAST = true;
	private static final boolean CHECK_NODE_GRAPH_MEMBERSHIP = false; // whether or not to check node/graph members

	// Global value overridden only for Rax-derived system test.
	public static boolean IS_OK_TO_REMOVE_TEMPORAL_CONSTRAINT_TWICE = false;

	private final Set<Dedge> edges = new HashSet<Dedge>();
	private final Dqueue dqueue = new Dqueue();
	private int dijkstraGeneration;

	protected final Vector<Dnode> nodes = new Vector<Dnode>();
	protected final BucketQueue bqueue = new BucketQueue(100);
	protected final List<Dedge> edgeNogoodList = new ArrayList<Dedge>();

	/**
	 * @brief Create a new node and add it to the network
	 * @return the new network node
	 */
	public final Dnode createNode() {
		Dnode node = makeNode();
		node.potential = getTime(0L);
		nodes.add(node);
		return node;
	}

	/**
	 * @brief Remove node from network
	 *
	 * Detach the node's edges from all other nodes, and erase the edges.
	 *          Cleanup the node's fields. Remove the node from the network's global list of edges.
	 *          Call the node destructor (takes care of the edge arrays).
	 * @param node node to remove from the network
	 */
	public final void deleteNode(Dnode node) {
		if (!isValid(node)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}
		for (int i=0; i < node.outCount; i++) {
			Dedge edge = node.outArray[i];
			edge.to.inCount = detachEdge (edge.to.inArray, edge.to.inCount, edge);
			eraseEdge(edge);
		}
		for (int j=0; j < node.inCount; j++) {
			Dedge edge = node.inArray[j];
			edge.from.outCount = detachEdge (edge.from.outArray, edge.from.outCount, edge);
			eraseEdge(edge);
		}
		node.inCount = node.outCount = 0;
		node.potential = getTime(99L);  // A clue for debugging purposes
		deleteIfEqual(nodes, node);
		node.dispose();
	}

	/**
	 * @brief Add edge to the network 
	 * @param from start of the edge
	 * @param end end of the edge
	 * @param length length of the edge
	 */
	public final void addEdgeSpec(Dnode from, Dnode to, Time length) {
		if (length > MAX_LENGTH || length < MIN_LENGTH) {
			throw new IllegalArgumentException("addEdgeSpec with length too large or too small");
		}
		Dedge edge = findEdge (from,to);
		if (edge == null)
			edge = createEdge(from,to,length);
		edge.lengthSpecs.add(length);
		if (length < edge.length)
			edge.length = length;
	}

	/**
	 * @brief  Remove the constraint length from the edge lengthSpecs.
	 *  If no more constraints, delete the edge, else update
	 *  the edge length as the min of the lengthSpecs.
	 * @param from start of the edge
	 * @param to end of the edge
	 * @param length length of the edge
	 */
	public final void removeEdgeSpec(Dnode from, Dnode to, Time length) {
		if (!isValid(from) || !isValid(to)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}
		if (length > MAX_LENGTH || length < MIN_LENGTH) {
			throw new IllegalArgumentException("removeEdgeSpec with length too large or too small");
		}
		Dedge edge = findEdge (from,to);
		if (edge == null || !edge.isValid()) {
			return;
			// FIXME:  Commented out due to SPF-9297:  Cut makes the id invalid and undoing the cut leaves it invalid.
//			throw new IllegalStateException("Removing spec from non-existent edge");
		}
		Vector<Time> lengthSpecs = edge.lengthSpecs;

		deleteIfEqual(lengthSpecs, length);

		if (lengthSpecs.isEmpty())
			deleteEdge(edge);
		else {
			Time min = lengthSpecs.firstElement();
			for (Time current : lengthSpecs) {
				if (current < min) {
					min = current;
				}
			}
			edge.length = min;
		}  
	}

	/**
	 * @brief Constructor
	 */
	public DistanceGraph () {
		dijkstraGeneration = 0;
	}

	/**
	 * @brief Destructor
	 */
	public void dispose() {
		edges.clear();
		nodes.clear();
	}

	/**
	 * @brief Textbook Bellman Ford algorithm propagation to determine network consistency.
	 *
	 * Broken down into initialization + incremental propagation.
	 * Propagates "potentials" from initial zero values in all nodes,
	 * as in Johnson's algorithm.
	 * We also allow for subclasses to use specialized cycle detection
	 * by providing a (here no-op) cycle detection check.
	 * Changed the propagation to be more Dijkstra-like in that
	 *    it uses a PriorityQueue ordered by the amount of change in potential
	 *    from the previous value.  This cuts down on the amount of wasted
	 *    propagation of values that are later superseded.  Also rewrote
	 *    full-prop version as entirely separate function.
	 */
	public final boolean bellmanFord() {
		BucketQueue queue = initializeBqueue();
		for (Dnode node : nodes) {
			Time oldPotential = node.potential;
			// Cache beginning potential in distance field.
			node.distance = oldPotential;
			node.potential = getTime(0L);
			node.depth = 0;
			// Use diff from oldPotential as priority ordering.  This
			// minimizes the amount of wasted superseded propagations.
			queue.insertInQueue (node, getTime(-oldPotential));
		}
		int BFbound = nodes.size();
		while (true) {
			Dnode node = queue.popMinFromQueue();
			if (node == null)
				break;
			// Cache node vars -- Chucko 22 Apr 2002
			int nodeOutCount = node.outCount;
			if (nodeOutCount > 0) {
				Dedge[] nodeOutArray = node.outArray;
				Time nodePotential = node.potential;
				for (int i=0; i< nodeOutCount; i++) {
					Dedge edge = nodeOutArray[i];
					if (!edge.isValid()) {
						throw new IllegalStateException("bad edge");
					}
					Dnode next = edge.to;
					Time potential = getTime(nodePotential + edge.length);
					if (potential < next.potential) {
						next.potential = potential;
						next.predecessor = edge;
						handleNodeUpdate(next);
						// In following cycleDetected() is a no-op hook to allow
						// specialized cycle detectors to be defined in subclasses
						// ** Try to keep results in registers for speed.
						// Chucko 23 Apr 2002
						if ((next.depth = node.depth + 1) > BFbound  // Exceeded BF limit.
								|| cycleDetected (next)) {
							updateNogoodList(next);
							return false;
						}
						Time oldPotential = next.distance; // See earlier in function
						// Use diff from oldPotential as priority ordering.  This
						// minimizes the amount of wasted superseded propagations.
						queue.insertInQueue (next, getTime(potential - oldPotential));
					}
				}
			}
		}
		return true;
	}


	/**
	 * @brief incremental Bellman Ford propagation algorithm
	 */

	public final boolean incBellmanFord() {
		int BFbound = nodes.size();
		//Dqueue* queue = dqueue;
		BucketQueue queue = bqueue;

		preventGenerationOverflow();
		++dijkstraGeneration;

		while (true) {
			Dnode node = queue.popMinFromQueue();
			if (node == null)
				break;
			// Cache node vars -- Chucko 22 Apr 2002
			int nodeOutCount = node.outCount;
			if (nodeOutCount > 0) {
				Dedge[] nodeOutArray = node.outArray;
				Time nodePotential = node.potential;
				for (int i=0; i< nodeOutCount; i++) {
					Dedge edge = nodeOutArray[i];
					if (!edge.isValid()) {
						throw new IllegalStateException("bad edge");
					}
					Dnode next = edge.to;
					Time potential = getTime(nodePotential + edge.length);
					if (potential > MAX_DISTANCE || potential < MIN_DISTANCE) {
						throw new ArithmeticException("Potential over(under)flow during distance propagation");
					}
					if (potential < next.potential) {
						// Cache the beginning potential in next.distance
						if (next.generation < this.dijkstraGeneration) {
							next.generation = this.dijkstraGeneration;
							next.distance = next.potential;
						}
						Time oldPotential = next.distance;

						next.potential = potential;
						next.predecessor = edge;
						handleNodeUpdate(next);

						// In following cycleDetected() is a no-op hook to allow
						// specialized cycle detectors to be defined in subclasses
						// ** Try to keep results in registers for speed.
						// Chucko 23 Apr 2002
						if ((next.depth = node.depth + 1) > BFbound  // Exceeded BF limit.
								|| cycleDetected (next)) {
							updateNogoodList(next);
							return false;
						}
						// Give priority to "stronger" propagations.  This minimizes
						// the amount of wasted superseded propagations.
						queue.insertInQueue (next, getTime(potential - oldPotential));
					}
				}
			}
		}
		return true;
	}

	/**
	 * @brief Standard algorithm for finding the shortest paths from a single node to all other nodes in a 
	 *        weighted graph.
	 * @param source start node
	 * @param destination terminal node (optional)
	 */
	public final void dijkstra(Dnode source) {
		dijkstra(source, null);
	}
	public final void dijkstra(Dnode source, Dnode destination) {
		if (!isValid(source)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}

		// PHM 05/16/2007 The previous isValid(destination) check was
		// mistaken; a null destination was intended to be allowed; in that
		// case dijkstra computes the distance to ALL nodes in the graph.
		// (See DistanceGraph.hh, which has destination = noId() as default!)

		if ((destination != null) && !isValid(destination)) {
			throw new IllegalArgumentException("node supplied is not defined in this graph");
		}

		//debugMsg("DistanceGraph:dijkstra", "from " << source << " to " << destination);
		source.distance = getTime(0L);
		source.depth=0;
		preventGenerationOverflow();
		int generation = ++(this.dijkstraGeneration);
		source.generation = generation;
		BucketQueue queue = initializeBqueue();
		queue.insertInQueue (source);
		int BFbound = Integer.MAX_VALUE;
		if (!EUROPA_FAST ) {
			BFbound = this.nodes.size();
		}
		while (true) {
			Dnode node = queue.popMinFromQueue();
			//debugMsg("DistanceGraph:dijkstra", "Visiting " << node);
			if (node == null || node == destination)
				return;
			// Cache node vars -- Chucko 22 Apr 2002
			int nodeOutCount = node.outCount;
			if (nodeOutCount > 0) {
				Dedge[] nodeOutArray = node.outArray;
				Time nodeDistance = node.distance;
				for (int i=0; i< nodeOutCount; i++) {
					Dedge edge = nodeOutArray[i];
					Dnode next = edge.to;
					Time newDistance = getTime(nodeDistance + edge.length);
					/*
			condDebugMsg(next.generation >= generation, 
				     "DistanceGraph:dijkstra", next.generation << " <= " << generation << " for " << next);

		      condDebugMsg(newDistance >= next.distance, 
				     "DistanceGraph:dijkstra", newDistance << " >= " <<  next.distance << " for " << next);
					 */
					if (next.generation < generation || newDistance < next.distance) {
						next.generation = generation;
						if (newDistance > MAX_DISTANCE || newDistance < MIN_DISTANCE) {
							throw new ArithmeticException("Potential over(under)flow during distance propagation");
						}
						// Next check is a failsafe to prevent infinite propagation.
						if ((next.depth = node.depth + 1) > BFbound) {
							throw new IllegalStateException("Dijkstra propagation in inconsistent network");
						}
						next.distance = newDistance;
						next.predecessor = edge;
						queue.insertInQueue (next);
						//debugMsg("DistanceGraph:dijkstra", "New distance of " << newDistance << " through node " << next);
						handleNodeUpdate(next);
					}
				}
			}
		}
	}

//	/**
//	 * @brief Incremental version of Dijkstra's algorithum
//	 */
//	public final void incDijkstra(Int generation, Dnode destination);

	/**
	 * @brief compute distance from node to all other nodes in network
	 * @param node start node.
	 */
	public final Time getDistance(Dnode node) {
		if (!isValid(node)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}
		if (node.generation == this.dijkstraGeneration)
			return node.distance;
		else
			return getTime(POS_INFINITY);
	}

	/**
	 * @brief Determine if distance between nodes is less than bound
	 * @param src start node.
	 * @param targ end node.
	 * @param bound time bound
	 * @return true iff distance (src, targ) < bound
	 */
	public final boolean isDistanceLessThan (Dnode src, Dnode targ, Time bound) {
		if (!isValid(src) || !isValid(targ)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}

		// Depth-first simulated propagation from src to targ as if
		// a -bound constraint had been added from targ to src.
		// This would cause a cycle iff isDistanceLessThan is true,
		// so we need only check if the propagation reaches targ.
		preventNodeMarkOverflow();
		unmarkAll();
		Time newPotential = getTime(targ.potential - bound);
		if (bound == 1) {
			// In this case, the call is always from isSlotDurationZero().
			// For efficiency, we use the approximation of only checking
			// paths with all zero links.
			return (isAllZeroPropagationPath(src, targ, newPotential));
		}
		return isPropagationPath(src, targ, newPotential);
	}


	/**
	 * @brief test if node is a member of the network.
	 * @return true iff node is valid, false otherwise.
	 */
	public final boolean isValid(Dnode node) {
		return (!isCheckNodeGraphMembership() || hasNode(node));
	}

	/**
	 * @brief Produce a string representation of the network
	 * @return String containing the edges in the network.
	 */
	@Override
	public final String toString() {
		StringBuffer sstr = new StringBuffer();
		for (Dedge edge : edges) {
			sstr.append(edge.from + " " + edge.to + " " + edge.length + "\n");
		}
		return sstr.toString();
	}

	/**
	 * @brief Identify edge instance that connects nodes
	 * @param from start of edge
	 * @param to end of edge
	 * @return return the edge that connects from to to if such an edge exists.
	 *         returns a null edge otherwise.
	 */
	protected final Dedge findEdge(Dnode from, Dnode to) {
		if (!isValid(from) || !isValid(to)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}
		// Cache node vars -- Chucko 22 Apr 2002 
		int fromOutCount = from.outCount; 
		if (fromOutCount > 0) {
			/*
		    DedgeId* fromOutArray = from.outArray;
		    for (Int i=0; i < fromOutCount; i++) {
		      DedgeId edge = fromOutArray[i];
		      if (to == edge.to)
			return edge;
		    }
			 */
			// PHM 06/20/2007 Speedup by using map instead.
			return from.edgemap.get(to);
		}
		return null;
	}

	/**
	 * Note: because nodes is just a vector, this is SLOW.
	 *       so we avoid doing the check when in optimized mode.
	 * 
	 * @brief test if network contain node
	 * @param node to test membership
	 * @return true iff network contains node, false otherwise.
	 */
	protected final boolean hasNode(Dnode node) {
		for (Dnode dn : nodes) {
			if (dn == node) {
				return true;
			}
		}
		return false;
	}

	protected final Dqueue initializeDqueue() {
		preventNodeMarkOverflow();
		Dqueue queue = this.dqueue;
		queue.reset();
		return queue;
	}

	protected final BucketQueue initializeBqueue() {
		preventNodeMarkOverflow();
		BucketQueue queue = this.bqueue;
		queue.reset();
		return queue;
	}

	/**
	 * @brief If a subclass does not need EdgeSpec maintenance, it can call
	 * createEdge directly.  (The DispatchGraph uses this feature.)
	 * @param from from node
	 * @param to to node
	 * @param time duration of edge 
	 */
	protected final Dedge createEdge(Dnode from, Dnode to, Time length) {
		if (!isValid(from) || !isValid(to)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}
		Dedge edge = new Dedge();
		edge.from = from;
		edge.to = to;
		edge.length = length;
		this.edges.add(edge);
		{
			AttachEdgeArguments arguments = new AttachEdgeArguments();
			arguments.edgeArray = from.outArray;
			arguments.size = from.outArraySize;
			arguments.count = from.outCount;
			attachEdge (arguments, edge);
			from.outArray = arguments.edgeArray;
			from.outArraySize = arguments.size;
			from.outCount = arguments.count;
		}
		{
			AttachEdgeArguments arguments = new AttachEdgeArguments();
			arguments.edgeArray = to.inArray;
			arguments.size = to.inArraySize;
			arguments.count = to.inCount;
			attachEdge (arguments, edge);
			to.inArray = arguments.edgeArray;
			to.inArraySize = arguments.size;
			to.inCount = arguments.count;
		}
		from.edgemap.put(to, edge);
		return edge;
	}

	/**
	 * @brief virtual method to allows specialized Dnodes in subclasses
	 */
	protected Dnode makeNode() {
		return new Dnode();
	}

	/** 
	 * @brief Virtual method allows subclasses to provide specialized methods for
	 * detecting cycles in graphs.
	 * return false always. Specialization should return true if cycle is detected.
	 */
	protected boolean cycleDetected (Dnode next) { 
		// for subclass
		return false;
	}

	/**
	 * @brief Allow subclass to take action when a node is updated
	 * @param node node updated
	 */
	protected void handleNodeUpdate(Dnode node) {
		// for subclass
	}

	private final void deleteEdge(Dedge edge) {
		edge.from.outCount = detachEdge (edge.from.outArray, edge.from.outCount, edge);
		edge.to.inCount = detachEdge (edge.to.inArray, edge.to.inCount, edge);
		edge.from.edgemap.remove(edge.to);
		eraseEdge(edge);
	}
	private final void eraseEdge(Dedge edge) {
		//deleteIfEqual(edges, edge);
		edges.remove(edge);
		edge.from = null;
		edge.to = null;
		edge.length = getTime(99L);  // A clue for debugging purposes
	}
	
	private final void preventNodeMarkOverflow() {
		// Unlikely to happen, but just in case...
		if (markGlobal == Integer.MAX_VALUE) {
			// Roll all marks over to zero.
			for (Dnode node : nodes) {
				node.markLocal = 0;
			}
			markGlobal = 0;
		}
	}
	
	private final void preventGenerationOverflow() {
		// Unlikely to happen, but just in case...
		if (this.dijkstraGeneration == Integer.MAX_VALUE) {
			// Roll all generations over to zero.
			for (Dnode node : this.nodes) {
				node.generation = 0;
			}
			this.dijkstraGeneration = 0;
		}
	}
	
	private final void updateNogoodList(Dnode start) {
		preventNodeMarkOverflow();
		unmarkAll();
		Dnode node = start;
		// Search for predecessor cycle
		while (! node.isMarked()) {
			node.mark ();
			Dedge predEdge = node.predecessor;
			if (!predEdge.isValid()) {
				throw new IllegalStateException("Broken predecessor chain");
			}
			node = predEdge.from;
		}
		// Now trace out the cycle into edgeNogoodList
		edgeNogoodList.clear();
		Dnode node1 = node;
		Dedge edge;
		do {
			edge = node1.predecessor;
			edgeNogoodList.add(edge);
			node1 = edge.from;
		}
		while (node1 != node);
	}
	
	private final boolean isAllZeroPropagationPath(Dnode node, Dnode targ, Time potential) {
		if (!isValid(node) || !isValid(targ)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}
		if (potential >= node.potential)  // propagation is ineffective
			return false;
		if (node == targ)
			return true;
		if (node.isMarked())
			return false;
		node.mark();
		// Cache node vars -- Chucko 22 Apr 2002
		int nodeOutCount = node.outCount;
		if (nodeOutCount > 0) {
			Dedge[] nodeOutArray = node.outArray;
			for (int i=0; i< nodeOutCount; i++) {
				Dedge edge = nodeOutArray[i];
				Time length = getTime(edge.length);
				if (length == 0)
					if (isAllZeroPropagationPath(edge.to, targ, potential))
						return true;
			}
		}
		return false;
	}
	private final boolean isPropagationPath(Dnode src, Dnode targ, Time potential) {
		if (!isValid(src) || !isValid(targ)) {
			throw new IllegalArgumentException("node is not defined in this graph");
		}
		// Even though this seems like a full search, it is actually an
		// approximation because the marking scheme could possibly prevent a
		// new propagation with a smaller potential across nodes that have
		// previously been propagated across.  Would need a Dijkstra-like
		// priority queue to do a full search, but this seems a good enough
		// approximation to satisfy the calls from the zigzag check.
		if (potential >= src.potential)  // propagation is ineffective
			return false;
		src.mark();
		src.distance = potential;
		Dnode propQ = src; 
		propQ.link = null;
		while (propQ != null) {
			Dnode node = propQ; propQ = propQ.link;
			// Cache node vars -- Chucko 22 Apr 2002
			int nodeOutCount = node.outCount;
			Dedge[] nodeOutArray = node.outArray;
			// We iterate downwards to simulate the behavior of the previous
			// recursive version of this function (to satisfy make tests).
			for (int i=nodeOutCount-1; i>=0 ; i--) {
				Dedge edge = nodeOutArray[i];
				Dnode next = edge.to;
				if (next.isMarked())
					continue;
				Time newPotential = getTime(node.distance + edge.length);
				if (newPotential >= next.potential)  // propagation is ineffective
					continue;  // Don't mark---may be later effective propagation
				if (next == targ)
					return true;
				next.mark();
				next.distance = newPotential;
				next.link = propQ; propQ = next;
			}
		}
		return false;
	}

	/*
	 * Utility functions
	 */

	public final class AttachEdgeArguments {
		public Dedge[] edgeArray;
		public int size;
		public int count;
	}
	
	private final void attachEdge (AttachEdgeArguments arguments, Dedge edge)
	{
		Dedge[] edgeArray = arguments.edgeArray;
		int size = arguments.size;
		int count = arguments.count;
		if (count > size) {
			throw new IllegalStateException("Corrupted edge-array in TemporalNetwork");
		}

		if (count == size) {
			// Grow edge-array
			if (size < 1)
				size = 1;
			else
				size = 2*size;
		}
		Dedge[] newEdgeArray = new Dedge[size];
		System.arraycopy(edgeArray, 0, newEdgeArray, 0, count);
		edgeArray = newEdgeArray;
		edgeArray[count++] = edge;
		arguments.edgeArray = edgeArray;
		arguments.size = size;
		arguments.count = count;
	}

	private final int detachEdge (Dedge[] edgeArray, int count, Dedge edge)
	{
		int i = 0;
		while (i < count && edgeArray[i] != edge)
			i++;
		if (i == count && IS_OK_TO_REMOVE_TEMPORAL_CONSTRAINT_TWICE) {
			throw new IllegalArgumentException("Trying to delete edge not in edge-array");
		}
		for (--count; i < count; i++)
			edgeArray[i] = edgeArray[i + 1];
		return count;
	}

	private void deleteIfEqual(Vector<Time> lengthSpecs, Time length) {
		ListIterator<Time> iterator = lengthSpecs.listIterator();
		while (iterator.hasNext()) {
			Time next = iterator.next();
			if (next.equals(length)) {
				iterator.remove();
				return;
			}
		}
	}

	private void deleteIfEqual(Vector<Dnode> nodes, Dnode node) {
		ListIterator<Dnode> iterator = nodes.listIterator();
		while (iterator.hasNext()) {
			if (iterator.next() == node) {
				iterator.remove();
				return;
			}
		}
	}

	/**
	 * @class  Dnode
	 * @author Paul H. Morris (with mods by Conor McGann)
	 * @date   Mon Dec 27 2004
	 * @brief  Node in a distance graph. 
	 * @ingroup TemporalNetwork
	*/
	private static int markGlobal = 0;       // Global obsolescence number for marks.
	private final static void unmarkAll() {
		markGlobal++;
	}
	/**
	 * @return the checkNodeGraphMembership
	 */
	public static boolean isCheckNodeGraphMembership() {
		return CHECK_NODE_GRAPH_MEMBERSHIP;
	}

	public class Dnode extends Entity implements Comparable<Dnode> {

		private int node_counter = 0;
		public final int m_id = node_counter++;
		
		public Dnode link; // For creating linked-list of nodes (for Dqueue)

		protected Dedge predecessor; // For reconstructing negative cycles.
		
		private int markLocal;               // Used for obsoletable marking of nodes.
		protected int generation;     // Used for obsoleting Dijkstra-calculated distances.
		
		protected Dedge[] inArray;
		protected int inArraySize;
		protected int inCount;
		protected Dedge[] outArray;
		protected int outArraySize;
		protected int outCount;
		protected Map<Dnode, Dedge> edgemap = new HashMap<Dnode, Dedge>();
		protected Time distance;      // Distance from any source of propagation.
		protected Time potential;     // Distance from Johnson-type external source.
		protected int depth;  // Depth of propagation for testing against the BF limit.
		protected Time key; // Used for priority ordering */

		public Dnode() {
	      inArray  = new Dedge[] {};
	      inArraySize = 0;
	      inCount = 0;
	      outArray  = new Dedge[] {};
	      outArraySize = 0;
	      outCount = 0;
	      distance = getTime(0L);
	      potential = getTime(0L);
	      depth = 0;
	      key = getTime(0L);
	      link = null;
	      predecessor = null;
	      markLocal = 0;
	      generation = 0;
	  }
		
		protected final void handleDiscard() {
			if (inArray != null) {
				inArray = null;
			}
			if (outArray != null) {
				outArray = null;
			}
		}
		
		public final Time getTimeKey() { return getTime(distance - potential); }  // Used in Dijkstra
		
		// Following marking method allows easy global unmarking.
		// Mark is used to tell whether node is in queue.
		// Note that an aborted propagation (due to detection of
		// inconsistency) may leave some nodes still marked, so
		// simple flipping of a boolean is not enough.

		// WARNING: If there are multiple distance graphs, care must be
		// taken not to allow the global mark to cause interactions.
		// Since the mark only increases, this won't happen provided
		// procedures that rely on the marks, such as propagation, are
		// not interleaved between different distance graphs.  If such
		// interleaving is desired, then this marking mechanism must be
		// replaced by one that is graph-specific.

		public final void mark () {
			markLocal = markGlobal;
		}
		public final boolean isMarked() {
			return (markLocal == markGlobal);
		}
		public final void unmark () {
			markLocal = markGlobal - 1;
		}

		  /* Key accessors */
		public final Time getKey() {return key;}
		public final void setKey(Time t) {key = t;} 

		public final Dedge getPredecessor() { return predecessor;}
		public final void setPredecessor(Dedge edge) {predecessor = edge;}

		/**
		 * Modeled after Integer.compareTo(..)
		 */
		@Override
		public final int compareTo(Dnode other) {
			int thisVal = this.m_id;
			int anotherVal = other.m_id;
			return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
		}

	}

	/**
	 * @class  Dedge
	 * @author Paul H. Morris (with mods by Conor McGann)
	 * @date   Mon Dec 27 2004
	 * @brief  Directed Edge in distance graph
	 * @ingroup TemporalNetwork
	*/
	public static class Dedge<Time extends Long> extends Entity {

		protected Vector<Time> lengthSpecs = new Vector<Time>();
		
		public DistanceGraph.Dnode to;
		public DistanceGraph.Dnode from;
		public Time length;

		public Dedge() {
			// nothing to do
		}
		
	}

	/**
	 * @class  BucketQueue
	 * @author Paul H. Morris (with mods by Conor McGann)
	 * @date   Mon Dec 27 2004
	 * @brief  Utility class. An ordered linked-list of buckets
	 * designed to give an efficient implementation of Dijkstra's algorithum for
	 * finding the shortest path between nodes (where all weights are non negative).
	 * @ingroup TemporalNetwork
	 */
	public class BucketQueue {

		//  typedef std::priority_queue<DnodeId, std::vector<DnodeId>, EntityComparator<DnodeId> > DnodePriorityQueue;

		private PriorityQueue<Dnode> buckets;

		public BucketQueue(int n) {
			buckets = new PriorityQueue<Dnode>();
		}

		public final void dispose() {
			buckets = null;
		}

		/**
		 * @brief delete any buckets in the queue.
		 */
		public final void reset() {
			buckets = new PriorityQueue<Dnode>();
			unmarkAll();
		}

		/**
		 * @brief  Search through nodes in distance order, ignoring unmarked nodes.
		 * Return first marked node found.  Pop all the nodes until
		 * it is found.
		 */ 
		public final Dnode popMinFromQueue() {
			Dnode node;
			while ((node = buckets.poll()) != null) {
				if (node.isMarked()){
					node.unmark();
					return node;
				}
			}
			return null;
		}

		/** 
		 * @brief insert node into queue
		 * @param node node to insert
		 * @param key key attached to node
		 */
		public final void insertInQueue(Dnode node, Time key) {
			if (node == null)
				return;

			if (node.isMarked() && node.getKey() > -key ){
				return;
			}

			node.setKey(getTime(-key)); // Reverse since we want effective lowest priority first
			node.mark();
			this.buckets.add(node);

			//debugMsg("BucketQueue:insertInQueue", "Enqueueing " << node << " with key " << -key);
		}

		/**
		 * @brief insert node into queue. Use (distance - potential) as key
		 * @param node node to insert.
		 */
		public final void insertInQueue(Dnode node) {
			if (node == null)
				return;

			insertInQueue(node, getTime(node.distance - node.potential));
		}

//		/**
//		 * @brief test if bucket is empty
//		 * @return true iff bucket is empty, false otherwise. 
//		 */
//		public final boolean isEmpty();

		public class Bucket {

			public Time key;
			
			protected Bucket(Time distance) {
				key = distance;
			}
			
		}

	}

	public class Dqueue {

		private Dnode first;
		private Dnode last;

		/**
		 * @brief remove all nodes from the queue
		 */
		public final void reset() {
			this.first = null;
			unmarkAll();
		}

		/**
		 * @brief insert node into queue
		 * @param node node to insert
		 */
		public final void addToQueue (Dnode node) {
			// FIFO queue add to last.
			if (!node.isMarked()) {
				// If not already in queue...
				if (this.first == null)  // queue is empty
					this.first = node;
				else                      // splice in node
					this.last.link = node;
				node.link = null;
				this.last = node;
				node.mark();
			}
		}

		/**
		 * @brief pop first element from queue following first in first out strategy (FIFO).
		 * @return head of the queue
		 */
		public final Dnode popFromQueue () {
			// FIFO queue pop from first.
			Dnode node = this.first;
			if (node == null)
				return node;
			this.first = node.link;
			node.unmark();
			return node;
		}

		/**
		 * @brief test if queue is empty
		 * @return true iff queue is empty, false otherwise.
		 */
		public final boolean isEmpty() {
			return (first == null);
		}

	}

}
