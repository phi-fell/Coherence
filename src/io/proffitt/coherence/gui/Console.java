package io.proffitt.coherence.gui;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.annotation.Resources;

import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;

import io.proffitt.coherence.command.Command;
import io.proffitt.coherence.graphics.Text;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.resource.ResourceHandler;

public class Console {
	private Text				t;
	private ArrayList<String>	archive;
	private ArrayList<String>	log;
	private String				input;
	private final int			rollover	= 10;
	public Console() {
		t = null;
		archive = new ArrayList<String>();
		log = new ArrayList<String>();
		input = "";
	}
	public void registerTextInput(char c) {
		if (c == '\n') {
			pushString(input);
			input = "";
		} else {
			input += c;
		}
		cleanupText();
	}
	public void pushString(String s) {
		Command c = new Command(s);
		addMessage(s);
		if (!c.execute(ResourceHandler.get().getConfig("globals"))){
			addMessage("Invalid Command!");
		}
	}
	public void addMessage(String s) {
		log.add(s);
		if (log.size() > rollover) {
			archive.add(log.remove(0));
		}
		cleanupText();
	}
	private void cleanupText() {
		if (t != null) {
			t.destroy();
		}
		t = null;
	}
	public void draw(Window w) {
		if (t == null) {
			String s = "";
			for (int i = 0; i < log.size(); i++) {
				s += log.get(i) + "\n";
			}
			s += " >" + input;
			t = ResourceHandler.get().getFont("Courier New,12").getText(s);
		}
		t.getBackingImage().draw(0, w.getHeight(), 0, 1);
	}
}
