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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * @class  TemporalNetwork
 * @author Paul H. Morris (with mods by Conor McGann)
 * @date   Mon Dec 27 2004
 * @brief  Implements a Simple Temporal Network (@ref stp "STN")  
 *
 *         Provides incremental access algorithms for a Simple Temporal
 *         Network (@ref stp "STN"). This is a sublass of Distance Graph as it adds the fields
 *         required to implement STN accessors. The propagation strategy is 
 *         to propergate added or narrowed constraints eagerly.  
 *         This allows an efficient specialized cycle detection method because any new
 *         inconsistency must involve the added constraint, so we need only
 *         check for an effective propagation back to the start.
 * @ingroup TemporalNetwork
 */
public class TemporalNetwork<Time extends Long> extends DistanceGraph<Long> {

	private static final long g_maxInt = Long.MAX_VALUE / 8;
	private static final long g_infiniteTime = Long.MAX_VALUE / 8;

	@SuppressWarnings("unchecked")
	final Time getTime(Long i) {
		return (Time)i;
	}

	private static final boolean NO_ERROR_CHECKS = false;
	private final Time MAX_INT = getTime(g_maxInt);
	/* package */ final Time MAX_FINITE_TIME = MAX_INT;
	/* package */ final Time MIN_FINITE_TIME = getTime(-MAX_INT);

	private boolean consistent;  
	private boolean hasDeletions;
	private int nodeCounter;

	/**
	 * @brief Used for specialized cycle detection
	 */
	private Timepoint incrementalSource;

	// The following are provided for backward compatibility with previous
	// TemporalNetwork. They are not used internally.  Only the ones currently used
	// externally have been defined.  See DistanceGraph.hh for the macros
	// POS_INFINITY, etc.  These are defined to be symmetric under
	// negation, so that NEG_INFINITY = -POS_INFINITY.  Also note that
	// POS_INFINITY is defined so POS_INFINITY + 1 does not overflow.

	// g_noTime() is never returned by this Temporal Network;
	// instead, errors are generated for non-meaningful calls,
	// and infinity is returned where appropriate.

	// N.B. The bounds of added constraints are limited to being between
	// MIN_LENGTH and MAX_LENGTH, which might have considerably smaller 
	// magnitude than g_maxFiniteTime().  Propagated distance values may range
	// up to g_maxFiniteTime(), however.  This is so the addition of a legal
	// length to a legal distance value can never overflow.

	/**
	 * @brief Tests if a time t is outside of the temporal bounds of the network.
	 * @param t the time you wish to query. 
	 * @return true iff t is outside of allowable bounds of the network.False otherwise.
	 */

	public final boolean isOutOfBoundsTime(Time t) {
		return (t < NEG_INFINITY || t > POS_INFINITY);
	}

	/**
	 * @brief Tests if a time t is within the temporal bounds of the network. 
	 * @param t the time you wish to query. 
	 * @return true iff t is within the allowable bounds of the network. False otherwise.
	 */ 
	public final boolean isFiniteTime(Time t) {
		return (t >= MIN_FINITE_TIME && t <= MAX_FINITE_TIME);
	}

	// End of compatibility definitions.

	/**
	 * @brief Accessor for the upper and lower bound times of a timepoint. The method
	 *        will enforce consistentey.
	 * @param id the timepoint for which you require this information.
	 * @param lb return result giving lower bound of id.
	 * @param ub return result giving upper bound of id.
	 */
	public final Bounds getTimepointBounds(Timepoint id) {
		if (!isConsistent()) {
			throw new IllegalStateException("TemporalNetwork: Getting bounds from inconsistent network");
		}
		if (!isValidId(id)) {
			throw new IllegalArgumentException("TemporalNetwork: Invalid timepoint identifier");
		}
		// We need to be up-to-date to get the bounds.  Because of eager
		// prop on consistent additions, we only need to prop if there are
		// deletions.
		isConsistent(); // Forces appropriate propagation.
		return new Bounds(id.lowerBound, id.upperBound);
	}

	/**
	 * @brief Accessor for the upper and lower bound times of a timepoint. The method
	 *        will <b>not</b>consistentey.
	 * @param id the timepoint for which you require this information.
	 * @param lb return result giving lower bound of id.
	 * @param ub return result giving upper bound of id.
	 */
	public final Bounds getLastTimepointBounds(Timepoint node) {
		if (!isValidId(node)) {
			throw new IllegalArgumentException("TemporalNetwork: Invalid timepoint identifier");
		}
		return new Bounds(node.lowerBound, node.upperBound);
	}

	/**
	 * @brief Accessor for the Lower Timepoint bound. The method
	 *        will enforce consistentey.
	 * @param id the timepoint for which you require this information.
	 * @return lower bound of timepoint.
	 */
	public final Time getLowerTimepointBound(Timepoint id) {
		return getTimepointBounds(id).lowerBound;
	}

