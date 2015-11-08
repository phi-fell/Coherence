package io.proffitt.coherence.command;

import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.Keys;

public class Command {
	Expression	e;
	String		com;
	public Command(String s) {
		e = null;
		String lower = s.toLowerCase();
		if (lower.startsWith("bind ") || lower.startsWith("unbind ")) {
			com = s;
		} else {
			e = new Expression(s);
		}
	}
	public boolean execute(Configuration c) {
		try {
			if (e == null) {
				if (com.toLowerCase().startsWith("bind ")) {
					String[] tokens = com.toUpperCase().replace("BIND ", "").trim().split(" ");
					ResourceHandler.get().getConfig("keybindings").nullGet("" + Keys.getKey(tokens[0])).setString(tokens[1]);
					return true;
				} else if (com.toLowerCase().startsWith("unbind ")) {
					String token = com.toUpperCase().replace("UNBIND ", "").trim();
					ResourceHandler.get().getConfig("keybindings").remove("" + Keys.getKey(token));
					return true;
				}
				return false;
			}
			if (e.isValid()) {
				e.execute(c);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
