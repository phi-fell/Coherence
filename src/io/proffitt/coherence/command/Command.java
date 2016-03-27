package io.proffitt.coherence.command;

import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.Configuration;
import io.proffitt.coherence.settings.Keys;

public class Command {
	Expression	e;
	String		com;
	String		errMsg = "No Error";
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
	public String getErrorString() {
		if (e == null) {
			return errMsg;
		} else {
			return e.getErrorMessage();
		}
	}
	public boolean execute(Configuration[] c) {
		try {
			if (e == null) {
				if (com.toLowerCase().startsWith("bind ")) {
					String remainder = com.substring("bind ".length()).trim();
					int spaceIndex = remainder.indexOf(' ');
					if (spaceIndex > 0) {
						ResourceHandler.get().getConfig("keybindings").nullGet("" + Keys.getKey(remainder.substring(0, spaceIndex).trim())).setString(remainder.substring(spaceIndex).trim());
						return true;
					} else {
						errMsg = "";
						return false;
					}
				} else if (com.toLowerCase().startsWith("unbind ")) {
					String token = com.substring("unbind ".length()).trim();
					if (ResourceHandler.get().getConfig("keybindings").get("" + Keys.getKey(token)) != null) {
						ResourceHandler.get().getConfig("keybindings").remove("" + Keys.getKey(token));
						return true;
					} else {
						errMsg = "Key was not bound";
						return false;
					}
				}
				errMsg = "Not an extant command or expression";
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
