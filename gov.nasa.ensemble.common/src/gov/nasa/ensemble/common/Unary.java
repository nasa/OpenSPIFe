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
package gov.nasa.ensemble.common;

import fj.F;

/**
 * A function which takes one argument and produces an output. This class and others of the Fn variety can be used as a poor man's
 * approximation of closures, to be passed to higher order functions such as {@link CommonUtils#map(java.util.List, Unary)},
 * {@link CommonUtils#flatMap(java.util.List, Unary)}, etc.
 * 
 * You can get rid of this when Java 7 comes around with native closures.
 * 
 * @param <Input>
 *            the type of the input to the function
 * @param <Output>
 *            the type of the output to the function
 * @param input
 *            the input to the function
 * 
 * @deprecated use functionaljava's {@link F}
 */
@Deprecated
public interface Unary<Input, Output> {
	Output apply(Input input);
}
