package io.proffitt.coherence.command;

import io.proffitt.coherence.settings.Configuration;

public class Command {
	Expression e;
	public Command(String s) {
		e = new Expression(s);
	}
	public boolean execute(Configuration c) {
		if (e.isValid()) {
			e.execute(c);
			return true;
		} else {
			return false;
		}
	}
}
