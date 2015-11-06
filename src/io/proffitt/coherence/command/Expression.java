package io.proffitt.coherence.command;

import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.Value;

public class Expression {
	public static final int			OP_SET		= 0;
	public static final int			OP_ADD		= 1;
	public static final int			OP_SUB		= 2;
	public static final int			OP_MUL		= 3;
	public static final int			OP_DIV		= 4;
	public static final int			OP_ADD_TO	= 5;
	public static final int			OP_SUB_FROM	= 6;
	public static final int			OP_MUL_BY	= 7;
	public static final int			OP_DIV_BY	= 8;
	public static final String[]	ops			= { "=", "+", "-", "*", "/", "+=", "-=", "*=", "/=" };
	private boolean					valid;
	Value							v1, v2;
	int								op;
	public Expression(String s) {
		valid = true;
		int index = -1;
		for (int i = 0; i < ops.length && index == -1; i++) {
			index = s.indexOf(ops[i]);
			op = i;
		}
		if (index < 1) {
			valid = false;
		} else {
			v1 = new Value(s.substring(0, index).trim());
			v2 = new Value(s.substring(index + 1).trim());
			v1.parse();
			v2.parse();
		}
	}
	public boolean isValid() {
		return valid;
	}
	public Value execute(Configuration c) {
		Value vA, vB, vC;
		vA = (v1.getType() == Value.TYPE_STRING) ? c.nullGet(v1.getString()) : v1;
		vB = (v2.getType() == Value.TYPE_STRING) ? (c.get(v2.getString()) == null ? v2 : c.get(v2.getString())) : v2;
		vC = null;
		switch (op) {
		case OP_SET:
			vA = vB;
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
		}
		return vC;
	}
}
