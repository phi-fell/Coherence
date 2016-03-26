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
	public static final String[]	binOps		= { "=", "+", "-", "*", "/", "+=", "-=", "*=", "/=" };
	//binary operators until 99, binary from 100 to 199
	public static final int			OP_NONE		= 100;													//no unary operator on value
	public static final int			OP_LOG_INV	= 101;													//logical inverse
	public static final String[]	unOps		= { "", "!" };
	private int						v1_op, v2_op;
	private boolean					valid;
	Value							v1, v2;
	int								op;
	public Expression(String s) {
		valid = true;
		int index = -1;
		for (int i = 0; i < binOps.length && index == -1; i++) {
			index = s.indexOf(binOps[i]);
			op = i;
		}
		if (index == -1) {
			valid = false;
		} else {
			String v1Str = s.substring(0, index).trim();
			int v1index = -1;
			for (int i = 1; i < unOps.length && v1index == -1; i++) {
				v1index = v1Str.indexOf(unOps[i]);
				v1_op = 100 + i;
			}
			if (v1index == -1) {
				v1_op = OP_NONE;
			}
			v1 = new Value(v1Str.substring(v1index + 1).trim());
			String v2Str = s.substring(index + 1).trim();
			int v2index = -1;
			for (int i = 1; i < unOps.length && v2index == -1; i++) {
				v2index = v2Str.indexOf(unOps[i]);
				v2_op = 100 + i;
			}
			if (v2index == -1) {
				v2_op = OP_NONE;
			}
			v2 = new Value(v2Str.substring(v2index + 1).trim());
			v1.parse();
			v2.parse();
		}
	}
	public boolean isValid() {
		return valid;
	}
	public Value execute(Configuration c) {
		return this.execute(new Configuration[] { c });
	}
	public Value execute(Configuration[] c) {
		Value vA, vB, vC;
		vA = null;
		vB = null;
		if (v1.getType() == Value.TYPE_STRING) {
			for (int i = 0; i < c.length && vA == null; i++) {
				vA = c[i].get(v1.getString());
			}
		}
		if (vA == null) {
			vA = v1;
		}
		if (v2.getType() == Value.TYPE_STRING) {
			for (int i = 0; i < c.length && vB == null; i++) {
				vB = c[i].get(v2.getString());
			}
		}
		if (vB == null) {
			vB = v2;
		}
		switch (v1_op) {
		case OP_LOG_INV:
			vA = new Value(!vA.getBool());
			break;
		}
		switch (v2_op) {
		case OP_LOG_INV:
			vB = new Value(!vB.getBool());
			break;
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
		}
		return vC;
	}
}
