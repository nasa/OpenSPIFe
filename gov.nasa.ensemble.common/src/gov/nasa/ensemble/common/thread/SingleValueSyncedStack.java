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
package gov.nasa.ensemble.common.thread;

import java.util.Stack;

@SuppressWarnings("serial")
public class SingleValueSyncedStack<E> extends Stack<E> {
	
	public SingleValueSyncedStack() {
		super();
	}
	
	@Override
	public synchronized E pop(){
		E e = null;
		if(size() > 0)
			e = super.pop();
		return e;
	}
	
	@Override
	public synchronized E push(E e){
		if(this.elementCount!=0){
			super.pop();
		}		
		
		return super.push(e);
	}
	
	
	@Override
	public synchronized E peek(){
		if(size()==0){
			return null;
		}
		return super.peek();
	}
	
	@Override
	public synchronized boolean empty(){
		return size()==0;
	}
}