	/**
	 * @brief Accessor for the Upper Timepoint bound. The method
	 *        will enforce consistentey.
	 * @param id the timepoint for which you require this information.
	 * @return upper bound of timepoint.
	 */
	public final Time getUpperTimepointBound(Timepoint id) {
		return getTimepointBounds(id).upperBound;
	}

	/**
	 * @brief Test if the STN is consistent
	 * @return true iff network is consistent. False otherwise.
	 */
	public final synchronized boolean isConsistent() {
		// We propagate additions eagerly so only deletions need a new
		// propagation here, and then only if network was inconsistent.
		// (Deletions from a consistent network cannot cause inconsistency.)
		// (Yes, but this overlooks deletions followed by additions, so..)

		// More efficient test: (hasDeletions && (!consistent || hasAdditions))
		// but need to set up hasAdditions cache.  For now, just propagate...

		if (this.hasDeletions)
			fullPropagate(); // Otherwise changes have been incrementally propagated

		return this.consistent;
	}

	/**
	 * @brief Calculate the temporal distance between two timepoints.
	 * @param src the start node in the network.
	 * @param targ the end node in the network.
	 * @param lb returns the lower bound of the distance between src and targ
	 * @param ub returns the upper bound of the distnace between src and targ
	 * @param exact if true use Dijkstra's algorithim to compute exact distnace. Approximate if false 
	 */
	public final Bounds calcDistanceBounds(Timepoint src, Timepoint targ) {
		return calcDistanceBounds(src, targ, true);
	}
	public final Bounds calcDistanceBounds(Timepoint src, Timepoint targ, boolean exact) {
		if (!isConsistent()) {
			throw new IllegalStateException("TemporalNetwork: Getting bounds from inconsistent network");
		}
		if (!isValidId(src)) {
			throw new IllegalArgumentException("TemporalNetwork: Invalid source timepoint identifier");
		}
		if (!isValidId(targ)) {
			throw new IllegalArgumentException("TemporalNetwork: Invalid target timepoint identifier");
		} 
		// Trying to simulate as best as possible the AKJ approximation
		if (exact == false) {
			Dedge forwardEdge = findEdge (src,targ);
			Dedge reverseEdge = findEdge (targ,src);
			// if (forwardEdge != nullptr || reverseEdge != nullptr) {
			Time ub, lb;
			if (forwardEdge != null)
				ub = getTime(forwardEdge.length);
			else
				ub = getTime(POS_INFINITY);
			if (reverseEdge != null)
				lb = getTime(-(reverseEdge.length));
			else
				lb = getTime(NEG_INFINITY);
			return new Bounds(lb, ub);
			// }
		}

		// Otherwise calculate from two single-source propagations
		Time ub, lb;
		dijkstra(src,targ);
		ub = getTime(getDistance(targ));
		dijkstra(targ,src);
		lb = getTime(-getDistance(src));
		return new Bounds(lb, ub);
	}

	/**
	 * @brief Calculate the (exact) temporal distance from one timepoint to others.  Much more efficient when many targs.
	 * @param src the start node in the network.
	 * @param targs the end nodes in the network.
	 * @param lbs returns the lower bounds of the distances
	 * @param ubs returns the upper bounds of the distances
	 */
	public final void calcDistanceBounds(Timepoint src,
			Vector<Timepoint> targs,
			Vector<Time> lbs, Vector<Time> ubs) {
		// Method: Calculate lower/upper bounds as if src was the origin.
		// Afterwards restore the proper bounds.  Requires only four
		// dijkstras instead of 2*n dijkstras.

		if (!isConsistent()) {
			throw new IllegalStateException("TemporalNetwork: calcDistanceBounds from inconsistent network");
		}

		propagateBoundsFrom(src);

		lbs.clear();
		ubs.clear();

		for (Timepoint node : targs) {
			lbs.add(node.lowerBound);
			ubs.add(node.upperBound);
		}

		propagateBoundsFrom(getOriginNode());
	}

	/**
	 * @brief Get the upperbound on the time of a temporal constraint.
	 * @param id temporal constraint of interest. 
	 * @result Upperbound time on constraint.
	 */
	public final Time getConstraintUpperBound(TemporalConstraint id) {
		if (id.isInvalid()) {
			throw new IllegalArgumentException("Cannot get scope of invalid constraint.");
		}
		return id.upperBound;
	}

	/**
	 * @brief Get the lowerbound on the time of a temporal constraint.
	 * @param id temporal constraint of interest. 
	 * @result Lowerbound time on constraint.
	 */
	public final Time getConstraintLowerBound(TemporalConstraint id) {
		if (id.isInvalid()) {
			throw new IllegalArgumentException("Cannot get scope of invalid constraint.");
		}
		return id.lowerBound;
	}

