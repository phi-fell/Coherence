package io.proffitt.coherence.command;

import java.util.ArrayList;

import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.Value;

public class Expression {
	public static final int			OP_SET				= 0;
	public static final int			OP_MUL_BY			= 1;
	public static final int			OP_DIV_BY			= 2;
	public static final int			OP_ADD_TO			= 3;
	public static final int			OP_SUB_FROM			= 4;
	public static final int			OP_EQUALITY			= 5;
	public static final int			OP_DIV				= 6;
	public static final int			OP_ADD				= 7;
	public static final int			OP_SUB				= 8;
	public static final int			OP_MUL				= 9;
	public static final String[]	binOps				= { "=", "*=", "/=", "+=", "-=", "==", "/", "+", "-", "*" };
	//binary operators until 99, binary from 100 to 199
	public static final int			UNARY_OFFSET		= 100;
	public static final int			OP_IDENTITY			= UNARY_OFFSET;												//no unary operator on value
	public static final int			OP_LOG_INV			= UNARY_OFFSET + 1;											//logical inverse
	public static final String[]	unOps				= { "", "!" };
	public static final int			OP_TERNARY_IF		= 200;														// (condition)?(expression if true):(expression if false)
	private boolean					valid;
	private String					invalidityMessage	= "No Error";
	Value							v1, v2;
	Expression						e1, e2, ec;																		//ec is for ternary if only
	int								op;
	public Expression(String s) {
		valid = true;
		e1 = null;
		e2 = null;
		ec = null;
		s = s.trim();
		while (s.startsWith("(") && s.endsWith(")")) {
			s = s.substring(1, s.length() - 1).trim();
		}
		op = binOps.length;
		int parens = 0;
		int splitIndex = -1;
		int split2 = -1; //for ternary if
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				parens++;
			} else if (s.charAt(i) == ')') {
				parens--;
			}
			if (parens < 0) {
				invalidityMessage = "unexpected \')\' at location " + i;
				valid = false;
				return;
			} else if (parens == 0) {
				if (op != OP_TERNARY_IF) {
					if (s.charAt(i) == '?') {
						op = OP_TERNARY_IF;
						splitIndex = i;
					} else {
						for (int o = 0; o < op; o++) {
							if (s.charAt(i) == binOps[o].charAt(0) && s.substring(i, i + binOps[o].length()).equals(binOps[o])) {
								op = o;
								splitIndex = i;
							}
						}
					}
				} else if (split2 == -1) {
					if (s.charAt(i) == ':') {
						op = OP_TERNARY_IF;
						split2 = i;
					}
				}
			}
		}
		if (parens > 0) {
			invalidityMessage = "reached end of string while expecting \')\'";
			valid = false;
			return;
		}
		if (op == OP_TERNARY_IF) {
			if (split2 < 0) {
				invalidityMessage = "invalid ternary if";
				valid = false;
				return;
			} else {
				ec = new Expression(s.substring(0, splitIndex));
				e1 = new Expression(s.substring(splitIndex + 1, split2));
				e2 = new Expression(s.substring(split2 + 1));
				return;
			}
		}
		if (splitIndex == -1) {
			for (int u = 1; u < unOps.length && splitIndex == -1; u++) {
				if (s.startsWith(unOps[u])) {
					splitIndex = 0;
					op = UNARY_OFFSET + u;
					e1 = new Expression(s.substring(unOps[u].length()));
				}
			}
			if (splitIndex == -1) {
				op = OP_IDENTITY;
				v1 = new Value(s);
				v1.parse();
			}
		} else {
			e1 = new Expression(s.substring(0, splitIndex));
			e2 = new Expression(s.substring(splitIndex + 1));
		}
	}
	public boolean isValid() {
		return valid;
	}
	public String getErrorMessage() {
		return invalidityMessage;
	}
	public Value execute(Configuration c) {
		return this.execute(new Configuration[] { c });
	}
	public Value execute(Configuration[] c) {
		Value vA, vB, vC;
		vA = null;
		vB = null;
		if (op != OP_TERNARY_IF) {
			if (e1 == null) {
				if (v1.getType() == Value.TYPE_STRING) {
					for (int i = 0; i < c.length && vA == null; i++) {
						vA = c[i].get(v1.getString());
					}
				}
				if (vA == null) {
					vA = v1;
				}
			} else {
				vA = e1.execute(c);
			}
			if (e2 == null && op < UNARY_OFFSET) {
				if (v2.getType() == Value.TYPE_STRING) {
					for (int i = 0; i < c.length && vB == null; i++) {
						vB = c[i].get(v2.getString());
					}
				}
				if (vB == null) {
					vB = v2;
				}
			} else if (op < UNARY_OFFSET) {
				vB = e2.execute(c);
			}
		}
		vC = null;
		switch (op) {
		case OP_SET:
			vA.set(vB);
			vC = vA;
			break;
		case OP_ADD:
			vC = new Value(vA);
			vC.Add(vB);
			break;
		case OP_SUB:
			vC = new Value(vA);
			vC.Subtract(vB);
			break;
		case OP_MUL:
			vC = new Value(vA);
			vC.Multiply(vB);
			break;
		case OP_DIV:
			vC = new Value(vA);
			vC.Divide(vB);
			break;
		case OP_ADD_TO:
			vC = vA;
			vC.Add(vB);
			break;
		case OP_SUB_FROM:
			vC = vA;
			vC.Subtract(vB);
			break;
		case OP_MUL_BY:
			vC = vA;
			vC.Multiply(vB);
			break;
		case OP_DIV_BY:
			vC = vA;
			vC.Divide(vB);
			break;
		case OP_EQUALITY:
			vC = new Value(vA.equals(vB));
			break;
		case OP_IDENTITY:
			vC = vA;
			break;
		case OP_LOG_INV:
			vC = new Value(!vA.getBool());
			break;
		case OP_TERNARY_IF:
			System.out.println(ec.execute(c));
			vC = ec.execute(c).getBool() ? (e1 == null ? v1 : e1.execute(c)) : (e2 == null ? v2 : e2.execute(c));
			break;
		}
		return vC;
	}
}
