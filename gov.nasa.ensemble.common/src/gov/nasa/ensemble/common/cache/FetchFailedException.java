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
package gov.nasa.ensemble.common.cache;

/**
 * <P>Checked exception thrown if there is a problem fetching an object.</P>
 *
 * <P>Copyright 2001, California Institute of Technology.<BR>
 * ALL RIGHTS RESERVED. U.S. Government Sponsorship acknowledged.</P>
 *
 * @author Marty Vona 
 **/
@SuppressWarnings("serial")
public class FetchFailedException extends Exception {


	/* CONSTRUCTORS */
	
  public FetchFailedException() {
    super();
  }
  
  public FetchFailedException(String message) {
    super(message);
  }
  
  public FetchFailedException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public FetchFailedException(Throwable cause) {
    super(cause);
  }

	/* PUBLIC INTERFACE */

	/* Class Constants */

	/* Mutators */
	
	/* Accessors */
	
	/* Common Interface */
	
	//public String toString();
	//public boolean equals(Object obj);
	//protected Object clone() throws CloneNotSupportedException;
	//protected void finalize() throws Throwable;

	//public static void main(String arg[]);
	
	/* PRIVATE METHODS */
	
	/* CLASS AND OBJECT ATTRIBUTES */
}