	/**
	 * @brief Identify the timepoints that mark the head and foot of a temporal constraint.
	 * @param constraint temporal constraint of interest.
	 * @param returns head of temporal constraint 
	 * @param returns foot of temporal constraint
	 */
	public final Scope getConstraintScope(TemporalConstraint constraint) {
		if (!constraint.isValid()) {
			throw new IllegalArgumentException(String.valueOf(constraint));
		}
		return new Scope(constraint.head, constraint.foot);
	}

	/**
	 * @brief Add temporal constraint to the network
	 * @param src start or head of the constraint
	 * @param targ finish or tail of the constraint 
	 * @param lb lower bound time
	 * @param ub upper bound time
	 * @param propagate iff true this constraint will be included in propagation. 
	 */
	public final TemporalConstraint addTemporalConstraint(Timepoint src, Timepoint targ,
			Time lb, Time ub) {
		return addTemporalConstraint(src, targ, lb, ub, true);
	}
	public final TemporalConstraint addTemporalConstraint(Timepoint src, Timepoint targ,
			Time lb, Time ub, boolean propagate) {
		checkBoundsValidity(lb, ub);
		if (!isValidId(src)) {
			throw new IllegalArgumentException("addTemporalConstraint:  Invalid source timepoint");
		}
		if (!isValidId(targ)) {
			throw new IllegalArgumentException("addTemporalConstraint:  Invalid target timepoint");
		}
		if (src == targ) {
			throw new IllegalArgumentException("addTemporalConstraint:  source and target are the same");
		}
//		maintainTEQ (lb,ub,src,targ);

		int edgeCount = 0;

		if (ub <= MAX_LENGTH) {
			addEdgeSpec(src, targ, ub);
			edgeCount++;
		}

		if (lb >= MIN_LENGTH) {
			edgeCount++;
			addEdgeSpec(targ, src, getTime(-lb));
		}

		TemporalConstraint spec = new TemporalConstraint(this, src, targ, lb, ub, edgeCount);

		m_constraints.add(spec);

		// As long as propagation is not turned off, we can process this constraint
		if (propagate) {
			incPropagate(src, targ);
		}

		return spec;
	}
	/**
	 * @brief Tighten the temporal constraint to new bounds iff they are tighter. 
	 * @param tcId Constraint to tighten
	 * @param newLb New lower bound 
	 * @param newUb New Upper bound
	 */
	public final void narrowTemporalConstraint(TemporalConstraint tcId, Time newLb, Time newUb) {
		if (!tcId.isValid()) {
			throw new IllegalArgumentException(String.valueOf(tcId));
		}
		checkBoundsValidity(newLb, newUb);
		if (!isValidId(tcId)) {
			throw new IllegalArgumentException("narrowTemporalConstraint:  Invalid TemporalConstraint");
		}
		TemporalConstraint spec = tcId;
		Time oldLb = spec.lowerBound;
		Time oldUb = spec.upperBound;

		if (newLb < oldLb || newUb > oldUb) {
			throw new IllegalArgumentException("narrowTemporalConstraint: new bounds must be tighter");
		}

		Timepoint src = spec.head;
		Timepoint targ = spec.foot;
//		maintainTEQ (newLb,newUb,src,targ);

		if (newUb <= MAX_LENGTH) {
			addEdgeSpec(src, targ, newUb);
			spec.m_edgeCount++;
		}
		if (newLb >= MIN_LENGTH) {
			addEdgeSpec(targ, src, getTime(-newLb));
			spec.m_edgeCount++;
		}
		if (oldUb <= MAX_LENGTH) {
			removeEdgeSpec(src, targ, oldUb);
			spec.m_edgeCount--;
		}
		if (oldLb >= MIN_LENGTH) {
			removeEdgeSpec(targ, src, getTime(-oldLb));
			spec.m_edgeCount--;
		}

		spec.lowerBound = newLb;
		spec.upperBound = newUb;

		if (!(spec.m_edgeCount >= 0 && spec.m_edgeCount <= 2)) {
			throw new IllegalStateException("Invalid edge count: " + spec.m_edgeCount);
		}

		if (!this.hasDeletions)
			incPropagate(src, targ);
	}

