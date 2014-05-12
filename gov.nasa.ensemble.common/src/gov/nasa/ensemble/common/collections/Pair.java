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

import gov.nasa.ensemble.common.CommonUtils;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * A simple pair of objects, type checking provided by Java generics.
 *
 * @param <L> The required type for the left/first element in this Pair.
 * @param <R> The required type for the right/second element in this Pair.
 *
 * @author <a href="mailto:Matthew.E.Boyce@nasa.gov">Matthew E. Boyce</a>
 */
public class Pair<L, R> implements Serializable {
  /**
   * The left element of this Pair.
   */
  private L left;

  /**
   * The right element of this Pair.
   */
  private R right;

  /**
   * The hash code of this element, a convolution of the hash code for it's elements modified
   * slightly to encode order.
   */
  private transient int code;

  /**
   * Constructor, not much special to say here.
   *
   * @param left The left element of this Pair.
   * @param right The right element of this Pair.
   */
  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
    int leftHash = left != null? left.hashCode() : 0; 
    int rightHash = right != null? right.hashCode() : 0; 
    this.code = leftHash ^ (rightHash >>> 1);
  }

  /**
   * Get the left element of this Pair.
   *
   * @return The left element of this Pair.
   */
  public L getLeft() {
    return left;
  }

  /**
   * Get the right element of this Pair.
   *
   * @return The right element of this Pair.
   */
  public R getRight() {
    return right;
  }

  /**
   * Compare this Pair for equality with another.
   *
   * @param other the reference object with which to compare.
   *
   * @return true if this pair contains objects which compare as equal in the same position as
   * in another Pair, false otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Pair))
      throw new ClassCastException("Cannot compare a Pair with a " + other.getClass());
    if (hashCode() != other.hashCode())
      return false;
    return CommonUtils.equals(((Pair) other).getLeft(), left) && CommonUtils.equals(((Pair) other).getRight(), right);
  }

  /**
   * Returns a hash code value for this Pair. Used for hash tables and equals comparisons.
   *
   * @return A hash code value for this Pair.
   */
  @Override
  public int hashCode() {
    return code;
  }

  /**
   * Returns a string representation of this Pair.
   *
   * @return A string representation of this Pair.
   */
  @Override
  public String toString() {
    return "<" + left + ", " + right + ">";
  }

  /**
   * Read a serialized pair from the provided ObjectInputStream and regenerate its hash code.
   *
   * @param in Stream to read serialized pair from.
   *
   * @throws IOException If there was an I/O exception reading from in.
   * @throws ClassNotFoundException If there was an error in retrieving a class during
   * ObjectInputStream.defaultReadObject();
   */
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    int leftHash = left != null? left.hashCode() : 0; 
    int rightHash = right != null? right.hashCode() : 0; 
    this.code = leftHash ^ (rightHash >>> 1);
  }
}
