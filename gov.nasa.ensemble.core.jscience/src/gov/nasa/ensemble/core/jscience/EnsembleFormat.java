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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

/* package */ abstract class EnsembleFormat extends UnitFormat {

	/* package */ static final UnitFormat INSTANCE = createEnsembleFormat();

	private static UnitFormat createEnsembleFormat() {
		/* We NEED to use the global instance here since making aliasses and labels
		 * should apply to anything using Amount.format(...). There is an Amount.setInstance()
		 * method which seems like the right place to override this behavior and use our
		 * own unit parsing
		 */ 
		UnitFormat format = new EnsembleDefaultFormat();
		
	    format.alias(SI.BIT, "Bits");
	    format.alias(SI.BIT, "bits");
	    format.alias(JSciencePackage.KILO_BIT, "kb");
	    format.alias(JSciencePackage.KILO_BIT, "Kb");
	    format.alias(JSciencePackage.KILO_BIT, "KBits");
	    format.alias(JSciencePackage.KILO_BIT, "Kbits");
	    format.alias(JSciencePackage.KILO_BIT, "kilobits");
	    format.alias(JSciencePackage.MEGA_BIT, "mb");
	    format.alias(JSciencePackage.MEGA_BIT, "Mb");
	    format.alias(JSciencePackage.MEGA_BIT, "MBits");
	    format.alias(JSciencePackage.MEGA_BIT, "Mbits");
	    format.alias(JSciencePackage.MEGA_BIT, "megabits");
	    format.alias(JSciencePackage.GIGA_BIT, "gb");
	    format.alias(JSciencePackage.GIGA_BIT, "Gb");
	    format.alias(JSciencePackage.GIGA_BIT, "GBits");
	    format.alias(JSciencePackage.GIGA_BIT, "Gbits");
	    format.alias(JSciencePackage.GIGA_BIT, "gigabits");
	    format.alias(JSciencePackage.TERA_BIT, "tb");
	    format.alias(JSciencePackage.TERA_BIT, "Tb");
	    format.alias(JSciencePackage.TERA_BIT, "TBits");
	    format.alias(JSciencePackage.TERA_BIT, "Tbits");
	    format.alias(JSciencePackage.TERA_BIT, "terabits");
	    
	    format.alias(SI.WATT, "watt");
	    format.alias(SI.WATT, "Watt");
	    format.alias(SI.WATT, "Watts");
	    format.alias(SI.WATT, "watts");
	    
	    format.alias(SI.WATT.times(NonSI.HOUR), "watthrs");
	    format.alias(SI.WATT.times(NonSI.HOUR), "WattHours");
	    format.alias(SI.WATT.times(NonSI.HOUR), "Wh");
	    
		format.alias(SI.AMPERE.times(NonSI.HOUR), "Ah");
	    
	    format.alias(SI.SECOND, "sec");
	    format.alias(SI.SECOND, "Sec");
	    format.alias(SI.SECOND, "second");
	    format.alias(SI.SECOND, "seconds");
	    format.alias(NonSI.MINUTE, "min");
	    format.alias(NonSI.MINUTE, "minutes");
	    format.alias(NonSI.HOUR, "hours");
	    format.alias(NonSI.HOUR, "Hours");
	    format.alias(NonSI.HOUR, "hrs");
	    format.alias(NonSI.DAY, "days");
	    
	    // ANGLE
	    format.alias(NonSI.DEGREE_ANGLE, "?");
	    format.alias(NonSI.DEGREE_ANGLE, "deg");
	    format.alias(NonSI.DEGREE_ANGLE, "Deg");
	    format.alias(NonSI.DEGREE_ANGLE, "degrees");

	    // DISTANCE
	    format.alias(SI.METER, "m");
	    format.alias(SI.METER, "meters");
	    format.alias(SI.KILO(SI.METER), "km");
	    format.alias(NonSI.ASTRONOMICAL_UNIT, "AU");
	    
	    // THERMAL
	    format.alias(SI.CELSIUS, "C");
	    format.alias(SI.CELSIUS, "?C");
	    format.alias(SI.WATT, "Wth");  // Watts thermal (as opposed to electrical).

	    // Initialize the one-to-one unit to label map
	    // TIME
	    format.label(		SI.SECOND,  	"s");
	    format.label(		NonSI.HOUR, 	"h");
	    
		// DATA_AMOUNT
	    format.label(SI.BIT, "bit");
	    
	    format.label(JSciencePackage.KILO_BIT, "Kbit");
	    format.label(JSciencePackage.MEGA_BIT, "Mbit");
	    format.label(JSciencePackage.GIGA_BIT, "Gbit");
	    format.label(JSciencePackage.TERA_BIT, "Tbit");

	    format.label(SI.KILO(SI.BIT), "kbit");
	    format.label(SI.MEGA(SI.BIT), "mbit");
	    format.label(SI.GIGA(SI.BIT), "gbit");
	    format.label(SI.TERA(SI.BIT), "tbit");

	    format.label(NonSI.BYTE, "Byte");
	    
	    format.label(JSciencePackage.KILO_BYTE, "KByte");
	    format.label(JSciencePackage.MEGA_BYTE, "MByte");
	    format.label(JSciencePackage.GIGA_BYTE, "GByte");
	    format.label(JSciencePackage.TERA_BYTE, "TByte");

	    format.label(SI.KILO(NonSI.BYTE), "kByte");
	    format.label(SI.MEGA(NonSI.BYTE), "mByte");
	    format.label(SI.GIGA(NonSI.BYTE), "gByte");
	    format.label(SI.TERA(NonSI.BYTE), "tbyte");
		
		// ENERGY
	    format.label(		 SI.WATT, 	"W");
	    format.label(SI.KILO(SI.WATT), 	"kW");
		format.label(SI.MEGA(SI.WATT), 	"MW");
		format.label(SI.GIGA(SI.WATT), 	"GW");
		format.label(SI.TERA(SI.WATT), 	"TW");

		// POWER -  this is not really the friendliest way to do this
	    // but it appears that SI.KILO(SI.WATT).times(SI.SECOND) is not the same
	    // as SI.KILO(SI.WATT.times(SI.SECOND))
	    Unit WATT_SECOND = SI.WATT.times(SI.SECOND);
	    format.label(		   WATT_SECOND, "Ws");
	    format.label(SI.KILO(WATT_SECOND),"kWs");
		format.label(SI.MEGA(WATT_SECOND),"MWs");
		format.label(SI.GIGA(WATT_SECOND),"GWs");
		format.label(SI.TERA(WATT_SECOND),"TWs");

		format.label(NonSI.PERCENT, "%");

	    // TEMPERATURE
	    format.label(SI.CELSIUS, "C");
	    
	    // ANGLE
	    format.label(NonSI.DEGREE_ANGLE, "deg");

		return format;
	}

	/**
	 * This class is unused because we are bounded by the use of the UnitFormat.getInstance()
	 * which was trying to wrap for fatal calls to the alias method. Wrapping above instead.
	 */
	private static final class EnsembleDefaultFormat extends DefaultFormat {
		
		private static final int EOF = 0;
		private static final int IDENTIFIER = 1;
		private static final int OPEN_PAREN= 2;
		private static final int CLOSE_PAREN= 3;
		private static final int EXPONENT = 4;
		private static final int MULTIPLY = 5;
		private static final int DIVIDE = 6;
		private static final int PLUS = 7;
		private static final int INTEGER = 8;
		private static final int FLOAT = 9;

		private DefaultFormat inner = (DefaultFormat) UnitFormat.getInstance();

		@Override
		public Unit<? extends Quantity> parseProductUnit(CharSequence csq, ParsePosition pos) throws ParseException {
		    Unit result = Unit.ONE;
		    int token = nextToken(csq, pos);
		    switch (token) {
		    case IDENTIFIER:
		        result = parseSingleUnit(csq, pos);
		        break;
		    case OPEN_PAREN:
		        pos.setIndex(pos.getIndex() + 1);
		        result = parseProductUnit(csq, pos);
		        token = nextToken(csq, pos);
		        check(token == CLOSE_PAREN, "')' expected", csq, pos.getIndex());
		        pos.setIndex(pos.getIndex() + 1);
		        break;
		    }
		    token = nextToken(csq, pos);
		    while (true) {
		        switch (token) {
		        case EXPONENT:
		            EnsembleDefaultFormat.Exponent e = readExponent(csq, pos);
		            if (e.pow != 1) {
		                result = result.pow(e.pow);
		            }
		            if (e.root != 1) {
		                result = result.root(e.root);
		            }   
		            break;
		        case MULTIPLY:
		            pos.setIndex(pos.getIndex() + 1);
		            token = nextToken(csq, pos);
		            if (token == INTEGER) {
		                long n = readLong(csq, pos);
		                if (n != 1) {
		                    result = result.times(n);
		                }
		            } else if (token == FLOAT) {
		                double d = readDouble(csq, pos);
		                if (d != 1.0) {
		                    result = result.times(d);
		                }
		            } else {
		                result = result.times(parseProductUnit(csq, pos));
		            }
		            break;
		        case DIVIDE:
		            pos.setIndex(pos.getIndex() + 1);
		            token = nextToken(csq, pos);
		            if (token == INTEGER) {
		                long n = readLong(csq, pos);
		                if (n != 1) {
		                    result = result.divide(n);
		                }
		            } else if (token == FLOAT) {
		                double d = readDouble(csq, pos);
		                if (d != 1.0) {
		                    result = result.divide(d);
		                }
		            } else {
		                result = result.divide(parseProductUnit(csq, pos));
		            }
		            break;
		        case PLUS:
		            pos.setIndex(pos.getIndex() + 1);
		            token = nextToken(csq, pos);
		            if (token == INTEGER) {
		                long n = readLong(csq, pos);
		                if (n != 1) {
		                    result = result.plus(n);
		                }
		            } else if (token == FLOAT) {
		                double d = readDouble(csq, pos);
		                if (d != 1.0) {
		                    result = result.plus(d);
		                }
		            } else {
		                throw new ParseException("not a number", pos.getIndex());
		            }
		            break;
		        case EOF:
		        case CLOSE_PAREN:
		            return result;
		        default:
		            throw new ParseException("unexpected token " + token, pos.getIndex());
		        }
		        token = nextToken(csq, pos);
		    }
		}

		private int nextToken(CharSequence csq, ParsePosition pos) {
		    final int length = csq.length();
		    while (pos.getIndex() < length) {
		        char c = csq.charAt(pos.getIndex());
		        if (isUnitIdentifierPart(c)) {
		            return IDENTIFIER;
		        } else if (c == '(') {
		            return OPEN_PAREN;
		        } else if (c == ')') {
		            return CLOSE_PAREN;
		        } else if ((c == '^') || (c == '?') || (c == '?') || (c == '?')) {
		            return EXPONENT;
		        } else if (c == '*') {
		            char c2 = csq.charAt(pos.getIndex() + 1);
		            if (c2 == '*') {
		                return EXPONENT;
		            } else {
		                return MULTIPLY;
		            }
		        } else if (c == '?') {
		            return MULTIPLY;
		        } else if (c == '/') {
		            return DIVIDE;
		        } else if (c == '+') {
		            return PLUS;
		        } else if ((c == '-') || Character.isDigit(c)) {
		            int index = pos.getIndex()+1;
		            while ((index < length) && 
		                   (Character.isDigit(c) || (c == '-') || (c == '.') || (c == 'E'))) {
		                c = csq.charAt(index++);
		                if (c == '.') {
		                    return FLOAT;
		                }
		            }
		            return INTEGER;
		        }
		        pos.setIndex(pos.getIndex() + 1);
		    }
		    return EOF;
		}

		private void check(boolean expr, String message, CharSequence csq,
		        int index) throws ParseException {
		    if (!expr) {
		        throw new ParseException(message + " (in " + csq
		                + " at index " + index + ")", index);
		    }
		}

		private EnsembleDefaultFormat.Exponent readExponent (CharSequence csq, ParsePosition pos) {
		    char c = csq.charAt(pos.getIndex());
		    if (c == '^') {
		        pos.setIndex(pos.getIndex()+1);
		    } else if (c == '*') {
		        pos.setIndex(pos.getIndex()+2);
		    }
		    final int length = csq.length();
		    int pow = 0;
		    boolean isPowNegative = false;
		    int root = 0;
		    boolean isRootNegative = false;
		    boolean isRoot = false;
		    while (pos.getIndex() < length) {
		        c = csq.charAt(pos.getIndex());
		        if (c == '?') {
		            if (isRoot) {
		                root = root * 10 + 1;
		            } else {
		                pow = pow * 10 + 1;
		            }
		        } else if (c == '?') {
		            if (isRoot) {
		                root = root * 10 + 2;
		            } else {
		                pow = pow * 10 + 2;
		            }
		        } else if (c == '?') {
		            if (isRoot) {
		                root = root * 10 + 3;
		            } else {
		                pow = pow * 10 + 3;
		            }
		        } else if (c == '-') {
		            if (isRoot) {
		                isRootNegative = true;
		            } else {
		                isPowNegative = true;
		            }
		        } else if ((c >= '0') && (c <= '9')) {
		            if (isRoot) {
		                root = root * 10 + (c - '0');
		            } else {
		                pow = pow * 10 + (c - '0');
		            }
		        } else if (c == ':') {
		            isRoot = true;
		        } else {
		            break;
		        }
		        pos.setIndex(pos.getIndex()+1);
		    }
		    if (pow == 0) pow = 1;
		    if (root == 0) root = 1;
		    return new Exponent(isPowNegative ? -pow : pow, 
		                      isRootNegative ? -root : root);
		}

		private long readLong (CharSequence csq, ParsePosition pos) {
		    final int length = csq.length();
		    int result = 0;
		    boolean isNegative = false;
		    while (pos.getIndex() < length) {
		        char c = csq.charAt(pos.getIndex());
		        if (c == '-') {
		            isNegative = true;
		        } else if ((c >= '0') && (c <= '9')) {
		            result = result * 10 + (c - '0');
		        } else {
		            break;
		        }
		        pos.setIndex(pos.getIndex()+1);
		    }
		    return isNegative ? -result : result;
		}

		private double readDouble (CharSequence csq, ParsePosition pos) {
		    final int length = csq.length();
		    int start = pos.getIndex();
		    int end = start+1;
		    while (end < length) {
		        if ("0123456789+-.E".indexOf(csq.charAt(end)) < 0) {
		            break;
		        }
		        end += 1;
		    }
		    pos.setIndex(end+1);
		    return Double.parseDouble(csq.subSequence(start,end).toString());
		}

		static boolean isUnitIdentifierPart(char ch) {
		    return Character.isLetter(ch) || 
		       (!Character.isWhitespace(ch) && !Character.isDigit(ch)
		          && (ch != '?') && (ch != '*') && (ch != '/')
		          && (ch != '(') && (ch != ')') && (ch != '[') && (ch != ']')    
		          && (ch != '?') && (ch != '?') && (ch != '?') 
		          && (ch != '^') && (ch != '+') && (ch != '-'));
		}
		
		@Override
		public Appendable format(Unit<?> unit, Appendable appendable) throws IOException {
			return inner.format(unit, appendable);
		}
		
		@Override
		public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
			return inner.formatToCharacterIterator(obj);
		}
		
		@Override
		public boolean isValidIdentifier(String name) {
			return inner.isValidIdentifier(name);
		}
		
		@Override
		public Object parseObject(String source) throws ParseException {
			return inner.parseObject(source);
		}
		
		@Override
		public Unit<? extends Quantity> parseSingleUnit(CharSequence csq, ParsePosition pos) throws ParseException {
			return inner.parseSingleUnit(csq, pos);
		}

		@Override
		public void label(Unit<?> unit, String label) {
			inner.label(unit, label);
		}

		@Override
		public void alias(Unit<?> unit, String alias) {
			try {
				inner.alias(unit, alias);
			} catch (Exception e) {
				LogUtil.error(alias, e);
			}
		}

		/**
		 * This class represents an exponent with both a power (numerator)
		 * and a root (denominator).
		 */
		private static class Exponent {
		    public final int pow;
		    public final int root;
		    public Exponent (int pow, int root) {
		        this.pow = pow;
		        this.root = root;
		    }
		}
		
	}

	
}