	/**
	 * @brief Remove a constraint from the temporal network
	 * @param tcId Constraint to remove
	 * @param markDeleted set to true iff you want the STN's state updated to indicate a deleteation has occured. False otherwise.
	 */
	public final void removeTemporalConstraint(TemporalConstraint tcId) {
		removeTemporalConstraint(tcId, true);
	}
	public final void removeTemporalConstraint(TemporalConstraint tcId, boolean markDeleted) {
		// Make sure it is valid, including belonging to this id manager
		if (!isValidId(tcId)) {
		// FIXME:  Commented out due to SPF-9297:  Cut makes the id invalid and undoing the cut leaves it invalid.
		return;
//		throw new IllegalArgumentException("removeTemporalConstraint: invalid Id");
		}
		TemporalConstraint spec = tcId;
		Time lb = spec.lowerBound;
		Time ub = spec.upperBound;
		Timepoint src = spec.head;
		Timepoint targ = spec.foot;
		// FIXME:  Commented out due to SPF-9297:  Cut makes the id invalid and undoing the cut leaves it invalid.
		if (!isValidId(src) || !isValidId(targ)) {
			return;
//			throw new IllegalStateException();
		}

		if (ub <= MAX_LENGTH)
			removeEdgeSpec(src, targ, ub);
		if (lb >= MIN_LENGTH)
			removeEdgeSpec(targ, src, getTime(-lb));
		this.hasDeletions = this.hasDeletions || markDeleted;
		m_constraints.remove(spec);
		spec.dispose();
	}

	/**
	 * @brief get the TimePointId of the origin of the STN.
	 * @return the TimePointId of the origin of the STN.
	 */
	public final Timepoint getOrigin() {
		return getOriginNode();
	}

	/**
	 * @brief Create a new timepoint in the STN.
	 * @return The TimepointId of the new timepoin.
	 */
	@SuppressWarnings("unchecked")
	public final Timepoint addTimepoint() {
		Timepoint node = (Timepoint) createNode();
		node.ordinal=++(nodeCounter);
		return node;
	}

	/**
	 * @brief Delete a timepoint from the STN. Note: this must not be the origin and it must be a valid timepoint.
	 */
	public final void deleteTimepoint(Timepoint node) {
		if (!isValidId(node)) {
			throw new IllegalArgumentException("TemporalNetwork:: deleting invalid timepoint.");
		}
		if (node == getOriginNode()) {
			throw new IllegalArgumentException("TemporalNetwork:: deleting origin timepoint.");
		}
		this.hasDeletions = this.hasDeletions || node.getDeletionMarker();

//		cleanupTEQ(node);

		m_updatedTimepoints.remove(node);

		// Note: following causes all edges involving
		// the node to be removed before removing the node.
		deleteNode(node);
	}

	/**
	 * @brief Identify the set of timepoints that lead to an inconsistent network. Note the network must be inconsistent to call this method.
	 * @return The set of timepoints behind the inconsistency.
	 */
	@SuppressWarnings("unchecked")
	public final List<Timepoint> getInconsistencyReason() {
		if (isConsistent()) {
			throw new IllegalStateException("Network is not inconsistent");
		}
		List<Timepoint> ans = new ArrayList<Timepoint>(edgeNogoodList.size());
		for (Dedge edge : edgeNogoodList) { 
			Timepoint node = (Timepoint) edge.to;
			ans.add(node);
		}
		return ans;
	}

	/**
	 * @brief Identify the set of edges that lead to an inconsistent network.
	 * @return The set of edges behind the inconsistency if the network is inconsistent. An empty list if the network is consistent.
	 */
	public final List<Dedge> getEdgeNogoodList() {
		if (isConsistent())
			return Collections.emptyList();
		return edgeNogoodList;
	}

	/**
	 * @brief Check if distance between two timepoints is less than a time bound
	 * @param from start timepointId
	 * @param to end timepointId
	 * @param bound distance bound
	 * @return true iff distance(form, to) < bound. False otherwise.
	 */
	public final boolean isDistanceLessThan (Timepoint from, Timepoint to, Time bound) {
		if (!isConsistent()) {
			throw new IllegalStateException("TemporalNetwork: Checking distance in inconsistent network");
		}
		return super.isDistanceLessThan(from, to, bound);
	}

	/**
	 * @brief Check if distance between two timepoints is less or equal to a time bound
	 * @param from start timepointId
	 * @param to end timepointId
	 * @param bound distance bound
	 * @return true iff distance(form, to) <= bound. False otherwise.
	 */
	public final boolean isDistanceLessThanOrEqual (Timepoint from, Timepoint to,
			Time bound) {
		return isDistanceLessThan(from, to, getTime(bound + TIME_TICK));
	}

