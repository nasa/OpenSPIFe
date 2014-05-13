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
package gov.nasa.ensemble.javascript.rhino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.VariableInitializer;

public class FormulaInfo extends AbstractFormulaParsingThing {

	/**
	 * Usage:  FormulaInfo.getVariableNames(formula).
	 * Similar in implementation to FormulaUnitsChecker but unrelated.
	 * @param formula to examine
	 * @return Set of variables referenced by formula
	 */
	public static Set<String> getVariableNames(String formula) throws EvaluatorException {
		return findVariablesUnder(parseIntoTree(formula));
	}
	
	private static Set<String> findFeaturePathsWithinRightSide(Node node) {
		Set<String> resultSoFar = new TreeSet<String>();
		Node firstChild = node.getFirstChild();
		for (Node child = firstChild.getNext() ; (child != null) ; child = child.getNext()) {
			resultSoFar.addAll(findFeaturePathsUnder(child, null));
		}
		return resultSoFar;
	}
	
	private static String getPathString(List<String> pathSoFar) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String name : pathSoFar) {
			if (!first) {
				builder.append('.');
			}
			builder.append(name);
			first = false;
		}
		return builder.toString();
	}
	
	protected static Set<String> findFeaturePathsUnder(Node node, List<String> pathSoFar) {
		Set<String> resultSoFar = new TreeSet<String>();
		switch (node.getType()) {
		case Token.NAME: // e.g. the x in x + 42
			if (pathSoFar != null) {
				pathSoFar.add(node.getString());
			} else {
				resultSoFar.add(node.getString());
			}
			break;
		case Token.STRING:
			if (pathSoFar != null) {
				pathSoFar.add(node.getString());
			}
			break;
		case Token.GETPROP: // e.g. foo.bar
			boolean topLevel = pathSoFar == null;
			if (topLevel) {
				pathSoFar = new ArrayList<String>();
			}
			for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
				resultSoFar.addAll(findFeaturePathsUnder(child, pathSoFar));
			}
			if (topLevel) {
				resultSoFar.add(getPathString(pathSoFar));
			}
			return resultSoFar;
		case Token.SETNAME: // Left-hand variable being written:  x in x = y;
		case Token.VAR:
		case Token.CALL:
			// ignore the left side and process the right side now
			resultSoFar.addAll(findFeaturePathsWithinRightSide(node));
			break;
		default:
			break;
		}
		// Now do the nested subexpressions, for all cases that said break instead of return.
		for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
			resultSoFar.addAll(findFeaturePathsUnder(child, pathSoFar));
		}
		return resultSoFar;
	}
	
	public static Set<String> getFeaturePaths(String formula) {
		Set<String> result = findFeaturePathsUnder(parseIntoTree(formula), null);
		return result;
	}
	
	/**
	 * Usage:  FormulaInfo.getFunctionNames(formula).
	 * Similar in implementation to FormulaUnitsChecker but unrelated.
	 * @param formula to examine
	 * @return Set of variables referenced by formula
	 */
	public static Set<String> getFunctionNames(String formula) {
		return findFunctionsUnder(parseIntoTree(formula));
	}
	
	/**
	 * Example:  FormulaInfo.getArrayIndexes("foo[\"bar\"] + + other[\"thing\"] + foo[\"baz\"]", "foo")
	 * ----> {"bar", "baz"}
	 * @param formula to examine
	 * @param arrayName name of array whose indexes to find
	 * @return Set of array indexes  formula returns
	 */
	public static Set<String> getArrayIndexes(String formula, String arrayName) {
		return findArrayIndexesUnder(arrayName, parseIntoTree(formula));
	}
	
	/**
	 * Find references to properties with respect to an anchor object name using either subscript
	 * notation or dot notation
	 * Example:  FormulaInfo.getArrayIndexes("foo[\"bar\"] + + other[\"thing\"] + foo.baz", "foo")
	 * ----> {"bar", "baz"}
	 * 
	 * @param formula to examine
	 * @param objectName name of object whose property references are to be found
	 * @return Set of property names referenced with respect to the objectName
	 */
	public static Set<String> getPropertyReferences(String formula, String objectName) {
		return findPropertyRefsUnder(objectName, parseIntoTree(formula));
	}
	
	public static Set<String> getVariableNamesInFunctionBody(String functionBody) {
		return findVariablesUnder(parseFunctionBodyIntoTree(functionBody));
	}

	public static Set<String> getFunctionNamesInFunctionBody(String functionBody) {
		return findFunctionsUnder(parseFunctionBodyIntoTree(functionBody));
	}

	public static Set<String> getArrayIndexesInFunctionBody(String functionBody, String arrayName) {
		return findArrayIndexesUnder(arrayName, parseFunctionBodyIntoTree(functionBody));
	}
	
	public static boolean isTrivial (String formula) {
		// No formulas, just one constant or one variable.
		final ArrayList<AstNode> result = new ArrayList<AstNode>();
		parseIntoTree(formula).visit(new NodeVisitor(){

			@Override
			public boolean visit(AstNode node) {
				if(node.depth()>1){
					result.add(node);
					return false;
				}
				return true;
			}
			
		});
		if(result.size()>0){
			switch (result.get(0).getType()) {
				case Token.NUMBER: return true;
				case Token.NAME: return true;
				case Token.STRING: return true;
				default: return false;
			}
		}
		return false;
	}

	static class FindVariablesNodeVisitor implements NodeVisitor{
		
		Set<String> resultSoFar = new HashSet<String>();
		Set<String> localVariable = new HashSet<String>();
		
		public Set<String> getResults(){
			return resultSoFar;
		}
		
		@Override
		public boolean visit(AstNode node) {
			switch (node.getType()) {
			case Token.NUMBER: // e.g. the 42 in x + 42
				break;
			case Token.NAME: // e.g. the x in x + 42
				if(node.getParent() instanceof Assignment){
					Assignment parent = (Assignment)node.getParent();
					if(parent.getLeft()==node){
						break;
					}
				}else if(node.getParent() instanceof VariableInitializer){
					VariableInitializer parent = (VariableInitializer)node.getParent();
					if(parent.getTarget()==node){
						localVariable.add(node.getString());
						break;
					}
				} else if(node.getParent() instanceof PropertyGet && node.getParent().getParent() instanceof FunctionCall){
					if(((PropertyGet) node.getParent()).getLeft()==node){
						// for foo.match(...), count "foo" but not "match" as a variable.
						resultSoFar.add(safeGetString(node));
					}
					return true;
				}
				if(!localVariable.contains(node.getString())){
					resultSoFar.add(safeGetString(node));
				}
				break;
			case Token.CALL:
				FunctionCall call = (FunctionCall)node;
				int targetType = call.getTarget().getType();
				if ( targetType == Token.NAME || targetType==Token.STRING) {
					String string = safeGetString(call.getTarget());
					resultSoFar.add(string);
				}
				break;
			default:
				break;
			}
			return true;
		}
	}
	
	protected static Set<String> findVariablesUnder(Node node) {
		AstNode aNode = (AstNode)node;
		FindVariablesNodeVisitor visitor = new FindVariablesNodeVisitor();
		aNode.visit(visitor);
		return visitor.getResults();
	}

	static class FunctionsUnderNodeVisitor implements NodeVisitor{

		Set<String> resultSoFar = new HashSet<String>();
		public Set<String> getResult(){
			return resultSoFar;
		}
		
		@Override
		public boolean visit(AstNode node) {
			if( Token.NAME == node.getType()){
				if(node.getParent().getType() == Token.CALL){
					FunctionCall parent = (FunctionCall)node.getParent();
					if(parent.getTarget()==node){
						resultSoFar.add(safeGetString(node));
					}
				}
			}
			return true;
		}
		
	}
	
	protected static Set<String> findFunctionsUnder(Node node) {
		AstNode aNode = (AstNode) node;
		FunctionsUnderNodeVisitor visitor = new FunctionsUnderNodeVisitor();
		aNode.visit(visitor);
		return visitor.getResult();
	}
	
	static class ArrayIndexesUnderNodeVisitor implements NodeVisitor{
		private Set<String> resultSoFar = new HashSet<String>();
		private String arrayName;

		public ArrayIndexesUnderNodeVisitor(String arrayName) {
			super();
			this.arrayName = arrayName;
		}
		
		public Set<String> getResult(){
			return resultSoFar;
		}

		@Override
		public boolean visit(AstNode node) {
			if (Token.GETELEM == node.getType()){
				ElementGet gNode = (ElementGet)node;
				if( gNode.getTarget().getType()==Token.NAME
					&& arrayName.equals(safeGetString(gNode.getTarget()))) { // foo["bar"]
						AstNode el = gNode.getElement();
						resultSoFar.add(safeGetString(el));
				}
			}
			return true;
		}
		
	}
	
	protected static Set<String> findArrayIndexesUnder(String arrayName, Node node) {
		ArrayIndexesUnderNodeVisitor visitor = new ArrayIndexesUnderNodeVisitor(arrayName);
		AstNode aNode = (AstNode)node;
		aNode.visit(visitor);
		return visitor.getResult();
	}
	
	static class PropertyRefsUnderNodeVisitor implements NodeVisitor{
		String objectName;
		Set<String> resultSoFar = new HashSet<String>();
		public PropertyRefsUnderNodeVisitor(String objectName) {
			super();
			this.objectName = objectName;
		}
		@Override
		public boolean visit(AstNode node) {
			switch (node.getType()) {
			case Token.GETPROP: // foo.bar (firstChild="foo", lastChild="bar")
				PropertyGet propertyGet = (PropertyGet)node;
				int propType = propertyGet.getProperty().getType();
				if(propertyGet.getTarget().getType()==Token.NAME
						&& (propType == Token.STRING || propType == Token.NAME)
						&& objectName.equals(safeGetString(propertyGet.getTarget()))
				){
					resultSoFar.add(safeGetString(propertyGet.getProperty()));
				}
				break;
			case Token.GETELEM: // foo["bar"]
				ElementGet elemGet = (ElementGet)node;
				int elemType = elemGet.getElement().getType();
				if(elemGet.getTarget().getType()==Token.NAME
					&&  (elemType == Token.STRING || elemType==Token.NAME)
					&& objectName.equals(safeGetString(elemGet.getTarget()))
				){
					resultSoFar.add(safeGetString(elemGet.getElement()));
				}
				break;
			default:
				break;
			}
			return true;
		}
		public Set<String> getResult() {
			return this.resultSoFar;
		}
		
	}
	
	protected static String safeGetString(AstNode node){
		if(node.getType() == Token.STRING){
			StringLiteral sl = (StringLiteral)node;
			return sl.getValue();
		}else if(node.getType()==Token.NUMBER){
			NumberLiteral nl = (NumberLiteral)node;
			return nl.getValue();
		}else{
			return node.getString();
		}
	}
	
	protected static Set<String> findPropertyRefsUnder(String objectName, Node node) {
		PropertyRefsUnderNodeVisitor visitor = new PropertyRefsUnderNodeVisitor(objectName);
		if (node == null) {
			return visitor.getResult();
		}
		AstNode aNode = (AstNode)node;
		aNode.visit(visitor);
		return visitor.getResult();
	}
	
	public static void debugFormula(String formula) {
		debugFormula(parseIntoTree(formula), 0);
	}
	
	private static void debugFormula(Node node, int depth) {
		String string = "";
		if (node.getClass().getSimpleName().equals("StringNode")) {
			string = " - " + node.getString();
		} else if (node.getClass().getSimpleName().equals("NumberNode")) {
			string = " - " + node.getDouble();
		}
		Logger logger = Logger.getLogger(FormulaInfo.class);
		logger.debug(s(depth*4) + getTokenName(node.getType()) + string);
		for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
			debugFormula(child, depth + 1);
		}
	}
	
	private static String s(int length) {
		char chars[] = new char[length];
		Arrays.fill(chars, ' ');
		return new String(chars);
	}
	
	/** Copy/pasted from Token.name... */
	public static String getTokenName(int token) {
		String string = Token.name(token);
		if (UNKNOWN_TOKEN_TYPE.equals(string)) {
	        // Token without name
	        throw new IllegalStateException(String.valueOf(token));
		}
		return string;
    }

}
