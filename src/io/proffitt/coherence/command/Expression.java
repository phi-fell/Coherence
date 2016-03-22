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
		if (vA == null){
			vA = v1;
		}
		if (v2.getType() == Value.TYPE_STRING) {
			for (int i = 0; i < c.length && vB == null; i++) {
				vB = c[i].get(v2.getString());
			}
		}
		if (vB == null){
			vB = v2;
		}
		System.out.println(vA);
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
		System.out.println(vA);
		return vC;
	}
}