	/**
	 * @brief An efficent approimate verion of isDistanceLessThan. It performans an
	 *        unrolled recursion only to depth 1 with some extra checks involving upper/
	 *        lower bounds of src/dest. 
	 * @param from start timepointId
	 * @param to end timepointId
	 * @param bound distance bound
	 * @return true if distance(form, to) ~< bound. False otherwise.
	 */
	public final boolean isDistancePossiblyLessThan (Timepoint src, Timepoint dest,
			Time bound) {
		// An efficient approximate version of isDistanceLessThan.
		// (Performs the unrolled recursion only to depth 1 with
		//  some extra checks involving upper/lower bounds of src/dest.)
		if (!isConsistent()) {
			throw new IllegalStateException("TemporalNetwork: Checking distance in inconsistent network");
		}
		// We do not expect bound to be -infinity for normal
		// use of this function.
		if (bound < MIN_DISTANCE) {
			throw new IllegalArgumentException("isDistancePossiblyLessThan:  bound is too small");
		}

		// The potential is always finite, so if bound is infinite,
		// following test will always safely fail.
		if (dest.potential >= src.potential + bound)
			return false;

		// Extra filtering from an analogous test using lower bounds, but we
		// must deal appropriately with infinite cases.
		if (dest.lowerBound >= MIN_DISTANCE) {  // There is path from dest to origin
			if (src.lowerBound == NEG_INFINITY)   // Can't be path from src to dest
				return false;
			// Now we know the src/dest lower bounds are both finite
			if (dest.lowerBound >= src.lowerBound + bound)
				return false;
		}

		// The increment in filtering power from the analogous test on upper
		// bounds seems to be not worth it.
		/*
	      if (src.upperBound <= MAX_DISTANCE) {  // There is path from origin to src
	      if (dest.upperBound == POS_INFINITY) // Can't be path from src to dest
	      return false;
	      // Now we know the src/dest upper bounds are both finite
	      if (dest.upperBound >= src.upperBound + bound)
	      return false;
	      }
		 */

		return true;
	}

	/**
	 * @brief Clear the set of updated timepoints.
	 */
	public final void resetUpdatedTimepoints() {
		m_updatedTimepoints.clear();
	}

	/**
	 * @brief Add node to set of updated timepoints.
	 * @param node node to add.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void handleNodeUpdate(Dnode node) {
		Timepoint tnode = (Timepoint)node;
		if (node != getOrigin()) {
			m_updatedTimepoints.add(tnode);
		}
	}

	/**
	 * @brief Returns the set of updated timepoints.
	 * @return the set of updated timepoints.
	 */
	public final Set<Timepoint> getUpdatedTimepoints() {
		return Collections.unmodifiableSet(m_updatedTimepoints);
	}

	/**
	 * @brief Identify if timepoint is connected to the origin of the STN through edges in the network
	 * @param timepoint 
	 * @return true iff timepoint has edge to the origin. False, otherwise.
	 * @todo make const when we constify methods in the DistanceGraph
	 */
	public final boolean hasEdgeToOrigin(Timepoint timepoint) {
		// Order of operands is important for speed. Should be faster to look towards the origin
		Dedge edgeToTheOrigin = findEdge(timepoint, getOrigin());
		if ((edgeToTheOrigin != null) && !edgeToTheOrigin.isValid()) {
			throw new IllegalStateException(String.valueOf(edgeToTheOrigin));
		}
		return (edgeToTheOrigin != null);
	}

	/**
	 * @brief Constructor creates and empty STN
	 */
	public TemporalNetwork() {
		consistent=true; 
		hasDeletions=false; 
		nodeCounter=0;
		m_constraints = new LinkedHashSet<TemporalConstraint>();

		addTimepoint();
		fullPropagate();
	}

	/**
	 * @brief Destructor
	 */
	@Override
	public void dispose() {
		for (TemporalConstraint constraint : m_constraints) {
			if (!constraint.isValid()) {
				throw new IllegalStateException("invalid constraint during dispose");
			}
			constraint.dispose();
		}
		super.dispose();
	}

	/**
	 * @brief Get the origin of the STN
	 * @return  origin timepointId in the STN
	 */
	@SuppressWarnings("unchecked")
	private final Timepoint getOriginNode() {
		return (Timepoint)this.nodes.firstElement();
	}

	/**
	 * @brief propagate the entire STN
	 */
	@SuppressWarnings("unchecked")
	private final void fullPropagate() {
		m_updatedTimepoints.clear();
		this.incrementalSource = null;   // Not applicable to a full prop.
		this.consistent = bellmanFord();
		this.hasDeletions = false;
		if (this.consistent == false)
			return;

		// We also need to do specialized Dijkstras in the forward
		// and backward directions to update the lower/upper bounds.
		// Note: these could be done lazily on request for bounds.
		for (Dnode dnode : nodes) {
			Timepoint node = (Timepoint) dnode;
			node.upperBound = getTime(POS_INFINITY);
			node.lowerBound = getTime(NEG_INFINITY);
		}

		Timepoint origin = getOriginNode();
		origin.upperBound = getTime(0L);
		origin.lowerBound = getTime(0L);
		origin.depth = 0;

		BucketQueue queue = initializeBqueue();
		queue.insertInQueue(origin);
		incDijkstraForward();
		queue.insertInQueue(origin);
		incDijkstraBackward();
	}

