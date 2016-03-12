package io.proffitt.coherence.gui;

import java.util.ArrayList;

import io.proffitt.coherence.command.Command;
import io.proffitt.coherence.graphics.Text;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.resource.ResourceHandler;

public class Console {
	private Text				t;
	private ArrayList<String>	log;
	private ArrayList<String>	history;
	private String				input;
	private int					historyPos;
	private final int			rollover	= 20;
	public Console() {
		historyPos = 0;
		t = null;
		log = new ArrayList<String>();
		history = new ArrayList<String>();
		input = "";
		addMessage("Coherence Console.  \'help\' or \'?\' for help.");
	}
	public void registerTextInput(char c) {
		if (c == '\n') {
			if (input.length() != 0) {
				pushString(input);
				history.add(input);
				input = "";
				historyPos = 0;
			}
		} else if (c == '\b') {
			input = input.substring(0, input.length() > 0 ? input.length() - 1 : 0);
		} else {
			input += c;
		}
		cleanupText();
	}
	public boolean clearInput() {
		if (input.length() == 0) {
			return false;
		} else {
			input = "";
			historyPos = 0;
			cleanupText();
			return true;
		}
	}
	public void inputUp() {
		if (historyPos < history.size()) {
			historyPos++;
		}
		if (history.size() > 0) {
			input = history.get(history.size() - historyPos);
		}
		cleanupText();
	}
	public void inputDown() {
		if (historyPos > 0) {
			historyPos--;
		}
		input = (historyPos == 0) ? "" : history.get(history.size() - historyPos);
		cleanupText();
	}
	public void pushString(String s) {
		if (s.equalsIgnoreCase("help") || s.equalsIgnoreCase("?")){
			addMessage("---------<HELP>---------");
			addMessage("You can enter an expression such as");
			addMessage("\tLOG_INPUT = true");
			addMessage("or");
			addMessage("\tVSYNC = false");
			addMessage("or even");
			addMessage("\tFPS = 100");
			addMessage("but this will only change the displayed FPS, not the actual # of rendered frames.");
			addMessage("the FPS variable is also reset whenever FPS is recalculated (approx. every second).");
			addMessage("");
			addMessage("You can also enter a command such as");
			addMessage("\tbind KEY_SPACE JUMP");
			addMessage("");
			addMessage("To list commands use \'list commands\'");
			addMessage("To list variables use \'list globals\'");
			addMessage("---------</HELP>---------");
			return;
		}
		Command c = new Command(s);
		addMessage(s);
		if (!c.execute(ResourceHandler.get().getConfig("globals"))) {
			String lower = s.toLowerCase();
			if ((lower.contains(" ") && ResourceHandler.get().getProperty("commands").getObj(lower.substring(0, lower.indexOf(' '))) != null) || ResourceHandler.get().getProperty("commands").getObj(lower) != null) {
				addMessage("Correct usage: " + ResourceHandler.get().getProperty("commands").getObj("bind").getSub("usage").getValue().getString());
			} else {
				addMessage("Invalid Command!");
			}
		}
	}
	public void addMessage(String s) {
		log.add(s);
		cleanupText();
	}
	private void cleanupText() {
		if (t != null) {
			t.destroy();
			t = null;
		}
	}
	public void draw(Window w) {
		if (t == null) {
			String s = "";
			for (int i = rollover < log.size() ? rollover : log.size(); i > 0; i--) {
				s += log.get(log.size() - i) + "\n";
			}
			s += " >" + input;
			t = ResourceHandler.get().getFont("Courier New,12").getText(s);
		}
		t.getBackingImage().draw(0, w.getHeight(), 0, 1);
	}
}
