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

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.NodeVisitor;

///CLOVER:OFF
public abstract class AbstractFormulaParsingThing {

	protected static final String UNKNOWN_TOKEN_TYPE = "(unknown token type)";

	protected static String nodeTypeString(int token) {
		switch (token) {
        case Token.ERROR:           return "ERROR";
        case Token.EOF:             return "EOF";
        case Token.EOL:             return "EOL";
        case Token.ENTERWITH:       return "ENTERWITH";
        case Token.LEAVEWITH:       return "LEAVEWITH";
        case Token.RETURN:          return "RETURN";
        case Token.GOTO:            return "GOTO";
        case Token.IFEQ:            return "IFEQ";
        case Token.IFNE:            return "IFNE";
        case Token.SETNAME:         return "SETNAME";
        case Token.BITOR:           return "BITOR";
        case Token.BITXOR:          return "BITXOR";
        case Token.BITAND:          return "BITAND";
        case Token.EQ:              return "EQ";
        case Token.NE:              return "NE";
        case Token.LT:              return "LT";
        case Token.LE:              return "LE";
        case Token.GT:              return "GT";
        case Token.GE:              return "GE";
        case Token.LSH:             return "LSH";
        case Token.RSH:             return "RSH";
        case Token.URSH:            return "URSH";
        case Token.ADD:             return "ADD";
        case Token.SUB:             return "SUB";
        case Token.MUL:             return "MUL";
        case Token.DIV:             return "DIV";
        case Token.MOD:             return "MOD";
        case Token.NOT:             return "NOT";
        case Token.BITNOT:          return "BITNOT";
        case Token.POS:             return "POS";
        case Token.NEG:             return "NEG";
        case Token.NEW:             return "NEW";
        case Token.DELPROP:         return "DELPROP";
        case Token.TYPEOF:          return "TYPEOF";
        case Token.GETPROP:         return "GETPROP";
        case Token.SETPROP:         return "SETPROP";
        case Token.GETELEM:         return "GETELEM";
        case Token.SETELEM:         return "SETELEM";
        case Token.CALL:            return "CALL";
        case Token.NAME:            return "NAME";
        case Token.NUMBER:          return "NUMBER";
        case Token.STRING:          return "STRING";
        case Token.NULL:            return "NULL";
        case Token.THIS:            return "THIS";
        case Token.FALSE:           return "FALSE";
        case Token.TRUE:            return "TRUE";
        case Token.SHEQ:            return "SHEQ";
        case Token.SHNE:            return "SHNE";
        case Token.REGEXP:          return "OBJECT";
        case Token.BINDNAME:        return "BINDNAME";
        case Token.THROW:           return "THROW";
        case Token.RETHROW:         return "RETHROW";
        case Token.IN:              return "IN";
        case Token.INSTANCEOF:      return "INSTANCEOF";
        case Token.LOCAL_LOAD:      return "LOCAL_LOAD";
        case Token.GETVAR:          return "GETVAR";
        case Token.SETVAR:          return "SETVAR";
        case Token.CATCH_SCOPE:     return "CATCH_SCOPE";
        case Token.ENUM_INIT_KEYS:  return "ENUM_INIT_KEYS";
        case Token.ENUM_INIT_VALUES:  return "ENUM_INIT_VALUES";
        case Token.ENUM_NEXT:       return "ENUM_NEXT";
        case Token.ENUM_ID:         return "ENUM_ID";
        case Token.THISFN:          return "THISFN";
        case Token.RETURN_RESULT:   return "RETURN_RESULT";
        case Token.ARRAYLIT:        return "ARRAYLIT";
        case Token.OBJECTLIT:       return "OBJECTLIT";
        case Token.GET_REF:         return "GET_REF";
        case Token.SET_REF:         return "SET_REF";
        case Token.DEL_REF:         return "DEL_REF";
        case Token.REF_CALL:        return "REF_CALL";
        case Token.REF_SPECIAL:     return "REF_SPECIAL";
        case Token.DEFAULTNAMESPACE:return "DEFAULTNAMESPACE";
        case Token.ESCXMLTEXT:      return "ESCXMLTEXT";
        case Token.ESCXMLATTR:      return "ESCXMLATTR";
        case Token.REF_MEMBER:      return "REF_MEMBER";
        case Token.REF_NS_MEMBER:   return "REF_NS_MEMBER";
        case Token.REF_NAME:        return "REF_NAME";
        case Token.REF_NS_NAME:     return "REF_NS_NAME";
        case Token.TRY:             return "TRY";
        case Token.SEMI:            return "SEMI";
        case Token.LB:              return "LB";
        case Token.RB:              return "RB";
        case Token.LC:              return "LC";
        case Token.RC:              return "RC";
        case Token.LP:              return "LP";
        case Token.RP:              return "RP";
        case Token.COMMA:           return "COMMA";
        case Token.ASSIGN:          return "ASSIGN";
        case Token.ASSIGN_BITOR:    return "ASSIGN_BITOR";
        case Token.ASSIGN_BITXOR:   return "ASSIGN_BITXOR";
        case Token.ASSIGN_BITAND:   return "ASSIGN_BITAND";
        case Token.ASSIGN_LSH:      return "ASSIGN_LSH";
        case Token.ASSIGN_RSH:      return "ASSIGN_RSH";
        case Token.ASSIGN_URSH:     return "ASSIGN_URSH";
        case Token.ASSIGN_ADD:      return "ASSIGN_ADD";
        case Token.ASSIGN_SUB:      return "ASSIGN_SUB";
        case Token.ASSIGN_MUL:      return "ASSIGN_MUL";
        case Token.ASSIGN_DIV:      return "ASSIGN_DIV";
        case Token.ASSIGN_MOD:      return "ASSIGN_MOD";
        case Token.HOOK:            return "HOOK";
        case Token.COLON:           return "COLON";
        case Token.OR:              return "OR";
        case Token.AND:             return "AND";
        case Token.INC:             return "INC";
        case Token.DEC:             return "DEC";
        case Token.DOT:             return "DOT";
        case Token.FUNCTION:        return "FUNCTION";
        case Token.EXPORT:          return "EXPORT";
        case Token.IMPORT:          return "IMPORT";
        case Token.IF:              return "IF";
        case Token.ELSE:            return "ELSE";
        case Token.SWITCH:          return "SWITCH";
        case Token.CASE:            return "CASE";
        case Token.DEFAULT:         return "DEFAULT";
        case Token.WHILE:           return "WHILE";
        case Token.DO:              return "DO";
        case Token.FOR:             return "FOR";
        case Token.BREAK:           return "BREAK";
        case Token.CONTINUE:        return "CONTINUE";
        case Token.VAR:             return "VAR";
        case Token.WITH:            return "WITH";
        case Token.CATCH:           return "CATCH";
        case Token.FINALLY:         return "FINALLY";
        case Token.VOID:         	return "VOID";
        case Token.RESERVED:        return "RESERVED";
        case Token.EMPTY:           return "EMPTY";
        case Token.BLOCK:           return "BLOCK";
        case Token.LABEL:           return "LABEL";
        case Token.TARGET:          return "TARGET";
        case Token.LOOP:            return "LOOP";
        case Token.EXPR_VOID:       return "EXPR_VOID";
        case Token.EXPR_RESULT:     return "EXPR_RESULT";
        case Token.JSR:             return "JSR";
        case Token.SCRIPT:          return "SCRIPT";
        case Token.TYPEOFNAME:      return "TYPEOFNAME";
        case Token.USE_STACK:       return "USE_STACK";
        case Token.SETPROP_OP:      return "SETPROP_OP";
        case Token.SETELEM_OP:      return "SETELEM_OP";
        case Token.LOCAL_BLOCK:     return "LOCAL_BLOCK";
        case Token.SET_REF_OP:      return "SET_REF_OP";
        case Token.DOTDOT:          return "DOTDOT";
        case Token.COLONCOLON:      return "COLONCOLON";
        case Token.XML:             return "XML";
        case Token.DOTQUERY:        return "DOTQUERY";
        case Token.XMLATTR:         return "XMLATTR";
        case Token.XMLEND:          return "XMLEND";
        case Token.TO_OBJECT:       return "TO_OBJECT";
        case Token.TO_DOUBLE:       return "TO_DOUBLE";
        case Token.GET:             return "GET";
        case Token.SET:             return "SET";
        case Token.CONST:           return "CONST";
        case Token.SETCONST:        return "SETCONST";
        case Token.SETCONSTVAR:     return "SETCONSTVAR";
        case Token.LAST_TOKEN:     	return "LAST_TOKEN";
		}
	
		// Token without name
		return UNKNOWN_TOKEN_TYPE;
	}

	protected static AstRoot parseIntoTree(String formula) {
		Context cx = Context.enter();
	
		CompilerEnvirons compilerEnv = new CompilerEnvirons();
		compilerEnv.initFromContext(cx);
		ErrorReporter compilationErrorReporter = compilerEnv.getErrorReporter();
	
		Parser parser = new Parser(compilerEnv, compilationErrorReporter);
		AstRoot tree = parser.parse(formula, "(formula being checked by unit-checker)", 0);
		Context.exit();
		return tree;
	}
	
	protected static Node parseFunctionBodyIntoTree(String functionBody) {
		 AstRoot node = parseIntoTree("function() {" + functionBody + "}");
		 final ArrayList<Node> functionBodyList = new ArrayList<Node>();
		 NodeVisitor visitor = new NodeVisitor(){

			@Override
			public boolean visit(AstNode node) {
				if(node.getType() == Token.BLOCK){
					functionBodyList.add(node);
					return false;
				}
				return true;
			}
			 
		 };
		 node.visit(visitor);
		 if(functionBodyList.size()>0){
			 return functionBodyList.get(0);
		 }
		 return null;
	}

	protected final String pad(int depth) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < depth; i++)
			b.append("\t");
		return b.toString();
	}

}