	private final class PropagationDistances {
		public Time headDistance;
		public Time footDistance;
		public PropagationDistances(Time headDistance, Time footDistance) {
			this.headDistance = headDistance;
			this.footDistance = footDistance;
		}
	}

	/**
	 * @brief propagate only edges between two points in the STN
	 * @param src start point for propagation
	 * @param targ end point for propagation
	 */
	private final void incPropagate(Timepoint src, Timepoint targ) {
		// Do nothing if network inconsistent or there are deletions.
		// The next consistency check will cause full propagation.
		if (this.hasDeletions || this.consistent == false)
			return;

		if (!isValidId(src) || !isValidId(targ)) {
			throw new IllegalArgumentException();
		}

		BucketQueue queue = initializeBqueue();
		PropagationDistances distances = new PropagationDistances(getTime(src.potential), getTime(targ.potential));
		Timepoint next = startNode(src, targ, distances);
		src.potential = distances.headDistance; targ.potential = distances.footDistance;
		if (next != null) {
			Timepoint start = (next == src) ? targ : src;
			incrementalSource = start;  // Used in specialized cycle detection
			next.predecessor = findEdge(start,next);  // Used to trace nogood
			handleNodeUpdate(next);
			queue.insertInQueue(next);
			this.consistent = incBellmanFord();
		}

		// Can't do Dijkstra if network is now inconsistent.
		if (this.consistent == false)
			return;

		// Now we need to do specialized Dijkstras in the forward
		// and backward directions to update the lower/upper bounds.

		BucketQueue queue1 = initializeBqueue();

		distances = new PropagationDistances(src.upperBound, targ.upperBound);
		next = startNode(src, targ, distances);
		src.upperBound = distances.headDistance; targ.upperBound = distances.footDistance;
		if (next != null) {
			queue1.insertInQueue(next);
			handleNodeUpdate(next);
			incDijkstraForward();
		}

		// For lower-bound propagation we need to do some finagling (Irish
		// word) to get the right effect from startNode().

		// Backwards propagation, so call with "forward" flag false.
		distances = new PropagationDistances(getTime(-(src.lowerBound)), getTime(-(targ.lowerBound)));
		next = startNode(src, targ, distances, false);
		if (next != null) {
			src.lowerBound = getTime(-distances.headDistance); targ.lowerBound = getTime(-distances.footDistance);

			queue1.insertInQueue(next);
			handleNodeUpdate(next);
			incDijkstraBackward();
		}
	}

	/**
	 * @brief For incremental propagation, determines whether a propagation
	 *        should be tried from head to foot or vice versa.  
	 * 
	 *  The supplied
	 * edge must be in the direction of propagation from head to foot.
	 * There is another edge (not needed for the determination, so not
	 * supplied) in the other direction.  Used with various
	 * distance-like values.  Note that an effective prop in one
	 * direction precludes one in the other direction.
	 * @return node at which incremental propogation should be tried.
	 */
	/**
	 * @brief For incremental propagation, determines whether a propagation
	 *        is started from head to foot or vice versa, and does first 
	 *        propagation.  (PHM: 06/21/2007 Recoded for efficiency.)
	 * @return node from which to continue
	 *        the propagation (or noId if first prop is ineffective).
	 */
	private final Timepoint startNode (Timepoint head, Timepoint foot, PropagationDistances distances) {
		return startNode(head, foot, distances, true);
	}

	private final Timepoint startNode (Timepoint head, Timepoint foot, PropagationDistances distances, boolean forwards) {
		// PHM 06/21/2007 Modified for efficiency to do first propagation
		// as side-effect.  (Avoids waste of unnecessary fan-out at first
		// node, which can be huge, for example O(n) at the origin.)

		Dedge edge = findEdge(forwards ? head : foot,
				forwards ? foot : head);

		if ((edge != null) && (distances.headDistance < g_infiniteTime)
				&& (distances.headDistance + edge.length < distances.footDistance)) {
			// Propagate across edge
			distances.footDistance = getTime(distances.headDistance + edge.length);
			head.depth = 0;
			foot.depth = 1;
			return foot;  // Continue propagation from foot
		}

		// Else Propagation, if any, is in the other direction.
		Dedge revEdge = findEdge(forwards ? foot : head,
				forwards ? head : foot);

		if ((revEdge != null) && (distances.footDistance < g_infiniteTime)
				&& (distances.footDistance + revEdge.length < distances.headDistance)) {
			// Propagate across reverse edge
			distances.headDistance = getTime(distances.footDistance + revEdge.length);
			foot.depth = 0;
			head.depth = 1;
			return head;  // Continue propagation from head
		}
		return null;
	}

