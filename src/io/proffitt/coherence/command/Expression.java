package io.proffitt.coherence.command;

import io.proffitt.coherence.settings.Value;

public class Expression {
	public static final int OP_SET = 0;
	public static final int OP_ADD = 1;
	public static final int OP_SUB = 2;
	public static final int OP_MUL = 3;
	public static final int OP_DIV = 4;
	public static final int OP_ADD_TO = 5;
	public static final int OP_SUB_FROM = 6;
	public static final int OP_MUL_BY = 7;
	public static final int OP_DIV_BY = 8;
	public static final String[] ops = {"=","+","-","*","/","+=","-=","*=","/="};
	Value v1, v2;
	int op;
	public Expression(String s){
		int index = -1;
		for (int i = 0; i < ops.length && index == -1; i++){
			index = s.indexOf(ops[i]);
			op = i;
		}
		if (index < 1){
			throw new RuntimeException("Invalid Expression: " + s);
		}
		v1 = new Value(s.substring(0, index).trim());
		v2 = new Value(s.substring(index+1).trim());
		v1.parse();
		v2.parse();
	}
}
