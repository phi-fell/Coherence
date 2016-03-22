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
		if ((lower.contains(" ") && ResourceHandler.get().getProperty("commands").getObj(lower.substring(0, lower.indexOf(' '))) != null) || ResourceHandler.get().getProperty("commands").getObj(lower) != null) {
			com = s;
		} else {
			e = new Expression(s);
		}
	}
	public boolean execute(Configuration c) {
		return this.execute(new Configuration[] { c });
	}
	public boolean execute(Configuration[] c) {
		try {
			if (e == null) {
				if (com.toLowerCase().startsWith("bind ")) {
					String[] tokens = com.toUpperCase().replace("BIND ", "").trim().split(" ");
					if (tokens.length == 2) {
						ResourceHandler.get().getConfig("keybindings").nullGet("" + Keys.getKey(tokens[0])).setString(tokens[1]);
						return true;
					} else {
						return false;
					}
				} else if (com.toLowerCase().startsWith("unbind ")) {
					String token = com.toUpperCase().replace("UNBIND ", "").trim();
					if (ResourceHandler.get().getConfig("keybindings").get("" + Keys.getKey(token)) != null) {
						ResourceHandler.get().getConfig("keybindings").remove("" + Keys.getKey(token));
						return true;
					} else {
						return false;
					}
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