	/**
	 * @brief Similar to the DistanceGraph Dijkstra, but propagates the
	 * upper bound, and is incremental.
	 */
	@SuppressWarnings("unchecked")
	private final void incDijkstraForward() {
		BucketQueue queue = this.bqueue;
		int BFbound = this.nodes.size();
		while (true) {
			Dnode dnode = queue.popMinFromQueue();
			if (dnode == null)
				return;
			Timepoint node = (Timepoint)dnode;
			for (int i=0; i< node.outCount; i++) {
				Dedge edge = node.outArray[i];
				Timepoint next = (Timepoint) edge.to;
				Time newDistance = getTime(node.upperBound + edge.length);
				if (newDistance < next.upperBound) {
					if (newDistance > MAX_DISTANCE || newDistance < MIN_DISTANCE) {
						throw new IllegalStateException("Potential over(under)flow during upper bound propagation");
					}
					// Next check is a failsafe to prevent infinite propagation.
					if ((next.depth = node.depth + 1) > BFbound) {
						throw new IllegalStateException("Dijkstra propagation in inconsistent network");
					}
					next.upperBound = newDistance;
					// Appropriate priority key as derived from Johnson's algorithm
					queue.insertInQueue (next, getTime(newDistance - next.potential));

					// Store in set of updated timepoints
					handleNodeUpdate(next);
				}
			}
		}
	}

	/**
	 * @brief Similar to the DistanceGraph Dijkstra, but propagates the
	 * negation of the lower bound, and is incremental, and goes
	 * in the reverse direction.
	 */
	@SuppressWarnings("unchecked")
	private final void incDijkstraBackward() {
		BucketQueue queue = this.bqueue;
		int BFbound = this.nodes.size();
		while (true) {
			Dnode dnode =  queue.popMinFromQueue();
			if(dnode == null)
				return;

			Timepoint node = (Timepoint)dnode;

			for (int i=0; i< node.inCount; i++) {
				Dedge edge = node.inArray[i];
				Timepoint next = (Timepoint) edge.from;
				Time newDistance = getTime(-(node.lowerBound) + edge.length);
				if (newDistance < -(next.lowerBound)) {
					if (newDistance > MAX_DISTANCE || newDistance < MIN_DISTANCE) {
						throw new IllegalStateException("Potential over(under)flow during lower bound propagation");
					}
					// Next check is a failsafe to prevent infinite propagation.
					if ((next.depth = node.depth + 1) > BFbound) {
						throw new IllegalStateException("Dijkstra propagation in inconsistent network");
					}
					next.lowerBound = getTime(-newDistance);
					// 12/13/2002 Fix queue key computation.  Correct formula for
					// backward prop is key = (distance + potential).
					queue.insertInQueue (next, getTime(newDistance + next.potential));

					// Store in set of updated timepoints
					handleNodeUpdate(next);
				}
			}
		}
	}

	/**
	 * @brief Propagates lower/upper distance bounds from src
	 * using backward and forward Dijkstra propagations.
	 */
	@SuppressWarnings("unchecked")
	private final void propagateBoundsFrom (Timepoint src) {
		for (Dnode dNode : nodes) {
			Timepoint node = (Timepoint) dNode;
			node.upperBound = getTime(POS_INFINITY);
			node.lowerBound = getTime(NEG_INFINITY);
		}
		src.upperBound = getTime(0L);
		src.lowerBound = getTime(0L);
		src.depth = 0;
		BucketQueue queue = initializeBqueue();
		queue.insertInQueue(src);
		incDijkstraForward();
		queue.insertInQueue(src);
		incDijkstraBackward();
	}

	/**
	 * @brief check if node is valid
	 * @return true iff node is valid.
	 */
	private final boolean isValidId(Timepoint id) {
		return (id.isValid() && (id.owner == this) && (!isCheckNodeGraphMembership() || hasNode(id)));
	}

	/**
	 * @brief check if constraint is valid
	 */
	private final boolean isValidId(TemporalConstraint id) {
		return (id.isValid() && id.owner == this);
	}

	/**
	 * @brief set of constraints in the temporal network
	 */
	private final Set<TemporalConstraint> m_constraints;

	// Overridden virtual functions

	/**
	 * @brief Creates a new node for the temporal network.
	 * @return the new node.
	 */
	@Override
	protected Timepoint makeNode() {
		Timepoint node = new Timepoint(this);
		return node;
	}

	/**
	 * @brief Identify if the network has cycles.
	 * @return returns true iff network contains cycles, false otherwise. 
	 */
	@Override
	protected boolean cycleDetected (Dnode next) {
		return (next == this.incrementalSource);
	}

	/**
	 * @brief Stores the changes made to nodes during propogation for more efficent incremental update
	 */
	protected Set<Timepoint> m_updatedTimepoints = new HashSet<Timepoint>();

	/**
	 * Used to return lower and upper bounds together
	 * 
	 * @author Andrew
	 *
	 * @param <Time>
	 */
	public final class Bounds {
		public final Time lowerBound;
		public final Time upperBound;
		public Bounds(Time lb, Time ub) {
			this.lowerBound = lb;
			this.upperBound = ub;
		}
	}

	/**
	 * User to return the source and target timepoints together
	 * 
	 * @author Andrew
	 *
	 */
	public final class Scope {
		public final Timepoint source;
		public final Timepoint target;
		Scope(Timepoint source, Timepoint target) {
			this.source = source;
			this.target = target;
		}
	}

	/**
	 * @class  Tnode
	 * @author Paul H. Morris (with mods by Conor McGann)
	 * @date   Mon Dec 27 2004
	 * @brief  Node in a temporal network.
	 * @ingroup TemporalNetwork
	 */
	public class Timepoint extends Dnode {

		private Time lowerBound;
		private Time upperBound;
		@SuppressWarnings("unused")
		private int ordinal;
		private TemporalConstraint m_baseDomainConstraint; /*!< Constraint used to enforce timepoint bounds input.*/
		private boolean m_deletionMarker;
		public TemporalNetwork owner;

		public Timepoint(TemporalNetwork t) {
			m_deletionMarker = true;
			owner = t;
			lowerBound = getTime(NEG_INFINITY);
			upperBound = getTime(POS_INFINITY);
		}

		public final TemporalConstraint getBaseDomainConstraint() {
			return m_baseDomainConstraint;
		}

		public final void setBaseDomainConstraint(TemporalConstraint constraint) {
			m_baseDomainConstraint = constraint;
		}

		public final void clearDeletionMarker() {
			m_deletionMarker = false;
		}

		public final boolean getDeletionMarker() { 
			return m_deletionMarker; 
		}

		public final Time getLowerBound() {
			return lowerBound;
		}

		public final Time getUpperBound() {
			return upperBound;
		}

		public final Bounds getBounds() {
			return new Bounds(lowerBound, upperBound);
		}

	}

	public class TemporalConstraint extends Entity {

		private Time lowerBound;
		private Time upperBound;
		private Timepoint head;
		private Timepoint foot;
		private TemporalNetwork owner;
		private int m_edgeCount;

		/**
		 * @brief constructor
		 * @param t the temporal network to which this specification belongs
		 * @param src start of the specification
		 * @param targ end of the specification
		 * @param lb lower bound
		 * @param ub upper bound
		 * @param edgeCount edge count
		 */
		public TemporalConstraint(TemporalNetwork t, Timepoint src,Timepoint targ,Time lb,Time ub, int edgeCount)
		{ 
			head=src; 
			foot=targ;
			owner = t;
			lowerBound=lb; 
			upperBound=ub;
			m_edgeCount = edgeCount;
		}
		
		public final Timepoint getSource() {
			return head;
		}
		
		public final Timepoint getTarget() {
			return foot;
		}

		/** 
		 * @brief get the upper and lower bounds of this Tspec
		 * @param lb returns the lower bound
		 * @param ub returns the upper bound
		 */
		public final Bounds getBounds() {
			return new Bounds(lowerBound, upperBound);
		}

		/**
		 * @brief test if Tspec is complete
		 * @return returns true if Tspec is complete, false otherwise.
		 */
		public final boolean isComplete() {
			return m_edgeCount == 2;
		}
	}

	private final void checkBoundsValidity(Time lb, Time ub) {
		if (!NO_ERROR_CHECKS) {
			doCheckBounds(lb, ub);
		}
	}

	private void doCheckBounds(Time lb, Time ub) {
		if (lb > ub) {
			throw new IllegalStateException("addTemporalConstraint: (LB, UB) interval was empty");
		}
		if ((ub > MAX_LENGTH && ub < POS_INFINITY) || ub > POS_INFINITY) {
			throw new IllegalStateException("addTemporalConstraint:  UB is in forbidden range");
		}
		if (ub < MIN_LENGTH) {
			throw new IllegalStateException("addTemporalConstraint:  UB is too small");
		}
		if ((-lb > MAX_LENGTH && -lb < POS_INFINITY) || -lb > POS_INFINITY) {
			throw new IllegalStateException("addTemporalConstraint:  LB is in forbidden range");
		}
		if (-lb < MIN_LENGTH) {
			throw new IllegalStateException("addTemporalConstraint:  LB is too large");
		}
	}

}

